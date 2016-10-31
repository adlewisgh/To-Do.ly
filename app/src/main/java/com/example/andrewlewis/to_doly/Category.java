package com.example.andrewlewis.to_doly;

/**
 * Created by andrewlewis on 10/28/16.
 */

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Category {
    @SerializedName("name")
    private String name;

    @SerializedName("notes")
    public ArrayList<TodoConstructor> notes;

    public Category(String name, ArrayList<TodoConstructor> notes) {
        this.name = name;
        this.notes = notes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}