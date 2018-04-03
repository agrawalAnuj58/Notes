package com.example.anuj.notes;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    static SharedPreferences sharedPreferences;
    static ArrayList<String> mainNote = new ArrayList<>();
    static ArrayList<String> noteDetail = new ArrayList<>();
    static ArrayAdapter arrayAdapter;
    EditText noteDetailView;
    ListView notesList;
    static Gson gson;
    Toolbar myToolbar;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = new MenuInflater(this);
        menuInflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.addNote){
            Intent intent = new Intent(getApplicationContext(), noteDetailActivity.class);
            intent.putExtra("position", -1);
            startActivity(intent);
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myToolbar = findViewById(R.id.mainToolbar);
        setSupportActionBar(myToolbar);

        mainNote.clear();
        noteDetail.clear();

        sharedPreferences = this.getSharedPreferences("com.example.anuj.notes", MODE_PRIVATE);
        String allNote = sharedPreferences.getString("allNote", null);

        gson = new Gson();
        Type type = new TypeToken<ArrayList<String>>(){}.getType();

        noteDetailView = findViewById(R.id.noteDetailView);
        notesList = findViewById(R.id.notesList);

        if (allNote == null){
            mainNote.add("Example Note");
            noteDetail.add("Example Note Here...");

            sharedPreferences.edit().putString("allNote", gson.toJson(mainNote)).apply();
            sharedPreferences.edit().putString("noteDetail", gson.toJson(noteDetail)).apply();

            arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, mainNote);
            notesList.setAdapter(arrayAdapter);
        }
        else {

            mainNote.clear();
            noteDetail.clear();

            mainNote = gson.fromJson(sharedPreferences.getString("allNote", null), type);
            noteDetail = gson.fromJson(sharedPreferences.getString("noteDetail", null), type);

            arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, mainNote);
            notesList.setAdapter(arrayAdapter);
        }

        notesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getApplicationContext(), noteDetailActivity.class);
                intent.putExtra("position", position);

                startActivity(intent);

            }
        });

        notesList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                createDialog(view, position);
                return true;
            }
        });

    }

    public void createDialog(View view, final int position){
        AlertDialog.Builder abd = new AlertDialog.Builder(this);
        abd.setIcon(android.R.drawable.ic_delete)
                .setTitle("Delete this note!")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivity.noteDetail.remove(position);
                        MainActivity.mainNote.remove(position);
                        MainActivity.sharedPreferences.edit().putString("allNote", MainActivity.gson.toJson(MainActivity.mainNote)).apply();
                        MainActivity.sharedPreferences.edit().putString("noteDetail", MainActivity.gson.toJson(MainActivity.noteDetail)).apply();
                        MainActivity.arrayAdapter.notifyDataSetChanged();
                        Toast.makeText(MainActivity.this, "Note Deleted!!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No",null);
        abd.show();
    }
}
