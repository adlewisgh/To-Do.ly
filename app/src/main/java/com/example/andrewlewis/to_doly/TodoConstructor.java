package com.example.andrewlewis.to_doly;

import java.util.Date;

/**
 * Created by andrewlewis on 10/24/16.
 */

public class TodoConstructor implements Comparable<TodoConstructor>
{
    private Boolean checkbox;
    private String noteTitle;
    private String DueDate;
    private Date dateModified;
    private String category;
    private String dueTime;

    public TodoConstructor(Boolean checkbox, String noteTitle, String DueDate, Date dateModified, String category, String dueTime){
        this.checkbox = checkbox;
        this.noteTitle = noteTitle;
        this.DueDate = DueDate;
        this.dateModified = dateModified;
        this.category = category;
        this.dueTime = dueTime;
    }

    public Boolean getCheckbox() {

        return checkbox;
    }

    public void setCheckbox(Boolean checkbox) {

        this.checkbox = checkbox;
    }

    public String getNoteTitle() {

        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {

        this.noteTitle = noteTitle;
    }

    public String getDueDate() {

        return DueDate;
    }

    public void setDueDate(String DueDate) {

        this.DueDate = DueDate;
    }

    public Date getDateModified() {

        return dateModified;
    }

    public void setDateModified(Date dateModified) {

        this.dateModified = dateModified;
    }

    public String getCategory() {

        return category;
    }

    public void setCategory(String category) {

        this.category = category;
    }

    public String getDueTime() {
        return dueTime;
    }

    public void setDueTime(String dueTime) {
        this.dueTime = dueTime;
    }

    @Override
    public int compareTo(TodoConstructor another) {
        return another.getDueDate().compareTo(getDueDate());
    }
}
