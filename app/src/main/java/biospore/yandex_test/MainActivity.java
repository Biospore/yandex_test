package biospore.yandex_test;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private static String NOTES_BUNDLE_VALUE = "notes";
    private static ArrayList<String> titles = new ArrayList<String>();
    NoteDatabaseHelper db;

    public static final String NOTE_ID = "biospore.yandex_test.NOTE_ID";
    public static final String NOTE_POSITION = "note_postion";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Log.i("Bundle Resume", "oncreate");

        db = new NoteDatabaseHelper(this);
        listView = (ListView) findViewById(R.id.list_view);

        createAdapter();

        if (savedInstanceState != null)
        {
            Log.i("Debug Bundle Resume", "instance is not null");


            NoteParcelStorage storage = (NoteParcelStorage) savedInstanceState.get(NOTES_BUNDLE_VALUE);
            if (storage != null) {
                Log.i("Debug Bundle Resume", "storage is not null");

                fillArrayAdapter(storage.getNotes());
            }
        }
        else
        {
            ArrayList<Note> notes = db.getAllNotes();
            fillArrayAdapter(notes);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }

    private void createAdapter() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                titles
        );

        listView.setAdapter(adapter);
    }

    private void addNoteToAdapter(Note note)
    {
        ArrayAdapter adapter = (ArrayAdapter) listView.getAdapter();
        if (note.getTitle().isEmpty())
            note.setTitle(getString(R.string.empty_title));
            /*У класса Note вызывается метод toString(), так что все должно быть OK.*/
        adapter.add(note);
    }

    private void deleteNoteFromAdapter(int position)
    {
        ArrayAdapter adapter = (ArrayAdapter) listView.getAdapter();
        adapter.remove(adapter.getItem(position));
    }

    private void updateNoteOnAdapter(int position, Note note)
    {
        ArrayAdapter adapter = (ArrayAdapter) listView.getAdapter();
        adapter.remove(adapter.getItem(position));
        adapter.insert(note, position);
    }

    private void fillArrayAdapter(ArrayList<Note> notes) {
        ArrayAdapter adapter = (ArrayAdapter) listView.getAdapter();

        adapter.clear();
        titles.clear();
        ((ArrayAdapter) listView.getAdapter()).notifyDataSetChanged();
        for (Note n : notes) {
            if (n.getTitle().isEmpty())
                n.setTitle(getString(R.string.empty_title));
            /*У класса Note вызывается метод toString(), так что все должно быть OK.*/
            adapter.add(n);
        }
        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(MainActivity.this, ShowAndEditNoteActivity.class);
                        Note n = (Note) parent.getItemAtPosition(position);
                        intent.putExtra(NOTE_ID, String.valueOf(n.getId()));
                        intent.putExtra(NOTE_POSITION, String.valueOf(position));
                        Log.i("Debug Button", "clicked");
                        startActivityForResult(intent, ShowAndEditNoteActivity.DELETE | ShowAndEditNoteActivity.CHANGED);
                    }
                });
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
        ArrayAdapter adapter = (ArrayAdapter) listView.getAdapter();
        ArrayList<Note> notes = new ArrayList<Note>();
        int count = adapter.getCount();
        for (int i = 0; i < count; i++)
        {
            notes.add((Note) adapter.getItem(i));
        }
        NoteParcelStorage storage = new NoteParcelStorage(notes);
        Log.i("Debug Bundle Resume", "storage saved");
        outState.putParcelable(NOTES_BUNDLE_VALUE, storage);
    }
}
