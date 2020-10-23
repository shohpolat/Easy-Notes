package com.example.roomjava2;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Note.class}, version = 1)
public abstract class NoteDatabase extends RoomDatabase {

    public abstract NoteDao noteDao();

    private static NoteDatabase instance;

    public static synchronized NoteDatabase getInstance(Context context)
    {
        if (instance==null){
            instance = Room.databaseBuilder(context.getApplicationContext(),NoteDatabase.class,"Room database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }


    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {

            new PopulateAsycTask(instance).execute();
            super.onCreate(db);


        }
    };

    private static class PopulateAsycTask extends AsyncTask<Void,Void,Void>{

        private NoteDao noteDao;

        PopulateAsycTask(NoteDatabase noteDatabase){
            noteDao = noteDatabase.noteDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            noteDao.insert(new Note(1,"Example Title: ","example description"));


            return null;
        }
    }

}
