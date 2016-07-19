package biospore.yandex_test;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.transition.Explode;
import android.transition.Transition;
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


    public static final int DELETE = 1;
    public static final int CHANGED = 2;
    public static final String NOTE_CHANGE = "change_note";
    public static final String NOTE_CHANGE_POSITION = "changed_note_position";
    public static final String NOTE_DELETED = "delete_note";
    Intent intent;
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new NoteDatabaseHelper(getActivity());
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
//                Log.i("ID RECEIVED", String.valueOf(nodeId));
                note = db.getNoteById(nodeId);
                editEnabled = false;
                break;
        }
//        Transition transition = new Explode();
//        transition.addTarget(getString(R.string.transition_list_element));
//        setSharedElementEnterTransition(transition);
//        setExitTransition(transition);
//        setSharedElementReturnTransition(transition);
//        setEnterTransition(transition);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_edit_note, container, false);
//        view.findViewById(R.id.line_lay).setTransitionName(String.valueOf(note.getId()));
        ViewCompat.setTransitionName(view.findViewById(R.id.line_lay), String.valueOf(note.getId()));
        Log.i("GG", view.findViewById(R.id.line_lay).getTransitionName());
        return view;
    }


    public void deleteNote() {
        Intent result = new Intent();
        result.putExtra(NOTE_DELETED, position);
//        setResult(DELETE, result);
        db.deleteNote(note);
        db.close();
//        finish();
    }

    public void addNote() {
        if (titleField != null && textField != null) {
            Note new_note = new Note(
                    titleField.getText().toString(),
                    textField.getText().toString());
            db.addNote(new_note);

            Intent result = new Intent();
//            result.putExtra(NEW_NOTE, new_note);
//            setResult(OK, result);

        } else {
//            setResult(ERROR);
            throw (new RuntimeException("Null title or text"));
        }
        db.close();
//        finishAfterTransition();
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
                Intent result = new Intent();
                result.putExtra(NOTE_CHANGE, note);
                result.putExtra(NOTE_CHANGE_POSITION, position);
//                setResult(CHANGED, result);
                db.updateNote(note);
//                Log.i("FF", "fff");


                db.close();
//                getFragmentManager()
//                        .beginTransaction()
//                        .hide(getActivity()
//                                .getFragmentManager()
//                                .findFragmentByTag(MainFragmentActivity.EDIT_FRAGMENT_TAG))
//                        .commit();
                getFragmentManager()
                        .beginTransaction()
//                        .addSharedElement(getView(), getString(R.string.transition_list_element))
//                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .replace(R.id.fragment_container, new NoteListFragment(), MainFragmentActivity.LIST_FRAGMENT_TAG)
                        .commit();
//                finishAfterTransition();
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
//            view.findViewById(R.id.line_lay).setTransitionName(String.valueOf(position));
//            Log.i("hhh", String.valueOf(position))
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
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
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
}
