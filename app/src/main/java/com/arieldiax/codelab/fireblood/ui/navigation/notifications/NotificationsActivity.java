package com.arieldiax.codelab.fireblood.ui.navigation.notifications;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.arieldiax.codelab.fireblood.R;
import com.arieldiax.codelab.fireblood.ui.navigation.main.MainActivity;

public class NotificationsActivity extends MainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.fragment_notifications, (FrameLayout) findViewById(R.id.main_frame_layout));
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
        mMainBottomNavigationView.setSelectedItemId(R.id.notifications_navigation_item);
    }
}
