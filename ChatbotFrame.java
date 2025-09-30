import javax.swing.*;
import taxation.db.DatabaseManager;

public class ChatbotFrame {
    private int userId;
    private DatabaseManager dbManager;

    public ChatbotFrame(int userId) {
        this.userId = userId;
        this.dbManager = DatabaseManager.getInstance();
    }

    public void launch() {
        JFrame frame = new JFrame("Taxation System - Chatbot");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        JLabel queryLabel = new JLabel("Query:");
        queryLabel.setBounds(20, 20, 100, 30);
        frame.add(queryLabel);

        JTextField queryField = new JTextField();
        queryField.setBounds(120, 20, 200, 30);
        frame.add(queryField);

        JButton submitButton = new JButton("Submit");
        submitButton.setBounds(120, 60, 100, 30);
        frame.add(submitButton);

        JButton backButton = new JButton("Back");
        backButton.setBounds(230, 60, 100, 30);
        frame.add(backButton);

        submitButton.addActionListener(e -> {
            String query = queryField.getText();
            try {
                if (userId == 0) {
                    // Guest mode: do not store in DB, just show a canned response
                    JOptionPane.showMessageDialog(frame, "Response: This is a guest chatbot response. Please login for personalized answers.");
                } else {
                    String response = dbManager.submitChatbotQuery(userId, query);
                    JOptionPane.showMessageDialog(frame, "Response: " + response);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
            }
        });

        backButton.addActionListener(e -> {
            frame.dispose();
            try {
                String userType = dbManager.getUserTypeById(userId);
                if (userType.equals("UnSalaried") || userId == 4) {
                    new UnSalariedUserFrame(4).launch();
                } else if (userType.equals("Salaried") && dbManager.getUserRoleById(userId).equals("Admin")) {
                    new AdminFrame(userId).launch();
                } else if (userType.equals("Salaried")) {
                    new SalariedUserFrame(userId).launch();
                } else if (userType.equals("SelfEmployed")) {
                    new SelfEmployedUserFrame(userId).launch();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
            }
        });

        frame.setVisible(true);
    }
}