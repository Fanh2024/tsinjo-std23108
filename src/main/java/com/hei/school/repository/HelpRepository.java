package com.hei.school.repository;

import com.hei.school.config.DataBaseConnexion;
import com.hei.school.domain.Beneficiary;
import com.hei.school.domain.Help;
import com.hei.school.domain.Payment;
import com.hei.school.domain.PaymentStatus;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class HelpRepository {
    private final DataBaseConnexion dataSource;

    public HelpRepository(DataBaseConnexion dataSource) {
        this.dataSource = dataSource;
    }

    public void save(Help help) {
        String sql = """
            INSERT INTO help (
                id, beneficiary_name, beneficiary_email,
                payment_reference, amount, payment_method,
                payment_date, status, description
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, UUID.randomUUID());
            stmt.setString(2, help.beneficiary().fullName());
            stmt.setString(3, help.beneficiary().email());
            stmt.setString(4, help.payment().reference());
            stmt.setDouble(5, help.payment().amount());
            stmt.setString(6, help.payment().paymentMethod());
            stmt.setDate(7, Date.valueOf(help.payment().date()));
            stmt.setString(8, help.payment().status().name());
            stmt.setString(9, help.description());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save help", e);
        }
    }

    public List<Help> findAllOrderedByDateDesc() {
        String sql = """
            SELECT * FROM help
            ORDER BY payment_date DESC
        """;

        List<Help> helpList = new ArrayList<>();

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Beneficiary beneficiary = new Beneficiary(
                        rs.getString("beneficiary_name"),
                        rs.getString("beneficiary_email")
                );

                Payment payment = new Payment(
                        rs.getString("payment_reference"),
                        rs.getDouble("amount"),
                        rs.getString("payment_method"),
                        rs.getDate("payment_date").toLocalDate(),
                        PaymentStatus.valueOf(rs.getString("status"))
                );

                Help help = new Help(beneficiary, payment, rs.getString("description"));
                helpList.add(help);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch help records", e);
        }

        return helpList;
    }
}
