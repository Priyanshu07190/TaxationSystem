import javax.swing.*;
import java.util.List;
import taxation.db.DatabaseManager;

public class TaxCalendarFrame {
    private int userId;
    private DatabaseManager dbManager;

    public TaxCalendarFrame(int userId) {
        this.userId = userId;
        this.dbManager = DatabaseManager.getInstance();
    }

    public void launch() {
        JFrame frame = new JFrame("Taxation System - Tax Calendar");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        JButton viewButton = new JButton("View Calendar");
        viewButton.setBounds(50, 20, 150, 30);
        frame.add(viewButton);

        JButton backButton = new JButton("Back");
        backButton.setBounds(50, 60, 150, 30);
        frame.add(backButton);

        viewButton.addActionListener(e -> {
            try {
                System.out.println("[DEBUG] Fetching tax calendar for userId: " + userId);
                List<String> events = dbManager.getTaxCalendar(userId);
                System.out.println("[DEBUG] Events fetched: " + (events == null ? "null" : events.size()));
                if (events == null || events.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "No tax calendar events found.", "Info", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JTextArea textArea = new JTextArea(String.join("\n", events));
                    textArea.setEditable(false);
                    System.out.println("[DEBUG] Events: " + events);
                    JOptionPane.showMessageDialog(frame, new JScrollPane(textArea), "Tax Calendar", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception ex) {
                System.out.println("[ERROR] Exception fetching tax calendar: " + ex.getMessage());
                ex.printStackTrace();
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