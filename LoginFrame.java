import javax.swing.*;
import java.awt.*;
import taxation.db.DatabaseManager;

public class LoginFrame {
    private DatabaseManager dbManager;
    private String userType;
    private JComboBox<String> roleCombo;

    public LoginFrame(String userType) {
        this.dbManager = DatabaseManager.getInstance();
        this.userType = userType;
    }

    public void launch() {
        JFrame frame = new JFrame("Taxation System - Login");
        frame.setSize(600, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridBagLayout());
        frame.setLocationRelativeTo(null); // Center the frame

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel titleLabel = new JLabel("Login", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        frame.add(titleLabel, gbc);

        // Email
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        frame.add(new JLabel("Email:"), gbc);

        JTextField emailField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        frame.add(emailField, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy = 2;
        frame.add(new JLabel("Password:"), gbc);

        JPasswordField passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 2;
        frame.add(passwordField, gbc);

        // User Type (label only)
        gbc.gridx = 0;
        gbc.gridy = 3;
        frame.add(new JLabel("User Type:"), gbc);

        JLabel userTypeLabel = new JLabel(userType);
        gbc.gridx = 1;
        gbc.gridy = 3;
        frame.add(userTypeLabel, gbc);

        // Role
        gbc.gridx = 0;
        gbc.gridy = 4;
        frame.add(new JLabel("Role:"), gbc);

        roleCombo = null;
        JLabel roleLabel = null;
        if (userType.equals("Salaried")) {
            String[] roles = {"User", "Admin"};
            roleCombo = new JComboBox<>(roles);
            gbc.gridx = 1;
            gbc.gridy = 4;
            frame.add(roleCombo, gbc);
        } else {
            roleLabel = new JLabel("User");
            gbc.gridx = 1;
            gbc.gridy = 4;
            frame.add(roleLabel, gbc);
        }

        // Buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");
        JButton backButton = new JButton("Back");
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonPanel.add(loginButton);
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(registerButton);
        buttonPanel.add(Box.createVerticalStrut(10));
        buttonPanel.add(backButton);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        frame.add(buttonPanel, gbc);

        loginButton.addActionListener(e -> {
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            String role = "User";
            if (userType.equals("Salaried") && roleCombo != null) {
                role = (String) roleCombo.getSelectedItem();
            }
            try {
                int userId = dbManager.loginUser(userType, role, email, password);
                if (userId > 0) {
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
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid credentials");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
            }
        });

        registerButton.addActionListener(e -> {
            frame.dispose();
            new RegistrationFrame().launch();
        });

        backButton.addActionListener(e -> {
            frame.dispose();
            new UserTypeSelectionFrame().launch();
        });

        frame.setVisible(true);
    }
}