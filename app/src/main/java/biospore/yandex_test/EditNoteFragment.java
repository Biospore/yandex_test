package biospore.yandex_test;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EditNoteFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class EditNoteFragment extends Fragment {

    private OnFragmentInteractionListener mListener;


    Note note;
    EditText titleField;
    EditText textField;
    int position;
    private NoteDatabaseHelper db;
    private int menuId;
    private boolean editEnabled = false;

    public EditNoteFragment() {
        // Required empty public constructor
    }

    public static EditNoteFragment newInstance(long id, int position) {
        EditNoteFragment fragment = new EditNoteFragment();
        Bundle args = new Bundle();
        args.putLong(NoteListFragment.NOTE_ID, id);
        args.putInt(NoteListFragment.NOTE_POSITION, position);
        args.putInt(MainFragmentActivity.MENU_TYPE, MainFragmentActivity.EDIT_NOTE);
        fragment.setArguments(args);
        return fragment;
    }

    public static EditNoteFragment newInstance() {
        EditNoteFragment fragment = new EditNoteFragment();
        Bundle args = new Bundle();
        args.putInt(MainFragmentActivity.MENU_TYPE, MainFragmentActivity.ADD_NOTE);
        fragment.setArguments(args);
        return fragment;
    }

    public Note getNote() {
        return note;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new NoteDatabaseHelper(getActivity().getApplicationContext());
        Bundle bundle = this.getArguments();
        int menuType = bundle.getInt(MainFragmentActivity.MENU_TYPE);
        switch (menuType) {
            case MainFragmentActivity.ADD_NOTE:
                menuId = R.menu.add_note_activity_menu;
                note = new Note();
                editEnabled = true;
                break;
            case MainFragmentActivity.EDIT_NOTE:
                menuId = R.menu.show_and_edit_activity_menu;
                long nodeId = bundle.getLong(NoteListFragment.NOTE_ID);
                position = bundle.getInt(NoteListFragment.NOTE_POSITION);
                note = db.getNoteById(nodeId);
                editEnabled = false;
                break;
        }
    }

    public int getPosition() {
        return position;
    }


    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        MainFragmentActivity activity = ((MainFragmentActivity) getActivity());
        NoteListFragment listFragment = activity.listFragment;
        switch (item.getItemId()) {
            case R.id.button_delete:
                listFragment.deleteNoteFromAdapter(this.getPosition());
                this.deleteNote();
                getFragmentManager().popBackStack();
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, listFragment, MainFragmentActivity.LIST_FRAGMENT_TAG)
                        .commit();
                break;
            case R.id.button_edit:
                this.changeEditFlag();
                item.setOnMenuItemClickListener(this.getSaveAfterEditListener());
                Toolbar toolbar = (Toolbar) activity.findViewById(R.id.toolbar_test);
                if (toolbar != null) {
                    toolbar
                            .getMenu()
                            .findItem(R.id.button_delete)
                            .setVisible(false);
                }
                break;
            case R.id.button_save:
                this.addNote();
                getFragmentManager().popBackStack();
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, listFragment, MainFragmentActivity.LIST_FRAGMENT_TAG)
                        .commit();
                listFragment.addNoteToAdapter(this.getNote());

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_edit_note, container, false);
        if (!editEnabled) {
            ViewCompat.setTransitionName(view.findViewById(R.id.line_lay), String.valueOf(note.getId()));
        }
        return view;
    }


    public void deleteNote() {
        db.deleteNote(note);
        db.close();
    }

    public void addNote() {
        if (titleField != null && textField != null) {
            note = new Note(
                    titleField.getText().toString(),
                    textField.getText().toString());
            db.addNote(note);
            Log.i("ID", String.valueOf(note.getId()));
        } else {
            throw (new RuntimeException("Null title or text"));
        }
        db.close();
    }

    public void changeEditFlag() {
        textField.setEnabled(true);
        titleField.setEnabled(true);
    }

    public MenuItem.OnMenuItemClickListener getSaveAfterEditListener() {
        return new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                note.setTitle(titleField.getText().toString());
                note.setText(textField.getText().toString());
                db.updateNote(note);
                db.close();
                NoteListFragment listFragment = ((MainFragmentActivity) getActivity()).listFragment;
                getFragmentManager().popBackStack();
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, listFragment, MainFragmentActivity.LIST_FRAGMENT_TAG)
                        .commit();
                listFragment.updateNoteOnAdapter(note, position);
                return false;
            }
        };
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        titleField = (EditText) view.findViewById(R.id.title_field);
        textField = (EditText) view.findViewById(R.id.text_field);

        if (!editEnabled) {
            if (note.getTitle().isEmpty()) {
                titleField.setHint(R.string.empty_title);
            } else {
                titleField.setText(note.getTitle());
            }
            if (note.getText().isEmpty()) {
                textField.setHint(R.string.empty_text);
            } else {
                textField.setText(note.getText());
            }
        }
        textField.setEnabled(editEnabled);
        titleField.setEnabled(editEnabled);

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            mListener = new OnFragmentInteractionListener() {
                @Override
                public void onFragmentInteraction(Uri uri) {
                    Log.i("URI", uri.toString());

                }
            };
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(menuId, menu);


    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }
}
