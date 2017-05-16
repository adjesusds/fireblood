package com.arieldiax.codelab.fireblood.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.arieldiax.codelab.fireblood.R;
import com.arieldiax.codelab.fireblood.utils.NavigationUtils;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    /**
     * Views of the activity.
     */
    BottomNavigationView mMainBottomNavigationView;

    /**
     * Instance of the FirebaseAuth class.
     */
    FirebaseAuth mFirebaseAuth;

    /**
     * Canonical name of the class.
     */
    String mClassCanonicalName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Initializes the user interface view bindings.
     */
    protected void initUi() {
        mMainBottomNavigationView = (BottomNavigationView) findViewById(R.id.main_bottom_navigation_view);
    }

    /**
     * Initializes the back end logic bindings.
     */
    protected void init() {
        mFirebaseAuth = FirebaseAuth.getInstance();
        mClassCanonicalName = "";
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
                    case R.id.notifications_navigation_item:
                        activityClass = NotificationsActivity.class;
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
                NavigationUtils.stackCustomActivity(MainActivity.this, activityClass, true);
                return false;
            }
        });
    }

    /**
     * Updates the user interface view bindings.
     */
    protected void updateUi() {
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
