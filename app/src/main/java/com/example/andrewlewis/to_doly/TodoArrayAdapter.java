package com.example.andrewlewis.to_doly;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by andrewlewis on 10/24/16.
 */

public class TodoArrayAdapter extends ArrayAdapter<TodoConstructor> {
    private int resource;
    private ArrayList<TodoConstructor> toDoItems;
    private LayoutInflater inflater;
    private SimpleDateFormat formatter;


    public TodoArrayAdapter(Context context, int resource, ArrayList<TodoConstructor> objects)
    {
        super(context, resource, objects);
        this.resource = resource;
        this.toDoItems = objects;

        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        formatter = new SimpleDateFormat ("MM/dd/yyyy");
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View toDoItemsRow = inflater.inflate(resource, parent, false);

        TextView itemTaskName = (TextView)toDoItemsRow.findViewById(R.id.noteTitle);
        TextView itemDueDate = (TextView)toDoItemsRow.findViewById(R.id.dueDate);
        TextView  itemDueTime= (TextView)toDoItemsRow.findViewById(R.id.dueTime);
        TextView itemCategory = (TextView)toDoItemsRow.findViewById(R.id.category);


        TodoConstructor todoConstructor = toDoItems.get(position);

        itemTaskName.setText(todoConstructor.getNoteTitle());
        itemDueDate.setText(todoConstructor.getDueDate());
        itemDueTime.setText(todoConstructor.getDueTime());
        itemCategory.setText(todoConstructor.getCategory());

        return toDoItemsRow;
    }

    public void updateAdapter(ArrayList<TodoConstructor> todoitems)
    {
        this.toDoItems = todoitems;
        super.notifyDataSetChanged();
    }

}
