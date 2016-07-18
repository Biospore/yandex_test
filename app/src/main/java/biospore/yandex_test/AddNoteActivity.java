package biospore.yandex_test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Explode;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.RemoteViews;

public class AddNoteActivity extends AppCompatActivity {
    public static final int OK = 0;
    public static final int ERROR = 1;
    public static final String NEW_NOTE = "new_note";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configureTransition();
        setContentView(R.layout.activity_add_note);
    }

    private void configureTransition()
    {
        Window window = getWindow();
        window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        window.setExitTransition(new Explode());
        window.setEnterTransition(new Explode());
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
}
