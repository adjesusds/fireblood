package com.arieldiax.codelab.fireblood.ui;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.arieldiax.codelab.fireblood.R;

public class ProfileActivity extends MainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.fragment_profile, (FrameLayout) findViewById(R.id.main_frame_layout));
        initUi();
        init();
        initListeners();
        updateUi();
    }

    @Override
    protected void init() {
        super.init();
        mClassCanonicalName = getClass().getCanonicalName();
    }

    @Override
    protected void updateUi() {
        super.updateUi();
        mMainBottomNavigationView.setSelectedItemId(R.id.profile_navigation_item);
    }
}
