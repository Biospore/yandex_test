package biospore.yandex_test;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Fade;
import android.transition.Transition;
import android.view.Display;
import android.view.View;
import android.view.Window;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import android.support.v4.util.Pair;
import java.util.List;

//import android.support.v7.widget.View;

public class MainActivity extends Activity implements CustomClickListener {

    private RecyclerView mainView;
    private static String NOTES_BUNDLE_VALUE = "notes";
    private static List<Note> notes = new ArrayList<>();
    private WeakReference<EvenOddAdapter> tAdapter;
    private Point size;

    NoteDatabaseHelper db;

    public static final String NOTE_ID = "biospore.yandex_test.NOTE_ID";
    public static final String NOTE_POSITION = "note_postion";


    @Override
    public void onItemClick(View v, int position) {
        Intent intent = new Intent(MainActivity.this, ShowAndEditNoteActivity.class);
        EvenOddAdapter adapter = tAdapter.get();
        Note note;
        if (adapter != null) {
            note = (Note) adapter.getItem(position);
        } else {
            throw new RuntimeException("Adapter is null!");
        }

//        Log.i("IDP ID", String.valueOf(note.getId()));
//        Log.i("IDP POS", String.valueOf(position));
        intent.putExtra(NOTE_ID, String.valueOf(note.getId()));
        intent.putExtra(NOTE_POSITION, String.valueOf(position));

        Pair p1 = Pair.create(v, getString(R.string.transition_list_element));
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, p1);
//        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, );
        startActivityForResult(intent, ShowAndEditNoteActivity.DELETE | ShowAndEditNoteActivity.CHANGED, options.toBundle());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configureTransition();

        db = new NoteDatabaseHelper(this);
        setContentView(R.layout.activity_main_recycler);
        mainView = (RecyclerView) findViewById(R.id.main_view);
        final EvenOddAdapter adapter = new EvenOddAdapter();
        adapter.setOnItemClickListener(new WeakReference<CustomClickListener>(this));

        mainView.setAdapter(adapter);
        tAdapter = new WeakReference<EvenOddAdapter>(adapter);
        EvenOddLayoutManager layoutManager = new EvenOddLayoutManager(
                this,
                2);
        layoutManager.setSpanSizeLookup(getSpanSize());
        mainView.setLayoutManager(layoutManager);

        if (savedInstanceState != null) {
            NoteParcelStorage storage = (NoteParcelStorage) savedInstanceState.get(NOTES_BUNDLE_VALUE);
            if (storage != null) {
                fillAdapter(storage.getNotes());
            }
        } else {
            ArrayList<Note> notes = db.getAllNotes();
            fillAdapter(notes);
        }
        /*
        adapter.clear();
        for (Note n: db.getAllNotes())
        {
            db.deleteNote(n);
        }*/
    }

    private void configureTransition()
    {
        Window window = getWindow();
//        window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        Transition transition = new Fade();
        transition.addTarget(getString(R.string.transition_list_element));

        window.setExitTransition(transition);
        window.setEnterTransition(transition);
    }


    private GridLayoutManager.SpanSizeLookup getSpanSize() {
        final Display display = this.getWindowManager().getDefaultDisplay();
        return new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                /*
                * Возвращаемое значение - количество занимаемых элементом столбцов
                * */

//              getResources().getConfiguration().orientation == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                if (size == null) {
                    size = new Point();
                }
                display.getSize(size);

                if (size.x <= size.y) {
                    return 2;
                } else {
                    if (position == 0)
                        return 1;
                    return ((position + 1) % 3 == 0) ? 2 : 1;
                }
            }
        }

                ;
    }

    /* TODO
    *  переписать все на recycler view
    *  все должно выглядеть также
    *  одна xml на landscape и portrait
    * */

    private void addNoteToAdapter(Note note) {
        EvenOddAdapter adapter = getViewAdapter();
        if (note.getTitle().isEmpty())
            note.setTitle(getString(R.string.empty_title));
            /*У класса Note вызывается метод toString(), так что все должно быть OK.*/
        adapter.add(note);
    }


    private EvenOddAdapter getViewAdapter() {

        return (EvenOddAdapter) mainView.getAdapter();

    }

    private void deleteNoteFromAdapter(int position) {
        EvenOddAdapter adapter = getViewAdapter();

        adapter.remove(adapter.getItem(position));
    }

    private void updateNoteOnAdapter(int position, Note note) {
        EvenOddAdapter adapter = getViewAdapter();
        adapter.remove(adapter.getItem(position));
        if (note.getTitle().isEmpty())
            note.setTitle(getString(R.string.empty_title));
        adapter.insert(note, position);
    }

    private void fillAdapter(ArrayList<Note> notes) {
        EvenOddAdapter adapter = getViewAdapter();
        adapter.clear();
        MainActivity.notes.clear();
        for (Note note : notes) {
            if (note.getTitle().isEmpty())
                note.setTitle(getString(R.string.empty_title));
            /*У класса Note вызывается метод toString(), так что все должно быть OK.*/
            adapter.add(note);
        }
    }

    public void addNote(View view) {
        Intent intent = new Intent(this, AddNoteActivity.class);
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, view, "title");
        startActivityForResult(intent, AddNoteActivity.OK, options.toBundle());
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
