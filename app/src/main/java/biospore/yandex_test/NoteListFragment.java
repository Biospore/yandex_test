package biospore.yandex_test;

import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Explode;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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

    private RecyclerView mainView;
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
//        Transition transition = new Explode();
//        transition.addTarget(getString(R.string.transition_list_element));
//        setExitTransition(transition);
//        setSharedElementReturnTransition(transition);
//        setSharedElementEnterTransition(transition);
//        setEnterTransition(transition);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
//        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_menu, menu);
//        return true;
    }

    /*
    @Override
    public void onStart() {
        super.onStart();
        Log.i("START", "sss");
    }*/

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void fillAdapter(ArrayList<Note> notes) {
        EvenOddAdapter adapter = getViewAdapter();
        adapter.clear();
//        MainActivity.notes.clear();
        for (Note note : notes) {
            if (note.getTitle().isEmpty())
                note.setTitle(getString(R.string.empty_title));
            adapter.add(note);
        }
    }


    private void initializeRecyclerView(View view, Bundle savedInstance) {

        db = new NoteDatabaseHelper(view.getContext());
        mainView = (RecyclerView) view.findViewById(R.id.main_recycler_view);
//        Log.i("REC", view.toString());
        final EvenOddAdapter adapter = new EvenOddAdapter();
        tAdapter = new WeakReference<EvenOddAdapter>(adapter);
        adapter.setOnItemClickListener(new WeakReference<CustomClickListener>(this));
        mainView.setAdapter(adapter);
        GridLayoutManager layoutManager = new GridLayoutManager(
                view.getContext(),
                2);
        layoutManager.setSpanSizeLookup(getSpanSize());
        mainView.setLayoutManager(layoutManager);

        if (savedInstance != null) {
            NoteParcelStorage storage = (NoteParcelStorage) savedInstance.get(NOTES_BUNDLE_VALUE);
            if (storage != null) {
                fillAdapter(storage.getNotes());
            }
        } else {
            ArrayList<Note> notes = db.getAllNotes();
            fillAdapter(notes);
        }
    }


    private GridLayoutManager.SpanSizeLookup getSpanSize() {
//        activity = new WeakReference<>((MainFragmentActivity) getActivity());
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
        // Inflate the layout for this fragment
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
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemClick(View v, int position) {

        EditNoteFragment editFragment = new EditNoteFragment();
        Bundle bundle = new Bundle();
        long id = 0;
        if (tAdapter.get() != null) {
            id = tAdapter
                    .get()
                    .getItemId(position);
        }

        bundle.putLong(NOTE_ID, id);
        bundle.putInt(NOTE_POSITION, position);
        bundle.putInt(MainFragmentActivity.MENU_TYPE, MainFragmentActivity.EDIT_NOTE);
        editFragment.setArguments(bundle);
        Transition transition = new Slide();
//        Transition transition1 = TransitionInflater.from(v.getContext()).inflateTransition(android.R.transition.explode);
//        v.setTransitionName(String.valueOf(position));
        transition.addTarget(v.getTransitionName());
//        transition.addTarget(getString(R.string.transition_list_element));
//        editFragment.setSharedElementEnterTransition(transition1);

//        editFragment.setEnterTransition(new Explode());
        editFragment.setSharedElementEnterTransition(transition);
        editFragment.setAllowEnterTransitionOverlap(true);
//        setExitTransition(new Explode());

//        editFragment.setReenterTransition(transition1);
//        Log.i("VIEW", v.toString());
        Log.i("fff", v.getTransitionName());
        getFragmentManager()
                .beginTransaction()
                .addSharedElement(v, v.getTransitionName())
                .replace(R.id.fragment_container, editFragment, MainFragmentActivity.EDIT_FRAGMENT_TAG)
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

    private EvenOddAdapter getViewAdapter() {
        return (EvenOddAdapter) mainView.getAdapter();
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
