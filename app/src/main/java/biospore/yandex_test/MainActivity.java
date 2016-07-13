package biospore.yandex_test;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private View mainView;
    //    private WeakReference<ListView> weakListView;
    private static String NOTES_BUNDLE_VALUE = "notes";
    private static ArrayList<String> titles = new ArrayList<String>();
    NoteDatabaseHelper db;

    public static final String NOTE_ID = "biospore.yandex_test.NOTE_ID";
    public static final String NOTE_POSITION = "note_postion";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new NoteDatabaseHelper(this);

        initializeView();
        initializeAdapter();

        if (savedInstanceState != null) {
            NoteParcelStorage storage = (NoteParcelStorage) savedInstanceState.get(NOTES_BUNDLE_VALUE);
            if (storage != null) {
                fillArrayAdapter(storage.getNotes());
            }
        } else {
            ArrayList<Note> notes = db.getAllNotes();
            fillArrayAdapter(notes);
        }
    }

    private void initializeView() {

//        if (viewIsGrid()) {
//            mainView = findViewById(R.id.grid_main_view);
//        } else {
//            mainView = findViewById(R.id.list_main_view);
//        }

        mainView = findViewById(R.id.main_view);
    }

    private void initializeAdapter() {
        createAdapter();
        setItemClickListenerToView(mainView);
    }

//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//
//    }

    private void createAdapter() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                titles
        );

//        Log.i("VT", String.valueOf(viewIsGrid()));
        if (mainView instanceof GridView) {
            ((GridView) mainView).setAdapter(adapter);
        } else if (mainView instanceof ListView) {
            ((ListView) mainView).setAdapter(adapter);
        } else {

        }


    }

    private void addNoteToAdapter(Note note) {
        ArrayAdapter adapter = getViewAdapter();
        if (note.getTitle().isEmpty())
            note.setTitle(getString(R.string.empty_title));
            /*У класса Note вызывается метод toString(), так что все должно быть OK.*/
        adapter.add(note);
    }


//    private boolean viewIsGrid() { //deprecated
//        return mainView instanceof GridView;
////        return findViewById(R.id.grid_main_view) != null;
//    }

    private ArrayAdapter getViewAdapter() {

        if (mainView instanceof GridView) {
            return (ArrayAdapter) ((GridView) mainView).getAdapter();
        } else if (mainView instanceof ListView) {
            return (ArrayAdapter) ((ListView) mainView).getAdapter();
        } else {
            throw new RuntimeException("Wrong View");
        }
    }

    private void deleteNoteFromAdapter(int position) {
        ArrayAdapter adapter = getViewAdapter();
        adapter.remove(adapter.getItem(position));
    }

    private void updateNoteOnAdapter(int position, Note note) {
        ArrayAdapter adapter = getViewAdapter();
        adapter.remove(adapter.getItem(position));
        adapter.insert(note, position);
    }

    private void fillArrayAdapter(ArrayList<Note> notes) {
        ArrayAdapter adapter = getViewAdapter();
        adapter.clear();
        titles.clear();
        getViewAdapter().notifyDataSetChanged();
        for (Note n : notes) {
            if (n.getTitle().isEmpty())
                n.setTitle(getString(R.string.empty_title));
            /*У класса Note вызывается метод toString(), так что все должно быть OK.*/
            adapter.add(n);
        }
    }

    private AdapterView.OnItemClickListener getOnItemClickListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, ShowAndEditNoteActivity.class);
                Note n = (Note) parent.getItemAtPosition(position);
                intent.putExtra(NOTE_ID, String.valueOf(n.getId()));
                intent.putExtra(NOTE_POSITION, String.valueOf(position));
                startActivityForResult(intent, ShowAndEditNoteActivity.DELETE | ShowAndEditNoteActivity.CHANGED);
            }
        };
    }

    private void setItemClickListenerToView(View view) {
        if (mainView instanceof GridView) {
            ((GridView) view).setOnItemClickListener(getOnItemClickListener());
        } else if (mainView instanceof ListView) {
            ((ListView) view).setOnItemClickListener(getOnItemClickListener());
        } else {
            throw new RuntimeException("Wrong View");
        }
    }

    public void addNote(View view) {
        Intent intent = new Intent(this, AddNoteActivity.class);
        startActivityForResult(intent, AddNoteActivity.OK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            switch (resultCode) {
                case AddNoteActivity.OK:
                    Note new_note = data.getExtras().getParcelable(AddNoteActivity.NEW_NOTE);
                    addNoteToAdapter(new_note);
                    break;
                case ShowAndEditNoteActivity.DELETE:
                    int position = data.getExtras().getInt(ShowAndEditNoteActivity.NOTE_DELETED);
                    deleteNoteFromAdapter(position);
                    break;
                case ShowAndEditNoteActivity.CHANGED:
                    int changed_position = data.getExtras().getInt(ShowAndEditNoteActivity.NOTE_CHANGE_POSITION);
                    Note changed_note = data.getExtras().getParcelable(ShowAndEditNoteActivity.NOTE_CHANGE);
                    updateNoteOnAdapter(changed_position, changed_note);
                    break;

            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ArrayAdapter adapter = getViewAdapter();
        ArrayList<Note> notes = new ArrayList<Note>();
        int count = adapter.getCount();
        for (int i = 0; i < count; i++) {
            notes.add((Note) adapter.getItem(i));
        }
        NoteParcelStorage storage = new NoteParcelStorage(notes);
        outState.putParcelable(NOTES_BUNDLE_VALUE, storage);
    }
}
