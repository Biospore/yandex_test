package biospore.yandex_test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
//    private Button button;
    public static ArrayList<String> titles = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NoteDatabaseHelper db = new NoteDatabaseHelper(this);
        List<Note> notes = db.getAllNotes();

        listView = (ListView) findViewById(R.id.list_view);
//        button = (Button) findViewById(R.id.button);



        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                titles
                );

        for (Note n : notes)
        {
            adapter.add(n.getTitle() + String.valueOf(n.getId()));
        }
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Log.d("long id", String.valueOf(id));
                    }
                });
//        listView.setOnItemClickListener(
//                new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        Toast.makeText(getApplicationContext(), ((TextView) view).getText(), Toast.LENGTH_SHORT).show();
//                    }
//                }
//        );
    }

    public void addNote(View view)
    {
        Intent intent = new Intent(this, AddNoteActivity.class);
        startActivity(intent);
    }
}
