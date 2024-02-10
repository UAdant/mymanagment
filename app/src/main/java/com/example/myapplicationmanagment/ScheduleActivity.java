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

import com.example.myapplicationmanagment.DBHelper;
import com.example.myapplicationmanagment.MainActivity;
import com.example.myapplicationmanagment.R;
import com.example.myapplicationmanagment.ScheduleAdapter;
import com.example.myapplicationmanagment.ScheduleItem;

import java.util.ArrayList;
import java.util.List;

public class ScheduleActivity extends AppCompatActivity {

    private DBHelper dbHelper;
    private SQLiteDatabase db;
    private List<ScheduleItem> scheduleItemList;
    private ScheduleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();

        Button backToMainMenuButton = findViewById(R.id.backToMainMenuButton);
        backToMainMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScheduleActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Button addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddDialog();
            }
        });

        // Ініціалізація списку та адаптера
        scheduleItemList = new ArrayList<>();
        adapter = new ScheduleAdapter(scheduleItemList);

        // Ініціалізація RecyclerView та встановлення адаптера
        RecyclerView recyclerView = findViewById(R.id.recyclerViewSchedule);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Завантаження даних з бази даних
        loadScheduleFromDatabase();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (db != null) {
            db.close();
        }
    }

    private void showAddDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.add_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText editTextSubject = dialogView.findViewById(R.id.editTextSubject);
        final EditText editTextDate = dialogView.findViewById(R.id.editTextDate);
        final EditText editTextType = dialogView.findViewById(R.id.editTextType);

        dialogBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String subject = editTextSubject.getText().toString();
                String date = editTextDate.getText().toString();
                String type = editTextType.getText().toString();

                if (db != null) {
                    ContentValues values = new ContentValues();
                    values.put("name", subject);
                    values.put("date_time", date);
                    values.put("type", type);
                    long newRowId = db.insert("schedule", null, values);
                    if (newRowId != -1) {
                        // Додавання нового елемента до списку та оновлення адаптера
                        ScheduleItem newItem = new ScheduleItem(subject, date, type);
                        scheduleItemList.add(newItem);
                        adapter.notifyDataSetChanged();

                        Toast.makeText(ScheduleActivity.this, "New item added", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ScheduleActivity.this, "Failed to add item", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ScheduleActivity.this, "Failed to access database", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialogBuilder.setNegativeButton("Cancel", null);

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    private void loadScheduleFromDatabase() {
        // Очистити список перед завантаженням нових даних
        scheduleItemList.clear();

        // Запит до бази даних для отримання розкладу занять
        Cursor cursor = db.query("schedule", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            String subject = cursor.getString(cursor.getColumnIndex("name"));
            String date = cursor.getString(cursor.getColumnIndex("date_time"));
            String type = cursor.getString(cursor.getColumnIndex("type"));
            // Додавання отриманих даних до списку
            scheduleItemList.add(new ScheduleItem(subject, date, type));
        }
        cursor.close();

        // Оновлення адаптера після завантаження даних
        adapter.notifyDataSetChanged();
    }
}
