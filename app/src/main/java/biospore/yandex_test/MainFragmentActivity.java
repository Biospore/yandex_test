package biospore.yandex_test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

public class MainFragmentActivity extends AppCompatActivity {

    public static final String MENU_TYPE = "menu";
    public static final int ADD_NOTE = 0;
    public static final int EDIT_NOTE = 1;
    public static final String NOTE_ID = "id";
    public static final String LIST_FRAGMENT_TAG = "list";
    public static final String EDIT_FRAGMENT_TAG = "edit";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_fragment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_test);
        setSupportActionBar(toolbar);
        NoteListFragment listFragment = new NoteListFragment();
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, listFragment, LIST_FRAGMENT_TAG)
                .commit();
    }

/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

    }
*/


    public void onMenuItemClick(MenuItem item) {
        EditNoteFragment fragmentEdit = (EditNoteFragment) getFragmentManager().findFragmentByTag(EDIT_FRAGMENT_TAG);
        NoteListFragment fragmentList = (NoteListFragment) getFragmentManager().findFragmentByTag(LIST_FRAGMENT_TAG);
//        Log.i("MENU ENTRY ID", String.valueOf(item.getItemId()));
        switch (item.getItemId()) {
            case R.id.button_add:
//                Log.i("MENU CLICKED", "ADD NOTE");
                EditNoteFragment editFragment = new EditNoteFragment();
                Bundle bundle = new Bundle();
                bundle.putInt(MENU_TYPE, ADD_NOTE);
                editFragment.setArguments(bundle);

                getFragmentManager()
                        .beginTransaction()
//                        .addSharedElement(item.getActionView(), getString(R.string.transition_list_element))
                        .replace(R.id.fragment_container, editFragment, EDIT_FRAGMENT_TAG)
                        .commit();
//                Intent intent = new Intent(this, AddNoteActivity.class);
//                Pair p1 = Pair.create(mainView, getString(R.string.transition_list_element));
//                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, p1);
//                startActivityForResult(intent, AddNoteActivity.OK, options.toBundle());
                break;
            case R.id.button_delete:
//                Log.i("MENU CLICKED", "DELETE NOTE");
                /*
                Intent result = new Intent();
                result.putExtra(NOTE_DELETED, position);
                setResult(DELETE, result);
                db.deleteNote(note);
                db.close();
                finishAfterTransition();
                */
//                EditNoteFragment fragment = (EditNoteFragment) getFragmentManager().findFragmentByTag(EDIT_FRAGMENT_TAG);
                if (fragmentEdit != null) {
                    fragmentEdit.deleteNote();
                    getFragmentManager()
                            .beginTransaction()
//                            .addSharedElement(fragmentEdit.getView(), getString(R.string.transition_list_element))
                            .replace(R.id.fragment_container, new NoteListFragment(), LIST_FRAGMENT_TAG)
                            .commit();
                }
                break;
            case R.id.button_edit:
//                Log.i("MENU CLICKED", "EDIT NOTE");

                if (fragmentEdit != null) {
                    fragmentEdit.changeEditFlag();
                    item.setOnMenuItemClickListener(fragmentEdit.getSaveAfterEditListener());
                }
                /*
                item.setTitle(R.string.save);
                titleField.setEnabled(true);
                textField.setEnabled(true);


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
                        return false;
                    }
                });*/
                Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_test);
                if (toolbar != null) {
                    toolbar
                            .getMenu()
                            .findItem(R.id.button_delete)
                            .setVisible(false);
                }
                break;
            case R.id.button_save:
//                Log.i("MENU CLICKED", "SAVE NOTE");
                if (fragmentEdit != null) {
                    fragmentEdit.addNote();
                    getFragmentManager()
                            .beginTransaction()
//                            .addSharedElement(fragmentEdit.getView(), getString(R.string.transition_list_element))
                            .replace(R.id.fragment_container, new NoteListFragment(), LIST_FRAGMENT_TAG)
                            .commit();
                }
                break;


        }
    }

}
