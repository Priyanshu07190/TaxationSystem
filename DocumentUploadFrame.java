import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import taxation.db.DatabaseManager;

public class DocumentUploadFrame {
    private int userId;
    private DatabaseManager dbManager;

    public DocumentUploadFrame(int userId) {
        this.userId = userId;
        this.dbManager = DatabaseManager.getInstance();
    }

    public void launch() {
        JFrame frame = new JFrame("Upload Document");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new GridLayout(5, 2, 10, 10));

        JLabel typeLabel = new JLabel("Document Type:");
        JComboBox<String> typeCombo = new JComboBox<>(new String[]{"Form 16", "PAN", "Aadhaar", "Bank Statement"});

        JLabel pathLabel = new JLabel("File Path:");
        JTextField pathField = new JTextField();

        JLabel sizeLabel = new JLabel("File Size (KB):");
        JTextField sizeField = new JTextField();

        JButton uploadButton = new JButton("Upload");
        JButton backButton = new JButton("Back");

        uploadButton.addActionListener(e -> {
            try {
                String docType = (String) typeCombo.getSelectedItem();
                String filePath = pathField.getText();
                int fileSize = Integer.parseInt(sizeField.getText());

                if (userId == 0) {
                    // Just show chatbot responses, do not store queries in DB
                } else {
                    int docId = dbManager.uploadDocument(userId, docType, filePath, fileSize);
                    JOptionPane.showMessageDialog(frame, "Document uploaded successfully. ID: " + docId);
                    frame.dispose();
                    navigateBack();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Please enter a valid file size", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(frame, "Error uploading document: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        backButton.addActionListener(e -> {
            frame.dispose();
            navigateBack();
        });

        frame.add(typeLabel);
        frame.add(typeCombo);
        frame.add(pathLabel);
        frame.add(pathField);
        frame.add(sizeLabel);
        frame.add(sizeField);
        frame.add(uploadButton);
        frame.add(backButton);

        frame.setVisible(true);
    }

    private void navigateBack() {
        try {
            String userType = dbManager.getUserTypeById(userId);
            String role = dbManager.getUserRoleById(userId);
            if (userType.equals("Salaried") && role.equals("Admin")) {
                new AdminFrame(userId).launch();
            } else if (userType.equals("Salaried")) {
                new SalariedUserFrame(userId).launch();
            } else if (userType.equals("SelfEmployed")) {
                new SelfEmployedUserFrame(userId).launch();
            } else if (userType.equals("UnSalaried")) {
                new UnSalariedUserFrame(42).launch();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}