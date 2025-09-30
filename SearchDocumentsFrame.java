import javax.swing.*;
import java.util.List;
import taxation.db.DatabaseManager;

public class SearchDocumentsFrame {
    private int userId;
    private DatabaseManager dbManager;

    public SearchDocumentsFrame(int userId) {
        this.userId = userId;
        this.dbManager = DatabaseManager.getInstance();
    }

    public void launch() {
        JFrame frame = new JFrame("Taxation System - Search Documents");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        JLabel docTypeLabel = new JLabel("Document Type:");
        docTypeLabel.setBounds(20, 20, 100, 30);
        frame.add(docTypeLabel);

        JTextField docTypeField = new JTextField();
        docTypeField.setBounds(120, 20, 200, 30);
        frame.add(docTypeField);

        JButton searchButton = new JButton("Search");
        searchButton.setBounds(120, 60, 100, 30);
        frame.add(searchButton);

        JButton backButton = new JButton("Back");
        backButton.setBounds(230, 60, 100, 30);
        frame.add(backButton);

        searchButton.addActionListener(e -> {
            String docType = docTypeField.getText();
            try {
                List<String> documents = dbManager.searchDocuments(userId, docType);
                JTextArea textArea = new JTextArea(String.join("\n", documents));
                textArea.setEditable(false);
                JOptionPane.showMessageDialog(frame, new JScrollPane(textArea), "Documents", JOptionPane.INFORMATION_MESSAGE);
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