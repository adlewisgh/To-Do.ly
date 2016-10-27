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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private ListView notesList;
    //String[] notes = new String[]{"note 1", "note 2", "note 3"};
    private TodoArrayAdapter todoArrayAdapter;
    private ArrayList<TodoConstructor> toDoArray;
    private SharedPreferences toDoPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setContentView(R.layout.activity_main);
        toDoPrefs = getPreferences(Context.MODE_PRIVATE);
        setupNotes();

        //hook up our UI elements to our Java code
        //this was our simple adapter using a simple string array as data and a simple resource
        notesList = (ListView) findViewById(R.id.list_view);


        //This is our complex, custom adapter using Notes class as data and a complex resource.
        todoArrayAdapter = new TodoArrayAdapter(this, R.layout.todo_list_items, toDoArray);
        notesList.setAdapter(todoArrayAdapter);
        //CoordinatorLayout activityMain = (CoordinatorLayout) findViewById(R.id.content_main);
        //View contentview = getLayoutInflater().inflate(R.layout.content_main,activityMain,false);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
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

                TodoConstructor todoConstructor = toDoArray.get(position);

                Intent intent = new Intent(MainActivity.this, TodoDetailActivity.class);

                intent.putExtra("noteTitle", todoConstructor.getNoteTitle());
                intent.putExtra("completionDueTime", todoConstructor.getDateModified());
                intent.putExtra("category", todoConstructor.getCategory());
                intent.putExtra("DueDate", todoConstructor.getDueDate());
                intent.putExtra("DueTime", todoConstructor.getDueTime());

                intent.putExtra("Index", position);

                //startActivity(intent);
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
                        TodoConstructor note = toDoArray.get(position);
                        deleteFile("##" + note.getNoteTitle());
                        toDoArray.remove(position);
                        todoArrayAdapter.updateAdapter(toDoArray);
                    }
                });
                alertBuilder.create().show();
                return true;
            }
        });
    }

    //create adapter and wire it to the listView
    //notesList.setAdapter(new ArrayAdapter<>(this,R.layout.notes_textview_list_item,notes));
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            int index = data.getIntExtra("Index", -1);
            TodoConstructor todoConstructor = new TodoConstructor(false, data.getStringExtra("noteTitle"),data.getStringExtra("DueDate"), new Date(),
                    data.getStringExtra("category"), data.getStringExtra("DueTime"));

            if (index == -1) {
                toDoArray.add(todoConstructor);
                writeFile(todoConstructor);
            } else {
                TodoConstructor oldNote = toDoArray.get(index);
                toDoArray.set(index, todoConstructor);
                if (!oldNote.getNoteTitle().equals(todoConstructor.getNoteTitle())) {
                    File oldFile = new File(this.getFilesDir(), "##" + oldNote.getNoteTitle());
                    File newFile = new File(this.getFilesDir(), "##" + todoConstructor.getNoteTitle());
                    oldFile.renameTo(newFile);
                }
            }

            Collections.sort(toDoArray);
            todoArrayAdapter.updateAdapter(toDoArray);
        }
    }

    private void setupNotes() {
        toDoArray = new ArrayList<>();
//        if (toDoPrefs.getBoolean("firstRun", true)) {
//            SharedPreferences.Editor editor = toDoPrefs.edit();
//            editor.putBoolean("firstRun", false);
//            editor.apply();
//
//            TodoConstructor note1 = new TodoConstructor(true, "Task", new Date().toString(), new Date(), "Category1");
//            toDoArray.add(note1);
//            toDoArray.add(new TodoConstructor(true, "Task 2", new Date().toString(), new Date(), "Category2"));
//            toDoArray.add(new TodoConstructor(true, "Task 3", new Date().toString(), new Date(), "Category3"));
//
//            for (TodoConstructor todoconstructor : toDoArray) {
//                writeFile(todoconstructor);
//            }
//        } else {
//            File[] filesDir = this.getFilesDir().listFiles();
//            for (File file : filesDir) {
//                FileInputStream inputStream = null;
//                String title = file.getName();
//                if (!title.startsWith("##")) {
//                    continue;
//                } else {
//                    title = title.substring(2, title.length());
//                }
//
//                Date date = new Date(file.lastModified());
//                String DueDate = "";
//                String text = "";
//                try {
//                    inputStream = openFileInput(title);
//                    byte[] input = new byte[inputStream.available()];
//                    while (inputStream.read(input) != -1) {
//                    }
//                    text += new String(input);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                } finally {
//
//                    try {
//                        inputStream.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    } catch (NullPointerException e) {
//                        e.printStackTrace();
//                    }
//                }
//                    toDoArray.add(new TodoConstructor(false, title, DueDate, date, text));
//                }
//            }
//        }
    }

    private void writeFile(TodoConstructor toDoItems) {
        FileOutputStream outputStream = null;
        try {
            outputStream = openFileOutput("##" + toDoItems.getNoteTitle(), Context.MODE_PRIVATE);
            outputStream.write(toDoItems.getCategory().getBytes());
            //need to add date modified, category, and any other items.
            outputStream.flush();
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
}
