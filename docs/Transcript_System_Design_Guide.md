# Design & Build Guide — Academic Transcript Generation System
### A study scaffold for building it yourself (Java Swing)

> **For:** Assiyaw Lawrence (MS/ITE/25/0052) — INF811D Object Oriented Programming
> **How to use this:** This is a *learning guide*, not a finished project. It gives you the class structure, the formulas, and the OOP mapping. **You write the actual code**, in your own style — that way it's genuinely your work and you'll be able to explain every line at the demo. Build it in the order shown; test each layer before moving on.

---

## 1. What the system does

An Academic Transcript Generation System stores a student's courses and grades across semesters, computes the **semester GPA** and **cumulative GPA (CGPA)**, determines the **degree classification**, and generates a printable **transcript**.

**Core flow:**
```
Registrar signs in ─► select/enter a student ─► add course results per semester
                    ─► generate transcript ─► view GPA, CGPA, classification ─► print/export
```

---

## 2. Package structure (mirror this)

```
src/com/<yourname>/transcript/
├── Main.java
├── model/        # domain objects — your OOP core
├── service/      # logic: GPA calc, storage, auth, validation
├── exception/    # custom exceptions
└── gui/          # Java Swing screens
```

Keeping these four layers separate is itself worth marks (methods & modularisation).

---

## 3. The domain model — classes to build

Build these first, in the `model` package. Field lists and method **signatures** are given; **you write the bodies.**

### 3.1 `Course` (abstract) — abstraction + inheritance root
```java
public abstract class Course {
    private final String code;       // e.g. "INF811D"
    private final String title;
    private final int creditHours;
    // constructor validates: code not blank, creditHours > 0
    // getters only (encapsulation)

    // Different course categories weight/label differently → override this
    public abstract String getCategory();   // e.g. "Core", "Elective"
}
```
Then make two subclasses to demonstrate **inheritance + polymorphism**:
- `CoreCourse extends Course` → `getCategory()` returns `"Core"`
- `ElectiveCourse extends Course` → `getCategory()` returns `"Elective"`

### 3.2 `Grade` — mapping a score/letter to grade points
Decide how you input grades. Two common options — pick one:
- **Letter grade in** (A, B+, …) → map to grade point.
- **Raw score in** (0–100) → convert to letter → grade point.

A clean way is a small helper (or an `enum`):
```java
public enum Grade {
    A(4.0), B_PLUS(3.5), B(3.0), C_PLUS(2.5), C(2.0), D(1.0), F(0.0);
    private final double points;
    // constructor + getPoints()
    // static Grade fromScore(int score) { ... }   // e.g. >=80 -> A
}
```
> ⚠️ Use **your school's official grading scale** — confirm the exact thresholds and points. The numbers above are just an example.

### 3.3 `CourseResult` — one course + the grade earned
```java
public class CourseResult {
    private final Course course;
    private final Grade grade;
    // qualityPoints = grade.getPoints() * course.getCreditHours();
    public double getQualityPoints() { ... }
    public int getCreditHours() { ... }   // delegates to course
}
```

### 3.4 `SemesterRecord` — a semester's list of results
```java
public class SemesterRecord {
    private final String semesterName;    // e.g. "2025/2026 Sem 1"
    private final List<CourseResult> results = new ArrayList<>();
    public void addResult(CourseResult r) { ... }
    public double getGpa() { ... }        // see formula in §4
    public int getTotalCredits() { ... }
}
```

### 3.5 `Student`
```java
public class Student {
    private final String id;              // e.g. "MS/ITE/25/0052"
    private final String name;
    private final String programme;
    private final List<SemesterRecord> semesters = new ArrayList<>();
    // add semester, getters
}
```

### 3.6 `Transcript` — ties it together
```java
public class Transcript {
    private final Student student;
    public double getCgpa() { ... }               // across all semesters
    public String getClassification() { ... }     // see §5
    public LocalDate getGeneratedOn() { ... }
}
```

---

## 4. The key formula — GPA / CGPA

This is the heart of the system. Learn it, don't just copy it:

```
For each course result:
    qualityPoints = gradePoint × creditHours

Semester GPA = Σ(qualityPoints in semester) ÷ Σ(creditHours in semester)

CGPA        = Σ(qualityPoints across ALL semesters) ÷ Σ(all creditHours)
```

**Worked example (one semester):**
| Course | Credits | Grade | Points | Quality (pts×credits) |
|---|---|---|---|---|
| INF811D | 3 | A (4.0) | 4.0 | 12.0 |
| INF812D | 3 | B (3.0) | 3.0 | 9.0 |
| INF813D | 2 | B+ (3.5) | 3.5 | 7.0 |
| **Total** | **8** | | | **28.0** |

GPA = 28.0 ÷ 8 = **3.5**

Implement `getGpa()` by looping the results, summing quality points and credits, then dividing. **Guard against divide-by-zero** when there are no courses (throw or return 0.0).

---

## 5. Degree classification (polymorphism opportunity)

```
CGPA ≥ 3.6           First Class
3.0 – 3.59           Second Class Upper
2.5 – 2.99           Second Class Lower
2.0 – 2.49           Third Class
below 2.0            Pass / Fail
```
> Again — confirm your institution's actual bands.

**Nice OOP touch:** make transcript output polymorphic. Define an interface `TranscriptFormatter` with `format(Transcript t)`, then two implementations — `OfficialTranscriptFormatter` and `UnofficialTranscriptFormatter` — that render differently. The GUI calls `formatter.format(t)` without knowing which one it holds → clean polymorphism.

---

## 6. Service layer

| Class | Responsibility | Key methods |
|---|---|---|
| `GpaService` | Compute semester GPA & CGPA (or put this in the model) | `semesterGpa(...)`, `cgpa(...)` |
| `StudentRepository` | Hold students in memory; seed one or two demo students | `getStudents()`, `findById(...)`, `add(...)` |
| `AuthService` | Registrar login + validation | `login(user, pin)` throws your exception |
| `ClassificationService` | Map CGPA → classification (optional) | `classify(double cgpa)` |

## 7. Exception layer

```java
public class InvalidInputException extends Exception {
    public InvalidInputException(String message) { super(message); }
}
```
Throw it for: blank student name/ID, credit hours ≤ 0, score outside 0–100, wrong registrar PIN. **Catch it in the GUI** and show a `JOptionPane` dialog — that satisfies *exception handling + input validation*.

---

## 8. GUI screens (Java Swing)

Use a main `JFrame` with a `CardLayout` to switch screens (search "Java Swing CardLayout navigation").

1. **Login** — registrar username + PIN, validated.
2. **Student list / entry** — pick an existing student or add a new one.
3. **Add results** — form: choose course, credit hours, grade → adds a `CourseResult`; shows a running list per semester.
4. **Transcript view** — a `JTable` of all courses grouped by semester, with semester GPA rows, then CGPA and classification at the bottom. A **"Print"** button (look up `JTable`/`JTextComponent` printing or export the text to a file).

**GUI concepts to hit:** `JButton`, `JComboBox`, `JTextField`, `JTable`, action listeners (event-driven), and consistent styling (define your colours/fonts once and reuse).

---

## 9. OOP mapping (fill this table in your report)

| Concept | Where you'll implement it |
|---|---|
| **Encapsulation** | Private fields + getters in `Course`, `Student`, `CourseResult`, etc. |
| **Inheritance** | `Course → CoreCourse / ElectiveCourse` |
| **Polymorphism** | `getCategory()` overrides; `TranscriptFormatter` implementations |
| **Abstraction** | abstract `Course`; `TranscriptFormatter` interface |

Also demonstrate: operators/expressions (GPA math), control structures (loops summing points), methods/modularisation (layered packages), collections (`List` of results/semesters), event handling (Swing listeners), exception handling + validation.

---

## 10. Suggested build order (test after each step)

1. `Course` + `CoreCourse` + `ElectiveCourse` → print a couple to confirm.
2. `Grade` mapping → test `fromScore()` / `getPoints()`.
3. `CourseResult` → check `getQualityPoints()`.
4. `SemesterRecord.getGpa()` → verify with the worked example above (should give 3.5).
5. `Student` + `Transcript.getCgpa()` + classification.
6. `StudentRepository` with one seeded student.
7. `AuthService` + `InvalidInputException`.
8. GUI screens one at a time: Login → Student list → Add results → Transcript view.
9. `Main` — set look-and-feel, launch on the event dispatch thread.
10. README + screenshots + commit history on **his own** GitHub.

---

## 11. Git & GitHub (his own account, his own commits)

```bash
git init
git add .
git commit -m "Add course and grade model"     # commit in small logical chunks
# ...more commits as each layer is built...
git remote add origin <his repo URL>
git branch -M main
git push -u origin main
```
Commit **as he builds**, with meaningful messages — the rubric rewards a real development history.

---

### Final note
Work through this at his own pace when he's well. When he gets stuck on a specific piece — the GPA loop, a Swing layout, an exception — that's exactly the kind of focused question I can help him with directly, and it keeps the work genuinely his. Wishing him a quick recovery.
