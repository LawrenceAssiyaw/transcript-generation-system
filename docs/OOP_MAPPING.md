# OOP Mapping

| Concept | Implementation |
|---|---|
| Encapsulation | Model fields are private in `Course`, `Student`, `SemesterRecord`, `CourseResult`, and `Transcript`. Access is through methods. |
| Inheritance | `CoreCourse` and `ElectiveCourse` inherit from the abstract `Course` class. |
| Polymorphism | `getCategory()` behaves differently in each course subclass. `OfficialTranscriptFormatter` and `UnofficialTranscriptFormatter` both implement `TranscriptFormatter`. |
| Abstraction | `Course` hides shared course state while forcing subclasses to provide a category. `TranscriptFormatter` hides formatting details from the GUI. |
| Methods and modularisation | Logic is separated into `model`, `service`, `formatter`, `exception`, and `gui` packages. |
| Control structures | GPA and CGPA calculations loop through semester and result collections. |
| Operators and expressions | `qualityPoints = gradePoint * creditHours`; GPA divides total quality points by total credits. |
| Collections | `ArrayList` stores students, semester records, and course results. |
| Exception handling | `InvalidInputException` is thrown for invalid login and invalid data, then shown in Swing dialogs. |
| Event handling | Buttons use Swing action listeners in `MainFrame`. |
