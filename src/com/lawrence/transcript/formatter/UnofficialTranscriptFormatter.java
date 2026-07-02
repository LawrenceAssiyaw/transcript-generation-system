package com.lawrence.transcript.formatter;

import com.lawrence.transcript.model.SemesterRecord;
import com.lawrence.transcript.model.Transcript;

public class UnofficialTranscriptFormatter implements TranscriptFormatter {
    @Override
    public String format(Transcript transcript) {
        StringBuilder builder = new StringBuilder();
        builder.append("Unofficial GPA Summary\n");
        builder.append(transcript.getStudent().getName())
                .append(" (")
                .append(transcript.getStudent().getId())
                .append(")\n\n");
        for (SemesterRecord semester : transcript.getStudent().getSemesters()) {
            builder.append(semester.getSemesterName())
                    .append(": GPA ")
                    .append(String.format("%.2f", semester.getGpa()))
                    .append(" from ")
                    .append(semester.getTotalCredits())
                    .append(" credits\n");
        }
        builder.append("\nCGPA: ")
                .append(String.format("%.2f", transcript.getCgpa()))
                .append(" - ")
                .append(transcript.getClassification());
        return builder.toString();
    }
}
