package io.github.adsuper.multi_media.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 *自定义 recycleView，方便在无网络或无数据的时候显示空白 view
 */
public class EmptyRecyclerView extends RecyclerView {
    private View mEmptyView;
    private Context mContext;

    public EmptyRecyclerView(Context context) {
        super(context);
        this.mContext = context;
    }

    public EmptyRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    public EmptyRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
    }

    /**
     * 根据数据源判断是否显示空白view
     */
    private void checkIfEmpty() {

        if (mEmptyView != null || getAdapter() != null) {
            mEmptyView.setVisibility(getAdapter().getItemCount() > 0 ? GONE : VISIBLE);
        }
    }

    public void setmEmptyView(View mEmptyView) {
        this.mEmptyView = mEmptyView;
        checkIfEmpty();
    }

    public void hideEmptyView() {
        if (mEmptyView.getVisibility() == VISIBLE) {
            mEmptyView.setVisibility(GONE);
        }
    }

    @Override
    public void setAdapter(Adapter adapter) {
        Adapter adapterOld = getAdapter();
        if (adapterOld != null) {
            adapterOld.unregisterAdapterDataObserver(observer);
        }
        super.setAdapter(adapter);
        if (adapter != null) {
            adapter.registerAdapterDataObserver(observer);
        }
    }

    AdapterDataObserver observer = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            checkIfEmpty();
        }
    };
}
