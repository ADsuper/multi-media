package io.github.adsuper.multi_media.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;

import java.util.List;

import io.github.adsuper.multi_media.R;
import io.github.adsuper.multi_media.common.Constant;
import io.github.adsuper.multi_media.common.Utils;
import io.github.adsuper.multi_media.image.ImageManager;
import io.github.adsuper.multi_media.model.ReadModel;

/**
 * 作者：luoshen/rsf411613593@gmail.com
 * 时间：2017年07月18日
 * 说明：ReadFragment RecycleView 对应的 adapter
 */

public class ReadFragmentAdapter extends RecyclerView.Adapter<ReadFragmentAdapter.ViewHolder> {
    private Context mContext;
    private List<ReadModel.NewslistEntity> mListData;
    private int mItemType;//条目布局类型
    private OnBaseClickListener mBaseClickListener;

    public interface OnBaseClickListener {
        void onClick(int position, ReadModel.NewslistEntity entity);

        void onCoverClick(int position, ReadModel.NewslistEntity entity);
    }

    public ReadFragmentAdapter(Context mContext, List<ReadModel.NewslistEntity> mListData, int mItemType) {
        this.mContext = mContext;
        this.mListData = mListData;
        this.mItemType = mItemType;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (mItemType == Constant.ITEM_TYPE_TEXT) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_homefragment, parent, false);
        } else {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_homefragment_girl, parent, false);
        }

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final ReadModel.NewslistEntity resultsEntity = mListData.get(position);
        if (mItemType == Constant.ITEM_TYPE_TEXT) {
            holder.tvTitle.setText(resultsEntity.getTitle());
            holder.tvTime.setText(TimeUtils.getFriendlyTimeSpanByNow(Utils.formatDateFromStr(resultsEntity.getCtime())));
            holder.tvAuthor.setText(resultsEntity.getDescription());
            if (resultsEntity.getPicUrl() != null) {
                ImageManager.getInstance().loadImage(mContext, resultsEntity.getPicUrl(), holder.ivCover);
            } else {
                ImageManager.getInstance().loadImage(mContext, R.drawable.notfound, holder.ivCover);
            }
            holder.ivCover.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (resultsEntity.getPicUrl() != null) {
                        mBaseClickListener.onCoverClick(position, resultsEntity);
                    } else {
                        ToastUtils.showShortSafe("木有发现图片哟");
                    }
                }
            });
        } else {
            ImageManager.getInstance().loadImage(mContext, resultsEntity.getUrl(), holder.ivGirl);
        }
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBaseClickListener.onClick(position, resultsEntity);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mListData.size();
    }

    public void setmListData(List<ReadModel.NewslistEntity> mListData) {
        this.mListData = mListData;
    }


    public void addOnClickListener(OnBaseClickListener baseClickListener) {
        this.mBaseClickListener = baseClickListener;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        View rootView;
        TextView tvTitle;
        TextView tvAuthor;
        TextView tvTime;
        ImageView ivCover;//封面缩率图
        ImageView ivGirl;

        public ViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvAuthor = (TextView) itemView.findViewById(R.id.tv_author);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            ivCover = (ImageView) itemView.findViewById(R.id.iv_cover);
            ivGirl = (ImageView) itemView.findViewById(R.id.iv_girl);
        }
    }
}
