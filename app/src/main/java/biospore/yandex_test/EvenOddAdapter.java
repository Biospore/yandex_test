package biospore.yandex_test;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hsxrjd on 13.07.16.
 */
public class EvenOddAdapter<T> extends RecyclerView.Adapter<EvenOddAdapter.EvenOddViewHolder> {

    private List<T> tObjects;
    private WeakReference<CustomClickListener> tListener;

    public EvenOddAdapter() {
        tObjects = new ArrayList<>();
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

    public T getItem(int position) {
        return tObjects.get(position);
    }

    public void setOnItemClickListener(WeakReference<CustomClickListener> listener) {
        tListener = listener;
    }

    @Override
    public EvenOddViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.yandex_test_list_item_0, parent, false);
        EvenOddViewHolder holder = new EvenOddViewHolder(item);
        holder.setOnItemClickListener(tListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(EvenOddViewHolder holder, int position) {
        holder.textViewItem.setText(tObjects.get(position).toString());
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return tObjects.size();
    }

    public static class EvenOddViewHolder extends RecyclerView.ViewHolder {
        TextView textViewItem;
        WeakReference<CustomClickListener> tListener;

        public void setOnItemClickListener(WeakReference<CustomClickListener> listener) {
            tListener = listener;
        }

        public EvenOddViewHolder(final View itemView) {
            super(itemView);
            textViewItem = (TextView) itemView.findViewById(R.id.text_ya_test_0);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CustomClickListener listener = tListener.get();
                    if (listener != null) {
                        listener.onItemClick(v, getAdapterPosition());
                    } else {
                        throw new RuntimeException("Listener is null!");
                    }
                }
            });
        }
    }
}

