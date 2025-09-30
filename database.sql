-- Database Creation
CREATE DATABASE IndianTaxationSystem;
USE IndianTaxationSystem;

-- User Table
CREATE TABLE User (
    UserID INT AUTO_INCREMENT PRIMARY KEY,
    UserType ENUM('Salaried', 'SelfEmployed', 'UnSalaried') NOT NULL,
    Name VARCHAR(100) NOT NULL,
    Email VARCHAR(100) UNIQUE NOT NULL,
    Phone VARCHAR(15),
    Address TEXT,
    PAN VARCHAR(10) UNIQUE,
    AadhaarNumber VARCHAR(12) UNIQUE,
    RegistrationDate DATE NOT NULL,
    Role ENUM('User', 'Admin') NOT NULL DEFAULT 'User',
    Password VARCHAR(255) NOT NULL
);

-- Trigger to Enforce Admin Role Restriction
DELIMITER //
CREATE TRIGGER enforce_admin_role
BEFORE INSERT ON User
FOR EACH ROW
BEGIN
    IF NEW.Role = 'Admin' AND NEW.UserType != 'Salaried' THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Only Salaried users can be assigned the Admin role';
    END IF;
END //

CREATE TRIGGER enforce_admin_role_update
BEFORE UPDATE ON User
FOR EACH ROW
BEGIN
    IF NEW.Role = 'Admin' AND NEW.UserType != 'Salaried' THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Only Salaried users can be assigned the Admin role';
    END IF;
END //
DELIMITER ;

-- TaxCalculation Table
CREATE TABLE TaxCalculation (
    CalculationID INT AUTO_INCREMENT PRIMARY KEY,
    UserID INT NOT NULL,
    TaxType ENUM('Income', 'Property', 'Corporate', 'GST', 'SalesTax', 'ExciseTax', 'WealthTax') NOT NULL,
    IncomeAmount DECIMAL(15,2),
    TaxableIncome DECIMAL(15,2),
    CalculatedTax DECIMAL(15,2),
    CalculationDate DATE,
    FOREIGN KEY (UserID) REFERENCES User(UserID) ON DELETE CASCADE
);

-- TaxCalendar Table
CREATE TABLE TaxCalendar (
    CalendarID INT AUTO_INCREMENT PRIMARY KEY,
    UserID INT NOT NULL,
    EventDate DATE NOT NULL,
    EventType ENUM('FilingDeadline', 'PaymentDue') NOT NULL,
    Description TEXT,
    FOREIGN KEY (UserID) REFERENCES User(UserID) ON DELETE CASCADE
);

-- ChatbotInteraction Table
CREATE TABLE ChatbotInteraction (
    InteractionID INT AUTO_INCREMENT PRIMARY KEY,
    UserID INT NOT NULL,
    QueryText TEXT,
    ResponseText TEXT,
    InteractionDate DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (UserID) REFERENCES User(UserID) ON DELETE CASCADE
);

-- Gamification Table
CREATE TABLE Gamification (
    GamificationID INT AUTO_INCREMENT PRIMARY KEY,
    UserID INT NOT NULL,
    PointsEarned INT DEFAULT 0,
    QuizPoints INT DEFAULT 0,
    Level INT DEFAULT 1,
    Achievement VARCHAR(100),
    EarnedDate DATE,
    LastQuizDate DATE,
    FOREIGN KEY (UserID) REFERENCES User(UserID) ON DELETE CASCADE
);

-- Trigger to Update Gamification Level
DELIMITER //
CREATE TRIGGER update_gamification_level
AFTER UPDATE ON Gamification
FOR EACH ROW
BEGIN
    DECLARE newLevel INT;
    SET newLevel = FLOOR((NEW.PointsEarned + NEW.QuizPoints) / 100) + 1;
    
    IF newLevel != NEW.Level THEN
        UPDATE Gamification
        SET Level = newLevel,
            Achievement = CONCAT('Reached Level ', newLevel),
            EarnedDate = CURDATE()
        WHERE UserID = NEW.UserID;
        
        INSERT INTO UserActivity (UserID, ActivityType, Description, UserRole)
        VALUES (NEW.UserID, 'LevelUp', CONCAT('User reached level ', newLevel), (SELECT Role FROM User WHERE UserID = NEW.UserID));
    END IF;
END //
DELIMITER ;

-- QuizQuestions Table
CREATE TABLE QuizQuestions (
    QuestionID INT AUTO_INCREMENT PRIMARY KEY,
    QuestionText TEXT NOT NULL,
    OptionA VARCHAR(255) NOT NULL,
    OptionB VARCHAR(255) NOT NULL,
    OptionC VARCHAR(255) NOT NULL,
    OptionD VARCHAR(255) NOT NULL,
    CorrectAnswer ENUM('A', 'B', 'C', 'D') NOT NULL,
    Points INT NOT NULL DEFAULT 10
);

-- QuizAttempts Table
CREATE TABLE QuizAttempts (
    AttemptID INT AUTO_INCREMENT PRIMARY KEY,
    UserID INT NOT NULL,
    QuestionID INT NOT NULL,
    UserAnswer ENUM('A', 'B', 'C', 'D') NOT NULL,
    IsCorrect BOOLEAN NOT NULL,
    PointsEarned INT NOT NULL,
    AttemptDate DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (UserID) REFERENCES User(UserID) ON DELETE CASCADE,
    FOREIGN KEY (QuestionID) REFERENCES QuizQuestions(QuestionID) ON DELETE CASCADE
);

-- Documents Table
CREATE TABLE Documents (
    DocumentID INT AUTO_INCREMENT PRIMARY KEY,
    UserID INT NOT NULL,
    DocumentType VARCHAR(50) NOT NULL,
    FilePath VARCHAR(255) NOT NULL,
    FileSize INT NOT NULL,
    Status ENUM('Uploaded', 'Failed') DEFAULT 'Uploaded',
    UploadDate DATE,
    FOREIGN KEY (UserID) REFERENCES User(UserID) ON DELETE CASCADE
);

-- TRR (Tax Return Receipt) Table
CREATE TABLE TRR (
    TRRID INT AUTO_INCREMENT PRIMARY KEY,
    UserID INT NOT NULL,
    FilingDate DATE NOT NULL,
    Status ENUM('Pending', 'Filed', 'Processed') DEFAULT 'Pending',
    TaxYear YEAR NOT NULL,
    FOREIGN KEY (UserID) REFERENCES User(UserID) ON DELETE CASCADE
);

-- TRR_Documents Junction Table
CREATE TABLE TRR_Documents (
    TRRID INT,
    DocumentID INT,
    PRIMARY KEY (TRRID, DocumentID),
    FOREIGN KEY (TRRID) REFERENCES TRR(TRRID) ON DELETE CASCADE,
    FOREIGN KEY (DocumentID) REFERENCES Documents(DocumentID) ON DELETE CASCADE
);

-- Grievances Table
CREATE TABLE Grievances (
    GrievanceID INT AUTO_INCREMENT PRIMARY KEY,
    UserID INT NOT NULL,
    Description TEXT NOT NULL,
    Status ENUM('Open', 'InProgress', 'Closed') DEFAULT 'Open',
    RegistrationDate DATE NOT NULL,
    FOREIGN KEY (UserID) REFERENCES User(UserID) ON DELETE CASCADE
);

-- TDSTransactions Table
CREATE TABLE TDSTransactions (
    TDSTransactionID INT AUTO_INCREMENT PRIMARY KEY,
    UserID INT NOT NULL,
    Amount DECIMAL(15,2) NOT NULL,
    Date DATE NOT NULL,
    TDSRate DECIMAL(5,2),
    FOREIGN KEY (UserID) REFERENCES User(UserID) ON DELETE CASCADE
);

-- TaxSlabs Table
CREATE TABLE TaxSlabs (
    SlabID INT AUTO_INCREMENT PRIMARY KEY,
    TaxYear YEAR NOT NULL,
    TaxType ENUM('Income', 'Property', 'Corporate', 'GST', 'SalesTax', 'ExciseTax', 'WealthTax') NOT NULL,
    LowerLimit DECIMAL(15,2) NOT NULL,
    UpperLimit DECIMAL(15,2),
    TaxRate DECIMAL(5,2) NOT NULL
);

-- TaxPreparer Table
CREATE TABLE TaxPreparer (
    PreparerID INT AUTO_INCREMENT PRIMARY KEY,
    Name VARCHAR(100) NOT NULL,
    ContactInfo VARCHAR(100),
    Location VARCHAR(100),
    Specialization VARCHAR(50)
);

-- UserActivity Table
CREATE TABLE UserActivity (
    ActivityID INT AUTO_INCREMENT PRIMARY KEY,
    UserID INT NOT NULL,
    ActivityType VARCHAR(50) NOT NULL,
    ActivityDate DATETIME DEFAULT CURRENT_TIMESTAMP,
    Description TEXT,
    UserRole ENUM('User', 'Admin') NOT NULL,
    FOREIGN KEY (UserID) REFERENCES User(UserID) ON DELETE CASCADE
);

-- Indexes for Performance Optimization
CREATE INDEX idx_userid_taxcalculation ON TaxCalculation(UserID);
CREATE INDEX idx_userid_taxcalendar ON TaxCalendar(UserID);
CREATE INDEX idx_userid_chatbot ON ChatbotInteraction(UserID);
CREATE INDEX idx_userid_gamification ON Gamification(UserID);
CREATE INDEX idx_userid_quizattempts ON QuizAttempts(UserID);
CREATE INDEX idx_userid_documents ON Documents(UserID);
CREATE INDEX idx_userid_trr ON TRR(UserID);
CREATE INDEX idx_userid_grievances ON Grievances(UserID);
CREATE INDEX idx_userid_tds ON TDSTransactions(UserID);
CREATE INDEX idx_userid_activity ON UserActivity(UserID);
CREATE INDEX idx_taxyear_taxslabs ON TaxSlabs(TaxYear, TaxType);

-- Trigger to Log User Activity on Tax Calculation
DELIMITER //
CREATE TRIGGER log_tax_calculation_activity
AFTER INSERT ON TaxCalculation
FOR EACH ROW
BEGIN
    INSERT INTO UserActivity (UserID, ActivityType, Description, UserRole)
    VALUES (NEW.UserID, 'TaxCalculation', CONCAT('Performed ', NEW.TaxType, ' tax calculation on ', DATE_FORMAT(NEW.CalculationDate, '%Y-%m-%d')), (SELECT Role FROM User WHERE UserID = NEW.UserID));
END //
DELIMITER ;

-- Stored Procedure for Registering a New User
DELIMITER //
CREATE PROCEDURE RegisterUser(
    IN p_UserType ENUM('Salaried', 'SelfEmployed', 'UnSalaried'),
    IN p_Name VARCHAR(100),
    IN p_Email VARCHAR(100),
    IN p_Phone VARCHAR(15),
    IN p_Address TEXT,
    IN p_PAN VARCHAR(10),
    IN p_AadhaarNumber VARCHAR(12),
    IN p_Password VARCHAR(255),
    OUT p_UserID INT
)
BEGIN
    INSERT INTO User (UserType, Name, Email, Phone, Address, PAN, AadhaarNumber, RegistrationDate, Role, Password)
    VALUES (p_UserType, p_Name, p_Email, p_Phone, p_Address, p_PAN, p_AadhaarNumber, CURDATE(), 'User', p_Password);
    
    SET p_UserID = LAST_INSERT_ID();
    
    INSERT INTO Gamification (UserID, PointsEarned, QuizPoints, Level, Achievement, EarnedDate)
    VALUES (p_UserID, 10, 0, 1, 'New User Registration', CURDATE());
    
    INSERT INTO UserActivity (UserID, ActivityType, Description, UserRole)
    VALUES (p_UserID, 'Registration', CONCAT('User ', p_Name, ' registered with ID ', p_UserID), 'User');
END //
DELIMITER ;

-- Stored Procedure for User Login
DELIMITER //
CREATE PROCEDURE LoginUser(
    IN p_UserType ENUM('Salaried', 'SelfEmployed', 'UnSalaried'),
    IN p_Role ENUM('User', 'Admin'),
    IN p_Email VARCHAR(100),
    IN p_Password VARCHAR(255),
    OUT p_UserID INT
)
BEGIN
    SELECT UserID INTO p_UserID
    FROM User
    WHERE UserType = p_UserType
    AND Role = p_Role
    AND Email = p_Email
    AND Password = p_Password
    LIMIT 1;

    IF p_UserID IS NOT NULL THEN
        INSERT INTO UserActivity (UserID, ActivityType, Description, UserRole)
        VALUES (p_UserID, 'Login', CONCAT('User logged in as ', p_Role), p_Role);
    ELSE
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Invalid credentials or user type/role';
    END IF;
END //
DELIMITER ;

-- Stored Procedure for Updating a User
DELIMITER //
CREATE PROCEDURE UpdateUser(
    IN p_CallerUserID INT,
    IN p_UserID INT,
    IN p_UserType ENUM('Salaried', 'SelfEmployed', 'UnSalaried'),
    IN p_Name VARCHAR(100),
    IN p_Email VARCHAR(100),
    IN p_Phone VARCHAR(15),
    IN p_Address TEXT,
    IN p_PAN VARCHAR(10),
    IN p_AadhaarNumber VARCHAR(12)
)
BEGIN
    DECLARE v_CallerRole ENUM('User', 'Admin');
    
    SELECT Role INTO v_CallerRole FROM User WHERE UserID = p_CallerUserID;
    
    IF v_CallerRole = 'Admin' OR p_CallerUserID = p_UserID THEN
        UPDATE User
        SET UserType = p_UserType, Name = p_Name, Email = p_Email, Phone = p_Phone,
            Address = p_Address, PAN = p_PAN, AadhaarNumber = p_AadhaarNumber
        WHERE UserID = p_UserID;
        
        INSERT INTO UserActivity (UserID, ActivityType, Description, UserRole)
        VALUES (p_UserID, 'UpdateUser', CONCAT('User ID ', p_UserID, ' updated their profile'), (SELECT Role FROM User WHERE UserID = p_CallerUserID));
    ELSE
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Only Admins or the user themselves can update user profiles';
    END IF;
END //
DELIMITER ;

-- Stored Procedure for Deleting a User
DELIMITER //
CREATE PROCEDURE DeleteUser(
    IN p_CallerUserID INT,
    IN p_UserID INT
)
BEGIN
    DECLARE v_CallerRole ENUM('User', 'Admin');
    
    SELECT Role INTO v_CallerRole FROM User WHERE UserID = p_CallerUserID;
    
    IF v_CallerRole = 'Admin' THEN
        DELETE FROM User WHERE UserID = p_UserID;
        
        INSERT INTO UserActivity (UserID, ActivityType, Description, UserRole)
        VALUES (p_CallerUserID, 'DeleteUser', CONCAT('Admin deleted user ID ', p_UserID), 'Admin');
    ELSE
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Only Admins can delete user accounts';
    END IF;
END //
DELIMITER ;

-- Stored Procedure for Tax Calculation
DELIMITER //
CREATE PROCEDURE CalculateTax(
    IN p_UserID INT,
    IN p_TaxType ENUM('Income', 'Property', 'Corporate', 'GST', 'SalesTax', 'ExciseTax', 'WealthTax'),
    IN p_Amount DECIMAL(15,2),
    IN p_TaxYear YEAR,
    OUT p_TaxAmount DECIMAL(15,2)
)
BEGIN
    DECLARE v_TaxableAmount DECIMAL(15,2);
    DECLARE v_TaxRate DECIMAL(5,2);
    DECLARE v_LowerLimit DECIMAL(15,2);
    DECLARE v_UpperLimit DECIMAL(15,2);
    DECLARE v_Tax DECIMAL(15,2) DEFAULT 0;
    DECLARE v_ActualTaxYear YEAR;

    SET v_ActualTaxYear = IF(p_TaxYear IS NULL OR p_TaxYear = 0, YEAR(CURDATE()), p_TaxYear);

    SET v_TaxableAmount = p_Amount;

    SELECT TaxRate, LowerLimit, UpperLimit
    INTO v_TaxRate, v_LowerLimit, v_UpperLimit
    FROM TaxSlabs
    WHERE TaxYear = v_ActualTaxYear
    AND TaxType = p_TaxType
    AND v_TaxableAmount >= LowerLimit
    AND (UpperLimit IS NULL OR v_TaxableAmount <= UpperLimit)
    LIMIT 1;

    IF v_TaxRate IS NOT NULL THEN
        IF v_LowerLimit = 0 THEN
            SET v_Tax = v_TaxableAmount * (v_TaxRate / 100);
        ELSE
            SET v_Tax = (v_TaxableAmount - v_LowerLimit) * (v_TaxRate / 100);
        END IF;
    ELSE
        SET v_Tax = 0;
    END IF;

    INSERT INTO TaxCalculation (UserID, TaxType, IncomeAmount, TaxableIncome, CalculatedTax, CalculationDate)
    VALUES (p_UserID, p_TaxType, p_Amount, v_TaxableAmount, v_Tax, CURDATE());

    SET p_TaxAmount = v_Tax;
END //
DELIMITER ;

-- Stored Procedure for Document Upload
DELIMITER //
CREATE PROCEDURE UploadDocument(
    IN p_UserID INT,
    IN p_DocumentType VARCHAR(50),
    IN p_FilePath VARCHAR(255),
    IN p_FileSize INT,
    OUT p_DocumentID INT
)
BEGIN
    DECLARE v_Status ENUM('Uploaded', 'Failed');

    IF p_FileSize <= 0 THEN
        SET v_Status = 'Failed';
    ELSE
        SET v_Status = 'Uploaded';
    END IF;

    INSERT INTO Documents (UserID, DocumentType, FilePath, FileSize, Status, UploadDate)
    VALUES (p_UserID, p_DocumentType, p_FilePath, p_FileSize, v_Status, CURDATE());

    SET p_DocumentID = LAST_INSERT_ID();

    INSERT INTO UserActivity (UserID, ActivityType, Description, UserRole)
    VALUES (p_UserID, 'DocumentUpload', CONCAT('Uploaded document ID ', p_DocumentID, ' with status ', v_Status), (SELECT Role FROM User WHERE UserID = p_UserID));
END //
DELIMITER ;

-- Stored Procedure for Chatbot Interaction
DELIMITER //
CREATE PROCEDURE SubmitChatbotQuery(
    IN p_UserID INT,
    IN p_QueryText TEXT,
    OUT p_ResponseText TEXT
)
BEGIN
    SET p_ResponseText = 'I am not sure how to answer that.';
    
    IF p_QueryText LIKE '%GST%' THEN
        SET p_ResponseText = 'The GST rate for 2025 is 18%.';
    ELSEIF p_QueryText LIKE '%ITR%' THEN
        SET p_ResponseText = 'You can file ITR by uploading documents and submitting through the File ITR option.';
    END IF;

    INSERT INTO ChatbotInteraction (UserID, QueryText, ResponseText)
    VALUES (p_UserID, p_QueryText, p_ResponseText);

    INSERT INTO UserActivity (UserID, ActivityType, Description, UserRole)
    VALUES (p_UserID, 'ChatbotInteraction', CONCAT('Submitted chatbot query: ', p_QueryText), (SELECT Role FROM User WHERE UserID = p_UserID));
END //
DELIMITER ;

-- Stored Procedure for Quiz Attempt
DELIMITER //
CREATE PROCEDURE SubmitQuizAnswer(
    IN p_UserID INT,
    IN p_QuestionID INT,
    IN p_UserAnswer ENUM('A', 'B', 'C', 'D'),
    OUT p_PointsEarned INT
)
BEGIN
    DECLARE v_CorrectAnswer ENUM('A', 'B', 'C', 'D');
    DECLARE v_QuestionPoints INT;
    DECLARE v_IsCorrect BOOLEAN;

    SELECT CorrectAnswer, Points
    INTO v_CorrectAnswer, v_QuestionPoints
    FROM QuizQuestions
    WHERE QuestionID = p_QuestionID;

    SET v_IsCorrect = (p_UserAnswer = v_CorrectAnswer);
    SET p_PointsEarned = IF(v_IsCorrect, v_QuestionPoints, 0);

    INSERT INTO QuizAttempts (UserID, QuestionID, UserAnswer, IsCorrect, PointsEarned)
    VALUES (p_UserID, p_QuestionID, p_UserAnswer, v_IsCorrect, p_PointsEarned);

    UPDATE Gamification
    SET QuizPoints = QuizPoints + p_PointsEarned,
        LastQuizDate = CURDATE()
    WHERE UserID = p_UserID;

    INSERT INTO UserActivity (UserID, ActivityType, Description, UserRole)
    VALUES (p_UserID, 'QuizAttempt', CONCAT('Answered quiz question ID ', p_QuestionID, ' and earned ', p_PointsEarned, ' points'), (SELECT Role FROM User WHERE UserID = p_UserID));
END //
DELIMITER ;

-- Stored Procedure for Filing a Tax Return
DELIMITER //
CREATE PROCEDURE FileTaxReturn(
    IN p_UserID INT,
    IN p_TaxYear YEAR,
    IN p_DocumentIDs VARCHAR(255),
    OUT p_TRRID INT
)
BEGIN
    DECLARE v_DocID INT;
    DECLARE v_Done INT DEFAULT 0;
    DECLARE doc_cursor CURSOR FOR 
        SELECT CAST(REGEXP_SUBSTR(p_DocumentIDs, '[0-9]+') AS UNSIGNED) AS DocID;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET v_Done = 1;

    INSERT INTO TRR (UserID, FilingDate, Status, TaxYear)
    VALUES (p_UserID, CURDATE(), 'Pending', p_TaxYear);

    SET p_TRRID = LAST_INSERT_ID();

    SET @remainingDocs = p_DocumentIDs;
    WHILE @remainingDocs != '' DO
        SET v_DocID = CAST(REGEXP_SUBSTR(@remainingDocs, '^[0-9]+') AS UNSIGNED);
        IF v_DocID > 0 THEN
            INSERT INTO TRR_Documents (TRRID, DocumentID)
            VALUES (p_TRRID, v_DocID);
        END IF;
        SET @remainingDocs = REGEXP_REPLACE(@remainingDocs, '^[0-9]+,?', '');
    END WHILE;

    INSERT INTO UserActivity (UserID, ActivityType, Description, UserRole)
    VALUES (p_UserID, 'FileTaxReturn', CONCAT('Filed tax return ID ', p_TRRID, ' for tax year ', p_TaxYear), (SELECT Role FROM User WHERE UserID = p_UserID));
END //
DELIMITER ;

-- Stored Procedure for Raising a Grievance
DELIMITER //
CREATE PROCEDURE RaiseGrievance(
    IN p_UserID INT,
    IN p_Description TEXT,
    OUT p_GrievanceID INT
)
BEGIN
    INSERT INTO Grievances (UserID, Description, Status, RegistrationDate)
    VALUES (p_UserID, p_Description, 'Open', CURDATE());

    SET p_GrievanceID = LAST_INSERT_ID();

    INSERT INTO UserActivity (UserID, ActivityType, Description, UserRole)
    VALUES (p_UserID, 'RaiseGrievance', CONCAT('Raised grievance ID ', p_GrievanceID), (SELECT Role FROM User WHERE UserID = p_UserID));
END //
DELIMITER ;

-- Stored Procedure for Calculating TDS
DELIMITER //
CREATE PROCEDURE CalculateTDS(
    IN p_UserID INT,
    IN p_Amount DECIMAL(15,2),
    IN p_TDSRate DECIMAL(5,2),
    OUT p_TDSAmount DECIMAL(15,2)
)
BEGIN
    SET p_TDSAmount = p_Amount * (p_TDSRate / 100);

    INSERT INTO TDSTransactions (UserID, Amount, Date, TDSRate)
    VALUES (p_UserID, p_Amount, CURDATE(), p_TDSRate);

    INSERT INTO UserActivity (UserID, ActivityType, Description, UserRole)
    VALUES (p_UserID, 'CalculateTDS', CONCAT('Calculated TDS of ', p_TDSAmount, ' on amount ', p_Amount), (SELECT Role FROM User WHERE UserID = p_UserID));
END //
DELIMITER ;

-- Stored Procedure for Updating Tax Slabs
DELIMITER //
CREATE PROCEDURE UpdateTaxSlab(
    IN p_CallerUserID INT,
    IN p_SlabID INT,
    IN p_TaxYear YEAR,
    IN p_TaxType ENUM('Income', 'Property', 'Corporate', 'GST', 'SalesTax', 'ExciseTax', 'WealthTax'),
    IN p_LowerLimit DECIMAL(15,2),
    IN p_UpperLimit DECIMAL(15,2),
    IN p_TaxRate DECIMAL(5,2)
)
BEGIN
    DECLARE v_CallerRole ENUM('User', 'Admin');
    
    SELECT Role INTO v_CallerRole FROM User WHERE UserID = p_CallerUserID;
    
    IF v_CallerRole = 'Admin' THEN
        UPDATE TaxSlabs
        SET TaxYear = p_TaxYear, TaxType = p_TaxType, LowerLimit = p_LowerLimit, UpperLimit = p_UpperLimit, TaxRate = p_TaxRate
        WHERE SlabID = p_SlabID;
        
        INSERT INTO UserActivity (UserID, ActivityType, Description, UserRole)
        VALUES (p_CallerUserID, 'UpdateTaxSlab', CONCAT('Updated tax slab ID ', p_SlabID), 'Admin');
    ELSE
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Only Admins can update tax slabs';
    END IF;
END //
DELIMITER ;

-- Stored Procedure for Locating Tax Preparers
DELIMITER //
CREATE PROCEDURE LocateTaxPreparer(
    IN p_Location VARCHAR(100)
)
BEGIN
    SELECT Name, ContactInfo, Location, Specialization
    FROM TaxPreparer
    WHERE Location = p_Location;
END //
DELIMITER ;

-- Stored Procedure for Checking ITR Status
DELIMITER //
CREATE PROCEDURE CheckITRStatus(
    IN p_UserID INT
)
BEGIN
    SELECT TRRID, FilingDate, Status, TaxYear
    FROM TRR
    WHERE UserID = p_UserID;
END //
DELIMITER ;

-- Stored Procedure for Getting Quiz Questions
DELIMITER //
CREATE PROCEDURE GetQuizQuestions()
BEGIN
    SELECT QuestionID, QuestionText, OptionA, OptionB, OptionC, OptionD
    FROM QuizQuestions;
END //
DELIMITER ;

-- Stored Procedure for Viewing Tax Calendar
DELIMITER //
CREATE PROCEDURE ViewTaxCalendar(
    IN p_UserID INT
)
BEGIN
    SELECT EventDate, EventType, Description
    FROM TaxCalendar
    WHERE UserID = p_UserID;
END //
DELIMITER ;

-- Stored Procedure for Viewing User Activity
DELIMITER //
CREATE PROCEDURE ViewUserActivity(
    IN p_UserID INT,
    IN p_IsAdmin BOOLEAN
)
BEGIN
    IF p_IsAdmin THEN
        SELECT ActivityID, UserID, ActivityType, ActivityDate, Description, UserRole
        FROM UserActivity;
    ELSE
        SELECT ActivityID, UserID, ActivityType, ActivityDate, Description, UserRole
        FROM UserActivity
        WHERE UserID = p_UserID;
    END IF;
END //
DELIMITER ;

-- Stored Procedure for Searching Documents
DELIMITER //
CREATE PROCEDURE SearchDocuments(
    IN p_UserID INT,
    IN p_DocumentType VARCHAR(50)
)
BEGIN
    SELECT DocumentID, DocumentType, FilePath, FileSize, Status, UploadDate
    FROM Documents
    WHERE UserID = p_UserID
    AND DocumentType = p_DocumentType;
END //
DELIMITER ;

-- Stored Procedure for Verifying PAN Status
DELIMITER //
CREATE PROCEDURE VerifyPAN(
    IN p_PAN VARCHAR(10)
)
BEGIN
    SELECT UserID
    FROM User
    WHERE PAN = p_PAN;
END //
DELIMITER ;

-- Sample Data for User Table
INSERT INTO User (UserType, Name, Email, Phone, Address, PAN, AadhaarNumber, RegistrationDate, Role, Password) VALUES
('Salaried', 'Amit Sharma', 'amit.sharma@example.com', '9876543210', '123 Mumbai Street', 'ABCDE1234F', '123456789012', '2025-01-01', 'Admin', 'password1'),
('SelfEmployed', 'Priya Patel', 'priya.patel@example.com', '9123456789', '456 Ahmedabad Road', 'FGHIJ5678K', '987654321098', '2025-01-02', 'User', 'password2'),
('UnSalaried', 'Rahul Verma', 'rahul.verma@example.com', '9988776655', '789 Delhi Avenue', 'KLMNO9012P', '456789123456', '2025-01-03', 'User', 'password3'),
('Salaried', 'Neha Gupta', 'neha.gupta@example.com', '9876541230', '321 Pune Road', 'PQRST5678U', '654321987654', '2025-01-04', 'User', 'password4');

-- Sample Data for TaxSlabs Table
INSERT INTO TaxSlabs (TaxYear, TaxType, LowerLimit, UpperLimit, TaxRate) VALUES
(2025, 'Income', 0, 300000, 0),
(2025, 'Income', 300001, 600000, 5),
(2025, 'Income', 600001, 900000, 10),
(2025, 'Income', 900001, 1200000, 15),
(2025, 'Income', 1200001, 1500000, 20),
(2025, 'Income', 1500001, NULL, 30),
(2025, 'GST', 0, NULL, 18),
(2025, 'Property', 0, 500000, 2),
(2025, 'Property', 500001, NULL, 5),
(2025, 'Corporate', 0, NULL, 25),
(2025, 'SalesTax', 0, NULL, 10),
(2025, 'ExciseTax', 0, NULL, 12),
(2025, 'WealthTax', 0, 3000000, 0),
(2025, 'WealthTax', 3000001, NULL, 1);

-- Insert Tax Slabs for Income Tax (2023-2024)
INSERT INTO TaxSlabs (TaxYear, TaxType, LowerLimit, UpperLimit, TaxRate) VALUES
(2024, 'Income', 0, 300000, 0),
(2024, 'Income', 300001, 600000, 5),
(2024, 'Income', 600001, 900000, 10),
(2024, 'Income', 900001, 1200000, 15),
(2024, 'Income', 1200001, 1500000, 20),
(2024, 'Income', 1500001, NULL, 30);

-- Insert Tax Slabs for Property Tax (2023-2024)
INSERT INTO TaxSlabs (TaxYear, TaxType, LowerLimit, UpperLimit, TaxRate) VALUES
(2024, 'Property', 0, 500000, 1),
(2024, 'Property', 500001, 1000000, 2),
(2024, 'Property', 1000001, NULL, 3);

-- Insert Tax Slabs for Corporate Tax (2023-2024)
INSERT INTO TaxSlabs (TaxYear, TaxType, LowerLimit, UpperLimit, TaxRate) VALUES
(2024, 'Corporate', 0, 10000000, 25),
(2024, 'Corporate', 10000001, NULL, 30);

-- Sample Data for TaxPreparer Table
INSERT INTO TaxPreparer (Name, ContactInfo, Location, Specialization) VALUES
('John Tax Expert', 'john@example.com', 'Mumbai', 'ITR Filing'),
('Priya Sharma', 'priya@example.com', 'Delhi', 'TDS Consultation'),
('Vikram Singh', 'vikram@example.com', 'Bangalore', 'GST Compliance');

-- Sample Data for TaxCalendar Table
INSERT INTO TaxCalendar (UserID, EventDate, EventType, Description) VALUES
(1, '2025-07-31', 'FilingDeadline', 'ITR Filing Deadline for FY 2024-25'),
(1, '2025-06-15', 'PaymentDue', 'Advance Tax Payment Due'),
(2, '2025-07-31', 'FilingDeadline', 'ITR Filing Deadline for FY 2024-25');

-- Sample Data for QuizQuestions Table
INSERT INTO QuizQuestions (QuestionText, OptionA, OptionB, OptionC, OptionD, CorrectAnswer, Points) VALUES
('What is the GST rate for 2025?', '12%', '18%', '20%', '25%', 'B', 10),
('What is the tax rate for income above 1500000 in 2025?', '20%', '25%', '30%', '35%', 'C', 15),
('When is the ITR filing deadline for FY 2024-25?', '2025-06-30', '2025-07-31', '2025-08-15', '2025-09-30', 'B', 10),
('What is the corporate tax rate for 2025?', '15%', '20%', '25%', '30%', 'C', 10),
('What is the tax rate for property value above 500000 in 2025?', '2%', '3%', '4%', '5%', 'D', 15);