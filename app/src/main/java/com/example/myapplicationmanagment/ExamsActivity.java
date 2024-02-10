package com.example.myapplicationmanagment;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ExamsActivity extends AppCompatActivity {

    private DBHelper dbHelper; // Оголошення змінної
    private SQLiteDatabase db;
    private List<ExamItem> examItemList;
    private ExamAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exams);

        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();

        // Ініціалізація списку та адаптера
        examItemList = new ArrayList<>();
        adapter = new ExamAdapter(examItemList);

        // Ініціалізація RecyclerView та встановлення адаптера
        RecyclerView recyclerView = findViewById(R.id.recyclerViewExams);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Додавання обробника кліків для кнопки "Add Exam"
        Button addExamButton = findViewById(R.id.addButton);
        addExamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddExamDialog();
            }
        });

        // Додавання обробника кліків для кнопки "Back to Main Menu"
        Button backToMainMenuButton = findViewById(R.id.backToMainMenuButton);
        backToMainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Перехід до головного меню
                Intent intent = new Intent(ExamsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // Завантаження даних з бази даних
        loadExamsFromDatabase();
    }

    private void showAddExamDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.add_exam_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText editTextSubject = dialogView.findViewById(R.id.editTextSubject);
        final EditText editTextDate = dialogView.findViewById(R.id.editTextDate);
        final EditText editTextTime = dialogView.findViewById(R.id.editTextTime);

        dialogBuilder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String subject = editTextSubject.getText().toString();
                String date = editTextDate.getText().toString();
                String time = editTextTime.getText().toString();

                // Додати новий іспит до бази даних
                addExamToDatabase(subject, date, time);

                // Оновити список іспитів
                loadExamsFromDatabase();
            }
        });

        dialogBuilder.setNegativeButton("Cancel", null);

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    private void addExamToDatabase(String subject, String date, String time) {
        ContentValues values = new ContentValues();
        values.put("name", subject);
        values.put("date", date);
        values.put("time", time);

        long newRowId = db.insert("exams", null, values);
        if (newRowId != -1) {
            Toast.makeText(ExamsActivity.this, "Exam added successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(ExamsActivity.this, "Failed to add exam", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadExamsFromDatabase() {
        // Очистити список перед завантаженням нових даних
        examItemList.clear();

        // Запит до бази даних для отримання майбутніх іспитів
        Cursor cursor = db.query("exams", null, null, null, null, null, null);
        try {
            while (cursor.moveToNext()) {
                String subject = cursor.getString(cursor.getColumnIndex("name"));
                String date = cursor.getString(cursor.getColumnIndex("date"));
                String time = cursor.getString(cursor.getColumnIndex("time"));
                // Додавання отриманих даних до списку
                examItemList.add(new ExamItem(subject, date, time));
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error loading exams: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } finally {
            cursor.close();
        }

        // Оновлення адаптера після завантаження даних
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (db != null) {
            db.close();
        }
    }
}
