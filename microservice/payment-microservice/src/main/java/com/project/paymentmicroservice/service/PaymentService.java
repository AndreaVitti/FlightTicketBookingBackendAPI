package com.project.paymentmicroservice.service;

import com.project.paymentmicroservice.DTO.CheckoutComplReq;
import com.project.paymentmicroservice.DTO.PaymentRequest;
import com.project.paymentmicroservice.DTO.Response;
import com.project.paymentmicroservice.entity.Payment;
import com.project.paymentmicroservice.exception.EventCreationException;
import com.project.paymentmicroservice.exception.LineItemsException;
import com.project.paymentmicroservice.exception.PaymenetNotFound;
import com.project.paymentmicroservice.exception.SessionError;
import com.project.paymentmicroservice.feignClient.TicketClient;
import com.project.paymentmicroservice.mapper.PaymentMapper;
import com.project.paymentmicroservice.repository.PaymentRepository;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.LineItem;
import com.stripe.model.Price;
import com.stripe.model.Product;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.param.checkout.SessionListLineItemsParams;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final TicketClient ticketClient;
    private final PaymentMapper paymentMapper;
    @Value("${api.stripe.secretKey}")
    private String stripeSecretKey;
    @Value("${api.stripe.secretEndpoint}")
    private String endpointSecret;

    public Response makePayment(String bearerToken, PaymentRequest paymentRequest) {
        Response response = new Response();

        Stripe.apiKey = stripeSecretKey;

        SessionCreateParams.LineItem.PriceData.ProductData productData =
                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                        .setName(paymentRequest.ticketConfirmCode())
                        .build();

        SessionCreateParams.LineItem.PriceData priceData =
                SessionCreateParams.LineItem.PriceData.builder()
                        .setCurrency(paymentRequest.currency() != null ? paymentRequest.currency() : "EUR")
                        .setUnitAmount(((paymentRequest.price().multiply(new BigDecimal(100)).longValue())))
                        .setProductData(productData)
                        .build();

        SessionCreateParams.LineItem lineItem =
                SessionCreateParams
                        .LineItem.builder()
                        .setQuantity(1L)
                        .setPriceData(priceData)
                        .build();

        SessionCreateParams params =
                SessionCreateParams.builder()
                        .setMode(SessionCreateParams.Mode.PAYMENT)
                        .setSuccessUrl("http://localhost:8080/successfulPayment")
                        .setCancelUrl("http://localhost:8080/cancelPayment")
                        .addLineItem(lineItem)
                        .putMetadata("Authorization", bearerToken)
                        .build();

        Session session;
        try {
            session = Session.create(params);
        } catch (StripeException e) {
            throw new SessionError("Session couldn't be created");
        }
        response.setHttpCode(200);
        response.setSessionId(session.getId());
        response.setSessionUrl(session.getUrl());
        return response;
    }

    public Response stripeEventHandler(String payload, String signatureHeader) {
        Response response = new Response();
        Event event;
        try {
            event = Webhook.constructEvent(payload, signatureHeader, endpointSecret);
        } catch (SignatureVerificationException e) {
            throw new EventCreationException("Event couldn't be created");
        }
        if ("checkout.session.completed".equals(event.getType())) {
            Session session = (Session) event.getDataObjectDeserializer().getObject().orElse(null);
            if (session != null) {
                SessionListLineItemsParams params = SessionListLineItemsParams
                        .builder()
                        .addExpand("data.price.product")
                        .build();
                List<LineItem> lineItems;
                try {
                    lineItems = session.listLineItems(params).getData();
                } catch (StripeException e) {
                    throw new LineItemsException("Couldn't get data from LineItems");
                }
                lineItems.forEach(item -> {
                    String ticketConfirmCode;
                    Price price;
                    Product product;
                    try {
                        price = Price.retrieve(item.getPrice().getId());
                        product = Product.retrieve(price.getProduct());
                    } catch (StripeException e) {
                        throw new LineItemsException("Couldn't retrieve information on the product");
                    }
                    if (product != null) {
                        ticketConfirmCode = product.getName();
                        Long paymentId = savePayment(ticketConfirmCode, session);
                        CheckoutComplReq complReq = new CheckoutComplReq(ticketConfirmCode, paymentId);
                        ticketClient.checkoutCompletion(session.getMetadata().get("Authorization"), complReq);
                    }
                });
            }
        }
        response.setHttpCode(200);
        return response;
    }

    public Response getAll(int pageNum, int pageSize) {
        Response response = new Response();
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        Page<Payment> paymentPage = paymentRepository.findAll(pageable);
        List<Payment> payments = paymentPage.getContent();
        if (payments.isEmpty()) {
            throw new PaymenetNotFound("No payments were found");
        }
        response.setHttpCode(200);
        response.setPaymentDTOList(paymentMapper.mapPaymentListToPaymentDTOList(payments));
        return response;
    }

    public Response getById(Long id) {
        Response response = new Response();
        Payment payment = paymentRepository.findById(id).orElseThrow(() -> new PaymenetNotFound("Payment not found"));
        response.setHttpCode(200);
        response.setPaymentDTO(paymentMapper.mapPaymentToPaymentDTO(payment));
        return response;
    }

    private Long savePayment(String ticketConfirmCode, Session session) {
        Payment payment = new Payment();
        payment.setTicketConfirmCode(ticketConfirmCode);
        payment.setCurrency(session.getCurrency());
        payment.setPrice(new BigDecimal(session.getAmountTotal() / 100));
        payment.setCreationDate(LocalDateTime.now());
        paymentRepository.save(payment);
        return payment.getId();
    }
}
