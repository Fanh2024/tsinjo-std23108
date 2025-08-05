package com.hei.school.service;

import com.hei.school.client.VolaClient;
import com.hei.school.domain.Donation;
import com.hei.school.domain.Payment;
import com.hei.school.domain.PaymentStatus;
import com.hei.school.repository.DonationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DonationService {
    private final DonationRepository donationRepository;
    private final VolaClient volaClient;

    public DonationService(DonationRepository donationRepository, VolaClient volaClient) {
        this.donationRepository = donationRepository;
        this.volaClient = volaClient;
    }

    public void createDonation(Donation donation) {
        donationRepository.save(donation);
        // Démarre le polling dans un thread séparé
        new Thread(this::pollPayments).start();
    }

    public void pollPayments() {
        List<Donation> pendingDonations = donationRepository.findByStatus(PaymentStatus.VERIFYING);
        for (Donation donation : pendingDonations) {
            String reference = donation.payment().reference();
            PaymentStatus newStatus = volaClient.getPaymentStatus(reference);

            if (!newStatus.equals(donation.payment().status())) {
                // Crée un nouveau Payment avec le statut mis à jour
                Payment updatedPayment = new Payment(
                        donation.payment().reference(),
                        donation.payment().amount(),
                        donation.payment().paymentMethod(),
                        donation.payment().date(),
                        newStatus
                );

                // Crée un nouveau Donation avec payment mis à jour
                Donation updatedDonation = new Donation(
                        donation.donor(),
                        updatedPayment
                );

                // Met à jour en base de données (idéalement via la référence)
                donationRepository.updateStatus(reference, newStatus);
            }
        }
    }

}
