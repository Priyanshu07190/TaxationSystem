import javax.swing.*;
import java.awt.*;
import taxation.db.DatabaseManager;

public class RegistrationFrame {
    private DatabaseManager dbManager;

    public RegistrationFrame() {
        this.dbManager = DatabaseManager.getInstance();
    }

    public void launch() {
        JFrame frame = new JFrame("Taxation System - Registration");
        frame.setSize(600, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new GridBagLayout());
        frame.setLocationRelativeTo(null); // Center the frame

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel titleLabel = new JLabel("Registration", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        frame.add(titleLabel, gbc);

        // User Type
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        frame.add(new JLabel("User Type:"), gbc);

        String[] userTypes = {"Salaried", "SelfEmployed", "UnSalaried"};
        JComboBox<String> userTypeCombo = new JComboBox<>(userTypes);
        gbc.gridx = 1;
        gbc.gridy = 1;
        frame.add(userTypeCombo, gbc);

        // Name
        gbc.gridx = 0;
        gbc.gridy = 2;
        frame.add(new JLabel("Name:"), gbc);

        JTextField nameField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 2;
        frame.add(nameField, gbc);

        // Email
        gbc.gridx = 0;
        gbc.gridy = 3;
        frame.add(new JLabel("Email:"), gbc);

        JTextField emailField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 3;
        frame.add(emailField, gbc);

        // Phone
        gbc.gridx = 0;
        gbc.gridy = 4;
        frame.add(new JLabel("Phone:"), gbc);

        JTextField phoneField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 4;
        frame.add(phoneField, gbc);

        // Address
        gbc.gridx = 0;
        gbc.gridy = 5;
        frame.add(new JLabel("Address:"), gbc);

        JTextField addressField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 5;
        frame.add(addressField, gbc);

        // PAN
        gbc.gridx = 0;
        gbc.gridy = 6;
        frame.add(new JLabel("PAN:"), gbc);

        JTextField panField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 6;
        frame.add(panField, gbc);

        // Aadhaar
        gbc.gridx = 0;
        gbc.gridy = 7;
        frame.add(new JLabel("Aadhaar:"), gbc);

        JTextField aadhaarField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 7;
        frame.add(aadhaarField, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy = 8;
        frame.add(new JLabel("Password:"), gbc);

        JPasswordField passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 8;
        frame.add(passwordField, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton registerButton = new JButton("Register");
        JButton backButton = new JButton("Back");
        buttonPanel.add(registerButton);
        buttonPanel.add(backButton);

        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = 2;
        frame.add(buttonPanel, gbc);

        registerButton.addActionListener(e -> {
            String userType = (String) userTypeCombo.getSelectedItem();
            String name = nameField.getText();
            String email = emailField.getText();
            String phone = phoneField.getText();
            String address = addressField.getText();
            String pan = panField.getText();
            String aadhaar = aadhaarField.getText();
            String password = new String(passwordField.getPassword());

            if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || address.isEmpty() || pan.isEmpty() || aadhaar.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please fill in all fields");
                return;
            }

            try {
                int userId = dbManager.registerUser(userType, name, email, phone, address, pan, aadhaar, password);
                JOptionPane.showMessageDialog(frame, "Registration successful! Your User ID is: " + userId);
                frame.dispose();
                new LoginFrame(userType).launch();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
            }
        });

        backButton.addActionListener(e -> {
            frame.dispose();
            new UserTypeSelectionFrame().launch();
        });

        frame.setVisible(true);
    }
} 