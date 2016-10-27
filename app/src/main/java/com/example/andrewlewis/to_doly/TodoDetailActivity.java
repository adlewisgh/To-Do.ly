package com.example.andrewlewis.to_doly;

import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

/**
 * Created by andrewlewis on 10/25/16.
 */

public class TodoDetailActivity extends AppCompatActivity {
    private EditText noteTitle;
    private EditText completionDueTime;
    private EditText category;
    private TextView noteDueDate;
    private TextView noteDueTime;
    private Button save;
    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.todo_item_detail);

        noteTitle = (EditText) findViewById(R.id.noteTitle);
        category = (EditText) findViewById(R.id.category);
        noteDueDate =(TextView)findViewById(R.id.due_date);
        noteDueTime = (TextView)findViewById(R.id.due_time);
        save = (Button) findViewById(R.id.save);

        Intent intent = getIntent();

        noteTitle.setText(intent.getStringExtra("noteTitle"));
        category.setText(intent.getStringExtra("category"));
        noteDueDate.setText(intent.getStringExtra("DueDate"));
        noteDueTime.setText(intent.getStringExtra("DueTime"));

        index = intent.getIntExtra("Index", -1);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                intent.putExtra("noteTitle", noteTitle.getText().toString());
                intent.putExtra("DueTime", noteDueTime.getText().toString());
                intent.putExtra("category", category.getText().toString());
                intent.putExtra("DueDate", noteDueDate.getText().toString());
                intent.putExtra("Index", index);
                setResult(RESULT_OK, intent);

                finish();
            }
        });
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
    }

    //setting up local notification

    public void showNotification(View view) {
        PendingIntent pi = PendingIntent.getActivity(this, 0, new Intent(this, TodoDetailActivity.class), 0);
        Resources r = getResources();
        Notification notification = new NotificationCompat.Builder(this)
                .setTicker(r.getString(R.string.notification_title))
                .setSmallIcon(android.R.drawable.ic_menu_report_image)
                .setContentTitle(r.getString(R.string.notification_title))
                .setContentText(r.getString(R.string.notification_text))
                .setContentIntent(pi)
                .setAutoCancel(true)
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
    }
}

