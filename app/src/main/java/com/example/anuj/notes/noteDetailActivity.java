package com.example.anuj.notes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

public class noteDetailActivity extends AppCompatActivity {

    EditText noteDetailView;
    EditText noteTitleView;
    Intent intent;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);

        intent = getIntent();
        position = intent.getIntExtra("position",0);
        noteDetailView = findViewById(R.id.noteDetailView);
        noteTitleView = findViewById(R.id.noteTitleView);

        if (position != -1) {

            String title = MainActivity.mainNote.get(position);
            String noteDetail = MainActivity.noteDetail.get(position);

            noteTitleView.setText(title);
            noteDetailView.setText(noteDetail);

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        String newNote = noteDetailView.getText().toString();
        String title = noteTitleView.getText().toString();

        if (title.length()>20)
            title = newNote.substring(0,20) + "...";

        if (position != -1) {

            if (title.length() == 0)
                title = "Note " + (position+1);
            MainActivity.noteDetail.set(position, newNote);
            MainActivity.mainNote.set(position, title);
        }
        else{

            if (title.length() == 0)
                title = "Note " + (MainActivity.mainNote.size()+1);
            MainActivity.noteDetail.add(newNote);
            MainActivity.mainNote.add(title);
            Toast.makeText(this, "Note Created!!", Toast.LENGTH_SHORT).show();
        }

        MainActivity.sharedPreferences.edit().putString("allNote", MainActivity.gson.toJson(MainActivity.mainNote)).apply();
        MainActivity.sharedPreferences.edit().putString("noteDetail", MainActivity.gson.toJson(MainActivity.noteDetail)).apply();
        MainActivity.arrayAdapter.notifyDataSetChanged();
    }
}
