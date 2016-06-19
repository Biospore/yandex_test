package biospore.yandex_test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    public static ArrayList<String> titles = new ArrayList<String>();
    NoteDatabaseHelper db;

    public static final String NOTE_ID = "biospore.yandex_test.NOTE_ID";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new NoteDatabaseHelper(this);
        listView = (ListView) findViewById(R.id.list_view);
        createAdapter();
        refreshContent();
    }

    private void createAdapter()
    {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                titles
                );

        listView.setAdapter(adapter);
    }

    private void refreshContent()
    {
        titles.clear();
        /*Просто надеемся на то, что никто не поменяет код*/
        /*Можно впихнуть проверку на тип*/
        ArrayAdapter adapter = (ArrayAdapter) listView.getAdapter();
        ((ArrayAdapter)listView.getAdapter()).notifyDataSetChanged();

        List<Note> notes = db.getAllNotes();

        for (Note n : notes)
        {
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

    public void addNote(View view)
    {
        Intent intent = new Intent(this, AddNoteActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshContent();
    }
}
