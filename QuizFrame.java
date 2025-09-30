import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import taxation.db.DatabaseManager;
import taxation.model.QuizQuestion;

public class QuizFrame {
    private int userId;
    private DatabaseManager dbManager;
    private List<QuizQuestion> questions;
    private int currentQuestionIndex = 0;
    private int totalScore = 0;
    private String userType;
    private String role;

    public QuizFrame(int userId, String userType, String role) {
        this.userId = userId;
        this.userType = userType;
        this.role = role;
        this.dbManager = DatabaseManager.getInstance();
    }

    public void launch() {
        System.out.println("[DEBUG] QuizFrame.launch() called for userId: " + userId);
        JFrame frame = new JFrame("Tax Quiz");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        try {
            System.out.println("[DEBUG] Attempting to load quiz questions from database...");
            questions = dbManager.getQuizQuestions();
            System.out.println("[DEBUG] Loaded questions: " + (questions == null ? "null" : questions.size()));
            if (questions == null || questions.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "No questions available in the database.", "Error", JOptionPane.ERROR_MESSAGE);
                frame.setVisible(true);
                return;
            }
        } catch (SQLException e) {
            System.out.println("[ERROR] Exception while loading questions: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error loading questions: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            frame.setVisible(true);
            return;
        } catch (Exception e) {
            System.out.println("[ERROR] Unexpected exception: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Unexpected error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            frame.setVisible(true);
            return;
        }

        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel questionPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel questionLabel = new JLabel();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        questionPanel.add(questionLabel, gbc);

        ButtonGroup optionsGroup = new ButtonGroup();
        JRadioButton option1Radio = new JRadioButton();
        JRadioButton option2Radio = new JRadioButton();
        JRadioButton option3Radio = new JRadioButton();
        JRadioButton option4Radio = new JRadioButton();

        optionsGroup.add(option1Radio);
        optionsGroup.add(option2Radio);
        optionsGroup.add(option3Radio);
        optionsGroup.add(option4Radio);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        questionPanel.add(option1Radio, gbc);
        gbc.gridy = 2;
        questionPanel.add(option2Radio, gbc);
        gbc.gridy = 3;
        questionPanel.add(option3Radio, gbc);
        gbc.gridy = 4;
        questionPanel.add(option4Radio, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton submitButton = new JButton("Submit");
        JButton backButton = new JButton("Back");
        buttonPanel.add(submitButton);
        buttonPanel.add(backButton);
        backButton.setVisible(false);
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        questionPanel.add(buttonPanel, gbc);

        mainPanel.add(questionPanel, BorderLayout.CENTER);

        // Display first question
        displayQuestion(questionLabel, option1Radio, option2Radio, option3Radio, option4Radio);

        submitButton.addActionListener(e -> {
            if (!option1Radio.isSelected() && !option2Radio.isSelected() && 
                !option3Radio.isSelected() && !option4Radio.isSelected()) {
                JOptionPane.showMessageDialog(frame, "Please select an answer", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String selectedAnswer = "";
            if (option1Radio.isSelected()) selectedAnswer = "A";
            else if (option2Radio.isSelected()) selectedAnswer = "B";
            else if (option3Radio.isSelected()) selectedAnswer = "C";
            else if (option4Radio.isSelected()) selectedAnswer = "D";

            if (userId == 0) {
                // Guest mode: check answer locally, no DB write
                String correct = questions.get(currentQuestionIndex).getCorrectAnswer();
                int points = selectedAnswer.equals(correct) ? 1 : 0;
                totalScore += points;
                String feedback = (selectedAnswer.equals(correct)) ? "Correct!" : "Incorrect. Correct answer: " + correct;
                JOptionPane.showMessageDialog(frame, feedback + "\nPoints earned: " + points);
            } else {
                try {
                    int points = dbManager.submitQuizAnswer(userId, questions.get(currentQuestionIndex).getQuestionId(), selectedAnswer);
                    totalScore += points;
                    String correct = questions.get(currentQuestionIndex).getCorrectAnswer();
                    String feedback = (selectedAnswer.equals(correct)) ? "Correct!" : "Incorrect. Correct answer: " + correct;
                    JOptionPane.showMessageDialog(frame, feedback + "\nPoints earned: " + points);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(frame, "Error submitting answer: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

            currentQuestionIndex++;
            if (currentQuestionIndex < questions.size()) {
                displayQuestion(questionLabel, option1Radio, option2Radio, option3Radio, option4Radio);
                optionsGroup.clearSelection();
            } else {
                JOptionPane.showMessageDialog(frame, "Quiz completed!\nTotal Score: " + totalScore, "Success", JOptionPane.INFORMATION_MESSAGE);
                submitButton.setEnabled(false);
                backButton.setVisible(true);
            }
        });

        backButton.addActionListener(e -> {
            frame.dispose();
            try {
                if (userType.equals("Salaried") && role.equals("Admin")) {
                    new AdminFrame(userId).launch();
                } else if (userType.equals("Salaried")) {
                    new SalariedUserFrame(userId).launch();
                } else if (userType.equals("SelfEmployed")) {
                    new SelfEmployedUserFrame(userId).launch();
                } else {
                    new UnSalariedUserFrame(userId).launch();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error returning to dashboard: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        frame.add(mainPanel);
        System.out.println("[DEBUG] QuizFrame UI ready, setting visible...");
        frame.setVisible(true);
    }

    private void displayQuestion(JLabel questionLabel, JRadioButton option1Radio, 
                               JRadioButton option2Radio, JRadioButton option3Radio, 
                               JRadioButton option4Radio) {
        QuizQuestion q = questions.get(currentQuestionIndex);
        questionLabel.setText(q.getQuestionText());
        option1Radio.setText(q.getOptionA());
        option2Radio.setText(q.getOptionB());
        option3Radio.setText(q.getOptionC());
        option4Radio.setText(q.getOptionD());
    }
}