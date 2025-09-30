import javax.swing.*;
import java.util.List;
import taxation.db.DatabaseManager;

public class SelfEmployedUserFrame {
    private int userId;
    private DatabaseManager dbManager;

    public SelfEmployedUserFrame(int userId) {
        this.userId = userId;
        this.dbManager = DatabaseManager.getInstance();
    }

    public void launch() {
        JFrame frame = new JFrame("Taxation System - Self Employed User");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        JButton calcTaxButton = new JButton("Calculate Tax");
        calcTaxButton.setBounds(50, 30, 150, 30);
        frame.add(calcTaxButton);

        JButton fileITRButton = new JButton("File ITR");
        fileITRButton.setBounds(50, 70, 150, 30);
        frame.add(fileITRButton);

        JButton checkITRButton = new JButton("Check ITR Status");
        checkITRButton.setBounds(50, 110, 150, 30);
        frame.add(checkITRButton);

        JButton grievanceButton = new JButton("Register Grievance");
        grievanceButton.setBounds(50, 150, 150, 30);
        frame.add(grievanceButton);

        JButton quizButton = new JButton("Take Quiz");
        quizButton.setBounds(50, 190, 150, 30);
        frame.add(quizButton);

        JButton calendarButton = new JButton("Tax Calendar");
        calendarButton.setBounds(50, 230, 150, 30);
        frame.add(calendarButton);

        JButton uploadDocButton = new JButton("Upload Document");
        uploadDocButton.setBounds(50, 270, 150, 30);
        frame.add(uploadDocButton);

        JButton taxPreparerButton = new JButton("Locate Tax Preparer");
        taxPreparerButton.setBounds(50, 310, 150, 30);
        frame.add(taxPreparerButton);

        JButton chatbotButton = new JButton("Chatbot");
        chatbotButton.setBounds(250, 30, 150, 30);
        frame.add(chatbotButton);

        JButton tdsButton = new JButton("Calculate TDS");
        tdsButton.setBounds(250, 70, 150, 30);
        frame.add(tdsButton);

        JButton linkAadharButton = new JButton("Link Aadhaar");
        linkAadharButton.setBounds(250, 110, 150, 30);
        frame.add(linkAadharButton);

        JButton searchDocButton = new JButton("Search Documents");
        searchDocButton.setBounds(250, 150, 150, 30);
        frame.add(searchDocButton);

        JButton activityButton = new JButton("View Activity");
        activityButton.setBounds(250, 190, 150, 30);
        frame.add(activityButton);

        JButton verifyPanButton = new JButton("Verify PAN");
        verifyPanButton.setBounds(250, 230, 150, 30);
        frame.add(verifyPanButton);

        JButton backButton = new JButton("Back");
        backButton.setBounds(250, 270, 150, 30);
        frame.add(backButton);

        calcTaxButton.addActionListener(e -> {
            frame.dispose();
            new TaxCalculatorFrame(userId, "SelfEmployed", "User").launch();
        });

        fileITRButton.addActionListener(e -> {
            frame.dispose();
            new FileITRFrame(userId).launch();
        });

        checkITRButton.addActionListener(e -> {
            frame.dispose();
            new CheckITRStatusFrame(userId).launch();
        });

        grievanceButton.addActionListener(e -> {
            frame.dispose();
            new GrievanceFrame(userId).launch();
        });

        quizButton.addActionListener(e -> {
            try {
                System.out.println("[DEBUG] Take Quiz clicked for userId: " + userId);
                JOptionPane.showMessageDialog(frame, "Launching QuizFrame for userId: " + userId);
                frame.dispose();
                new QuizFrame(userId, "SelfEmployed", "User").launch();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error launching quiz: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        calendarButton.addActionListener(e -> {
            frame.dispose();
            new TaxCalendarFrame(userId).launch();
        });

        uploadDocButton.addActionListener(e -> {
            frame.dispose();
            new DocumentUploadFrame(userId).launch();
        });

        taxPreparerButton.addActionListener(e -> {
            frame.dispose();
            new TaxPreparerFrame(userId).launch();
        });

        chatbotButton.addActionListener(e -> {
            frame.dispose();
            new ChatbotFrame(userId).launch();
        });

        tdsButton.addActionListener(e -> {
            frame.dispose();
            new TDSFrame(userId).launch();
        });

        linkAadharButton.addActionListener(e -> {
            frame.dispose();
            new LinkAadharFrame(userId).launch();
        });

        searchDocButton.addActionListener(e -> {
            String docType = JOptionPane.showInputDialog(frame, "Enter document type to search:");
            if (docType != null) {
                try {
                    List<String> documents = dbManager.searchDocuments(userId, docType);
                    JTextArea textArea = new JTextArea(String.join("\n", documents));
                    textArea.setEditable(false);
                    JOptionPane.showMessageDialog(frame, new JScrollPane(textArea), "Documents", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
                }
            }
        });

        activityButton.addActionListener(e -> {
            frame.dispose();
            new UserActivityFrame(userId, false).launch();
        });

        verifyPanButton.addActionListener(e -> {
            frame.dispose();
            new VerifyPANFrame(userId).launch();
        });

        backButton.addActionListener(e -> {
            frame.dispose();
            new UserTypeSelectionFrame().launch();
        });

        frame.setVisible(true);
    }
}