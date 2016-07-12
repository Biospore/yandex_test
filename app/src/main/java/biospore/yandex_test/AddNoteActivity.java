package biospore.yandex_test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RemoteViews;

public class AddNoteActivity extends AppCompatActivity {
    public static final int ADD_NOTE_ACTIVITY_OK = 0;
    public static final int ADD_NOTE_ACTIVITY_ERROR = 1;
    public static final String ADD_NOTE_ACTIVITY_NEW_NOTE = "note";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
    }

    public void addNote(View view) {
        NoteDatabaseHelper db = new NoteDatabaseHelper(this);
        EditText title = (EditText) findViewById(R.id.title_field);
        EditText text =  (EditText) findViewById(R.id.text_field);
        /*Working with sql very slow*/
        /*If text or title is null - just throw exception*/
        if (text != null && title != null) {
                /*field not unique - don't need additional checking*/
            Note new_note = new Note(
                    title.getText().toString(),
                    text.getText().toString());
                db.addNote(new_note);

            Intent result = new Intent();
            result.putExtra(ADD_NOTE_ACTIVITY_NEW_NOTE, new_note);
            setResult(ADD_NOTE_ACTIVITY_OK, result);

            //else {/*Warning that title is empty or text is empty*/}
            /*              ^             */
            /*Problem with /|\ solved - just add empty value and show placeholder on screen*/
            /*              |           */
        } else {
            setResult(ADD_NOTE_ACTIVITY_ERROR);
            throw (new RemoteViews.ActionException("Null title or text"));
        }
        db.close();
        finish();
    }
}
