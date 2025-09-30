import javax.swing.*;
import taxation.db.DatabaseManager;

public class VerifyPANFrame {
    private int userId;
    private DatabaseManager dbManager;

    public VerifyPANFrame(int userId) {
        this.userId = userId;
        this.dbManager = DatabaseManager.getInstance();
    }

    public void launch() {
        JFrame frame = new JFrame("Taxation System - Verify PAN");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        JLabel panLabel = new JLabel("PAN Number:");
        panLabel.setBounds(20, 20, 100, 30);
        frame.add(panLabel);

        JTextField panField = new JTextField();
        panField.setBounds(120, 20, 200, 30);
        frame.add(panField);

        JButton verifyButton = new JButton("Verify");
        verifyButton.setBounds(120, 60, 100, 30);
        frame.add(verifyButton);

        JButton backButton = new JButton("Back");
        backButton.setBounds(230, 60, 100, 30);
        frame.add(backButton);

        verifyButton.addActionListener(e -> {
            String pan = panField.getText();
            try {
                boolean isValid = dbManager.verifyPan(pan);
                JOptionPane.showMessageDialog(frame, "PAN Status: " + (isValid ? "Valid" : "Invalid"));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
            }
        });

        backButton.addActionListener(e -> {
            frame.dispose();
            try {
                String userType = dbManager.getUserTypeById(userId);
                String role = dbManager.getUserRoleById(userId);
                if (userType.equals("Salaried") && role.equals("Admin")) {
                    new AdminFrame(userId).launch();
                } else if (userType.equals("Salaried")) {
                    new SalariedUserFrame(userId).launch();
                } else {
                    new UnSalariedUserFrame(4).launch();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
            }
        });

        frame.setVisible(true);
    }
}