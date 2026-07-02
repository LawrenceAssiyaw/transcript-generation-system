package com.lawrence.transcript;

import com.lawrence.transcript.exception.InvalidInputException;
import com.lawrence.transcript.model.CoreCourse;
import com.lawrence.transcript.model.CourseResult;
import com.lawrence.transcript.model.ElectiveCourse;
import com.lawrence.transcript.model.SemesterRecord;
import com.lawrence.transcript.model.Student;
import com.lawrence.transcript.model.Transcript;

public class TestHarness {
    public static void main(String[] args) throws InvalidInputException {
        Student student = new Student("MS/ITE/25/0052", "Assiyaw Lawrence", "MSc Information Technology Education");
        SemesterRecord semester = new SemesterRecord("2025/2026 Semester 1");
        semester.addResult(new CourseResult(new CoreCourse("INF811D", "Object Oriented Programming", 3), 85));
        semester.addResult(new CourseResult(new CoreCourse("INF812D", "Database Systems", 3), 62));
        semester.addResult(new CourseResult(new ElectiveCourse("INF813D", "Educational Technology", 2), 72));
        student.addSemester(semester);

        Transcript transcript = new Transcript(student);
        System.out.printf("Semester GPA: %.2f%n", semester.getGpa());
        System.out.printf("CGPA: %.2f%n", transcript.getCgpa());
        System.out.println("Classification: " + transcript.getClassification());
    }
}
