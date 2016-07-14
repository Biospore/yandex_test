package biospore.yandex_test;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by hsxrjd on 13.07.16.
 */
//public class EvenOddAdapter<T> extends BaseAdapter {
public class EvenOddAdapter<T> extends RecyclerView.Adapter {
    /* TODO
    * Пока без синхронизации - для нее (обычно) используется Object
    * Нужно для функций обращающихся к данным
    * */
//    private final LayoutInflater tInflater;
    private List<T> tObjects;

//    private int tResource;

    public EvenOddAdapter(@NonNull List<T> objects) {
//        tContext = context;
//        tInflater = LayoutInflater.from(context);
//        tResource = resource;
        tObjects = objects;
//        listener = itemListener;
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

    //    @Override
//    public int getCount() {
//        return tObjects.size();
//    }
//
//    @Override
    public T getItem(int position) {
        return tObjects.get(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.yandex_test_list_item_0, parent, false);
        return new EvenOddViewHolder(item);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//        T item = tObjects.get(position);
        ((EvenOddViewHolder) holder).textViewItem.setText(tObjects.get(position).toString());

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return tObjects.size();
    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        return createViewFromResource(tInflater, position, convertView, parent, tResource);
//    }

//    private View createViewFromResource(LayoutInflater inflater, int position, View convertView, ViewGroup parent, int resource) {
//        EvenOddViewHolder holder;
//
//
//        if (convertView == null) {
//
//
//            convertView = inflater.inflate(resource, parent, false);
//            holder = new EvenOddViewHolder();
//            holder.textViewItem = (TextView) convertView.findViewById(R.id.text_ya_test_1);
//            holder.linearLayout = (LinearLayout) convertView.findViewById(R.id.linear_layout_ya_test_1);
////            Log.i("VH", "first");
//            convertView.setTag(holder);
//        } else {
////            Log.i("VH", "not first");
//            holder = (EvenOddViewHolder) convertView.getTag();
//        }
////        T item = getItem(position);
//
//        if (item != null) {
//
//
////            Log.i("VH", item.toString() + "\t" + position);
////            Log.i("VH", String.valueOf((position - 3) %4) + "\t" + position);
////            if ((position-3) %4 == 0)
////            {
////                return  convertView;
////            }
//            holder.textViewItem.setText(item.toString());
//
//        }
////        Log.i("VH", parent.getClass().toString());
//
//
//        return convertView;
//    }

    private class EvenOddViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView textViewItem;


        public EvenOddViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            textViewItem = (TextView) itemView.findViewById(R.id.text_ya_test_0);
        }

        @Override
        public void onClick(View v) {
//            Log.i("GGG", v.toString() + "\t" + String.valueOf(getPosition()) + getItem(getPosition()).toString());

        }
    }

}

