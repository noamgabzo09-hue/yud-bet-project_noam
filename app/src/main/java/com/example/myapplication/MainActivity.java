package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.content.SharedPreferences;
import android.util.Patterns;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText firstName, lastName, email;
    RadioGroup classGroup;
    Spinner major;
    CheckBox notifications, terms;
    Button registerButton;
    TextView result, termsError, classError, majorError;

    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        email = findViewById(R.id.email);

        classGroup = findViewById(R.id.classGroup);
        major = findViewById(R.id.major);

        notifications = findViewById(R.id.notifications);
        terms = findViewById(R.id.terms);

        registerButton = findViewById(R.id.registerButton);
        result = findViewById(R.id.result);

        termsError = findViewById(R.id.termsError);
        classError = findViewById(R.id.classError);
        majorError = findViewById(R.id.majorError);

        preferences = getSharedPreferences(
                "StudentData", MODE_PRIVATE);

        String[] majors = {
                getString(R.string.choose_major),
                getString(R.string.computer_science),
                getString(R.string.physics),
                getString(R.string.biology),
                getString(R.string.electronics)
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                majors);

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);

        major.setAdapter(adapter);

        loadData();

        registerButton.setOnClickListener(v -> register());
    }

    private void register() {

        firstName.setError(null);
        lastName.setError(null);
        email.setError(null);

        termsError.setVisibility(View.GONE);
        classError.setVisibility(View.GONE);
        majorError.setVisibility(View.GONE);

        boolean valid = true;

        String fName = firstName.getText().toString().trim();
        String lName = lastName.getText().toString().trim();
        String mail = email.getText().toString().trim();

        if (fName.isEmpty()) {
            firstName.setError(
                    getString(R.string.error_first_name));
            valid = false;
        }

        if (lName.isEmpty()) {
            lastName.setError(
                    getString(R.string.error_last_name));
            valid = false;
        }

        if (mail.isEmpty() ||
                !Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
            email.setError(
                    getString(R.string.error_email));
            valid = false;
        }

        int selectedId = classGroup.getCheckedRadioButtonId();
        String studentClass = "";

        if (selectedId == -1) {
            classError.setText(
                    getString(R.string.error_class));
            classError.setVisibility(View.VISIBLE);
            valid = false;
        } else {
            RadioButton selectedClass = findViewById(selectedId);
            studentClass = selectedClass.getText().toString();
        }

        String selectedMajor = major.getSelectedItem().toString();

        if (major.getSelectedItemPosition() == 0) {
            majorError.setText(
                    getString(R.string.error_major));
            majorError.setVisibility(View.VISIBLE);
            valid = false;
        }

        if (!terms.isChecked()) {
            termsError.setText(
                    getString(R.string.error_terms));
            termsError.setVisibility(View.VISIBLE);
            valid = false;
        }

        if (!valid) {
            result.setVisibility(View.GONE);
            return;
        }

        Student student = new Student(
                fName,
                lName,
                mail,
                studentClass,
                selectedMajor,
                notifications.isChecked(),
                terms.isChecked()
        );

        saveData(student);
        showData(student, false);
    }

    private void saveData(Student student) {

        SharedPreferences.Editor editor = preferences.edit();

        editor.putString("firstName", student.firstName);
        editor.putString("lastName", student.lastName);
        editor.putString("email", student.email);
        editor.putString("studentClass", student.studentClass);
        editor.putString("major", student.major);

        editor.putBoolean("notifications", student.notifications);
        editor.putBoolean("terms", student.terms);
        editor.putBoolean("hasData", true);

        editor.apply();
    }

    private void loadData() {

        if (preferences.getBoolean("hasData", false)) {

            Student student = new Student(
                    preferences.getString("firstName", ""),
                    preferences.getString("lastName", ""),
                    preferences.getString("email", ""),
                    preferences.getString("studentClass", ""),
                    preferences.getString("major", ""),
                    preferences.getBoolean("notifications", false),
                    preferences.getBoolean("terms", false)
            );

            showData(student, true);
        }
    }

    private void showData(Student student, boolean saved) {

        String text;

        if (saved) {
            text = getString(R.string.saved_title) + "\n";
        } else {
            text = getString(R.string.student_details) + "\n";
        }

        text = text
                + getString(R.string.name_label) + ": "
                + student.firstName + " " + student.lastName + "\n"
                + getString(R.string.email_label) + ": "
                + student.email + "\n"
                + getString(R.string.class_label) + ": "
                + student.studentClass + "\n"
                + getString(R.string.major_label) + ": "
                + student.major + "\n"
                + getString(R.string.notifications_label) + ": ";

        if (student.notifications) {
            text = text + getString(R.string.notifications_yes);
        } else {
            text = text + getString(R.string.notifications_no);
        }

        text = text + "\n"
                + getString(R.string.terms_approved);

        result.setText(text);
        result.setVisibility(View.VISIBLE);
    }
}
