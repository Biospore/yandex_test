package biospore.yandex_test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.Transition;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

public class AddNoteActivity extends AppCompatActivity {
    public static final int OK = 0;
    public static final int ERROR = 1;
    public static final String NEW_NOTE = "new_note";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configureTransition();
        setContentView(R.layout.activity_add_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_test);
        setSupportActionBar(toolbar);
    }

    private void configureTransition() {
        Window window = getWindow();
        Transition transition = new Explode();

        window.setExitTransition(transition);
        window.setEnterTransition(transition);
    }

    public void addNote(View view) {
        NoteDatabaseHelper db = new NoteDatabaseHelper(this);
        EditText title = (EditText) findViewById(R.id.title_field);
        EditText text = (EditText) findViewById(R.id.text_field);
        if (text != null && title != null) {
            Note new_note = new Note(
                    title.getText().toString(),
                    text.getText().toString());
            db.addNote(new_note);

            Intent result = new Intent();
            result.putExtra(NEW_NOTE, new_note);
            setResult(OK, result);

        } else {
            setResult(ERROR);
            throw (new RuntimeException("Null title or text"));
        }
        db.close();
        finishAfterTransition();
    }

    public void onItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.button_add:
                NoteDatabaseHelper db = new NoteDatabaseHelper(this);
                EditText title = (EditText) findViewById(R.id.title_field);
                EditText text = (EditText) findViewById(R.id.text_field);
                if (text != null && title != null) {
                    Note new_note = new Note(
                            title.getText().toString(),
                            text.getText().toString());
                    Log.i("FAIL ID", String.valueOf(new_note.getId()));
                    db.addNote(new_note);

                    Intent result = new Intent();
                    result.putExtra(NEW_NOTE, new_note);
                    setResult(OK, result);

                } else {
                    setResult(ERROR);
                    throw (new RuntimeException("Null title or text"));
                }
                db.close();
                finishAfterTransition();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_note_activity_menu, menu);
        return true;
    }
}
