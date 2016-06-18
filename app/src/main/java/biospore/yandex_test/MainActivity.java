package biospore.yandex_test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    public static ArrayList<String> titles = new ArrayList<String>();
    NoteDatabaseHelper db;


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
        ArrayAdapter adapter = (ArrayAdapter) listView.getAdapter();
        ((ArrayAdapter)listView.getAdapter()).notifyDataSetChanged();

        List<Note> notes = db.getAllNotes();

        for (Note n : notes)
        {
            adapter.add(n.getTitle() + String.valueOf(n.getId()));
        }
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
