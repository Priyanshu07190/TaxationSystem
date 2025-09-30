import javax.swing.*;
import taxation.db.DatabaseManager;

public class LinkAadharFrame {
    private int userId;
    private DatabaseManager dbManager;

    public LinkAadharFrame(int userId) {
        this.userId = userId;
        this.dbManager = DatabaseManager.getInstance();
    }

    public void launch() {
        JFrame frame = new JFrame("Taxation System - Link Aadhaar");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        JLabel aadhaarLabel = new JLabel("Aadhaar Number:");
        aadhaarLabel.setBounds(20, 20, 100, 30);
        frame.add(aadhaarLabel);

        JTextField aadhaarField = new JTextField();
        aadhaarField.setBounds(120, 20, 200, 30);
        frame.add(aadhaarField);

        JLabel panLabel = new JLabel("PAN Number:");
        panLabel.setBounds(20, 60, 100, 30);
        frame.add(panLabel);

        JTextField panField = new JTextField();
        panField.setBounds(120, 60, 200, 30);
        frame.add(panField);

        JButton linkButton = new JButton("Link");
        linkButton.setBounds(120, 100, 100, 30);
        frame.add(linkButton);

        JButton backButton = new JButton("Back");
        backButton.setBounds(230, 100, 100, 30);
        frame.add(backButton);

        linkButton.addActionListener(e -> {
            String aadhaar = aadhaarField.getText();
            String pan = panField.getText();
            try {
                boolean linked = dbManager.linkAadhar(userId, aadhaar, pan);
                JOptionPane.showMessageDialog(frame, linked ? "Aadhaar Linked Successfully" : "Failed to Link Aadhaar");
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
                    new SelfEmployedUserFrame(userId).launch();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
            }
        });

        frame.setVisible(true);
    }
}