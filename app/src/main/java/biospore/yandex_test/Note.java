package biospore.yandex_test;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.ContextThemeWrapper;

/**
 * Created by biospore on 6/17/16.
 */
public class Note {
    
    private int id;
    private String title;
    private String text;

    public Note(){}

    public Note(int id, String title, String text)
    {
        this.id = id;
        this.title = title;
        this.text = text;
    }

    public Note(String title, String text)
    {
        this.title = title;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return title;
    }
}
