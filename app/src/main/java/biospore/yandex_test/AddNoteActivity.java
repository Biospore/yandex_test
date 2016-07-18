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
        //toolbar.inflateMenu(R.menu.main_activity_menu);
        setSupportActionBar(toolbar);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_test);
////        toolbar.addView(getAddNoteButton());
//
//        if (toolbar != null) {
//            toolbar.inflateMenu(R.menu.main_activity_menu);
//        }
    }

    private void configureTransition() {
        Window window = getWindow();
//        window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
//        transition.addTarget(getString(R.string.transition_title));
        Transition transition = new Explode();
//        transition.addTarget(getString(R.string.transition_list_element));

        window.setExitTransition(transition);
        window.setEnterTransition(transition);
    }

    public void addNote(View view) {
        NoteDatabaseHelper db = new NoteDatabaseHelper(this);
        EditText title = (EditText) findViewById(R.id.title_field);
        EditText text = (EditText) findViewById(R.id.text_field);
        /*Working with sql very slow*/
        /*If text or title is null - just throw exception*/
        if (text != null && title != null) {
                /*field not unique - don't need additional checking*/
            Note new_note = new Note(
                    title.getText().toString(),
                    text.getText().toString());
            Log.i("FAIL ID", String.valueOf(new_note.getId()));
            db.addNote(new_note);

//            Log.i("IDP A", String.valueOf(new_note.getId()));
            Intent result = new Intent();
            result.putExtra(NEW_NOTE, new_note);
            setResult(OK, result);

            //else {/*Warning that title is empty or text is empty*/}
            /*              ^             */
            /*Problem with /|\ solved - just add empty value and show placeholder on screen*/
            /*              |           */
        } else {
            setResult(ERROR);
            throw (new RuntimeException("Null title or text"));
        }
        db.close();
        finishAfterTransition();

//        finish();
    }

    public void onItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.button_add_note:
                NoteDatabaseHelper db = new NoteDatabaseHelper(this);
                EditText title = (EditText) findViewById(R.id.title_field);
                EditText text = (EditText) findViewById(R.id.text_field);
        /*Working with sql very slow*/
        /*If text or title is null - just throw exception*/
                if (text != null && title != null) {
                /*field not unique - don't need additional checking*/
                    Note new_note = new Note(
                            title.getText().toString(),
                            text.getText().toString());
                    Log.i("FAIL ID", String.valueOf(new_note.getId()));
                    db.addNote(new_note);

//            Log.i("IDP A", String.valueOf(new_note.getId()));
                    Intent result = new Intent();
                    result.putExtra(NEW_NOTE, new_note);
                    setResult(OK, result);

                    //else {/*Warning that title is empty or text is empty*/}
            /*              ^             */
            /*Problem with /|\ solved - just add empty value and show placeholder on screen*/
            /*              |           */
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
