package com.hei.school.domain;

import com.hei.school.domain.Donation;
import com.hei.school.domain.Donor;
import com.hei.school.domain.Payment;
import com.hei.school.domain.PaymentStatus;

import java.time.LocalDate;

public class DonationForm {
    private String fullName;
    private String email;
    private Double amount;
    private String paymentMethod;

    // Getters et setters requis pour le binding Spring
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    // Conversion vers Donation (pour service/domaine)
    public Donation toDonation() {
        Donor donor = new Donor(fullName, email);
        Payment payment = new Payment(
                generateReference(),
                amount,
                paymentMethod,
                LocalDate.now(),
                PaymentStatus.VERIFYING // par défaut, paiement en attente
        );
        return new Donation(donor, payment);
    }

    // Méthode utilitaire pour générer une référence de paiement
    private String generateReference() {
        return "MP" + System.currentTimeMillis(); // exemple : REF-1698347462851
    }
}
