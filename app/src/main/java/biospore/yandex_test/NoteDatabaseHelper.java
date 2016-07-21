package biospore.yandex_test;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by biospore on 6/17/16.
 */
public class NoteDatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "notes_database";

    private static final String TABLE_NAME = "notes";

    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_TEXT = "text";


    public NoteDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "create table " + TABLE_NAME + "(" +
                KEY_ID + " integer primary key," +
                KEY_TITLE + " text," +
                KEY_TEXT + " text)";
        db.execSQL(createTable);
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_NAME);
        onCreate(db);
    }

    void addNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, note.getTitle());
        values.put(KEY_TEXT, note.getText());
        long id = db.insert(TABLE_NAME, null, values);
//        Log.i("DB id from insert", String.valueOf(id));
        note.setId(id);
        db.close();
    }

    Note getNoteById(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
//        Log.i("DB query id", String.valueOf(id));
        @SuppressLint("Recycle") Cursor cursor = db.query(
                TABLE_NAME,
                new String[]{
                        KEY_ID,
                        KEY_TITLE,
                        KEY_TEXT},
                KEY_ID + " =? ",
                new String[]{String.valueOf(id)},
                null,
                null,
                null,
                null
        );
        if (cursor != null) {
            cursor.moveToFirst();
            Note new_note = new Note(
                    Integer.parseInt(cursor.getString(0)),
                    cursor.getString(1),
                    cursor.getString(2)
            );
            cursor.close();
            db.close();
            return new_note;
        }
        db.close();
        return null;
    }

    ArrayList<Note> getAllNotes() {
        ArrayList<Note> notes = new ArrayList<Note>();
        String selectQuery = "select * from " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Note note = new Note();
                note.setId(Integer.parseInt(cursor.getString(0)));
                note.setTitle(cursor.getString(1));
                note.setText(cursor.getString(2));
                notes.add(note);
            } while (cursor.moveToNext());
            cursor.close();
        }
        db.close();
        return notes;
    }

    int updateNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, note.getTitle());
        values.put(KEY_TEXT, note.getText());
        return db.update(
                TABLE_NAME,
                values,
                KEY_ID + " =? ",
                new String[]{String.valueOf(note.getId())}
        );
    }

    void deleteNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(
                TABLE_NAME,
                KEY_ID + " =? ",
                new String[]{String.valueOf(note.getId())}
        );
        db.close();
    }
}
