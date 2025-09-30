import javax.swing.*;
import java.awt.*;

public class UserTypeSelectionFrame {
    public void launch() {
        JFrame frame = new JFrame("Taxation System - Select User Type");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridBagLayout());
        frame.setLocationRelativeTo(null);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel titleLabel = new JLabel("Select User Type", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        frame.add(titleLabel, gbc);

        // Buttons
        JButton salariedButton = new JButton("Salaried");
        salariedButton.setFont(new Font("Arial", Font.PLAIN, 16));
        salariedButton.setPreferredSize(new Dimension(200, 40));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        frame.add(salariedButton, gbc);

        JButton selfEmployedButton = new JButton("Self Employed");
        selfEmployedButton.setFont(new Font("Arial", Font.PLAIN, 16));
        selfEmployedButton.setPreferredSize(new Dimension(200, 40));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        frame.add(selfEmployedButton, gbc);

        JButton unSalariedButton = new JButton("UnSalaried");
        unSalariedButton.setFont(new Font("Arial", Font.PLAIN, 16));
        unSalariedButton.setPreferredSize(new Dimension(200, 40));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        frame.add(unSalariedButton, gbc);

        salariedButton.addActionListener(e -> {
            frame.dispose();
            new LoginFrame("Salaried").launch();
        });

        selfEmployedButton.addActionListener(e -> {
            frame.dispose();
            new LoginFrame("SelfEmployed").launch();
        });

        unSalariedButton.addActionListener(e -> {
            frame.dispose();
            new UnSalariedUserFrame(4).launch();
        });

        frame.setVisible(true);
    }
}