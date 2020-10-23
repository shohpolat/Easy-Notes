package com.example.roomjava2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;


public class Add_Edit_Note_Activity extends AppCompatActivity {

    public static final String TITLE_EXTRA = "title_extra";
    public static final String DESCRIPTION_EXTRA = "description_extra";
    public static final String PRIORITY_EXTRA = "priority_extra";
    public static final String ID_EXTRA = "id_extra";
    private EditText title_edit;
    private EditText description_edit;
    private NumberPicker numberPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add__note_);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        setTitle("Add Note");
        title_edit = findViewById(R.id.edit_title_text);
        description_edit = findViewById(R.id.edit_description_text);
        numberPicker = findViewById(R.id.numberPicker);

        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(10);

        Intent intent = getIntent();
        if (intent.hasExtra(ID_EXTRA)) {
            setTitle("Edit Note");
            title_edit.setText(intent.getStringExtra(TITLE_EXTRA));
            description_edit.setText(intent.getStringExtra(DESCRIPTION_EXTRA));
            numberPicker.setValue(intent.getIntExtra(PRIORITY_EXTRA, 0));
        }else {
            setTitle("Add Note");
        }


    }


    private void saveNote(){
        String title = title_edit.getText().toString();
        String description = description_edit.getText().toString();
        int priority = numberPicker.getValue();

        if (title.trim().isEmpty() || description.trim().isEmpty()){
            Toast.makeText(this, "Please enter the title ans description!", Toast.LENGTH_SHORT).show();
            return;
        }


        Intent intent = new Intent();
        intent.putExtra(TITLE_EXTRA,title);
        intent.putExtra(DESCRIPTION_EXTRA,description);
        intent.putExtra(PRIORITY_EXTRA,priority);

        int id = getIntent().getIntExtra(ID_EXTRA,-1);
        if (id!=-1){
            intent.putExtra(ID_EXTRA,id);
        }
        setResult(RESULT_OK,intent);
        finish();

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_note,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.save_note){
             saveNote();
        }
        return true;
    }
}
