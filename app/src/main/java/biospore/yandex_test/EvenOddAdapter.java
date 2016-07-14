package biospore.yandex_test;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;
import java.util.ListIterator;

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
    private int tResource;
    private int counter = 0;

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
        BaseViewItemHolder holder;


        if (convertView == null) {


            convertView = inflater.inflate(resource, parent, false);
            holder = new BaseViewItemHolder();
            holder.textViewItem = (TextView) convertView.findViewById(R.id.text_ya_test_1);
            holder.linearLayout = (LinearLayout) convertView.findViewById(R.id.linear_layout_ya_test_1);
//            Log.i("VH", "first");
            convertView.setTag(holder);
        } else {
//            Log.i("VH", "not first");
            holder = (BaseViewItemHolder) convertView.getTag();
        }
        T item = getItem(position);

        if (item != null) {


//            Log.i("VH", item.toString() + "\t" + position);
//            Log.i("VH", String.valueOf((position - 3) %4) + "\t" + position);
//            if ((position-3) %4 == 0)
//            {
//                return  convertView;
//            }
            holder.textViewItem.setText(item.toString());

        }
//        Log.i("VH", parent.getClass().toString());


        return convertView;
    }

    private static class BaseViewItemHolder {
        TextView textViewItem;
        LinearLayout linearLayout;
    }

}

