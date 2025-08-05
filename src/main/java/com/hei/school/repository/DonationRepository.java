package com.hei.school.repository;

import com.hei.school.config.DataBaseConnexion;
import com.hei.school.domain.Donation;
import com.hei.school.domain.Donor;
import com.hei.school.domain.Payment;
import com.hei.school.domain.PaymentStatus;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class DonationRepository {
    private final DataBaseConnexion dataSource;

    public DonationRepository(DataBaseConnexion dataSource) {
        this.dataSource = dataSource;
    }

    public void save(Donation donation) {
        String sql = """
            INSERT INTO donation (
                id, donor_name, donor_email, payment_reference,
                amount, payment_method, payment_date, status
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, UUID.randomUUID());
            stmt.setString(2, donation.donor().fullName());
            stmt.setString(3, donation.donor().email());
            stmt.setString(4, donation.payment().reference());
            stmt.setDouble(5, donation.payment().amount());
            stmt.setString(6, donation.payment().paymentMethod());
            stmt.setDate(7, Date.valueOf(donation.payment().date()));
            stmt.setString(8, donation.payment().status().name());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save donation", e);
        }
    }

    public List<Donation> findAllOrderedByDateDesc() {
        String sql = """
            SELECT * FROM donation
            ORDER BY payment_date DESC
        """;

        List<Donation> donations = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Donor donor = new Donor(
                        rs.getString("donor_name"),
                        rs.getString("donor_email")
                );

                Payment payment = new Payment(
                        rs.getString("payment_reference"),
                        rs.getDouble("amount"),
                        rs.getString("payment_method"),
                        rs.getDate("payment_date").toLocalDate(),
                        PaymentStatus.valueOf(rs.getString("status"))
                );

                donations.add(new Donation(donor, payment));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch donations", e);
        }

        return donations;
    }

    public List<Donation> findByStatus(PaymentStatus status) {
        String sql = """
        SELECT * FROM donation
        WHERE status = ?
        ORDER BY payment_date DESC
    """;

        List<Donation> donations = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status.name());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Donor donor = new Donor(
                            rs.getString("donor_name"),
                            rs.getString("donor_email")
                    );

                    Payment payment = new Payment(
                            rs.getString("payment_reference"),
                            rs.getDouble("amount"),
                            rs.getString("payment_method"),
                            rs.getDate("payment_date").toLocalDate(),
                            PaymentStatus.valueOf(rs.getString("status"))
                    );

                    donations.add(new Donation(donor, payment));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find donations by status", e);
        }

        return donations;
    }

    public void updateStatus(String reference, PaymentStatus newStatus) {
        String sql = "UPDATE donation SET status = ? WHERE payment_reference = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, newStatus.name());
            stmt.setString(2, reference);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating donation status", e);
        }
    }

}
