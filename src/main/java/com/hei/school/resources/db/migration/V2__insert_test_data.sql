-- Données de test pour donation
INSERT INTO donation (id, donor_name, donor_email, payment_reference, amount, payment_method, payment_date, status)
VALUES
    (gen_random_uuid(), 'Rasolonjatovo Lova', 'lova@example.com', 'MP250804.0904.A01637', 50000, 'Mvola', '2025-08-01', 'SUCCEEDED'),
    (gen_random_uuid(), 'Rakoto Tiana', 'tiana@example.com', 'MP250804.0908.D15807', 100000, 'Orange Money', '2025-08-02', 'VERIFYING'),
    (gen_random_uuid(), 'Rabe Hanta', 'hanta@example.com', 'MP250804.0910.A02057', 75000, 'Airtel Money', '2025-08-03', 'FAILED');

-- Données de test pour help
INSERT INTO help (id, beneficiary_name, beneficiary_email, payment_reference, amount, payment_method, payment_date, status, description)
VALUES
    (gen_random_uuid(), 'Randrianarisoa Jean', 'jean@example.com', 'MP250804.1224.B31974', 30000, 'Mvola', '2025-08-04', 'SUCCEEDED', 'Aide pour les fournitures scolaires'),
    (gen_random_uuid(), 'Razanajatovo Marie', 'marie@example.com', 'MP250804.1224.B31976', 25000, 'Orange Money', '2025-08-05', 'SUCCEEDED', 'Aide alimentaire'),
    (gen_random_uuid(), 'Rakotomanga Joel', 'joel@example.com', 'MP250804.1856.D63944', 40000, 'Airtel Money', '2025-08-05', 'FAILED', 'Aide santé');
