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
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

public class ShowAndEditNoteActivity extends AppCompatActivity {
    //    public static final int ERROR = 3;
    public static final int DELETE = 1;
    public static final int CHANGED = 2;
    public static final String NOTE_CHANGE = "change_note";
    public static final String NOTE_CHANGE_POSITION = "changed_note_position";
    public static final String NOTE_DELETED = "delete_note";
    Intent intent;
    Note note;
    //    Button edit;
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
//        Log.i("FAIL", String.valueOf(note_id));
        note = db.getNoteById(note_id);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_test);
        //toolbar.inflateMenu(R.menu.main_activity_menu);
        setSupportActionBar(toolbar);
//        toolbar.addView(getAddNoteButton());

//        if (toolbar != null) {
//            toolbar.inflateMenu(R.menu.show_and_edit_activity_menu);
//        }


        titleField = (EditText) findViewById(R.id.title_field);
        textField = (EditText) findViewById(R.id.text_field);
//        edit = (Button) findViewById(R.id.edit);

//
//        edit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                    edit.setText(R.string.save);
//                    edit.setBackgroundResource(R.drawable.green_background);
//                    titleField.setEnabled(true);
//                    textField.setEnabled(true);
//                    View.OnClickListener listener = new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            note.setTitle(titleField.getText().toString());
//                            note.setText(textField.getText().toString());
//                            Intent result = new Intent();
//                            result.putExtra(NOTE_CHANGE, note);
//                            result.putExtra(NOTE_CHANGE_POSITION, position);
//                            setResult(CHANGED, result);
//                            db.updateNote(note);
//
//                            db.close();
//                            finishAfterTransition();
////                finish();
//                        }
//                    };
//                    edit.setOnClickListener(listener);
//
//            }
//
//
//        });

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
        titleField.setImeOptions(EditorInfo.IME_ACTION_DONE|EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        textField.setEnabled(false);
        textField.setImeOptions(EditorInfo.IME_ACTION_DONE|EditorInfo.IME_FLAG_NO_EXTRACT_UI);

    }

    private void configureTransition() {
        Window window = getWindow();
//        window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        Transition transition = new Explode();
        transition.addTarget(getString(R.string.transition_list_element));
        window.setExitTransition(transition);
        window.setEnterTransition(transition);
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

//    public void editNote(View view) {
//        edit.setText(R.string.save);
//        edit.setBackgroundResource(R.drawable.green_background);
//        titleField.setEnabled(true);
//        textField.setEnabled(true);
////        Log.i("LFTT", String.valueOf(edit.isClickable()));
//
//        View.OnClickListener listener = new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                note.setTitle(titleField.getText().toString());
//                note.setText(textField.getText().toString());
//                Intent result = new Intent();
//                result.putExtra(NOTE_CHANGE, note);
//                result.putExtra(NOTE_CHANGE_POSITION, position);
//                setResult(CHANGED, result);
//                db.updateNote(note);
//
//                db.close();
//                finishAfterTransition();
////                finish();
//            }
//        };
//        edit.setOnClickListener(listener);
//    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    public void onItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit:
//                edit.setText(R.string.save);
//                edit.setBackgroundResource(R.drawable.green_background);
                item.setTitle(R.string.save);
                titleField.setEnabled(true);
                textField.setEnabled(true);
//        Log.i("LFTT", String.valueOf(edit.isClickable()));


//                edit.setOnClickListener(listener);
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
//                        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_test);
//                        if (toolbar != null) {
//                            toolbar.getMenu().findItem(R.id.delete).setVisible(true);
//                        }
                        return false;
                    }
                });
                Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_test);
                if (toolbar != null) {
                    toolbar.getMenu().findItem(R.id.delete).setVisible(false);
                }
                break;
            case R.id.delete:
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


