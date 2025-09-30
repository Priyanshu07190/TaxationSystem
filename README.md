# 🏛️ Indian Taxation System

<div align="center">
  <img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white" alt="Java">
  <img src="https://img.shields.io/badge/MySQL-00000F?style=for-the-badge&logo=mysql&logoColor=white" alt="MySQL">
  <img src="https://img.shields.io/badge/Swing-GUI-blue?style=for-the-badge" alt="Swing">
  <img src="https://img.shields.io/badge/License-MIT-green?style=for-the-badge" alt="License">
</div>

<div align="center">
  <h3>🎯 A Comprehensive Desktop Application for Indian Tax Management</h3>
  <p><em>Complete tax calculation, ITR filing, document management, and interactive learning system</em></p>
</div>

---

## 📋 Table of Contents

- [🌟 Features](#-features)
- [️ Prerequisites](#️-prerequisites)
- [⚡ Quick Setup Guide](#-quick-setup-guide)
- [🚀 How to Run](#-how-to-run)
- [📁 Project Structure](#-project-structure)
- [🏗️ Architecture](#️-architecture)
- [💾 Database Schema](#-database-schema)
- [👥 User Types & Roles](#-user-types--roles)
- [🔧 Configuration](#-configuration)
- [🤝 Contributing](#-contributing)
- [📝 License](#-license)
- [❓ FAQ](#-faq)

---

## 🌟 Features

### 🧮 **Tax Management**
- ✅ **Multi-User Tax Calculator** - Supports Salaried, Self-Employed, and Un-Salaried users
- ✅ **Dynamic Tax Slab Management** - 2025 tax slabs with admin update capability
- ✅ **TDS Calculator** - Calculate Tax Deducted at Source
- ✅ **ITR Filing System** - Complete Income Tax Return filing workflow
- ✅ **Tax Calendar** - Important dates and deadline reminders

### 📊 **Document Management**
- ✅ **Document Upload & Storage** - Secure document management system
- ✅ **Document Search & Retrieval** - Advanced search functionality
- ✅ **PAN Verification** - Validate PAN card details
- ✅ **Aadhaar Linking** - Link Aadhaar with PAN seamlessly

### 🎓 **Learning & Engagement**
- ✅ **Interactive Tax Quiz** - Gamified learning experience
- ✅ **AI Chatbot Integration** - Get instant tax-related help
- ✅ **Gamification System** - Points, levels, and achievements
- ✅ **Tax Preparation Guidance** - Step-by-step tax filing assistance

### 👤 **User Management**
- ✅ **Multi-Role Authentication** - Admin and User roles
- ✅ **User Registration & Login** - Secure authentication system
- ✅ **Profile Management** - Update user information
- ✅ **Activity Tracking** - Monitor user activities and calculations
- ✅ **Grievance Management** - Submit and track complaints

### ⚙️ **Admin Features**
- ✅ **Tax Slab Updates** - Modify tax brackets and rates
- ✅ **User Management** - View and manage all users
- ✅ **System Analytics** - Track usage and performance
- ✅ **Database Administration** - Complete system oversight

---

## 🛠️ Prerequisites

Before you begin, ensure you have the following installed on your Windows system:

### 📋 **Required Software**

| Software | Version | Download Link | Purpose |
|----------|---------|---------------|---------|
| **Java JDK** | 8+ | [Oracle JDK](https://www.oracle.com/java/technologies/downloads/) | Core runtime environment |
| **MySQL Server** | 8.0+ | [MySQL Downloads](https://dev.mysql.com/downloads/mysql/) | Database server |
| **MySQL Workbench** | Latest | [MySQL Workbench](https://dev.mysql.com/downloads/workbench/) | Database management (optional) |
| **Command Prompt** | Built-in | N/A | Application execution |

### ✅ **Verification Steps**

Open Command Prompt (`cmd`) and verify installations:

```cmd
# Check Java installation
java -version

# Check Java Compiler
javac -version

# Check MySQL installation (after starting MySQL service)
mysql --version
```

**Expected Output:**
```
java version "1.8.0_XXX"
javac 1.8.0_XXX
mysql  Ver 8.0.XX for Win64 on x86_64
```

---

## ⚡ Quick Setup Guide

### 🗄️ **Step 1: Database Setup**

1. **Start MySQL Service:**
   ```cmd
   net start MySQL80
   ```

2. **Access MySQL:**
   ```cmd
   mysql -u root -p
   ```
   Enter your MySQL root password when prompted.

3. **Create Database:**
   ```sql
   CREATE DATABASE IndianTaxationSystem;
   exit
   ```

4. **Import Database Schema:**
   ```cmd
   mysql -u root -p IndianTaxationSystem < database.sql
   ```

5. **Import Sample Data (Optional):**
   ```cmd
   mysql -u root -p IndianTaxationSystem < sample_user.sql
   mysql -u root -p IndianTaxationSystem < tax_slabs_2025.sql
   mysql -u root -p IndianTaxationSystem < quiz_questions.sql
   mysql -u root -p IndianTaxationSystem < tax_calendar_events.sql
   ```

### 🔧 **Step 2: Database Configuration**

1. **Update Database Credentials:**
   Open `taxation\db\DatabaseManager.java` and modify:
   ```java
   private static final String USER = "your_mysql_username";
   private static final String PASSWORD = "your_mysql_password";
   ```

### 📦 **Step 3: Project Download**

1. **Clone Repository:**
   ```cmd
   git clone https://github.com/YourUsername/TaxationSystem.git
   cd TaxationSystem
   ```

   **OR Download ZIP:**
   - Download ZIP from GitHub
   - Extract to desired folder
   - Open Command Prompt in extracted folder

---

## 🚀 How to Run

### 🎯 **Method 1: Direct Command Line Execution (Recommended)**

1. **Open Command Prompt in Project Directory:**
   ```cmd
   cd C:\path\to\your\TaxationSystem
   ```

2. **Compile the Application:**
   ```cmd
   javac -cp "lib\mysql-connector-j-9.3.0.jar;." *.java taxation\db\*.java taxation\model\*.java
   ```

3. **Run the Application:**
   ```cmd
   java -cp "lib\mysql-connector-j-9.3.0.jar;." Main
   ```

### 🎯 **Method 2: Using Batch File (Easy)**

1. **Create `run.bat` file:**
   ```batch
   @echo off
   echo Starting Indian Taxation System...
   echo.
   echo Compiling Java files...
   javac -cp "lib\mysql-connector-j-9.3.0.jar;." *.java taxation\db\*.java taxation\model\*.java
   
   if %errorlevel% neq 0 (
       echo Compilation failed! Please check for errors.
       pause
       exit /b 1
   )
   
   echo Compilation successful!
   echo Starting application...
   java -cp "lib\mysql-connector-j-9.3.0.jar;." Main
   pause
   ```

2. **Run the batch file:**
   ```cmd
   run.bat
   ```

### ⚠️ **Troubleshooting Common Issues**

| Issue | Solution |
|-------|----------|
| `ClassNotFoundException: com.mysql.cj.jdbc.Driver` | Ensure MySQL connector JAR is in `lib` folder |
| `Connection refused` | Start MySQL service: `net start MySQL80` |
| `Access denied for user` | Update credentials in `DatabaseManager.java` |
| `Database doesn't exist` | Run database setup commands again |
| `Compilation errors` | Ensure Java JDK is properly installed |

---

## 📁 Project Structure

```
TaxationSystem/
├── 📄 Main.java                      # Application entry point
├── 🔐 LoginFrame.java                # User authentication
├── 📝 RegistrationFrame.java         # New user registration
├── 🏠 UserTypeSelectionFrame.java    # User type selection
├── 💼 SalariedUserFrame.java         # Salaried user dashboard
├── 🏢 SelfEmployedUserFrame.java     # Self-employed dashboard
├── 👤 UnSalariedUserFrame.java       # Un-salaried dashboard
├── ⚙️ AdminFrame.java                # Admin panel
├── 🧮 TaxCalculatorFrame.java        # Tax calculation engine
├── 📊 FileITRFrame.java              # ITR filing interface
├── 📅 TaxCalendarFrame.java          # Tax calendar & reminders
├── 🤖 ChatbotFrame.java              # AI assistant
├── 🎯 QuizFrame.java                 # Interactive quiz system
├── 📄 DocumentUploadFrame.java       # Document management
├── 🔍 SearchDocumentsFrame.java      # Document search
├── 🆔 VerifyPANFrame.java           # PAN verification
├── 🔗 LinkAadharFrame.java          # Aadhaar linking
├── 💰 TDSFrame.java                 # TDS calculator
├── ⚖️ UpdateTaxSlabFrame.java        # Tax slab management
├── 👥 UpdateUserFrame.java           # User profile updates
├── 📈 UserActivityFrame.java         # Activity tracking
├── 📋 GrievanceFrame.java            # Complaint system
├── 🎓 TaxPreparerFrame.java          # Tax preparation guide
├── 🗃️ lib/
│   └── mysql-connector-j-9.3.0.jar  # MySQL JDBC driver
├── 💾 taxation/
│   ├── db/
│   │   └── DatabaseManager.java     # Database operations
│   └── model/
│       └── QuizQuestion.java        # Data models
├── 🗄️ database.sql                  # Database schema
├── 👤 sample_user.sql               # Sample data
├── 💸 tax_slabs_2025.sql           # Current tax slabs
├── ❓ quiz_questions.sql            # Quiz database
├── 📅 tax_calendar_events.sql       # Calendar events
├── 🔧 logging.properties            # Logging configuration
└── 📖 README.md                     # This file
```

---

## 🏗️ Architecture

### 🎨 **Design Pattern: MVC (Model-View-Controller)**

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│     View        │    │   Controller    │    │     Model       │
│  (Swing Frames) │◄──►│  (Event Logic)  │◄──►│  (Data Access)  │
│                 │    │                 │    │                 │
│ • LoginFrame    │    │ • Button Events │    │ • DatabaseMgr   │
│ • AdminFrame    │    │ • Form Handlers │    │ • QuizQuestion  │
│ • Calculator    │    │ • Calculations  │    │ • User Data     │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

### 🔄 **Application Flow**

```
Start → User Type Selection → Login/Register → Dashboard → Features
  ↑                                                         ↓
  └─────────────────── Logout ←─────────────────────────────┘
```

### 🗃️ **Database Architecture**

```
User Management     Tax System          Learning System
┌─────────────┐    ┌─────────────┐    ┌─────────────┐
│    User     │    │TaxCalculation│    │Gamification │
├─────────────┤    ├─────────────┤    ├─────────────┤
│ • UserID    │────│ • UserID    │    │ • UserID    │
│ • UserType  │    │ • TaxType   │    │ • Points    │
│ • Role      │    │ • Amount    │    │ • Level     │
└─────────────┘    └─────────────┘    └─────────────┘
```

---

## 💾 Database Schema

### 📊 **Main Tables**

| Table | Purpose | Key Fields |
|-------|---------|------------|
| `User` | User management | UserID, UserType, Role, Email, PAN |
| `TaxCalculation` | Tax computations | CalculationID, UserID, TaxType, Amount |
| `TaxCalendar` | Important dates | CalendarID, EventDate, EventType |
| `ChatbotInteraction` | AI conversations | InteractionID, QueryText, ResponseText |
| `Gamification` | Points & achievements | GamificationID, PointsEarned, Level |
| `DocumentStorage` | File management | DocumentID, UserID, FileName, FilePath |
| `Grievance` | Complaints | GrievanceID, UserID, Subject, Status |
| `TaxSlab` | Tax brackets | SlabID, MinIncome, MaxIncome, TaxRate |

### 🔐 **User Roles & Permissions**

```sql
User Types:
├── Salaried     (Can be Admin or User)
├── SelfEmployed (User only)
└── UnSalaried   (User only)

Roles:
├── Admin        (Full system access)
└── User         (Limited access)
```

---

## 👥 User Types & Roles

### 🎭 **User Categories**

| User Type | Description | Features |
|-----------|-------------|-----------|
| **Salaried** | Employees with fixed salary | Standard deduction, HRA, 80C benefits |
| **Self-Employed** | Business owners, freelancers | Business expense deduction, professional tax |
| **Un-Salaried** | Students, unemployed | Basic tax calculation, educational content |

### 🛡️ **Role-Based Access**

| Feature | Admin | User |
|---------|-------|------|
| Tax Calculation | ✅ | ✅ |
| ITR Filing | ✅ | ✅ |
| Document Upload | ✅ | ✅ |
| Quiz & Learning | ✅ | ✅ |
| Update Tax Slabs | ✅ | ❌ |
| User Management | ✅ | ❌ |
| System Analytics | ✅ | ❌ |

---

## 🔧 Configuration

### ⚙️ **Database Configuration**

Edit `taxation/db/DatabaseManager.java`:

```java
// Database Connection Settings
private static final String URL = "jdbc:mysql://localhost:3306/IndianTaxationSystem?useSSL=false";
private static final String USER = "your_username";        // Change this
private static final String PASSWORD = "your_password";    // Change this
```

### 📋 **Logging Configuration**

Edit `logging.properties`:

```properties
# Logging Level (INFO, WARNING, SEVERE)
.level = INFO

# Console Handler
java.util.logging.ConsoleHandler.level = INFO
java.util.logging.ConsoleHandler.formatter = java.util.logging.SimpleFormatter

# File Handler
java.util.logging.FileHandler.pattern = taxation_system.log
java.util.logging.FileHandler.level = ALL
```

### 🎨 **UI Customization**

Modify look and feel in `Main.java`:

```java
// Available Look and Feel options:
UIManager.setLookAndFeel(UIManager.getSystemLookAndFeel());           // System default
UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel"); // Modern look
UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");   // Classic Java
```

---

## 🤝 Contributing

We welcome contributions! Here's how you can help:

### 🚀 **Getting Started**

1. **Fork the repository**
2. **Create a feature branch:**
   ```cmd
   git checkout -b feature/amazing-feature
   ```
3. **Make your changes**
4. **Commit your changes:**
   ```cmd
   git commit -m "Add amazing feature"
   ```
5. **Push to the branch:**
   ```cmd
   git push origin feature/amazing-feature
   ```
6. **Open a Pull Request**

### 📋 **Development Guidelines**

- Follow Java naming conventions
- Add comments for complex logic
- Update database schema if needed
- Test thoroughly before submitting
- Update README for new features

---

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

```
MIT License

Copyright (c) 2025 Taxation System Project

Permission is hereby granted, free of charge, to any person obtaining a copy...
```

---

## ❓ FAQ

### 🤔 **Common Questions**

**Q: Can I run this on macOS or Linux?**
A: Yes! Just use forward slashes (/) in paths and use colons (:) instead of semicolons (;) in classpath.

**Q: How do I reset the database?**
A: Run: `mysql -u root -p -e "DROP DATABASE IndianTaxationSystem;"` then recreate it.

**Q: Can I customize tax slabs?**
A: Yes! Login as Admin and use the "Update Tax Slabs" feature.

**Q: Is the data secure?**
A: Yes, we use parameterized queries and input validation to prevent SQL injection.

**Q: How do I add new features?**
A: Follow the MVC pattern, create new Frame classes, and update the database schema if needed.

### 🔧 **Technical Support**

**Database Issues:**
- Ensure MySQL service is running
- Check credentials in DatabaseManager.java
- Verify database exists and is accessible

**Compilation Issues:**
- Confirm Java JDK is installed (not just JRE)
- Check JAVA_HOME environment variable
- Ensure MySQL connector JAR is present

**Runtime Issues:**
- Check MySQL connection
- Verify all SQL files were imported
- Review application logs

###  **Acknowledgments**

- MySQL Team for the excellent database system
- Oracle for Java platform
- Swing community for UI inspiration
- All contributors who made this project better

---

<div align="center">
  <h3>🌟 If this project helped you, please give it a star! 🌟</h3>
  <p><strong>Made with ❤️ for the developer community</strong></p>
</div>
