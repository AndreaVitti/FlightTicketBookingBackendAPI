package com.project.paymentmicroservice.mapper;

import com.project.paymentmicroservice.DTO.PaymentDTO;
import com.project.paymentmicroservice.entity.Payment;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentMapper {
    public PaymentDTO mapPaymentToPaymentDTO(Payment payment) {
        return new PaymentDTO(
                payment.getId(),
                payment.getTicketConfirmCode(),
                payment.getCurrency(),
                payment.getPrice(),
                payment.getCreationDate());
    }

    public List<PaymentDTO> mapPaymentListToPaymentDTOList(List<Payment> payments) {
        return payments.stream().map(payment -> mapPaymentToPaymentDTO(payment)).toList();
    }
}
