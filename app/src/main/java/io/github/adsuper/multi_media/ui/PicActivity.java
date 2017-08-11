package io.github.adsuper.multi_media.ui;

import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;

import io.github.adsuper.multi_media.R;

public class PicActivity extends BaseActivity {



    @Override
    protected void initOperation(Intent intent) {

    }

    @Override
    protected View addChildContentView(LinearLayout rootLayout) {
        return null;
    }

    @Override
    protected String setToolbarTitle() {

        return "";
    }

    @Override
    protected void updateOptionsMenu(Menu menu) {

        menu.findItem(R.id.action_save).setVisible(false);
    }
}
