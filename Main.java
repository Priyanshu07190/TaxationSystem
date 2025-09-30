import javax.swing.*;
import taxation.db.DatabaseManager;
import java.util.logging.Logger;
import java.util.logging.Level;

public class Main {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {
        try {
            LOGGER.info("Starting application...");
            
            // Set look and feel
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                LOGGER.info("Look and feel set successfully");
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Error setting look and feel", e);
            }
            
            // Initialize database connection
            LOGGER.info("Initializing database connection...");
            DatabaseManager dbManager = DatabaseManager.getInstance();
            
            // Launch the application
            LOGGER.info("Launching application UI...");
            SwingUtilities.invokeLater(() -> {
                try {
                    LOGGER.info("Creating UserTypeSelectionFrame...");
                    UserTypeSelectionFrame frame = new UserTypeSelectionFrame();
                    LOGGER.info("Launching UserTypeSelectionFrame...");
                    frame.launch();
                    LOGGER.info("UserTypeSelectionFrame launched successfully");
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "Error launching application", e);
                    JOptionPane.showMessageDialog(null, 
                        "Error launching application: " + e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                    System.exit(1);
                }
            });
            
            // Add shutdown hook to close database connection
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                LOGGER.info("Shutting down application...");
                dbManager.close();
            }));
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Fatal error in main", e);
            JOptionPane.showMessageDialog(null, 
                "Fatal error: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }
}