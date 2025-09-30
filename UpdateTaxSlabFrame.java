import javax.swing.*;
import taxation.db.DatabaseManager;

public class UpdateTaxSlabFrame {
    private int userId;
    private DatabaseManager dbManager;

    public UpdateTaxSlabFrame(int userId) {
        this.userId = userId;
        this.dbManager = DatabaseManager.getInstance();
    }

    public void launch() {
        JFrame frame = new JFrame("Taxation System - Update Tax Slab");
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        JLabel slabIdLabel = new JLabel("Slab ID:");
        slabIdLabel.setBounds(20, 20, 100, 30);
        frame.add(slabIdLabel);

        JTextField slabIdField = new JTextField();
        slabIdField.setBounds(120, 20, 200, 30);
        frame.add(slabIdField);

        JLabel taxYearLabel = new JLabel("Tax Year:");
        taxYearLabel.setBounds(20, 60, 100, 30);
        frame.add(taxYearLabel);

        JTextField taxYearField = new JTextField();
        taxYearField.setBounds(120, 60, 200, 30);
        frame.add(taxYearField);

        JLabel taxTypeLabel = new JLabel("Tax Type:");
        taxTypeLabel.setBounds(20, 100, 100, 30);
        frame.add(taxTypeLabel);

        JComboBox<String> taxTypeCombo = new JComboBox<>(new String[]{"Income", "Property", "Corporate", "GST", "SalesTax", "ExciseTax", "WealthTax"});
        taxTypeCombo.setBounds(120, 100, 200, 30);
        frame.add(taxTypeCombo);

        JLabel lowerLimitLabel = new JLabel("Lower Limit:");
        lowerLimitLabel.setBounds(20, 140, 100, 30);
        frame.add(lowerLimitLabel);

        JTextField lowerLimitField = new JTextField();
        lowerLimitField.setBounds(120, 140, 200, 30);
        frame.add(lowerLimitField);

        JLabel upperLimitLabel = new JLabel("Upper Limit:");
        upperLimitLabel.setBounds(20, 180, 100, 30);
        frame.add(upperLimitLabel);

        JTextField upperLimitField = new JTextField();
        upperLimitField.setBounds(120, 180, 200, 30);
        frame.add(upperLimitField);

        JLabel taxRateLabel = new JLabel("Tax Rate (%):");
        taxRateLabel.setBounds(20, 220, 100, 30);
        frame.add(taxRateLabel);

        JTextField taxRateField = new JTextField();
        taxRateField.setBounds(120, 220, 200, 30);
        frame.add(taxRateField);

        JButton updateButton = new JButton("Update");
        updateButton.setBounds(120, 260, 100, 30);
        frame.add(updateButton);

        JButton backButton = new JButton("Back");
        backButton.setBounds(230, 260, 100, 30);
        frame.add(backButton);

        updateButton.addActionListener(e -> {
            try {
                int slabId = Integer.parseInt(slabIdField.getText());
                int taxYear = Integer.parseInt(taxYearField.getText());
                String taxType = (String) taxTypeCombo.getSelectedItem();
                double lowerLimit = Double.parseDouble(lowerLimitField.getText());
                String upperLimitStr = upperLimitField.getText();
                Double upperLimit = upperLimitStr.isEmpty() ? null : Double.parseDouble(upperLimitStr);
                double taxRate = Double.parseDouble(taxRateField.getText());
                dbManager.updateTaxSlab(userId, slabId, taxYear, taxType, lowerLimit, upperLimit, taxRate);
                JOptionPane.showMessageDialog(frame, "Tax Slab Updated Successfully");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
            }
        });

        backButton.addActionListener(e -> {
            frame.dispose();
            new AdminFrame(userId).launch();
        });

        frame.setVisible(true);
    }
}