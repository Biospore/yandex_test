package biospore.yandex_test;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NoteListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class NoteListFragment extends Fragment implements CustomClickListener {

    private static String NOTES_BUNDLE_VALUE = "notes";
    private Point size;
    private WeakReference<EvenOddAdapter> tAdapter;

    NoteDatabaseHelper db;

    public static final String NOTE_ID = "biospore.yandex_test.NOTE_ID";
    public static final String NOTE_POSITION = "note_postion";

    private OnFragmentInteractionListener mListener;

    public NoteListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new NoteDatabaseHelper(getActivity().getApplicationContext());

        final EvenOddAdapter adapter = new EvenOddAdapter();
        adapter.setOnItemClickListener(new WeakReference<CustomClickListener>(this));
        tAdapter = new WeakReference<>(adapter);
        if (savedInstanceState != null) {
            NoteParcelStorage storage = (NoteParcelStorage) savedInstanceState.get(NOTES_BUNDLE_VALUE);
            if (storage != null) {
                fillAdapter(storage.getNotes());
            }
        } else {
            ArrayList<Note> notes = db.getAllNotes();
            fillAdapter(notes);
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.main_activity_menu, menu);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.button_add:
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, EditNoteFragment.newInstance(), MainFragmentActivity.EDIT_FRAGMENT_TAG)
                        .addToBackStack(MainFragmentActivity.EDIT_BACK_STACK_NAME)
                        .commit();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void fillAdapter(ArrayList<Note> notes) {
        EvenOddAdapter adapter = getViewAdapter();
        adapter.clear();
        for (Note note : notes) {
            if (note.getTitle().isEmpty())
                note.setTitle(getString(R.string.empty_title));
            adapter.add(note);
        }
    }


    public void addNoteToAdapter(Note note) {
        EvenOddAdapter adapter = getViewAdapter();
        if (note.getTitle().isEmpty())
            note.setTitle(getString(R.string.empty_title));
        adapter.add(note);
    }

    public void updateNoteOnAdapter(Note note, int position) {
        EvenOddAdapter adapter = getViewAdapter();
        if (note.getTitle().isEmpty()) {
            note.setTitle(getString(R.string.empty_title));
        }
        adapter.remove(adapter.getItem(position));
        adapter.insert(note, position);
    }

    public void deleteNoteFromAdapter(int position) {
        EvenOddAdapter adapter = getViewAdapter();
        adapter.remove(adapter.getItem(position));
    }


    private void initializeRecyclerView(View view, Bundle savedInstance) {


        RecyclerView mainView = (RecyclerView) view.findViewById(R.id.main_recycler_view);
        mainView.setAdapter(tAdapter.get());
        GridLayoutManager layoutManager = new GridLayoutManager(
                view.getContext(),
                2);
        layoutManager.setSpanSizeLookup(getSpanSize());
        mainView.setLayoutManager(layoutManager);


    }


    private GridLayoutManager.SpanSizeLookup getSpanSize() {
        final Display display = getActivity().getWindowManager().getDefaultDisplay();
        return new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
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
        };
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_note_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeRecyclerView(view, savedInstanceState);
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
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemClick(View v, int position) {
        long id = 0;
        if (tAdapter.get() != null) {
            id = tAdapter
                    .get()
                    .getItemId(position);
        }

        getFragmentManager()
                .beginTransaction()
                .addSharedElement(v, v.getTransitionName())

                .replace(R.id.fragment_container, EditNoteFragment.newInstance(id, position), MainFragmentActivity.EDIT_FRAGMENT_TAG)
                .addToBackStack(MainFragmentActivity.EDIT_BACK_STACK_NAME)
                .commit();
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

    public EvenOddAdapter getViewAdapter() {
        return tAdapter.get();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        EvenOddAdapter adapter = getViewAdapter();
        ArrayList<Note> notes = new ArrayList<Note>();
        int count = adapter.getItemCount();
        for (int i = 0; i < count; i++) {
            notes.add((Note) adapter.getItem(i));
        }
        NoteParcelStorage storage = new NoteParcelStorage(notes);
        outState.putParcelable(NOTES_BUNDLE_VALUE, storage);
    }
}
