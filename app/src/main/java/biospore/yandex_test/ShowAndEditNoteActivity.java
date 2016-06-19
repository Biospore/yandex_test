package biospore.yandex_test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ShowAndEditNoteActivity extends AppCompatActivity {
    Intent intent;
    Note note;
    Button edit;
    NoteDatabaseHelper db;
    EditText titleField;
    EditText textField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_and_edit_note);
        intent = getIntent();
        int note_id = Integer.parseInt(intent.getStringExtra(MainActivity.NOTE_ID));

        db = new NoteDatabaseHelper(this);
        note = db.getNoteById(note_id);

        titleField = (EditText) findViewById(R.id.title_field);
        textField = (EditText) findViewById(R.id.text_field);
        edit = (Button) findViewById(R.id.edit);

        if (note.getTitle().isEmpty())
            titleField.setHint(R.string.empty_title);
        else
            titleField.setText(note.getTitle());
        if (note.getText().isEmpty())
            textField.setHint(R.string.empty_text);
        else
            textField.setText(note.getText());

        titleField.setEnabled(false);
        textField.setEnabled(false);
    }

    public void deleteNote(View view) {
        db.deleteNote(note);
        db.close();
        finish();
    }

    public void editNote(View view) {
        edit.setText(R.string.save);
        edit.setBackgroundResource(R.color.green);
        titleField.setEnabled(true);
        textField.setEnabled(true);
        View.OnClickListener listen = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                note.setTitle(titleField.getText().toString());
                note.setText(textField.getText().toString());
                db.updateNote(note);

                finish();
            }
        };
        edit.setOnClickListener(listen);
    }
}


