package biospore.yandex_test;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;

/**
 * Created by hsxrjd on 14.07.16.
 */
public class TestClickListener implements RecyclerView.OnItemTouchListener {
    AdapterView.OnItemClickListener tListener;
    GestureDetector tGestureDetector;

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public TestClickListener(Context context, AdapterView.OnItemClickListener listener) {
        tListener = listener;
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        View child = rv.findChildViewUnder(e.getX(), e.getY());
        if (child != null && tListener != null)
            return false;

//            tListener.onItemClick(child, rv.getChildAdapterPosition(child));
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}
