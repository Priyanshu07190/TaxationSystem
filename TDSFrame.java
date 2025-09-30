import javax.swing.*;
import taxation.db.DatabaseManager;

public class TDSFrame {
    private int userId;
    private DatabaseManager dbManager;

    public TDSFrame(int userId) {
        this.userId = userId;
        this.dbManager = DatabaseManager.getInstance();
    }

    public void launch() {
        JFrame frame = new JFrame("Calculate TDS");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(null);

        JLabel amountLabel = new JLabel("Amount:");
        amountLabel.setBounds(50, 30, 100, 30);
        frame.add(amountLabel);

        JTextField amountField = new JTextField();
        amountField.setBounds(150, 30, 200, 30);
        frame.add(amountField);

        JLabel rateLabel = new JLabel("TDS Rate (%):");
        rateLabel.setBounds(50, 70, 100, 30);
        frame.add(rateLabel);

        JTextField rateField = new JTextField();
        rateField.setBounds(150, 70, 200, 30);
        frame.add(rateField);

        JButton calculateButton = new JButton("Calculate");
        calculateButton.setBounds(50, 120, 150, 30);
        frame.add(calculateButton);

        JButton backButton = new JButton("Back");
        backButton.setBounds(210, 120, 150, 30);
        frame.add(backButton);

        calculateButton.addActionListener(e -> {
            try {
                double amount = Double.parseDouble(amountField.getText());
                double tdsRate = Double.parseDouble(rateField.getText());
                double tdsAmount = dbManager.calculateTDS(userId, amount, tdsRate);
                JOptionPane.showMessageDialog(frame, "TDS Amount: ₹" + String.format("%.2f", tdsAmount));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Please enter valid numbers");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
            }
        });

        backButton.addActionListener(e -> {
            frame.dispose();
            new SelfEmployedUserFrame(userId).launch();
        });

        frame.setVisible(true);
    }
}