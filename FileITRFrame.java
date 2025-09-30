import javax.swing.*;
import taxation.db.DatabaseManager;

public class FileITRFrame {
    private int userId;
    private DatabaseManager dbManager;

    public FileITRFrame(int userId) {
        this.userId = userId;
        this.dbManager = DatabaseManager.getInstance();
    }

    public void launch() {
        JFrame frame = new JFrame("Taxation System - File ITR");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        JLabel taxYearLabel = new JLabel("Tax Year:");
        taxYearLabel.setBounds(20, 20, 100, 30);
        frame.add(taxYearLabel);

        JTextField taxYearField = new JTextField("2024");
        taxYearField.setBounds(120, 20, 200, 30);
        frame.add(taxYearField);

        JLabel docIdsLabel = new JLabel("Document IDs (comma-separated):");
        docIdsLabel.setBounds(20, 60, 200, 30);
        frame.add(docIdsLabel);

        JTextField docIdsField = new JTextField();
        docIdsField.setBounds(120, 60, 200, 30);
        frame.add(docIdsField);

        JButton fileButton = new JButton("File ITR");
        fileButton.setBounds(120, 100, 100, 30);
        frame.add(fileButton);

        JButton backButton = new JButton("Back");
        backButton.setBounds(230, 100, 100, 30);
        frame.add(backButton);

        fileButton.addActionListener(e -> {
            try {
                int taxYear = Integer.parseInt(taxYearField.getText());
                String docIds = docIdsField.getText();
                int trrId = dbManager.fileTaxReturn(userId, taxYear, docIds);
                JOptionPane.showMessageDialog(frame, "ITR Filed Successfully. TRR ID: " + trrId);
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