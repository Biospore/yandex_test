package biospore.yandex_test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Explode;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import java.lang.ref.WeakReference;

public class ShowAndEditNoteActivity extends AppCompatActivity {
    //    public static final int ERROR = 3;
    public static final int DELETE = 1;
    public static final int CHANGED = 2;
    public static final String NOTE_CHANGE = "change_note";
    public static final String NOTE_CHANGE_POSITION = "changed_note_position";
    public static final String NOTE_DELETED = "delete_note";
    private WeakReference<ShowAndEditNoteActivity> activity = new WeakReference<>(this);
    Intent intent;
    Note note;
    Button edit;
    NoteDatabaseHelper db;
    EditText titleField;
    EditText textField;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configureTransition();
        setContentView(R.layout.activity_show_and_edit_note);
        intent = getIntent();
        long note_id = Integer.parseInt(intent.getStringExtra(MainActivity.NOTE_ID));
        position = Integer.parseInt(intent.getStringExtra(MainActivity.NOTE_POSITION));

        db = new NoteDatabaseHelper(this);
//        Log.i("IDP ES", intent.getStringExtra(MainActivity.NOTE_ID));
        Log.i("FAIL", String.valueOf(note_id));
        note = db.getNoteById(note_id);


        titleField = (EditText) findViewById(R.id.title_field);
        textField = (EditText) findViewById(R.id.text_field);
        edit = (Button) findViewById(R.id.edit);


        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (activity.get() != null) {
                    edit.setText(R.string.save);
                    edit.setBackgroundResource(R.drawable.green_background);
                    titleField.setEnabled(true);
                    textField.setEnabled(true);
                    View.OnClickListener listener = new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            note.setTitle(titleField.getText().toString());
                            note.setText(textField.getText().toString());
                            Intent result = new Intent();
                            result.putExtra(NOTE_CHANGE, note);
                            result.putExtra(NOTE_CHANGE_POSITION, position);
                            setResult(CHANGED, result);
                            db.updateNote(note);

                            db.close();
                            finishAfterTransition();
//                finish();
                        }
                    };
                    edit.setOnClickListener(listener);
                }
            }
        });

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

    private void configureTransition() {
        Window window = getWindow();
        window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        window.setExitTransition(new Explode());
        window.setEnterTransition(new Explode());
    }

    public void deleteNote(View view) {
//        edit.setClickable(false);
        Intent result = new Intent();
        result.putExtra(NOTE_DELETED, position);
        setResult(DELETE, result);
        db.deleteNote(note);
        db.close();
        finish();
//        finishAfterTransition();
    }

    public void editNote(View view) {
        edit.setText(R.string.save);
        edit.setBackgroundResource(R.drawable.green_background);
        titleField.setEnabled(true);
        textField.setEnabled(true);
//        Log.i("LFTT", String.valueOf(edit.isClickable()));

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                note.setTitle(titleField.getText().toString());
                note.setText(textField.getText().toString());
                Intent result = new Intent();
                result.putExtra(NOTE_CHANGE, note);
                result.putExtra(NOTE_CHANGE_POSITION, position);
                setResult(CHANGED, result);
                db.updateNote(note);

                db.close();
                finishAfterTransition();
//                finish();
            }
        };
        edit.setOnClickListener(listener);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}


