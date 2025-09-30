import javax.swing.*;
import taxation.db.DatabaseManager;

public class GrievanceFrame {
    private int userId;
    private DatabaseManager dbManager;

    public GrievanceFrame(int userId) {
        this.userId = userId;
        this.dbManager = DatabaseManager.getInstance();
    }

    public void launch() {
        JFrame frame = new JFrame("Taxation System - Register Grievance");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        JLabel descLabel = new JLabel("Description:");
        descLabel.setBounds(20, 20, 100, 30);
        frame.add(descLabel);

        JTextArea descArea = new JTextArea();
        descArea.setBounds(120, 20, 200, 100);
        frame.add(descArea);

        JButton submitButton = new JButton("Submit");
        submitButton.setBounds(120, 140, 100, 30);
        frame.add(submitButton);

        JButton backButton = new JButton("Back");
        backButton.setBounds(230, 140, 100, 30);
        frame.add(backButton);

        submitButton.addActionListener(e -> {
            String description = descArea.getText();
            try {
                int grievanceId = dbManager.raiseGrievance(userId, description);
                JOptionPane.showMessageDialog(frame, "Grievance Registered. ID: " + grievanceId);
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