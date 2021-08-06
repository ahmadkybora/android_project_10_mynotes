package com.example;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.models.Note;

import java.util.ArrayList;

public class SQLiteHelper extends SQLiteOpenHelper {

    private final static int DATABASE_VERSION = 1;
    private final static String DATABASE_NAME = "mySql";
    private static SQLiteHelper sqLiteHelper;

    public static SQLiteHelper builder(Context context) {
        if (sqLiteHelper == null) {
            sqLiteHelper = new SQLiteHelper(context);
        }
        return sqLiteHelper;
    }

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS note (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE, " +
                "noteId INTEGER, " +
                "title TEXT, " +
                "note TEXT, " +
                "date TEXT, " +
                "time TEXT)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS note");
            onCreate(sqLiteDatabase);
        }
    }

    public void insertNote(Note note) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("noteId", note.getNoteId());
        contentValues.put("title", note.getTitle());
        contentValues.put("note", note.getNote());
        contentValues.put("date", note.getDate());
        contentValues.put("time", note.getTime());

        database.insert("note :", null, contentValues);
        database.close();
    }

    public Note getNote(int noteId) {
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM note WHERE noteId = '" + noteId + "'", null);
        if (cursor == null) {
            return null;
        }

        cursor.moveToFirst();
        Note note = new Note();
        note.setNoteId(cursor.getInt(cursor.getColumnIndex("noteId")));
        note.setTitle(cursor.getString(cursor.getColumnIndex("title")));
        note.setNote(cursor.getString(cursor.getColumnIndex("note")));
        note.setDate(cursor.getString(cursor.getColumnIndex("date")));
        note.setTime(cursor.getString(cursor.getColumnIndex("time")));
        cursor.close();
        database.close();

        return note;
    }

    public ArrayList<Note> getNotes() {
        ArrayList<Note> notes = new ArrayList<>();
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM note", null);
        if (cursor == null) {
            return notes;
        }

        while (cursor.moveToNext()) {
            Note note = new Note();
            note.setNoteId(cursor.getInt(cursor.getColumnIndex("noteId")));
            note.setTitle(cursor.getString(cursor.getColumnIndex("title")));
            note.setNote(cursor.getString(cursor.getColumnIndex("note")));
            note.setDate(cursor.getString(cursor.getColumnIndex("date")));
            note.setTime(cursor.getString(cursor.getColumnIndex("time")));
            notes.add(note);
        }
        cursor.close();
        database.close();

        return notes;
    }

    public void updateNote(Note note) {
        SQLiteDatabase database = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("noteId", note.getNoteId());
        contentValues.put("title", note.getTitle());
        contentValues.put("note", note.getNote());
        contentValues.put("date", note.getDate());
        contentValues.put("time", note.getTime());

        database.update("note", contentValues, "noteId=?", new String[]{
                String.valueOf(note.getNoteId())
        });
        database.close();
    }

    public void deleteNote(Note note) {
        SQLiteDatabase database = getWritableDatabase();
        database.delete("note", "noteId=?", new String[]{
                String.valueOf(note.getNoteId())
        });
        database.close();
    }

    public void deleteNote(int noteId) {
        SQLiteDatabase database = getWritableDatabase();
        database.delete("note", "noteId=?", new String[]{
                String.valueOf(noteId)
        });
        database.close();
    }
}

