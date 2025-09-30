USE IndianTaxationSystem;

-- Insert Tax Slabs for Income Tax (2023-2024)
INSERT INTO TaxSlabs (TaxYear, TaxType, LowerLimit, UpperLimit, TaxRate) VALUES
(2024, 'Income', 0, 300000, 0),
(2024, 'Income', 300001, 600000, 5),
(2024, 'Income', 600001, 900000, 10),
(2024, 'Income', 900001, 1200000, 15),
(2024, 'Income', 1200001, 1500000, 20),
(2024, 'Income', 1500001, NULL, 30);

-- Insert Tax Slabs for Property Tax (2023-2024)
INSERT INTO TaxSlabs (TaxYear, TaxType, LowerLimit, UpperLimit, TaxRate) VALUES
(2024, 'Property', 0, 500000, 1),
(2024, 'Property', 500001, 1000000, 2),
(2024, 'Property', 1000001, NULL, 3);

-- Insert Tax Slabs for Corporate Tax (2023-2024)
INSERT INTO TaxSlabs (TaxYear, TaxType, LowerLimit, UpperLimit, TaxRate) VALUES
(2024, 'Corporate', 0, 10000000, 25),
(2024, 'Corporate', 10000001, NULL, 30); 