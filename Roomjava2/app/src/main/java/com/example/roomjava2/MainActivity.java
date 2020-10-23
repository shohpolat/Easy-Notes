package com.example.roomjava2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int ADD_REQUEST = 1;
    public static final int EDIT_REQUEST = 2;

    private NoteViewModel noteviewModel;
    private ImageButton imageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageButton = findViewById(R.id.floatingActionButton);




        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final NoteAdapter adapter = new NoteAdapter();
        recyclerView.setAdapter(adapter);

        setTitle("All Notes");


        noteviewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(NoteViewModel.class);

        noteviewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                adapter.submitList(notes);
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, Add_Edit_Note_Activity.class);
                startActivityForResult(intent, ADD_REQUEST);

            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                noteviewModel.delete(adapter.getNote(viewHolder.getAdapterPosition()));

            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new NoteAdapter.OnItemClickListener() {
            @Override
            public void OnItemCLick(Note note) {

                Intent intent = new Intent(MainActivity.this, Add_Edit_Note_Activity.class);
                intent.putExtra(Add_Edit_Note_Activity.PRIORITY_EXTRA, note.getPriority());
                intent.putExtra(Add_Edit_Note_Activity.TITLE_EXTRA, note.getTitle());
                intent.putExtra(Add_Edit_Note_Activity.DESCRIPTION_EXTRA, note.getDescription());
                intent.putExtra(Add_Edit_Note_Activity.ID_EXTRA, note.getID());

                startActivityForResult(intent, EDIT_REQUEST);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_REQUEST && resultCode == RESULT_OK) {
            int priority = data.getIntExtra(Add_Edit_Note_Activity.PRIORITY_EXTRA, 0);
            String title = data.getStringExtra(Add_Edit_Note_Activity.TITLE_EXTRA);
            String description = data.getStringExtra(Add_Edit_Note_Activity.DESCRIPTION_EXTRA);
            Note note = new Note(priority, title, description);
            noteviewModel.insert(note);

            Toast.makeText(this, "saved", Toast.LENGTH_SHORT).show();
        } else if (requestCode == EDIT_REQUEST && resultCode == RESULT_OK) {
            int priority = data.getIntExtra(Add_Edit_Note_Activity.PRIORITY_EXTRA, 0);
            String title = data.getStringExtra(Add_Edit_Note_Activity.TITLE_EXTRA);
            String description = data.getStringExtra(Add_Edit_Note_Activity.DESCRIPTION_EXTRA);
            int id = data.getIntExtra(Add_Edit_Note_Activity.ID_EXTRA, 0);
            Note note = new Note(priority, title, description);
            note.setID(id);
            noteviewModel.update(note);
            Toast.makeText(this, "edited", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "not saved", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.delete_all, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.delete_All) {
            noteviewModel.deleteAllNotes();
        }
        return true;
    }
}
