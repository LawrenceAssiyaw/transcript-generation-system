# Academic Transcript Generation System

Java Swing desktop application for generating student academic transcripts. It stores students, semesters, course results, GPA, CGPA, and degree classification.

## Login

- Username: `registrar`
- PIN: `1234`

## Features

- Registrar login validation
- Add and select students
- Add core or elective course results by semester
- Convert raw score to grade and grade points
- Compute semester GPA and CGPA
- Show degree classification
- View official and unofficial transcript formats
- Print transcript from the app
- Export transcript as a text file

## OOP Concepts Used

- Encapsulation: private fields and getters in model classes
- Inheritance: `Course` parent class with `CoreCourse` and `ElectiveCourse`
- Polymorphism: course category overrides and `TranscriptFormatter` implementations
- Abstraction: abstract `Course` class and `TranscriptFormatter` interface
- Exception handling: `InvalidInputException` for wrong login and invalid input
- Collections: `List` stores students, semesters, and course results
- Event handling: Swing action listeners drive buttons and navigation

## Project Structure

```text
src/com/lawrence/transcript/
  Main.java
  TestHarness.java
  exception/
  formatter/
  gui/
  model/
  service/
docs/
```

## Run on Windows

Double-click:

```text
run.bat
```

Or from PowerShell:

```powershell
.\run.bat
```

## Compile Manually

```powershell
javac -d out (Get-ChildItem -Recurse src\*.java).FullName
java -cp out com.lawrence.transcript.Main
```

## Run Formula Test

```powershell
.\test.bat
```

Expected output is a calculated GPA, CGPA, and classification from sample course results.

## Notes

The grading scale is implemented in `Grade.fromScore()`:

- 80-100: A, 4.0
- 70-79: B+, 3.5
- 60-69: B, 3.0
- 55-59: C+, 2.5
- 50-54: C, 2.0
- 40-49: D, 1.0
- 0-39: F, 0.0

Adjust this file if the institution uses a different official grading scale.
