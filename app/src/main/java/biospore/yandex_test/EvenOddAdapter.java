package biospore.yandex_test;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by hsxrjd on 13.07.16.
 */
public class EvenOddAdapter<T> extends BaseAdapter {
    /* TODO
    * Пока без синхронизации - для нее (обычно) используется Object
    * Нужно для функций обращающихся к данным
    * */
    private final LayoutInflater tInflater;
    private List<T> tObjects;
//    private Context tContext;
    private int tResource;
//    private int tDropDownResource;

    public EvenOddAdapter(Context context, @LayoutRes int resource, @NonNull List<T> objects) {
//        tContext = context;
        tInflater = LayoutInflater.from(context);
        tResource = resource;
        tObjects = objects;
    }

    public void add(T object) {
        tObjects.add(object);
        super.notifyDataSetChanged();
    }

    public void remove(T object) {
        tObjects.remove(object);
        super.notifyDataSetChanged();
    }

    public void insert(T object, int position) {
        tObjects.add(position, object);
        super.notifyDataSetChanged();
    }

    public void clear() {
        tObjects.clear();
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return tObjects.size();
    }

    @Override
    public T getItem(int position) {
        return tObjects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return createViewFromResource(tInflater, position, convertView, parent, tResource);
    }

    private View createViewFromResource(LayoutInflater inflater, int position, View convertView, ViewGroup parent, int resource) {
        View view;
        TextView text;
        T item = getItem(position);
        if (convertView == null) {
            view = inflater.inflate(resource, parent, false);
        } else {
            view = convertView;
        }
        text = (TextView) view;
        text.setText(item.toString());
        return null;
    }

    private static class BaseViewHolder {
        GridView gridView;
    }

}

