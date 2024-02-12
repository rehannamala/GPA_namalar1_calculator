package com.example.gpa_namalar1_calculator;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText[] courseGrades = new EditText[5];
    TextView gpaDisplay;
    boolean hasInvalidField = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        courseGrades[0] = findViewById(R.id.course1_grade);
        courseGrades[1] = findViewById(R.id.course2_grade);
        courseGrades[2] = findViewById(R.id.course3_grade);
        courseGrades[3] = findViewById(R.id.course4_grade);
        courseGrades[4] = findViewById(R.id.course5_grade);

        gpaDisplay = findViewById(R.id.gpa_display);

        // Add text change listeners to each EditText field
        for (final EditText courseGrade : courseGrades) {
            courseGrade.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(Editable s) {
                    checkValidity(courseGrade);
                }
            });
        }

        Button computeButton = findViewById(R.id.compute_button);
        computeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                computeGPA();
            }
        });
    }

    private void computeGPA() {
        double totalGradePoints = 0;
        int totalCredits = 0;
        hasInvalidField = false;

        for (EditText courseGrade : courseGrades) {
            if (!checkValidity(courseGrade)) {
                hasInvalidField = true;
                break;
            }

            double grade = Double.parseDouble(courseGrade.getText().toString().trim());
            // Assume all courses are of equal credits, say 3 credits each
            totalGradePoints += grade * 3;
            totalCredits += 3;
        }

        if (hasInvalidField) {
            return;
        }

        double gpa = totalGradePoints / totalCredits;
        gpaDisplay.setText("GPA: " + String.format("%.2f", gpa));

        // Change background color based on GPA range
        if (gpa < 60) {
            gpaDisplay.setBackgroundColor(Color.RED);
        } else if (gpa <= 79) {
            gpaDisplay.setBackgroundColor(Color.YELLOW);
        } else {
            gpaDisplay.setBackgroundColor(Color.GREEN);
        }

        Button computeButton = findViewById(R.id.compute_button);
        computeButton.setText("Clear Form");
        computeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearForm();
            }
        });
    }

    private boolean checkValidity(EditText editText) {
        String gradeString = editText.getText().toString().trim();
        if (gradeString.isEmpty()) {
            editText.setError("Please fill this field");
            return false;
        }

        try {
            double grade = Double.parseDouble(gradeString);
            if (grade < 0 || grade > 100) {
                editText.setError("Invalid grade. Enter a number between 0 and 100.");
                return false;
            }
        } catch (NumberFormatException e) {
            editText.setError("Invalid grade. Enter a valid number.");
            return false;
        }

        editText.setError(null); // Clear error if there was any
        return true;
    }

    private void clearForm() {
        for (EditText courseGrade : courseGrades) {
            courseGrade.setText("");
        }
        gpaDisplay.setText("GPA: ");
        gpaDisplay.setBackgroundColor(Color.TRANSPARENT);

        Button computeButton = findViewById(R.id.compute_button);
        computeButton.setText("Compute GPA");
        computeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                computeGPA();
            }
        });
    }
}
