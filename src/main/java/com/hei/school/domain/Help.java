package com.hei.school.domain;

public record Help(
        Beneficiary beneficiary,
        Payment payment,
        String description
) {}
