package biospore.yandex_test;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private static String NOTES_BUNDLE_VALUE = "notes";
//    private static String NOTES_BUNDLE_COUNT = "notes_count";
    private static ArrayList<String> titles = new ArrayList<String>();
    NoteDatabaseHelper db;

    public static final String NOTE_ID = "biospore.yandex_test.NOTE_ID";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        db = new NoteDatabaseHelper(this);
        listView = (ListView) findViewById(R.id.list_view);
        createAdapter();

        if (savedInstanceState != null) {

            NoteParcelStorage storage = (NoteParcelStorage) savedInstanceState.get(NOTES_BUNDLE_VALUE);
            if (storage != null) {
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


    private ArrayList<Note> getNotesFromAdapter(ArrayAdapter adapter)
    {
        ArrayList<Note> notes = new ArrayList<>();
        int count = adapter.getCount();
        for (int i = 0; i < count; i++)
        {
            notes.add((Note) adapter.getItem(i));
        }
        return notes;
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
                        startActivity(intent);
                    }
                });
    }

    public void addNote(View view) {
        Intent intent = new Intent(this, AddNoteActivity.class);
        startActivity(intent);
    }



    @Override
    protected void onResume() {
        super.onResume();
        
//        refreshContent();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        ArrayAdapter adapter = (ArrayAdapter) listView.getAdapter();
        ArrayList<Note> notes = new ArrayList<Note>();
        int count = adapter.getCount();
//        outState.putInt(NOTES_BUNDLE_COUNT, count);

        for (int i = 0; i < count; i++)
        {
            notes.add((Note) adapter.getItem(i));
        }
        NoteParcelStorage storage = new NoteParcelStorage(notes);

        outState.putParcelable(NOTES_BUNDLE_VALUE, storage);
        super.onSaveInstanceState(outState);
    }
}
