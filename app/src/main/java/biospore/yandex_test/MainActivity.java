package biospore.yandex_test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.List;

//import android.support.v7.widget.View;

public class MainActivity extends Activity {

    private RecyclerView mainView;
    private static String NOTES_BUNDLE_VALUE = "notes";
    private static List<String> titles = new ArrayList<String>();
    NoteDatabaseHelper db;

    public static final String NOTE_ID = "biospore.yandex_test.NOTE_ID";
    public static final String NOTE_POSITION = "note_postion";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new NoteDatabaseHelper(this);
        setContentView(R.layout.activity_main_recycler);
        mainView = (RecyclerView) findViewById(R.id.main_view);
        mainView.setAdapter(new EvenOddAdapter<String>(titles));
        mainView.setLayoutManager(new LinearLayoutManager(this));
//        Log.i("FFK", "loaded");

//        initializeAdapter();

        if (savedInstanceState != null) {
            NoteParcelStorage storage = (NoteParcelStorage) savedInstanceState.get(NOTES_BUNDLE_VALUE);
            if (storage != null) {
                fillAdapter(storage.getNotes());
            }
        } else {
            ArrayList<Note> notes = db.getAllNotes();
            fillAdapter(notes);
        }
    }

    private void initializeAdapter() {
        Log.i("FFK", "test");
        createAdapter();
        setItemClickListenerToView(mainView);
    }

    /* TODO
    *  переписать все на recycler view
    *  все должно выглядеть также
    *  одна xml на landscape и portrait
    * */
    private void createAdapter() {
//        ArrayAdapter<String> add = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,titles);
        EvenOddAdapter<String> adapter = new EvenOddAdapter<String>(
//                this,
//                R.layout.yandex_test_list_item_0,
                titles
        );

        mainView.setAdapter(adapter);
    }

    private void addNoteToAdapter(Note note) {
        EvenOddAdapter adapter = (EvenOddAdapter) getViewAdapter();
        if (note.getTitle().isEmpty())
            note.setTitle(getString(R.string.empty_title));
            /*У класса Note вызывается метод toString(), так что все должно быть OK.*/
        adapter.add(note);
    }


    private EvenOddAdapter getViewAdapter() {

        return (EvenOddAdapter) mainView.getAdapter();

    }

    private void deleteNoteFromAdapter(int position) {
        EvenOddAdapter adapter = (EvenOddAdapter) getViewAdapter();

        adapter.remove(adapter.getItem(position));
    }

    private void updateNoteOnAdapter(int position, Note note) {
        EvenOddAdapter adapter = (EvenOddAdapter) getViewAdapter();
        adapter.remove(adapter.getItem(position));
        adapter.insert(note, position);
    }

    private void fillAdapter(ArrayList<Note> notes) {
        EvenOddAdapter adapter = (EvenOddAdapter) getViewAdapter();
        adapter.clear();
        titles.clear();
//        getViewAdapter().notifyDataSetChanged();
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


    private void setItemClickListenerToView(RecyclerView view) {
//        if (mainView instanceof GridView) {
//            ((GridView) view).setOnItemClickListener(getOnItemClickListener());
//        } else if (mainView instanceof ListView) {
//            ((ListView) view).setOnItemClickListener(getOnItemClickListener());
//        } else {
//            throw new RuntimeException("Wrong View");
//        }
//        view.setOn
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
        EvenOddAdapter adapter = (EvenOddAdapter) getViewAdapter();
        ArrayList<Note> notes = new ArrayList<Note>();
        int count = adapter.getItemCount();
        for (int i = 0; i < count; i++) {
            notes.add((Note) adapter.getItem(i));
        }
        NoteParcelStorage storage = new NoteParcelStorage(notes);
        outState.putParcelable(NOTES_BUNDLE_VALUE, storage);
    }
}
