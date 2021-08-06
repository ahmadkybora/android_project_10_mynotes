package com.example.models;

public class Note {
    private int noteId;
    private String title;
    private String note;
    private String date;
    private String time;

    public Note(int noteId, String title, String note, String date, String time) {
        this.noteId = noteId;
        this.title = title;
        this.note = note;
        this.date = date;
        this.time = time;
    }

    public Note() {
    }

    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
