ThreadTalk
A Java-based discussion forum application that allows users to create and participate in threaded discussions.

Prerequisites
Java Development Kit (JDK) 23
MySQL 8.0 or higher
Maven
MySQL Workbench (recommended for database management)
Database Setup
Open MySQL Workbench
Connect to your local MySQL server
Create a new database:
CREATE DATABASE thread_talk;
Project Setup
Clone or download this repository
Open the project in your preferred IDE
Update database credentials in src/main/java/com/threadtalk/dao/DatabaseConfig.java if needed
Build the project using Maven:
mvn clean install
Running the Application
After building the project, you can run it using:

java -jar target/thread-talk-1.0-SNAPSHOT-jar-with-dependencies.jar
Project Structure
ThreadTalk/
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── threadtalk/
│                   ├── dao/         # Data Access Objects
│                   ├── model/       # Data Models
│                   ├── controller/  # Business Logic
│                   └── view/        # User Interface
├── pom.xml         # Maven configuration
└── README.md       # This file
