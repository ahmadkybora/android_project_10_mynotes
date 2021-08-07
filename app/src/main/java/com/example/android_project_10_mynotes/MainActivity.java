package com.example.android_project_10_mynotes;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.SQLiteHelper;
import com.example.models.Note;
import com.example.views.adapters.NoteAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NoteAdapter.NoteListener, View.OnClickListener {

    private TextView txt_no_items;
    private RecyclerView rcl_note;
    private ProgressBar prg_loading;
    private FloatingActionButton fab_insert_note;
    private Handler handler;
    private NoteAdapter noteAdapter;
    private ViewGroup btn_edit, btn_delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        handler = new Handler();
        initViews();
        //addNote();
        loadNote();
        /*ArrayList<Note> notes = SQLiteHelper.builder(this).getNotes();
    new Runnv    if (notes == null) {
            txt_no_items.setVisibility(View.VISIBLE);
            rcl_note.setVisibility(View.GONE);
        } else {
            setupRecyclerView(notes);
        }*/
    }

    public void loadNote() {
        showLoading();
        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<Note> notes = SQLiteHelper.builder(MainActivity.this).getNotes();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        hideLoading();
                        if (notes == null) {
                            noItems();
                        } else {
                            setupRecyclerView(notes);
                        }
                    }
                });
            }
        }).start();
    }

    public void addNote() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 1000; i++) {
                    Note note = new Note();
                    note.setNoteId(i);
                    note.setTitle("title : " + i);
                    note.setNote("note : " + i);
                    note.setDate("date : " + i);
                    note.setTime("time : " + i);
                    SQLiteHelper.builder(MainActivity.this).insertNote(note);
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        hideLoading();
                    }
                });
            }
        }).start();
    }

    public void setupRecyclerView(ArrayList<Note> notes) {
        rcl_note.setLayoutManager(new LinearLayoutManager(this));
        noteAdapter = new NoteAdapter(this, notes);
        noteAdapter.setNoteListener(this);
        rcl_note.setAdapter(noteAdapter);
    }

    public void initViews() {
        txt_no_items = findViewById(R.id.txt_no_items);
        fab_insert_note = findViewById(R.id.fab_insert_note);
        rcl_note = findViewById(R.id.rcl_note);
        prg_loading = findViewById(R.id.prg_loading);
        fab_insert_note.setOnClickListener(this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_insert_note:
                insertOrUpdateNoteDialog(0, null);
                break;
        }
    }

    private void showLoading() {
        prg_loading.setVisibility(View.VISIBLE);
        txt_no_items.setVisibility(View.GONE);
        rcl_note.setVisibility(View.GONE);
    }

    private void hideLoading() {
        prg_loading.setVisibility(View.GONE);
        txt_no_items.setVisibility(View.GONE);
        rcl_note.setVisibility(View.VISIBLE);
    }

    private void noItems() {
        prg_loading.setVisibility(View.GONE);
        txt_no_items.setVisibility(View.VISIBLE);
        rcl_note.setVisibility(View.GONE);
    }

    @Override
    public void onClickNote(int position, Note note) {
        showBottomSheedDialog(position, note);
    }

    public void showBottomSheedDialog(int position, Note note) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.view_bottom_sheet_dialog, null);
        btn_edit = view.findViewById(R.id.btn_edit);
        btn_delete = view.findViewById(R.id.btn_delete);

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
                needShowAlertDialog(position, note);
            }
        });

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
                insertOrUpdateNoteDialog(position, note);
            }
        });
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.setCancelable(true);
        bottomSheetDialog.show();
    }

    public void needShowAlertDialog(int position, Note note) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Note");
        alertDialog.setMessage("Are you sure");

        alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                if (noteAdapter != null) {
                    noteAdapter.deleteNote(position, note);
                }
            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        alertDialog.show();
    }

    private void insertOrUpdateNoteDialog(final int position, final Note note) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("ok");
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.view_insert_update, null);

        Button btn_confirm = view.findViewById(R.id.btn_confirm);
        Button btn_cancel = view.findViewById(R.id.btn_cancel);
        EditText edt_note = view.findViewById(R.id.edt_note);
        EditText edt_title = view.findViewById(R.id.edt_title);
        EditText edt_note_id = view.findViewById(R.id.edt_note_id);

        if (note != null) {
            edt_note.setText(note.getNote());
            edt_title.setText(note.getTitle());
            edt_note_id.setVisibility(View.GONE);
        } else {
            edt_note.setVisibility(View.VISIBLE);
        }
        final AlertDialog dialog = alertDialog.create();

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
                if (note != null) {
                    note.setTitle(edt_title.getText().toString());
                    note.setNote(edt_title.getText().toString());
                    noteAdapter.updateNote(position, note);
                    return;
                }
                Note _note = new Note();
                _note.setTitle(edt_title.getText().toString());
                _note.setNote(edt_title.getText().toString());
                _note.setNoteId(Integer.parseInt(edt_note_id.getText().toString().trim().replace(" ", "")));
                _note.setTime("12 : 56");
                _note.setDate("Apr 23");
                _note.setNoteId(100);

                noteAdapter.insertNote(_note);
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.setView(view);
        dialog.show();
    }
}