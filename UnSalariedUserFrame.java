import javax.swing.*;

public class UnSalariedUserFrame {
    private int userId;

    public UnSalariedUserFrame(int userId) {
        this.userId = userId;
    }

    public void launch() {
        JFrame frame = new JFrame("Taxation System - UnSalaried User");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        JButton calcTaxButton = new JButton("Calculate Tax");
        calcTaxButton.setBounds(50, 30, 150, 30);
        frame.add(calcTaxButton);

        JButton quizButton = new JButton("Take Quiz");
        quizButton.setBounds(50, 70, 150, 30);
        frame.add(quizButton);

        JButton calendarButton = new JButton("Tax Calendar");
        calendarButton.setBounds(50, 110, 150, 30);
        frame.add(calendarButton);

        JButton chatbotButton = new JButton("Chatbot");
        chatbotButton.setBounds(50, 150, 150, 30);
        frame.add(chatbotButton);

        JButton backButton = new JButton("Back");
        backButton.setBounds(50, 190, 150, 30);
        frame.add(backButton);

        calcTaxButton.addActionListener(e -> {
            frame.dispose();
            new TaxCalculatorFrame(userId, "UnSalaried", "User").launch();
        });

        quizButton.addActionListener(e -> {
            try {
                System.out.println("[DEBUG] Take Quiz clicked for userId: " + userId);
                JOptionPane.showMessageDialog(frame, "Launching QuizFrame for userId: " + userId);
                frame.dispose();
                new QuizFrame(userId, "UnSalaried", "User").launch();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error launching quiz: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        calendarButton.addActionListener(e -> {
            frame.dispose();
            new TaxCalendarFrame(userId).launch();
        });

        chatbotButton.addActionListener(e -> {
            frame.dispose();
            new ChatbotFrame(userId).launch();
        });

        backButton.addActionListener(e -> {
            frame.dispose();
            new UserTypeSelectionFrame().launch();
        });

        frame.setVisible(true);
    }
}