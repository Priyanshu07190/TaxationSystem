import javax.swing.*;
import java.util.List;
import taxation.db.DatabaseManager;

public class CheckITRStatusFrame {
    private int userId;
    private DatabaseManager dbManager;

    public CheckITRStatusFrame(int userId) {
        this.userId = userId;
        this.dbManager = DatabaseManager.getInstance();
    }

    public void launch() {
        JFrame frame = new JFrame("Taxation System - Check ITR Status");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        JButton checkButton = new JButton("Check Status");
        checkButton.setBounds(50, 20, 150, 30);
        frame.add(checkButton);

        JButton backButton = new JButton("Back");
        backButton.setBounds(50, 60, 150, 30);
        frame.add(backButton);

        checkButton.addActionListener(e -> {
            try {
                List<String> statuses = dbManager.getITRStatus(userId);
                JTextArea textArea = new JTextArea(String.join("\n", statuses));
                textArea.setEditable(false);
                JOptionPane.showMessageDialog(frame, new JScrollPane(textArea), "ITR Status", JOptionPane.INFORMATION_MESSAGE);
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