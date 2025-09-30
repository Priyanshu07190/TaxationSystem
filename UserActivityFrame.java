import javax.swing.*;
import java.util.List;
import taxation.db.DatabaseManager;

public class UserActivityFrame {
    private int userId;
    private boolean isAdmin;
    private DatabaseManager dbManager;

    public UserActivityFrame(int userId, boolean isAdmin) {
        this.userId = userId;
        this.isAdmin = isAdmin;
        this.dbManager = DatabaseManager.getInstance();
    }

    public void launch() {
        JFrame frame = new JFrame("Taxation System - View User Activities");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null); // Center the frame

        // View Activities Button
        JButton viewButton = new JButton("View Activities");
        viewButton.setBounds(50, 30, 150, 30);
        frame.add(viewButton);

        // Back Button
        JButton backButton = new JButton("Back");
        backButton.setBounds(50, 70, 150, 30);
        frame.add(backButton);

        // Action Listener for View Activities
        viewButton.addActionListener(e -> {
            try {
                // Fetch activities from DatabaseManager
                List<String> activities = dbManager.getUserActivities(userId, isAdmin);
                if (activities.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "No activities found.", "User Activities", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    // Display activities in a scrollable text area
                    JTextArea textArea = new JTextArea(10, 30);
                    textArea.setText(String.join("\n", activities));
                    textArea.setEditable(false);
                    JScrollPane scrollPane = new JScrollPane(textArea);
                    JOptionPane.showMessageDialog(frame, scrollPane, "User Activities", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Error fetching activities: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Action Listener for Back Button
        backButton.addActionListener(e -> {
            frame.dispose();
            try {
                String userType = dbManager.getUserTypeById(userId);
                String role = dbManager.getUserRoleById(userId);
                if ("Salaried".equals(userType) && "Admin".equals(role)) {
                    new AdminFrame(userId).launch();
                } else if ("Salaried".equals(userType)) {
                    new SalariedUserFrame(userId).launch();
                } else if ("SelfEmployed".equals(userType)) {
                    new SelfEmployedUserFrame(userId).launch();
                } else {
                    new UnSalariedUserFrame(4).launch();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error navigating back: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                new UserTypeSelectionFrame().launch(); // Fallback to user type selection
            }
        });

        frame.setVisible(true);
    }
}