import javax.swing.*;
import java.util.List;
import taxation.db.DatabaseManager;

public class TaxPreparerFrame {
    private int userId;
    private DatabaseManager dbManager;

    public TaxPreparerFrame(int userId) {
        this.userId = userId;
        this.dbManager = DatabaseManager.getInstance();
    }

    public void launch() {
        JFrame frame = new JFrame("Taxation System - Locate Tax Preparer");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        JLabel locationLabel = new JLabel("Location:");
        locationLabel.setBounds(20, 20, 100, 30);
        frame.add(locationLabel);

        JTextField locationField = new JTextField();
        locationField.setBounds(120, 20, 200, 30);
        frame.add(locationField);

        JButton searchButton = new JButton("Search");
        searchButton.setBounds(120, 60, 100, 30);
        frame.add(searchButton);

        JButton backButton = new JButton("Back");
        backButton.setBounds(230, 60, 100, 30);
        frame.add(backButton);

        searchButton.addActionListener(e -> {
            String location = locationField.getText();
            try {
                List<String> preparers = dbManager.locateTaxPreparer(location);
                JTextArea textArea = new JTextArea(String.join("\n", preparers));
                textArea.setEditable(false);
                JOptionPane.showMessageDialog(frame, new JScrollPane(textArea), "Tax Preparers", JOptionPane.INFORMATION_MESSAGE);
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