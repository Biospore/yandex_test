package biospore.yandex_test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Window;

public class MainFragmentActivity extends AppCompatActivity {

    public static final String MENU_TYPE = "menu";
    public static final int ADD_NOTE = 0;
    public static final int EDIT_NOTE = 1;
    public static final String LIST_FRAGMENT_TAG = "list";
    public static final String EDIT_FRAGMENT_TAG = "edit";
    public static final String EDIT_BACK_STACK_NAME = "EDIT";
    public NoteListFragment listFragment;


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(R.layout.activity_main_fragment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_test);
        setSupportActionBar(toolbar);

        NoteListFragment listFragmentInstance = (NoteListFragment) getFragmentManager().findFragmentByTag(LIST_FRAGMENT_TAG);
        EditNoteFragment editFragmentInstance = (EditNoteFragment) getFragmentManager().findFragmentByTag(EDIT_FRAGMENT_TAG);

        if (listFragmentInstance == null) {
            listFragment = new NoteListFragment();
            getFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container, listFragment, LIST_FRAGMENT_TAG)
                    .commit();
        } else if (editFragmentInstance != null) {
            listFragment = listFragmentInstance;
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, editFragmentInstance, EDIT_FRAGMENT_TAG)
                    .commit();
        } else {
            listFragment = listFragmentInstance;
        }


    }


}
