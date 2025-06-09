ThreadTalk Chat Application
A real-time chat application built with Java, featuring a client-server architecture and GUI interface.

Features
Real-time chat messaging
User list management
Message history storage
Username validation
Automatic reconnection
XSS protection
Error handling and logging
Prerequisites
Java JDK 8 or higher
MySQL Server 8.0 or higher
Maven (for building)
Setup
Database Setup

# Run the database setup script
./setup_database.bat
Build the Project

# Compile the project
javac -cp "lib/*" -d target/classes src/main/java/com/threadtalk/**/*.java
Running the Application
Start the Server

java -cp "target/classes;lib/*" com.threadtalk.server.ChatServer
Start the Client

java -cp "target/classes;lib/*" com.threadtalk.client.ChatClient
Project Structure
src/main/java/com/threadtalk/
├── client/         # Client-side code
├── server/         # Server-side code
├── ui/            # User interface components
├── dao/           # Data Access Objects
├── model/         # Data models
├── util/          # Utility classes
└── view/          # Additional UI components
Features in Detail
Client Features
Automatic reconnection attempts
Username validation
Real-time message updates
User list management
Error handling and user feedback
Server Features
Multi-client support
Message broadcasting
User management
Message validation and sanitization
Logging system
Error handling
Security Features
Basic XSS protection
Message length validation
Username validation
Input sanitization
Error Handling
The application includes comprehensive error handling for:

Connection failures
Invalid inputs
Database errors
Network interruptions
Server crashes
Logging
The application uses Java's built-in logging system to track:

Client connections/disconnections
Message processing
Error conditions
System events
Contributing
Fork the repository
Create your feature branch
Commit your changes
Push to the branch
Create a new Pull Request
License
This project is licensed under the MIT License - see the LICENSE file for details.
