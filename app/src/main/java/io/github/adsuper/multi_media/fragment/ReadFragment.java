package io.github.adsuper.multi_media.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * 作者：luoshen/rsf411613593@gmail.com
 * 时间：2017年07月17日
 * 说明：底部导航 read 对应的 fragment
 */

public class ReadFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        TextView textView = new TextView(getActivity());
        textView.setText("ReadFragment");
        return textView;
    }
}
