package com.molaja.android.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.molaja.android.R;

/**
 * Created by florianhidayat on 6/7/15.
 */
public class ShareActivity extends BaseActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        initToolbar();

        findViewById(R.id.share_btn).setOnClickListener(this);
        findViewById(R.id.cancel_btn).setOnClickListener(this);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.share_to_your_friends));
        toolbar.setTitleTextColor(R.color.DarkTeal);

        //toolbar.setNavigationIcon(R.drawable.ic_action_back);

        toolbar.inflateMenu(R.menu.menu_share);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_share_other_app:
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel_btn:
                onBackPressed();
                return;
            case R.id.share_btn:

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //overridePendingTransition(0, R.anim.fade_out);
    }
}
