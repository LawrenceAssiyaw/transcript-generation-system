# Technical Report

## Academic Transcript Generation System

**Student Name:** Assiyaw Lawrence  
**Student ID:** MS/ITE/25/0052  
**Course:** INF811D Object Oriented Programming  
**Project Type:** Java Swing Desktop Application  
**Project Title:** Academic Transcript Generation System  
**Submission Date:** July 2026

---

## Table of Contents

1. Abstract
2. Introduction
3. Problem Statement
4. Aim and Objectives
5. Scope of the System
6. System Requirements
7. System Design
8. Object Oriented Programming Concepts Applied
9. Implementation Details
10. Testing and Validation
11. User Guide
12. Challenges and Solutions
13. Conclusion
14. Recommendations
15. References


---

## 1. Abstract

The Academic Transcript Generation System is a Java Swing desktop application developed to help a registrar record student course results and generate academic transcripts. The system stores student details, semester records, courses, scores, grades, grade points, semester GPA, cumulative GPA, and degree classification. It applies Object Oriented Programming principles such as encapsulation, inheritance, polymorphism, and abstraction through a well-structured class design.

The application provides a graphical user interface where the registrar logs in, selects or adds a student, enters course results, and views an official or unofficial transcript. It also supports transcript printing and text export. The system demonstrates modular programming by separating the project into model, service, formatter, exception, and graphical user interface packages.

---

## 2. Introduction

Academic institutions need accurate and organized systems for processing student results. A transcript is an important academic document because it summarizes a student's courses, grades, GPA, CGPA, and classification. Manual transcript preparation can lead to errors in calculation, missing records, and delays.

This project was developed as an Object Oriented Programming practical application. It uses Java Swing for the graphical user interface and Java classes to model real-world academic objects such as students, courses, semester records, course results, and transcripts.

The project focuses on showing how OOP concepts can be used to solve a real academic record problem in a clean and understandable way.

---

## 3. Problem Statement

Many transcript processes require repeated manual calculations. This can create the following problems:

- Wrong GPA or CGPA calculations.
- Difficulty organizing course results by semester.
- Repetition when preparing transcript records.
- Poor validation of student details, scores, and credit hours.
- Lack of a simple interface for entering and viewing transcript information.

The Academic Transcript Generation System addresses these problems by automating result entry, grade conversion, GPA calculation, CGPA calculation, classification, and transcript display.

---

## 4. Aim and Objectives

### Aim

The aim of this project is to design and implement an Academic Transcript Generation System using Java Swing and Object Oriented Programming principles.

### Objectives

The objectives are to:

1. Create a registrar login interface.
2. Allow the registrar to add and select students.
3. Allow course results to be added under semesters.
4. Convert raw scores into grades and grade points.
5. Calculate semester GPA automatically.
6. Calculate cumulative GPA across all semesters.
7. Determine degree classification from CGPA.
8. Display transcript information in official and unofficial formats.
9. Demonstrate OOP concepts in a working Java application.
10. Provide a simple way to print or export transcript information.

---

## 5. Scope of the System

The system covers the following functions:

- Registrar authentication.
- Student record management.
- Course result entry.
- Core and elective course classification.
- Raw score to grade conversion.
- GPA and CGPA calculation.
- Degree classification.
- Transcript viewing.
- Transcript printing.
- Transcript text export.

The system is designed as a desktop application. It uses in-memory data storage, meaning that data entered during a session is available while the application is running. A future version can add database storage.

---

## 6. System Requirements

### Hardware Requirements

- A computer capable of running Java.
- At least 2 GB RAM.
- Keyboard and mouse.
- Printer, if transcript printing is required.

### Software Requirements

- Java Development Kit, version 17 or later.
- Windows operating system.
- Command Prompt or PowerShell.
- Git, if the project will be uploaded to GitHub.

### Development Tools

- Java programming language.
- Java Swing GUI library.
- Git for version control.
- GitHub for remote repository hosting.

---

## 7. System Design

### 7.1 Package Structure

The system is organized into packages to support modular programming.

```text
src/com/lawrence/transcript/
  Main.java
  TestHarness.java
  exception/
  formatter/
  gui/
  model/
  service/
```

### 7.2 Main Packages

| Package | Purpose |
|---|---|
| `model` | Contains the main domain classes such as `Student`, `Course`, `SemesterRecord`, and `Transcript`. |
| `service` | Contains business logic such as login, GPA services, classification, and student repository operations. |
| `formatter` | Contains transcript formatting classes. |
| `exception` | Contains the custom validation exception. |
| `gui` | Contains Java Swing user interface classes. |

### 7.3 System Flow

1. Registrar opens the application.
2. Registrar logs in with username and PIN.
3. Registrar selects an existing student or adds a new student.
4. Registrar adds course results for a semester.
5. The system converts the score to a grade.
6. The system calculates quality points, GPA, CGPA, and classification.
7. Registrar views the transcript.
8. Registrar prints or exports the transcript.

### 7.4 GPA and CGPA Formula

For each course:

```text
Quality Points = Grade Point * Credit Hours
```

For a semester:

```text
Semester GPA = Total Quality Points for Semester / Total Semester Credit Hours
```

For all semesters:

```text
CGPA = Total Quality Points Across All Semesters / Total Credit Hours Across All Semesters
```

### 7.5 Degree Classification

| CGPA Range | Classification |
|---|---|
| 3.60 and above | First Class |
| 3.00 to 3.59 | Second Class Upper |
| 2.50 to 2.99 | Second Class Lower |
| 2.00 to 2.49 | Third Class |
| Below 2.00 | Pass / Fail |

---

## 8. Object Oriented Programming Concepts Applied

### 8.1 Encapsulation

Encapsulation is used by making class fields private and providing public methods to access them. Examples include:

- `Course` stores `code`, `title`, and `creditHours` as private fields.
- `Student` stores `id`, `name`, `programme`, and semester records as private fields.
- `CourseResult` stores the course, score, and grade as private fields.

This prevents direct uncontrolled access to object data.

### 8.2 Inheritance

Inheritance is used through the `Course` class hierarchy.

```text
Course
  CoreCourse
  ElectiveCourse
```

`CoreCourse` and `ElectiveCourse` inherit common course properties from `Course` but provide their own course category.

### 8.3 Polymorphism

Polymorphism is used in two main places:

1. `getCategory()` is implemented differently in `CoreCourse` and `ElectiveCourse`.
2. `TranscriptFormatter` has different implementations:
   - `OfficialTranscriptFormatter`
   - `UnofficialTranscriptFormatter`

The GUI can call `formatter.format(transcript)` without needing to know the exact formatter implementation.

### 8.4 Abstraction

Abstraction is used through:

- The abstract `Course` class.
- The `TranscriptFormatter` interface.

These hide implementation details and define what subclasses or implementations must provide.

### 8.5 Exception Handling

The system uses a custom checked exception named `InvalidInputException`. It is thrown when invalid input is detected, such as:

- Blank student ID.
- Blank student name.
- Credit hours less than or equal to zero.
- Score outside 0 to 100.
- Wrong registrar login details.

The GUI catches the exception and displays the message using `JOptionPane`.

### 8.6 Collections

The system uses `ArrayList` to store:

- Students in `StudentRepository`.
- Semester records in `Student`.
- Course results in `SemesterRecord`.

### 8.7 Event Handling

Swing action listeners are used for button actions such as login, adding a student, adding a course result, viewing a transcript, printing, and exporting.

---

## 9. Implementation Details

### 9.1 Main Class

`Main.java` starts the application. It launches the Swing interface using `SwingUtilities.invokeLater()` so the GUI runs on the Event Dispatch Thread.

### 9.2 Model Classes

| Class | Description |
|---|---|
| `Course` | Abstract parent class for all course types. |
| `CoreCourse` | Represents a core course. |
| `ElectiveCourse` | Represents an elective course. |
| `Grade` | Enum that maps scores to grade points. |
| `CourseResult` | Combines a course with the score and grade earned. |
| `SemesterRecord` | Stores all course results for one semester. |
| `Student` | Stores student details and semester records. |
| `Transcript` | Calculates CGPA and classification for a student. |

### 9.3 Service Classes

| Class | Description |
|---|---|
| `AuthService` | Validates registrar login. |
| `StudentRepository` | Stores demo students and added students in memory. |
| `GpaService` | Provides GPA and CGPA helper methods. |
| `ClassificationService` | Maps CGPA to classification. |

### 9.4 Formatter Classes

| Class | Description |
|---|---|
| `TranscriptFormatter` | Interface for transcript output formatting. |
| `OfficialTranscriptFormatter` | Produces a detailed official transcript layout. |
| `UnofficialTranscriptFormatter` | Produces a short GPA summary. |

### 9.5 GUI Classes

| Class | Description |
|---|---|
| `MainFrame` | Main Swing frame with login, student, result, and transcript screens. |
| `AppTheme` | Centralizes colors, fonts, and button styles. |

---

## 10. Testing and Validation

### 10.1 Test Harness

A `TestHarness` class is included to test the GPA and CGPA logic without opening the graphical interface.

The test creates a student, adds course results, creates a transcript, and prints the semester GPA, CGPA, and classification.

### 10.2 Test Command

The test can be run with:

```powershell
.\test.bat
```

### 10.3 Test Output

The verified output was:

```text
Semester GPA: 3.50
CGPA: 3.50
Classification: Second Class Upper
```

### 10.4 Validation Rules

The system validates:

- Username and PIN.
- Student ID.
- Student name.
- Programme.
- Semester name.
- Course code.
- Course title.
- Credit hours.
- Score range.

---

## 11. User Guide

### 11.1 Running the Application

Double-click:

```text
run.bat
```

Or run from PowerShell:

```powershell
cd C:\Users\Administrator\Desktop\transcript-generation-system
.\run.bat
```

### 11.2 Login Details

```text
Username: registrar
PIN: 1234
```

### 11.3 Adding a Student

1. Log in as registrar.
2. Click `Add Student`.
3. Enter the student ID, name, and programme.
4. Click OK.
5. Select the student from the list.

### 11.4 Adding Course Results

1. Select a student.
2. Click `Add Results`.
3. Enter semester, course code, course title, credit hours, score, and course category.
4. Click `Add Result`.
5. The result appears in the table with grade and quality points.

### 11.5 Viewing Transcript

1. Select a student.
2. Click `View Transcript`.
3. Choose `Official` or `Unofficial` format.
4. Use `Print` to print or `Export Text` to save a text file.

---

## 12. Challenges and Solutions

| Challenge | Solution |
|---|---|
| Keeping the project modular | The code was separated into model, service, formatter, exception, and GUI packages. |
| Preventing invalid records | A custom exception was used for validation. |
| Showing OOP concepts clearly | The `Course` hierarchy and formatter interface were used to demonstrate inheritance, polymorphism, and abstraction. |
| Avoiding wrong GPA calculations | GPA and CGPA were calculated from quality points and total credits. |
| Running easily on Windows | `run.bat` and `test.bat` scripts were added. |

---

## 13. Conclusion

The Academic Transcript Generation System successfully demonstrates how Object Oriented Programming can be used to solve an academic record management problem. The system allows a registrar to manage students, enter course results, calculate GPA and CGPA, classify degree performance, and generate a transcript.

The project applies encapsulation, inheritance, polymorphism, abstraction, exception handling, collections, methods, control structures, and event-driven programming. It also demonstrates modular design by separating the code into clear packages.

---

## 14. Recommendations

Future improvements may include:

1. Adding database storage so student records are saved permanently.
2. Adding a password-protected administrator account setup.
3. Exporting transcripts directly to PDF.
4. Adding transcript search and filtering.
5. Adding more official grading scales.
6. Adding a backup and restore feature.
7. Adding institution branding, logo, and signature areas.

---

## 15. References

1. Oracle. Java Swing Documentation.
2. Oracle. Java Platform Standard Edition Documentation.
3. Course material for INF811D Object Oriented Programming.
4. Project source code: Academic Transcript Generation System.

---

## 16. Appendix

### Appendix A: Important Source Files

| File | Purpose |
|---|---|
| `src/com/lawrence/transcript/Main.java` | Starts the application. |
| `src/com/lawrence/transcript/gui/MainFrame.java` | Main GUI screens and event handling. |
| `src/com/lawrence/transcript/gui/AppTheme.java` | Interface colors and fonts. |
| `src/com/lawrence/transcript/model/Course.java` | Abstract course class. |
| `src/com/lawrence/transcript/model/CoreCourse.java` | Core course subclass. |
| `src/com/lawrence/transcript/model/ElectiveCourse.java` | Elective course subclass. |
| `src/com/lawrence/transcript/model/Grade.java` | Score to grade conversion. |
| `src/com/lawrence/transcript/model/SemesterRecord.java` | Semester GPA calculation. |
| `src/com/lawrence/transcript/model/Transcript.java` | CGPA and classification. |
| `src/com/lawrence/transcript/formatter/TranscriptFormatter.java` | Formatter interface. |
| `src/com/lawrence/transcript/exception/InvalidInputException.java` | Custom validation exception. |

### Appendix B: GitHub Upload Commands

```powershell
cd C:\Users\Administrator\Desktop\transcript-generation-system
git status
git remote -v
git push -u origin main
```

### Appendix C: Formula Test Output

```text
Semester GPA: 3.50
CGPA: 3.50
Classification: Second Class Upper
```