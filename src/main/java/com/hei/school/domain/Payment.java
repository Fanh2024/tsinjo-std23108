package com.hei.school.domain;

import java.time.LocalDate;

public record Payment(
        String reference,
        Double amount,
        String paymentMethod,
        LocalDate date,
        PaymentStatus status
) {}
