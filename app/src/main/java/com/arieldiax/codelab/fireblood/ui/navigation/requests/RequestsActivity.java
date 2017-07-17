package com.arieldiax.codelab.fireblood.ui.navigation.requests;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.arieldiax.codelab.fireblood.R;
import com.arieldiax.codelab.fireblood.ui.navigation.main.MainActivity;

public class RequestsActivity extends MainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.fragment_requests, (FrameLayout) findViewById(R.id.main_frame_layout));
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
        mMainBottomNavigationView.setSelectedItemId(R.id.requests_navigation_item);
    }
}
