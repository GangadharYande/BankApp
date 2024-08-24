# Banking Application with Spring Boot

## Table of Contents
- [Introduction](#introduction)
- [Technologies Used](#technologies-used)
- [Features](#features)
- [User Management](#user-management)
- [Account Management](#account-management)
- [Transactions](#transactions)
- [Notifications](#notifications)
- [Architecture](#architecture)
- [Database Schema](#database-schema)
- [Endpoints](#endpoints)
- [Security](#security)
- [Development Instructions](#development-instructions)

## Introduction
This is a comprehensive Banking Application built with Spring Boot. The application is designed to manage users, accounts, and transactions efficiently while ensuring security and reliability. It includes advanced features such as audit logging, role-based access control, and comprehensive notification systems.

## Technologies Used
- **Spring Boot:** Framework for building Java-based applications.
- **Spring Data JPA:** Abstraction over JPA for database operations.
- **Spring Security:** For securing the application.
- **H2 Database:** In-memory database for development and testing.
- **MySQL:** Production database.
- **Thymeleaf:** Template engine for rendering views.
- **RESTful API:** For client-server communication.
- **JUnit & Mockito:** For unit and integration testing.
- **Docker:** Containerization of the application.
- **Swagger:** API documentation and testing.

## Features
- **User Management:** Registration, login, and profile management.
- **Account Management:** Creation and management of bank accounts.
- **Transactions:** Money transfers, deposits, and withdrawals.
- **Notifications:** Email and SMS notifications for transactions.
- **Security:** Authentication and authorization using Spring Security.
- **Audit Logging:** Tracking of user activities and transactions.
- **Role-Based Access Control:** Different roles and permissions for users.

## User Management
### Registration
Users can register by providing necessary details such as name, email, password, and contact information. The registration process includes email verification.

### Login
Users can log in using their email and password. JWT tokens are used for session management.

### Profile Management
Users can view and update their profile information, including password changes and contact details.

## Account Management
### Account Creation
Users can create multiple bank accounts. Each account will have a unique account number, balance, and type (savings, current, etc.).

### Account Operations
- **View Account Details:** Users can view details of their accounts, including balance and transaction history.
- **Update Account:** Users can update account settings like account type and associated email for notifications.

## Transactions
### Money Transfer
Users can transfer money between accounts. The application ensures sufficient balance and updates the transaction history accordingly.

### Deposit and Withdrawal
Users can deposit or withdraw money from their accounts. These operations update the account balance and generate corresponding transactions.

### Transaction History
Users can view the history of transactions, filter by date range, and search for specific transactions.

### Bank Statement via Email
User Can request Transaction statements from start date to end date

## Notifications
### Email Notifications
Users receive email notifications for significant events such as account creation, transactions, and profile updates.

### SMS Notifications
SMS notifications are sent for critical transactions like withdrawals and deposits.

## Architecture
The application follows a layered architecture:
- **Controller Layer:** Handles HTTP requests and responses.
- **Service Layer:** Contains business logic.
- **Repository Layer:** Manages database interactions.
- **Security Layer:** Handles authentication and authorization.

## Database Schema
### Tables
- **Users:** Stores user information.
- **Accounts:** Stores account details.
- **Account type:** Store the type of account  w.r.t customer_id
- **Transactions:** Stores transaction records.
- **Notifications:** Stores notification preferences and history.

### Tables Descriptions
#### UserAccounts
- id (PRIMARY KEY)
- first_name
- last_name
- middle_name
- gender
- address
- State
- account_number
- accountBalance
- phone_number
- email_address
- alternatePhoneNumber
- status

#### Accounts
- id (PRIMARY KEY)
- user_id (FOREIGN KEY referencing Users.customer_id)
- account_number
- account_type  (Could be a  Saving, Current or Loan)
- balance  (Will be part  of dashboard  balance will be visible w.r.t account type)

#### AccountTypes
- id (PRIMARY KEY)
- type_name (e.g., Saving, Current, Loan)

#### Transactions
- transactionId (PRIMARY KEY)
- transactionType  (Debit, Credit, transfer)
- amount
- accountNumber 
- status (Success , Failure)

#### Notifications
- id (PRIMARY KEY)
- user_id (FOREIGN KEY referencing Users.customer_id)
- type (SMS/Email)
- message
- timestamp

### Relationships
- Users-Accounts: One-to-many relationship (one user can have multiple accounts).
- Accounts-Transactions: One-to-many relationship (one account can have multiple transactions).
- Users-Notifications: One-to-many relationship (one user can have multiple notifications).
- Accounts-AccountTypes: Many-to-one relationship (multiple accounts can have the same account type).

## Endpoints
### User Endpoints
- `POST /api/user/transfer`: Register a new user.
- `POST /api/user/register`: User login.
- `GET /api/user/debit`: Get user profile.
- `PUT /api/user/credit`: Update user profile.
- `GET /api/user/nameEnquiry` : Can view by AccountNumber
- `GET /api/user/balanceEnquiry` : Given an accountNumber,display available balance



### Account Endpoints
- `POST /api/accounts`: Create a new account.
- `GET /api/accounts/{accountId}`: Get account details.
- `PUT /api/accounts/{accountId}`: Update account details.

### Transaction Endpoints
- `POST /api/transactions/transfer`: Transfer money.
- `POST /api/transactions/deposit`: Deposit money.
- `POST /api/transactions/withdraw`: Withdraw money.
- `GET /api/transactions/history`: Get transaction history.

### Notification Endpoints
- `GET /api/notifications`: Get notification preferences.
- `PUT /api/notifications`: Update notification preferences.

## Security
### Authentication
- **JWT Tokens:** Used for securing API endpoints.
- **Login:** Users authenticate via username and password to receive a JWT token.

### Authorization
- **Role-Based Access Control (RBAC):** Different roles (e.g., admin, user) have different permissions.
- **Endpoint Protection:** Endpoints are protected based on user roles.

### Encryption
- **Password Encryption:** User passwords are encrypted using BCrypt.
- **Data Encryption:** Sensitive data is encrypted in the database.

### Audit Logging
- **Activity Logging:** All user activities and transactions are logged for audit purposes.

## Development Instructions
### Prerequisites
- **Java 17:** Ensure Java 17 is installed.
- **Maven:** For managing dependencies and building the project.
- **Docker:** For containerization.
- **MySQL:** For the production database.

### Setup
1. **Clone the Repository:**
   ```bash
      https://github.com/GangadharYande/BankingAppSpringBoot.git
2. Configure Database:
   Update the application.properties or application.yml file with your MySQL database configuration.
   ```bash
   spring.datasource.url=jdbc:mysql://localhost:3306/bank_app
   spring.datasource.username=root
   spring.datasource.password=root
   spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
   spring.jpa.hibernate.ddl-auto=update
   ```
3. Build the Application:
   ```bash
      mvn clean install

   ```
4. Run the Application:
   ```bash
      mvn spring-boot:run
   ```

5 . Running Tests
   ```bash 
      mvn test
   ```

This documentation provides a comprehensive guide to developing, deploying, and managing a Banking Application built with Spring Boot, covering its features, architecture, and security mechanisms in detail.

