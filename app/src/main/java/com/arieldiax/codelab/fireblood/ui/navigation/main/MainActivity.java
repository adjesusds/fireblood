package com.arieldiax.codelab.fireblood.ui.navigation.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.arieldiax.codelab.fireblood.R;
import com.arieldiax.codelab.fireblood.ui.launch.WelcomeActivity;
import com.arieldiax.codelab.fireblood.ui.navigation.profile.ProfileActivity;
import com.arieldiax.codelab.fireblood.ui.navigation.requests.RequestsActivity;
import com.arieldiax.codelab.fireblood.ui.navigation.search.SearchActivity;
import com.arieldiax.codelab.fireblood.utils.AnimationUtils;
import com.arieldiax.codelab.fireblood.utils.NavigationUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    /**
     * Views of the activity.
     */
    protected FrameLayout mMainFrameLayout;
    protected BottomNavigationView mMainBottomNavigationView;

    /**
     * Instance of the Toast class.
     */
    protected Toast mToast;

    /**
     * Instance of the DatabaseReference class.
     */
    protected DatabaseReference mDatabaseReference;

    /**
     * Instance of the FirebaseAuth class.
     */
    protected FirebaseAuth mFirebaseAuth;

    /**
     * Instance of the FirebaseUser class.
     */
    protected FirebaseUser mFirebaseUser;

    /**
     * Canonical name of the class.
     */
    protected String mClassCanonicalName;

    /**
     * Animations of the activity.
     */
    protected Animation mFadeInAnimation;
    protected Animation mFadeOutAnimation;

    /**
     * Whether or not the activity should be animated.
     */
    protected boolean shouldActivityAnimate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Initializes the user interface view bindings.
     */
    protected void initUi() {
        mMainFrameLayout = (FrameLayout) findViewById(R.id.main_frame_layout);
        mMainBottomNavigationView = (BottomNavigationView) findViewById(R.id.main_bottom_navigation_view);
    }

    /**
     * Initializes the back end logic bindings.
     */
    protected void init() {
        mToast = Toast.makeText(this, "", Toast.LENGTH_LONG);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mClassCanonicalName = "";
        mFadeInAnimation = AnimationUtils.getFadeInAnimation();
        mFadeOutAnimation = AnimationUtils.getFadeOutAnimation();
        shouldActivityAnimate = true;
    }

    /**
     * Initializes the event listener view bindings.
     */
    protected void initListeners() {
        mMainBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem navigationItem) {
                Class activityClass = null;
                switch (navigationItem.getItemId()) {
                    case R.id.requests_navigation_item:
                        activityClass = RequestsActivity.class;
                        break;
                    case R.id.search_navigation_item:
                        activityClass = SearchActivity.class;
                        break;
                    default:
                        activityClass = ProfileActivity.class;
                        break;
                }
                if (mClassCanonicalName.equals(activityClass.getCanonicalName())) {
                    return true;
                }
                shouldActivityAnimate = true;
                NavigationUtils.stackCustomActivity(MainActivity.this, activityClass, true);
                return false;
            }
        });
        mMainBottomNavigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {

            @Override
            public void onNavigationItemReselected(@NonNull MenuItem navigationItem) {
                onNavigationItemReselectedListener();
            }
        });
    }

    /**
     * Updates the user interface view bindings.
     */
    protected void updateUi() {
    }

    /**
     * Trigger for on navigation item reselected listener.
     */
    protected void onNavigationItemReselectedListener() {
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (shouldActivityAnimate) {
            mMainFrameLayout.startAnimation(mFadeInAnimation);
            shouldActivityAnimate = false;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (shouldActivityAnimate) {
            mMainFrameLayout.startAnimation(mFadeOutAnimation);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.sign_out_menu_item:
                signOutUser();
                return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    public void onBackPressed() {
        if (!NavigationUtils.isTaskRoot()) {
            NavigationUtils.unstackCustomActivity(this);
        } else {
            moveTaskToBack(true);
        }
    }

    /**
     * Signs out the user.
     */
    void signOutUser() {
        mFirebaseAuth.signOut();
        Intent activityIntent = new Intent(this, WelcomeActivity.class);
        activityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(activityIntent);
        NavigationUtils.clearClassesStack();
    }
}
