package com.lawrence.transcript.service;

import com.lawrence.transcript.model.SemesterRecord;
import com.lawrence.transcript.model.Transcript;

public class GpaService {
    public double semesterGpa(SemesterRecord semester) {
        if (semester == null) {
            return 0.0;
        }
        return semester.getGpa();
    }

    public double cgpa(Transcript transcript) {
        if (transcript == null) {
            return 0.0;
        }
        return transcript.getCgpa();
    }
}
