package com.project.paymentmicroservice.service;

import com.project.paymentmicroservice.DTO.PaymentRequest;
import com.project.paymentmicroservice.DTO.Response;
import com.project.paymentmicroservice.entity.Payment;
import com.project.paymentmicroservice.exception.SessionError;
import com.project.paymentmicroservice.repository.PaymentRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    @Value("${api.stripe.secretKey}")
    private String stripeSecretKey;

    public Response makePayment(PaymentRequest paymentRequest) {
        Response response = new Response();

        Stripe.apiKey = stripeSecretKey;

        /*Create productData*/
        SessionCreateParams.LineItem.PriceData.ProductData productData =
                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                        .setName(paymentRequest.getTicketConfirmCode())
                        .build();

        /*Create priceData with productData*/
        SessionCreateParams.LineItem.PriceData priceData =
                SessionCreateParams.LineItem.PriceData.builder()
                        .setCurrency(paymentRequest.getCurrency() != null ? paymentRequest.getCurrency() : "EUR")
                        .setUnitAmount(((paymentRequest.getPrice().multiply(new BigDecimal(100)).longValue())))
                        .setProductData(productData)
                        .build();

        /*Create lineItem with priceData*/
        SessionCreateParams.LineItem lineItem =
                SessionCreateParams
                        .LineItem.builder()
                        .setQuantity(1L)
                        .setPriceData(priceData)
                        .build();

        /*Create the session's parameters with lineItem*/
        SessionCreateParams params =
                SessionCreateParams.builder()
                        .setMode(SessionCreateParams.Mode.PAYMENT)
                        .setSuccessUrl("http://localhost:8080/successfulPayment")
                        .setCancelUrl("http://localhost:8080/cancelPayment")
                        .addLineItem(lineItem)
                        .build();

        Session session;
        try {
            session = Session.create(params);
        } catch (StripeException e) {
            throw new SessionError("Session couldn't be created");
        }
        Payment payment = new Payment();
        payment.setTicketConfirmCode(paymentRequest.getTicketConfirmCode());
        payment.setCurrency(paymentRequest.getCurrency());
        payment.setPrice(paymentRequest.getPrice());
        paymentRepository.save(payment);

        response.setHttpCode(200);
        response.setPaymentId(payment.getId());
        response.setSessionId(session.getId());
        response.setSessionUrl(session.getUrl());
        return response;
    }
}
