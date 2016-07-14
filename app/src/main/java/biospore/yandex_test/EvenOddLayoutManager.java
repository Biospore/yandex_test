package biospore.yandex_test;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Created by hsxrjd on 14.07.16.
 */
public class EvenOddLayoutManager extends RecyclerView.LayoutManager {
    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.MATCH_PARENT);
    }
}
