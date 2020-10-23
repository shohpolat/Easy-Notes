package com.example.roomjava2;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "notes_table")
public class Note {

    @PrimaryKey(autoGenerate = true)
    private int ID;


    private int priority;
    private String title;
    private String description;

    public Note(int priority, String title, String description) {
        this.priority = priority;
        this.title = title;
        this.description = description;
    }

    public int getPriority() {
        return priority;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }


}
