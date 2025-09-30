INSERT INTO TaxSlabs (TaxYear, TaxType, LowerLimit, UpperLimit, TaxRate) VALUES
(2025, 'Income', 0, 300000, 0),
(2025, 'Income', 300001, 600000, 5),
(2025, 'Income', 600001, 900000, 10),
(2025, 'Income', 900001, 1200000, 15),
(2025, 'Income', 1200001, 1500000, 20),
(2025, 'Income', 1500001, NULL, 30),
(2025, 'Property', 0, 500000, 1),
(2025, 'Property', 500001, 1000000, 2),
(2025, 'Property', 1000001, NULL, 3),
(2025, 'Corporate', 0, 10000000, 25),
(2025, 'Corporate', 10000001, NULL, 30); 