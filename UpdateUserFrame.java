import javax.swing.*;
import taxation.db.DatabaseManager;

public class UpdateUserFrame {
    private int userId;
    private DatabaseManager dbManager;

    public UpdateUserFrame(int userId) {
        this.userId = userId;
        this.dbManager = DatabaseManager.getInstance();
    }

    public void launch() {
        JFrame frame = new JFrame("Taxation System - Update User");
        frame.setSize(400, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        JLabel targetUserIdLabel = new JLabel("Target User ID:");
        targetUserIdLabel.setBounds(20, 20, 100, 30);
        frame.add(targetUserIdLabel);

        JTextField targetUserIdField = new JTextField();
        targetUserIdField.setBounds(120, 20, 200, 30);
        frame.add(targetUserIdField);

        JLabel userTypeLabel = new JLabel("User Type:");
        userTypeLabel.setBounds(20, 60, 100, 30);
        frame.add(userTypeLabel);

        JComboBox<String> userTypeCombo = new JComboBox<>(new String[]{"Salaried", "SelfEmployed", "UnSalaried"});
        userTypeCombo.setBounds(120, 60, 200, 30);
        frame.add(userTypeCombo);

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setBounds(20, 100, 100, 30);
        frame.add(nameLabel);

        JTextField nameField = new JTextField();
        nameField.setBounds(120, 100, 200, 30);
        frame.add(nameField);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(20, 140, 100, 30);
        frame.add(emailLabel);

        JTextField emailField = new JTextField();
        emailField.setBounds(120, 140, 200, 30);
        frame.add(emailField);

        JLabel phoneLabel = new JLabel("Phone:");
        phoneLabel.setBounds(20, 180, 100, 30);
        frame.add(phoneLabel);

        JTextField phoneField = new JTextField();
        phoneField.setBounds(120, 180, 200, 30);
        frame.add(phoneField);

        JLabel addressLabel = new JLabel("Address:");
        addressLabel.setBounds(20, 220, 100, 30);
        frame.add(addressLabel);

        JTextField addressField = new JTextField();
        addressField.setBounds(120, 220, 200, 30);
        frame.add(addressField);

        JLabel panLabel = new JLabel("PAN:");
        panLabel.setBounds(20, 260, 100, 30);
        frame.add(panLabel);

        JTextField panField = new JTextField();
        panField.setBounds(120, 260, 200, 30);
        frame.add(panField);

        JLabel aadhaarLabel = new JLabel("Aadhaar:");
        aadhaarLabel.setBounds(20, 300, 100, 30);
        frame.add(aadhaarLabel);

        JTextField aadhaarField = new JTextField();
        aadhaarField.setBounds(120, 300, 200, 30);
        frame.add(aadhaarField);

        JButton updateButton = new JButton("Update");
        updateButton.setBounds(120, 340, 100, 30);
        frame.add(updateButton);

        JButton backButton = new JButton("Back");
        backButton.setBounds(230, 340, 100, 30);
        frame.add(backButton);

        updateButton.addActionListener(e -> {
            try {
                int targetUserId = Integer.parseInt(targetUserIdField.getText());
                String userType = (String) userTypeCombo.getSelectedItem();
                String name = nameField.getText();
                String email = emailField.getText();
                String phone = phoneField.getText();
                String address = addressField.getText();
                String pan = panField.getText();
                String aadhaar = aadhaarField.getText();
                dbManager.updateUser(userId, targetUserId, userType, name, email, phone, address, pan, aadhaar);
                JOptionPane.showMessageDialog(frame, "User Updated Successfully");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
            }
        });

        backButton.addActionListener(e -> {
            frame.dispose();
            if (userTypeCombo.getSelectedItem().equals("UnSalaried")) {
                new UnSalariedUserFrame(4).launch();
            } else {
                new AdminFrame(userId).launch();
            }
        });

        frame.setVisible(true);
    }
}