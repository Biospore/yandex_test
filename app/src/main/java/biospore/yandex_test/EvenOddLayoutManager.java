package biospore.yandex_test;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by hsxrjd on 14.07.16.
 */
public class EvenOddLayoutManager extends GridLayoutManager {


    public EvenOddLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {

        super.onLayoutChildren(recycler, state);
    }

    @Override
    public void setSpanSizeLookup(SpanSizeLookup spanSizeLookup) {
        super.setSpanSizeLookup(spanSizeLookup);
    }

}
