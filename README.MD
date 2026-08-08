# Hive Game Project

## Overview
Hive is a strategic board game implemented as a full-stack application with real-time multiplayer capabilities. This project includes a Java backend using Spring Boot and WebSocket for game logic, and a frontend for player interaction.

## Key Features
- Real-time game play with WebSocket communication
- AI bot implementation using mimimax
- Game state validation and move publishing
- Cross-origin resource sharing (CORS) support
- Modular architecture with clear separation of concerns

## Tech Stack
**Backend:**
- Java 21
- Spring Boot (Spring MVC, Jakarta EE)
- WebSocket for real-time communication
- Lombok for boilerplate reduction
- Maven for dependency management

**Frontend:**
- React (v19)
- TypeScript (v6.0.2)
- Vite for fast development
- Sass for styling

## Project Structure
```
hive/
├── .idea/              # IntelliJ IDEA project files
├── client/             # React frontend application
├── server/             # Java backend application
│   ├── .mvn/           # Maven configuration
│   ├── src/            # Source code
│   │   ├── main/       # Main application code
│   │   │   ├── java/   # Java source files
│   │   │   │   └── com.hive.server/  # Core business logic
│   │   │   │       ├── config/       # Configuration classes
│   │   │   │       ├── controller/   # REST endpoints
│   │   │   │       ├── model/        # Data models
│   │   │   │       ├── service/      # Business logic
│   │   │   │       │   ├── bot/      # AI bot implementations
│   │   │   │       │   ├── engine/   # Game engine logic
│   │   │   │       │   ├── publisher/ # Move publishing
│   │   │   │       │   └── validator/ # Game state validation
│   │   │   │       └── ServerApplication.java # Main application
│   │   │   └── resources/ # Configuration files
│   │   └── test/        # Test files
│   ├── target/         # Build output
│   ├── .gitattributes
│   ├── HELP.md         # Additional setup instructions
│   ├── mvnw
│   ├── mvnw.cmd
│   └── pom.xml         # Maven project configuration
└── External Libraries   # Third-party dependencies
```


## Getting Started
### Prerequisites
- Java 21
- Node.js (for frontend)
- Maven
- Git

### Setup Instructions
1. **Clone the repository**
```shell script
git clone https://github.com/tinbob655/hive.git
```


2. **Backend Setup**
```shell script
cd server
mvn clean install
```


3. **Frontend Setup**
```shell script
cd client
npm install
```


4. **Run the application**
```shell script
# Start backend server
cd server
mvn spring-boot:run

# Start frontend development server
cd client
npm run dev
```