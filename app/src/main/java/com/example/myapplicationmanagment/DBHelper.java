package com.example.myapplicationmanagment;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    // Оголошення констант для таблиць і стовпців
    private static final String TABLE_SCHEDULE = "schedule";
    private static final String TABLE_EXAMS = "exams";
    private static final String TABLE_GRADES = "grades";
    private static final String TABLE_PROGRESS = "progress";

    // Оголошення констант для стовпців
    // Для TABLE_SCHEDULE
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_TYPE = "type";
    private static final String KEY_DATE_TIME = "date_time";
    private static final String KEY_LOCATION = "location";
    private static final String KEY_NOTES = "notes";
    // Додайте аналогічні константи для інших таблиць

    // Оголошення констант для стовпців інших таблиць
    // Для TABLE_EXAMS
    private static final String KEY_EXAM_ID = "exam_id";
    private static final String KEY_SUBJECT = "subject";
    private static final String KEY_EXAM_DATE_TIME = "exam_date_time";
    private static final String KEY_EXAM_LOCATION = "exam_location";
    private static final String KEY_EXAM_NOTES = "exam_notes";
    // Для TABLE_GRADES
    private static final String KEY_GRADE_ID = "grade_id";
    private static final String KEY_GRADE = "grade";
    private static final String KEY_GRADE_DATE_TIME = "grade_date_time";
    private static final String KEY_GRADE_NOTES = "grade_notes";
    // Для TABLE_PROGRESS
    private static final String KEY_PROGRESS_ID = "progress_id";
    private static final String KEY_SUBJECT_PROGRESS = "subject_progress";
    private static final String KEY_OVERALL_PROGRESS = "overall_progress";
    private static final String KEY_PROGRESS_NOTES = "progress_notes";

    private static final String DATABASE_NAME = "StudyAppDB";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Створення таблиць
        createScheduleTable(db);
        createExamsTable(db);
        createGradesTable(db);
        createProgressTable(db);
    }

    private void createScheduleTable(SQLiteDatabase db) {
        String createScheduleTableQuery = "CREATE TABLE " + TABLE_SCHEDULE + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
                + KEY_TYPE + " TEXT,"
                + KEY_DATE_TIME + " TEXT,"
                + KEY_LOCATION + " TEXT,"
                + KEY_NOTES + " TEXT" + ")";
        db.execSQL(createScheduleTableQuery);
    }

    // Метод для додавання нового заняття в таблицю Розклад
    public void addScheduleItem(String name, String type, String dateTime, String location, String notes) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name);
        values.put(KEY_TYPE, type);
        values.put(KEY_DATE_TIME, dateTime);
        values.put(KEY_LOCATION, location);
        values.put(KEY_NOTES, notes);
        db.insert(TABLE_SCHEDULE, null, values);
        db.close();
    }

    private void createGradesTable(SQLiteDatabase db) {
        String createGradesTable = "CREATE TABLE " + TABLE_GRADES + "("
                + KEY_GRADE_ID + " INTEGER PRIMARY KEY,"
                + KEY_SUBJECT + " TEXT,"
                + KEY_GRADE + " TEXT,"
                + KEY_GRADE_DATE_TIME + " TEXT,"
                + KEY_GRADE_NOTES + " TEXT" + ")";
        db.execSQL(createGradesTable);
    }

    private void createProgressTable(SQLiteDatabase db) {
        String createProgressTable = "CREATE TABLE " + TABLE_PROGRESS + "("
                + KEY_PROGRESS_ID + " INTEGER PRIMARY KEY,"
                + KEY_SUBJECT_PROGRESS + " TEXT,"
                + KEY_OVERALL_PROGRESS + " INTEGER,"
                + KEY_PROGRESS_NOTES + " TEXT" + ")";
        db.execSQL(createProgressTable);
    }

    private void createExamsTable(SQLiteDatabase db) {
        String createExamsTable = "CREATE TABLE " + TABLE_EXAMS + "("
                + KEY_EXAM_ID + " INTEGER PRIMARY KEY,"
                + KEY_SUBJECT + " TEXT,"
                + KEY_EXAM_DATE_TIME + " TEXT,"
                + KEY_EXAM_LOCATION + " TEXT,"
                + KEY_EXAM_NOTES + " TEXT" + ")";
        db.execSQL(createExamsTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCHEDULE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXAMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GRADES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROGRESS);
        onCreate(db);
    }
}
