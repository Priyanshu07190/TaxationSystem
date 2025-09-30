import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import taxation.db.DatabaseManager;

public class TaxCalculatorFrame {
    private int userId;
    private DatabaseManager dbManager;
    private String userType;
    private String role;

    public TaxCalculatorFrame(int userId, String userType, String role) {
        this.userId = userId;
        this.userType = userType;
        this.role = role;
        this.dbManager = DatabaseManager.getInstance();
    }

    public void launch() {
        JFrame frame = new JFrame("Tax Calculator");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new GridBagLayout());
        frame.setLocationRelativeTo(null);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel titleLabel = new JLabel("Tax Calculator", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        frame.add(titleLabel, gbc);

        // Tax Type
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        frame.add(new JLabel("Tax Type:"), gbc);

        JComboBox<String> taxTypeCombo = new JComboBox<>(new String[]{
            "Income", "GST", "Property", "Corporate", "SalesTax", "ExciseTax", "WealthTax"
        });
        gbc.gridx = 1;
        gbc.gridy = 1;
        frame.add(taxTypeCombo, gbc);

        // Amount
        gbc.gridx = 0;
        gbc.gridy = 2;
        frame.add(new JLabel("Amount:"), gbc);

        JTextField amountField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 2;
        frame.add(amountField, gbc);

        // Tax Year
        gbc.gridx = 0;
        gbc.gridy = 3;
        frame.add(new JLabel("Tax Year:"), gbc);

        JTextField yearField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 3;
        frame.add(yearField, gbc);

        // Result
        gbc.gridx = 0;
        gbc.gridy = 4;
        frame.add(new JLabel("Tax Amount:"), gbc);

        JLabel taxAmountLabel = new JLabel("0.00");
        taxAmountLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 1;
        gbc.gridy = 4;
        frame.add(taxAmountLabel, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton calculateButton = new JButton("Calculate");
        JButton backButton = new JButton("Back");
        buttonPanel.add(calculateButton);
        buttonPanel.add(backButton);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        frame.add(buttonPanel, gbc);

        calculateButton.addActionListener(e -> {
            try {
                String taxType = (String) taxTypeCombo.getSelectedItem();
                double amount = Double.parseDouble(amountField.getText());
                int taxYear = Integer.parseInt(yearField.getText());

                double tax = calculateTaxInJava(taxType, amount);
                // Store the result in the database
                try {
                    dbManager.insertTaxCalculation(userId, taxType, amount, taxYear, tax);
                } catch (Exception ex) {
                    System.out.println("[ERROR] Failed to store tax calculation: " + ex.getMessage());
                }
                taxAmountLabel.setText(String.format("₹%.2f", tax));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Please enter valid numbers", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error calculating tax: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        backButton.addActionListener(e -> {
            frame.dispose();
            if (userType.equals("Salaried") && role.equals("Admin")) {
                new AdminFrame(userId).launch();
            } else if (userType.equals("Salaried")) {
                new SalariedUserFrame(userId).launch();
            } else if (userType.equals("SelfEmployed")) {
                new SelfEmployedUserFrame(userId).launch();
            } else {
                new UnSalariedUserFrame(userId).launch();
            }
        });

        frame.setVisible(true);
    }

    private double calculateTaxInJava(String taxType, double amount) {
        switch (taxType) {
            case "Income":
                if (amount <= 300000) return 0;
                else if (amount <= 600000) return (amount - 300000) * 0.05;
                else if (amount <= 900000) return (amount - 600000) * 0.10 + 15000;
                else if (amount <= 1200000) return (amount - 900000) * 0.15 + 45000;
                else if (amount <= 1500000) return (amount - 1200000) * 0.20 + 90000;
                else return (amount - 1500000) * 0.30 + 150000;
            case "GST":
                return amount * 0.18;
            case "Property":
                if (amount <= 500000) return amount * 0.02;
                else return amount * 0.05;
            case "Corporate":
                return amount * 0.25;
            case "SalesTax":
                return amount * 0.10;
            case "ExciseTax":
                return amount * 0.12;
            case "WealthTax":
                if (amount <= 3000000) return 0;
                else return (amount - 3000000) * 0.01;
            default:
                throw new IllegalArgumentException("Invalid tax type");
        }
    }
}