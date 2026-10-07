<div align="center">

# Explain My Codebase

### A Java Full Stack Platform for Understanding Software Projects

**Upload → Analyze → Explore → Search**

A web-based codebase analysis platform that transforms an unfamiliar software project into structured, searchable information.

<br>

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-Backend-brightgreen)
![React](https://img.shields.io/badge/React-Frontend-61DAFB)
![MySQL](https://img.shields.io/badge/MySQL-Database-blue)
![JavaParser](https://img.shields.io/badge/JavaParser-Code%20Analysis-red)

</div>

---

**What is Explain My Codebase?**

Understanding an unfamiliar codebase usually means opening files one by one, finding classes, tracing methods, checking imports, and trying to understand how everything fits together.

**Explain My Codebase** automates the initial analysis.

Upload a project as a ZIP file and the system extracts and analyzes it, then presents important structural information through a web interface.

The project performs its analysis **programmatically using JavaParser and does not use AI**.

---

**What Can It Do?**

| Capability | Description |
|---|---|
| Project Management | Create, view, update and delete projects |
| ZIP Upload | Upload an existing software project |
| File Analysis | Identify different types of project files |
| Java Analysis | Analyze Java source code using JavaParser |
| Dependency Analysis | Identify Java imports and dependencies |
| Code Search | Search for classes and methods |
| Web Dashboard | View analysis results through a React interface |

---

**How It Works**

```text
                        USER
                          │
                          ▼
                  ┌───────────────┐
                  │ React Frontend│
                  └───────┬───────┘
                          │
                       REST API
                          │
                          ▼
                  ┌───────────────┐
                  │ Spring Boot   │
                  │    Backend    │
                  └───────┬───────┘
                          │
          ┌───────────────┼────────────────┐
          │               │                │
          ▼               ▼                ▼
     ZIP Upload      File Scanner     JavaParser
          │               │                │
          ▼               │                ▼
    ZIP Extraction        │          Java Analysis
          │               │                │
          └───────────────┼────────────────┘
                          │
                          ▼
                 Dependency Analysis
                          │
                          ▼
                     Code Search
                          │
                          ▼
                    MySQL Database


The Analysis Pipeline
Project ZIP
     │
     ▼
Extract Project
     │
     ▼
Scan Files
     │
     ├── Java
     ├── JavaScript
     ├── HTML
     ├── CSS
     ├── JSON
     └── Images
     
     ▼
Find Java Files
     │
     ▼
JavaParser
     │
     ├── Package
     ├── Imports
     ├── Classes
     ├── Interfaces
     ├── Fields
     ├── Constructors
     ├── Methods
     ├── Parameters
     └── Modifiers
     
     ▼
Dependency Analysis
     │
     ▼
Search
     │
     ▼
React Dashboard



Java Code Analysis
The Java analysis engine uses JavaParser to understand the structure of Java source files.
It currently extracts:
Package
Imports

Classes
Interfaces
Class Modifiers
Inheritance
Implemented Interfaces

Fields
Field Types
Field Modifiers

Constructors
Constructor Parameters

Methods
Method Return Types
Method Parameters
Method Modifiers


For example:
package com.example.demo;

public class Student {

    private Long id;
    private String name;

    public Student(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

The system can transform that source code into structured information:
Package
└── com.example.demo

Class
└── Student

Fields
├── id : Long
└── name : String

Constructor
└── Student(Long id, String name)

Methods
└── getName() : String

Technology Stack:
| Layer | Technologies |
|---|---|
| Frontend | React.js, JavaScript, Vite, Axios |
| Backend | Java, Spring Boot, Spring Data JPA |
| Code Analysis | JavaParser |
| File Processing | Apache Commons Compress |
| Database | MySQL |
| Build Tool | Maven |
| API | REST |
| Version Control | Git & GitHub |


System Architecture

┌─────────────────────────────────────────────┐
│                 FRONTEND                    │
│                                             │
│              React + Vite                   │
│                                             │
│  Dashboard • Upload • Search • Analysis    │
└──────────────────────┬──────────────────────┘
                       │
                       │ HTTP / REST
                       ▼
┌─────────────────────────────────────────────┐
│                  BACKEND                    │
│                                             │
│              Spring Boot                   │
│                                             │
│  Controllers                                │
│      │                                      │
│  Services                                   │
│      │                                      │
│  JavaParser • File Scanner • ZIP Handler    │
└──────────────────────┬──────────────────────┘
                       │
                       ▼
┌─────────────────────────────────────────────┐
│                  MySQL                      │
│                                             │
│              Project Data                   │
└─────────────────────────────────────────────┘


Project Structure

Explain-My-Codebase/
│
├── backend/
│   │
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/example/backend/
│   │   │   │       │
│   │   │   │       ├── controller/
│   │   │   │       │   ├── DependencyController.java
│   │   │   │       │   ├── JavaParserController.java
│   │   │   │       │   ├── ProjectAnalysisController.java
│   │   │   │       │   └── SearchController.java
│   │   │   │       │
│   │   │   │       ├── entity/
│   │   │   │       │   └── Project.java
│   │   │   │       │
│   │   │   │       ├── parser/
│   │   │   │       │   └── JavaCodeParser.java
│   │   │   │       │
│   │   │   │       ├── repository/
│   │   │   │       │   └── ProjectRepository.java
│   │   │   │       │
│   │   │   │       ├── service/
│   │   │   │       │   └── ProjectService.java
│   │   │   │       │
│   │   │   │       └── upload/
│   │   │   │           ├── ProjectFileScanner.java
│   │   │   │           ├── ProjectUploadController.java
│   │   │   │           ├── ProjectUploadService.java
│   │   │   │           └── ZipExtractionService.java
│   │   │   │
│   │   │   └── resources/
│   │   │       └── application.properties
│   │   │
│   │   └── test/
│   │
│   ├── pom.xml
│   ├── mvnw
│   └── mvnw.cmd
│
├── frontend/
│   │
│   ├── src/
│   │   ├── assets/
│   │   ├── App.jsx
│   │   ├── App.css
│   │   ├── index.css
│   │   └── main.jsx
│   │
│   ├── public/
│   ├── package.json
│   └── vite.config.js
│
├── .gitignore
└── README.md



REST API
http://localhost:8080

Project Management
POST    /api/projects
GET     /api/projects
GET     /api/projects/{id}
PUT     /api/projects/{id}
DELETE  /api/projects/{id}

Project Upload
POST    /api/projects/{id}/upload

Project Analysis
GET     /api/projects/{id}/analysis

Java Analysis
GET     /api/projects/{id}/java-analysis

Dependency Analysis
GET     /api/projects/{id}/dependencies

Code Search
GET     /api/projects/{id}/search?query=Student


Running the Project
Requirements
Java 17+
MySQL
Node.js
npm
Git

1. Clone the repository
git clone https://github.com/shaffu111/Explain-My-Codebase.git
cd Explain-My-Codebase

2. Configure MySQL
explain_my_codebase
Configure the database connection in:
backend/src/main/resources/application.properties

Example:
spring.application.name=backend

spring.datasource.url=jdbc:mysql://localhost:3306/explain_my_codebase
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

server.port=8080

3. Start the backend
cd backend
.\mvnw.cmd spring-boot:run

4. Start the frontend
Open another terminal:
cd frontend
npm install
npm run dev

Project Workflow
Create Project
      │
      ▼
Upload ZIP
      │
      ▼
Extract Project
      │
      ▼
Scan Files
      │
      ▼
Analyze Java Code
      │
      ├── Classes
      ├── Fields
      ├── Constructors
      ├── Methods
      └── Imports
      │
      ▼
Analyze Dependencies
      │
      ▼
Search Codebase
      │
      ▼
View Results

Why This Project?
Software developers frequently work with codebases they did not originally create.
Before making changes, they need to understand:

Where are the important classes?
Which methods exist?
What does each class contain?
Which files depend on others?
How is the project organized?
Explain My Codebase provides an automated starting point for answering these questions.
Instead of manually exploring every file, developers can upload the project and immediately view its structural information.


Current Status
Project Management       ✓
ZIP Upload               ✓
ZIP Extraction           ✓
File Scanning            ✓
Java Parsing             ✓
Class Analysis           ✓
Field Analysis           ✓
Constructor Analysis     ✓
Method Analysis          ✓
Dependency Analysis      ✓
Code Search              ✓
React Frontend           ✓
Spring Boot Backend      ✓
MySQL Integration        ✓

Future Scope
The current implementation focuses on Java project analysis.
Possible future extensions include:
• Support for additional programming languages
• Advanced dependency visualization
• Architecture diagrams
• REST API detection
• Database entity detection
• Improved code navigation
• More detailed project metrics
• User authentication
• Cloud deployment
These are future improvements and are not part of the current implementation.

Development Approach
This project focuses on understanding software structure through programmatic source-code analysis rather than AI-generated explanations.
The core analysis is performed using:
Java
   +
Spring Boot
   +
JavaParser
   +
File Processing
   +
REST APIs
   +
React
   +
MySQL


<div align="center">

Built by MD. Shafeequddin Ahmad