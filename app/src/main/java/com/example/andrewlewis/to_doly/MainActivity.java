package com.example.andrewlewis.to_doly;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.widget.AdapterView;
import android.widget.ListView;
import com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView notesList;
    //String[] notes = new String[]{"note 1", "note 2", "note 3"};
    private TodoArrayAdapter todoArrayAdapter;
    private ArrayList<TodoConstructor> toDoArray;
    private SharedPreferences toDoPrefs;
    private int catNumber;
    String filename = "TodoItemsFile";
    Gson gson = new Gson();
    List<TodoConstructor> notesLists = new ArrayList<>();
    // Stores categories + notes to pass to our custom adapter
    //Custom adapter to display categories and notes to list view

    private List <Category> categories = new ArrayList<>();

    private ArrayList <Object>allItems = new ArrayList<>();
    private CategoryAdapter categoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setContentView(R.layout.activity_main);
        toDoPrefs = getPreferences(Context.MODE_PRIVATE);
        gson = new Gson();
        setupNotes();

        //hook up our UI elements to our Java code
        //this was our simple adapter using a simple string array as data and a simple resource

        //MIGHT NEED TO CHANGE THIS!!
        notesList = (ListView) findViewById(R.id.list_view);
        updateAllItems();

        categoryAdapter = new CategoryAdapter(this,allItems);
        notesList.setAdapter(categoryAdapter);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, TodoDetailActivity.class);

                intent.putExtra("noteTitle", "");
                intent.putExtra("DueTime", "");
                intent.putExtra("category", "");
                intent.putExtra("DueDate", "");

                startActivityForResult(intent, 1);

            }
        });

        notesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                allItems.get(position);
                TodoConstructor note = (TodoConstructor) allItems.get(position);

                Intent intent = new Intent(MainActivity.this, TodoDetailActivity.class);

                intent.putExtra("noteTitle", note.getNoteTitle());
                intent.putExtra("completionDueTime", note.getDateModified());
                intent.putExtra("category", note.getCategory());
                intent.putExtra("DueDate", note.getDueDate());
                intent.putExtra("DueTime", note.getDueTime());

                intent.putExtra("Index", position);

                startActivityForResult(intent, 1);

            }
        });
        notesList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(MainActivity.this);
                alertBuilder.setTitle("Delete");
                alertBuilder.setMessage("You sure?");
                alertBuilder.setNegativeButton("Cancel", null);
                alertBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        TodoConstructor note = (TodoConstructor) allItems.get(position);
                        categories.get(catNumber).notes.remove(note);
                        allItems.remove(position);
                        deleteFile(note.getNoteTitle());
                        writeTodos();
                        categoryAdapter.notifyDataSetChanged();
                        writeTodos();
                    }
                });
                alertBuilder.create().show();
                return true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            int index = data.getIntExtra("Index", -1);

            TodoConstructor note = new TodoConstructor(false, data.getStringExtra("noteTitle"),data.getStringExtra("DueDate"), new Date(),
                    data.getStringExtra("category"), data.getStringExtra("DueTime"));


                switch (data.getStringExtra("category")) {
                    case "home":
                        catNumber = 0;
                        break;
                    case "work":
                        catNumber = 1;
                        break;
                    case "personal":
                        catNumber = 2;
                        break;


            }
            categories.get(catNumber).notes.add(note);

            writeTodos();
            updateAllItems();
            categoryAdapter.notifyDataSetChanged();

            writeTodos();
        }
    }

    private void setupNotes() {
        toDoArray = new ArrayList<>();
        File fileDir = this.getFilesDir();
        File todoFile = new File(filename + File.separator + filename);
        if (todoFile.exists()) {
            readTodos(todoFile);
        }else{
            categories.add(new Category("HOME", new ArrayList<TodoConstructor>()));
            categories.add(new Category("WORK", new ArrayList<TodoConstructor>()));
            categories.add(new Category("MISC", new ArrayList<TodoConstructor>()));

            for(int i = 0; i < categories.size(); i++) {
                categories.get(i).notes.add(new TodoConstructor(false, "Task 1", "Date", new Date(), "Category 1", "Time"));
                categories.get(i).notes.add(new TodoConstructor(false, "Task 2", "Date", new Date(), "Category 2", "Time"));
                categories.get(i).notes.add(new TodoConstructor(false, "Task 3", "Date", new Date(), "Category 3", "Time"));
            }
            writeTodos();
        }
    }

    private void readTodos(File todoFile) {
        FileInputStream inputStream = null;
        String todosText = "";
        try {
            inputStream = openFileInput(todoFile.getName());
            byte[] input = new byte[inputStream.available()];
            while (inputStream.read(input) != -1) {}
            todosText = new String(input);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Determine type of our collection
            Type collectionType = new TypeToken<List<Category>>(){}.getType();
            // Pull out our categories in a list
            List<Category> categoryList = gson.fromJson(todosText, collectionType);
            // Create a LinkedList that we can edit from our categories list and save it
            // to our global categories
            categories = new LinkedList(categoryList);
        }
    }
    private void writeTodos() {
        FileOutputStream outputStream = null;
        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            String json = gson.toJson(categories);
            byte[] bytes = json.getBytes();
            outputStream.write(bytes);
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.close();
            } catch (Exception ignored) {}
        }
    }
    private void writeFile(TodoConstructor toDoItems) {
        FileOutputStream outputStream = null;
        try {
            outputStream = openFileOutput("##" + toDoItems.getNoteTitle(), Context.MODE_PRIVATE);
            outputStream.write(toDoItems.getCategory().getBytes());
            //need to add date modified, category, and any other items.
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.close();
            } catch (IOException ioe) {
            } catch (NullPointerException npe) {
            } catch (Exception e) {
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    private void updateAllItems() {
        allItems.clear();
        for(int i = 0; i < categories.size(); i++){
            allItems.add(categories.get(i).getName());
            for(int j = 0; j < categories.get(i).notes.size(); j++){
                allItems.add(categories.get(i).notes.get(j));
            }
        }
    }
}
