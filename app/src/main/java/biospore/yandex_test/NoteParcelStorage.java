package biospore.yandex_test;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by hsxrjd on 12.07.16.
 */
public class NoteParcelStorage implements Parcelable {
    public ArrayList<Note> getNotes() {
        return notes;
    }

    private ArrayList<Note> notes;

    public NoteParcelStorage(ArrayList<Note> in) {
        notes = in;
    }

    protected NoteParcelStorage(Parcel in) {
        notes = in.readArrayList(Note.class.getClassLoader());
    }

    public static final Creator<NoteParcelStorage> CREATOR = new Creator<NoteParcelStorage>() {
        @Override
        public NoteParcelStorage createFromParcel(Parcel in) {
            return new NoteParcelStorage(in);
        }

        @Override
        public NoteParcelStorage[] newArray(int size) {
            return new NoteParcelStorage[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(notes);
    }
}
