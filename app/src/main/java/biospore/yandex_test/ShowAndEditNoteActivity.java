package biospore.yandex_test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.Transition;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

public class ShowAndEditNoteActivity extends AppCompatActivity {
    public static final int DELETE = 1;
    public static final int CHANGED = 2;
    public static final String NOTE_CHANGE = "change_note";
    public static final String NOTE_CHANGE_POSITION = "changed_note_position";
    public static final String NOTE_DELETED = "delete_note";
    public static final int OK = 0;
    public static final int ERROR = 1;
    public static final String NEW_NOTE = "new_note";
    Intent intent;
    Note note;
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
        note = db.getNoteById(note_id);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_test);
        setSupportActionBar(toolbar);

        titleField = (EditText) findViewById(R.id.title_field);
        textField = (EditText) findViewById(R.id.text_field);

        if (note.getTitle().isEmpty()) {
            titleField.setHint(R.string.empty_title);
        } else {
            titleField.setText(note.getTitle());
        }
        if (note.getText().isEmpty()) {
            textField.setHint(R.string.empty_text);
        } else {
            textField.setText(note.getText());
        }

        titleField.setEnabled(false);
        textField.setEnabled(false);

    }

    private void configureTransition() {
        Window window = getWindow();
        Transition transition = new Explode();
        transition.addTarget(getString(R.string.transition_list_element));
        window.setExitTransition(transition);
        window.setEnterTransition(transition);
    }

    public void deleteNote(View view) {
        Intent result = new Intent();
        result.putExtra(NOTE_DELETED, position);
        setResult(DELETE, result);
        db.deleteNote(note);
        db.close();
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    public void onItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.button_edit:
                item.setTitle(R.string.save);
                titleField.setEnabled(true);
                textField.setEnabled(true);


                item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        note.setTitle(titleField.getText().toString());
                        note.setText(textField.getText().toString());
                        Intent result = new Intent();
                        result.putExtra(NOTE_CHANGE, note);
                        result.putExtra(NOTE_CHANGE_POSITION, position);
                        setResult(CHANGED, result);
                        db.updateNote(note);

                        db.close();
                        finishAfterTransition();
                        return false;
                    }
                });
                Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_test);
                if (toolbar != null) {
                    toolbar.getMenu().findItem(R.id.button_delete).setVisible(false);
                }
                break;
            case R.id.button_delete:
                Intent result = new Intent();
                result.putExtra(NOTE_DELETED, position);
                setResult(DELETE, result);
                db.deleteNote(note);
                db.close();
                finishAfterTransition();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.show_and_edit_activity_menu, menu);
        return true;
    }
}


