package taxation.db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import taxation.model.*;
import javax.swing.JOptionPane;

public class DatabaseManager {
    private static final String URL = "jdbc:mysql://localhost:3306/IndianTaxationSystem?useSSL=false";
    private static final String USER = "root";
    private static final String PASSWORD = "Priyanshu@2005";
    private static final Logger LOGGER = Logger.getLogger(DatabaseManager.class.getName());
    
    private static DatabaseManager instance;
    private Connection connection;
    
    private DatabaseManager() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            LOGGER.info("Database connection established successfully");
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "MySQL JDBC Driver not found", e);
            JOptionPane.showMessageDialog(null, 
                "MySQL JDBC Driver not found. Please ensure the MySQL connector JAR is in the classpath.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error connecting to database", e);
            JOptionPane.showMessageDialog(null, 
                "Error connecting to database: " + e.getMessage() + "\nPlease ensure MySQL is running and the database exists.",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }
    
    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }
    
    public Connection getConnection() {
        return connection;
    }
    
    public boolean login(String email, String password, String role) {
        String query = "SELECT * FROM User WHERE Email = ? AND Password = ? AND Role = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, email);
            stmt.setString(2, password);
            stmt.setString(3, role);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error during login", e);
            return false;
        }
    }

    public boolean register(String userType, String name, String email, String phone, 
                          String address, String pan, String aadhaar, String password) {
        String query = "INSERT INTO User (UserType, Name, Email, Phone, Address, PAN, AadhaarNumber, RegistrationDate, Role, Password) " +
                      "VALUES (?, ?, ?, ?, ?, ?, ?, CURDATE(), 'User', ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, userType);
            stmt.setString(2, name);
            stmt.setString(3, email);
            stmt.setString(4, phone);
            stmt.setString(5, address);
            stmt.setString(6, pan);
            stmt.setString(7, aadhaar);
            stmt.setString(8, password);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error during registration", e);
            return false;
        }
    }
    
    public void logActivity(int userId, String activityDescription) {
        String query = "INSERT INTO UserActivity (UserID, ActivityDescription, ActivityDate) VALUES (?, ?, NOW())";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setString(2, activityDescription);
            stmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error logging activity", e);
        }
    }
    
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error closing database connection", e);
        }
    }

    public int loginUser(String userType, String role, String email, String password) throws SQLException {
        String query = "SELECT UserID FROM User WHERE UserType = ? AND Role = ? AND Email = ? AND Password = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, userType);
            stmt.setString(2, role);
            stmt.setString(3, email);
            stmt.setString(4, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("UserID");
            }
            throw new SQLException("Invalid credentials or user type/role.");
        }
    }

    public int registerUser(String userType, String name, String email, String phone, 
                          String address, String pan, String aadhaar, String password) throws SQLException {
        String query = "INSERT INTO User (UserType, Name, Email, Phone, Address, PAN, AadhaarNumber, RegistrationDate, Role, Password) " +
                      "VALUES (?, ?, ?, ?, ?, ?, ?, CURDATE(), 'User', ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, userType);
            stmt.setString(2, name);
            stmt.setString(3, email);
            stmt.setString(4, phone);
            stmt.setString(5, address);
            stmt.setString(6, pan);
            stmt.setString(7, aadhaar);
            stmt.setString(8, password);
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
            throw new SQLException("Registration failed.");
        }
    }

    public String getUserTypeById(int userId) throws SQLException {
        String query = "SELECT UserType FROM User WHERE UserID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("UserType");
            }
            throw new SQLException("User not found.");
        }
    }

    public String getUserRoleById(int userId) throws SQLException {
        String query = "SELECT Role FROM User WHERE UserID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("Role");
            }
            throw new SQLException("User not found.");
        }
    }

    public double calculateTax(int userId, String taxType, double amount, int taxYear) throws SQLException {
        String query = "{CALL CalculateTax(?, ?, ?, ?, ?)}";
        try (CallableStatement stmt = connection.prepareCall(query)) {
            stmt.setInt(1, userId);
            stmt.setString(2, taxType);
            stmt.setDouble(3, amount);
            stmt.setInt(4, taxYear);
            stmt.registerOutParameter(5, Types.DOUBLE);
            stmt.execute();
            return stmt.getDouble(5);
        }
    }

    public int fileTaxReturn(int userId, int taxYear, String documentIds) throws SQLException {
        String query = "{CALL FileTaxReturn(?, ?, ?, ?)}";
        try (CallableStatement stmt = connection.prepareCall(query)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, taxYear);
            stmt.setString(3, documentIds);
            stmt.registerOutParameter(4, Types.INTEGER);
            stmt.execute();
            return stmt.getInt(4);
        }
    }

    public List<String> getITRStatus(int userId) throws SQLException {
        List<String> statuses = new ArrayList<>();
        String query = "CALL CheckITRStatus(?)";
        try (CallableStatement stmt = connection.prepareCall(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                statuses.add("TRR ID: " + rs.getInt("TRRID") + 
                           ", Year: " + rs.getInt("TaxYear") + 
                           ", Status: " + rs.getString("Status"));
            }
        }
        return statuses;
    }

    public int raiseGrievance(int userId, String description) throws SQLException {
        String query = "{CALL RaiseGrievance(?, ?, ?)}";
        try (CallableStatement stmt = connection.prepareCall(query)) {
            stmt.setInt(1, userId);
            stmt.setString(2, description);
            stmt.registerOutParameter(3, Types.INTEGER);
            stmt.execute();
            return stmt.getInt(3);
        }
    }

    public List<QuizQuestion> getQuizQuestions() throws SQLException {
        List<QuizQuestion> questions = new ArrayList<>();
        String query = "CALL GetQuizQuestions()";
        try (CallableStatement stmt = connection.prepareCall(query)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                questions.add(new QuizQuestion(
                    rs.getInt("QuestionID"),
                    rs.getString("QuestionText"),
                    rs.getString("OptionA"),
                    rs.getString("OptionB"),
                    rs.getString("OptionC"),
                    rs.getString("OptionD"),
                    rs.getString("CorrectAnswer")
                ));
            }
        }
        return questions;
    }

    public int submitQuizAnswer(int userId, int questionId, String answer) throws SQLException {
        String query = "{CALL SubmitQuizAnswer(?, ?, ?, ?)}";
        try (CallableStatement stmt = connection.prepareCall(query)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, questionId);
            stmt.setString(3, answer);
            stmt.registerOutParameter(4, Types.INTEGER);
            stmt.execute();
            return stmt.getInt(4);
        }
    }

    public List<String> getTaxCalendar(int userId) throws SQLException {
        List<String> events = new ArrayList<>();
        String query = "CALL ViewTaxCalendar(?)";
        try (CallableStatement stmt = connection.prepareCall(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                events.add("Date: " + rs.getDate("EventDate") + 
                         ", Type: " + rs.getString("EventType") + 
                         ", Description: " + rs.getString("Description"));
            }
        }
        return events;
    }

    public int uploadDocument(int userId, String docType, String filePath, int fileSize) throws SQLException {
        String query = "{CALL UploadDocument(?, ?, ?, ?, ?)}";
        try (CallableStatement stmt = connection.prepareCall(query)) {
            stmt.setInt(1, userId);
            stmt.setString(2, docType);
            stmt.setString(3, filePath);
            stmt.setInt(4, fileSize);
            stmt.registerOutParameter(5, Types.INTEGER);
            stmt.execute();
            return stmt.getInt(5);
        }
    }

    public List<String> locateTaxPreparer(String location) throws SQLException {
        List<String> preparers = new ArrayList<>();
        String query = "CALL LocateTaxPreparer(?)";
        try (CallableStatement stmt = connection.prepareCall(query)) {
            stmt.setString(1, location);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                preparers.add("Name: " + rs.getString("Name") + 
                            ", Contact: " + rs.getString("ContactInfo") + 
                            ", Location: " + rs.getString("Location") + 
                            ", Specialization: " + rs.getString("Specialization"));
            }
        }
        return preparers;
    }

    public String submitChatbotQuery(int userId, String query) throws SQLException {
        String sql = "{CALL SubmitChatbotQuery(?, ?, ?)}";
        try (CallableStatement stmt = connection.prepareCall(sql)) {
            stmt.setInt(1, userId);
            stmt.setString(2, query);
            stmt.registerOutParameter(3, Types.VARCHAR);
            stmt.execute();
            return stmt.getString(3);
        }
    }

    public double calculateTDS(int userId, double amount, double tdsRate) throws SQLException {
        String query = "{CALL CalculateTDS(?, ?, ?, ?)}";
        try (CallableStatement stmt = connection.prepareCall(query)) {
            stmt.setInt(1, userId);
            stmt.setDouble(2, amount);
            stmt.setDouble(3, tdsRate);
            stmt.registerOutParameter(4, Types.DOUBLE);
            stmt.execute();
            return stmt.getDouble(4);
        }
    }

    public void updateTaxSlab(int userId, int slabId, int taxYear, String taxType, double lowerLimit, Double upperLimit, double taxRate) throws SQLException {
        String query = "CALL UpdateTaxSlab(?, ?, ?, ?, ?, ?, ?)";
        try (CallableStatement stmt = connection.prepareCall(query)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, slabId);
            stmt.setInt(3, taxYear);
            stmt.setString(4, taxType);
            stmt.setDouble(5, lowerLimit);
            stmt.setObject(6, upperLimit);
            stmt.setDouble(7, taxRate);
            stmt.execute();
        }
    }

    public void deleteUser(int adminId, int userId) throws SQLException {
        String query = "CALL DeleteUser(?, ?)";
        try (CallableStatement stmt = connection.prepareCall(query)) {
            stmt.setInt(1, adminId);
            stmt.setInt(2, userId);
            stmt.execute();
        }
    }

    public void updateUser(int adminId, int userId, String userType, String name, String email, String phone, String address, String pan, String aadhaar) throws SQLException {
        String query = "CALL UpdateUser(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (CallableStatement stmt = connection.prepareCall(query)) {
            stmt.setInt(1, adminId);
            stmt.setInt(2, userId);
            stmt.setString(3, userType);
            stmt.setString(4, name);
            stmt.setString(5, email);
            stmt.setString(6, phone);
            stmt.setString(7, address);
            stmt.setString(8, pan);
            stmt.setString(9, aadhaar);
            stmt.execute();
        }
    }

    public List<String> getUserActivities(int userId, boolean isAdmin) throws SQLException {
        List<String> activities = new ArrayList<>();
        String query = "CALL ViewUserActivity(?, ?)";
        try (CallableStatement stmt = connection.prepareCall(query)) {
            stmt.setInt(1, userId);
            stmt.setBoolean(2, isAdmin);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                activities.add("Activity: " + rs.getString("ActivityType") + 
                             ", Date: " + rs.getTimestamp("ActivityDate") + 
                             ", Description: " + rs.getString("Description") + 
                             ", Role: " + rs.getString("UserRole"));
            }
        }
        return activities;
    }

    public List<String> searchDocuments(int userId, String docType) throws SQLException {
        List<String> documents = new ArrayList<>();
        String query = "CALL SearchDocuments(?, ?)";
        try (CallableStatement stmt = connection.prepareCall(query)) {
            stmt.setInt(1, userId);
            stmt.setString(2, docType);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                documents.add("Document ID: " + rs.getInt("DocumentID") + 
                            ", Type: " + rs.getString("DocumentType") + 
                            ", Status: " + rs.getString("Status") + 
                            ", Upload Date: " + rs.getDate("UploadDate"));
            }
        }
        return documents;
    }

    public boolean verifyPan(String pan) throws SQLException {
        String query = "CALL VerifyPAN(?)";
        try (CallableStatement stmt = connection.prepareCall(query)) {
            stmt.setString(1, pan);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        }
    }

    public boolean linkAadhar(int userId, String aadhaar, String pan) throws SQLException {
        String query = "UPDATE User SET AadhaarNumber = ? WHERE UserID = ? AND PAN = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, aadhaar);
            stmt.setInt(2, userId);
            stmt.setString(3, pan);
            return stmt.executeUpdate() > 0;
        }
    }

    public void insertTaxCalculation(int userId, String taxType, double amount, int taxYear, double calculatedTax) throws SQLException {
        String query = "INSERT INTO TaxCalculation (UserID, TaxType, IncomeAmount, TaxableIncome, CalculatedTax, CalculationDate) VALUES (?, ?, ?, ?, ?, CURDATE())";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setString(2, taxType);
            stmt.setDouble(3, amount);
            stmt.setDouble(4, amount); // TaxableIncome same as amount for now
            stmt.setDouble(5, calculatedTax);
            stmt.executeUpdate();
        }
    }
}