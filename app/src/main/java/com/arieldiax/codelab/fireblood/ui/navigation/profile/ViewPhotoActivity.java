package com.arieldiax.codelab.fireblood.ui.navigation.profile;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;

import com.arieldiax.codelab.fireblood.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class ViewPhotoActivity extends AppCompatActivity {

    /**
     * Properties of the activity.
     */
    public static final String PROP_IN_USER_PHOTO_URL = "user_photo_url";

    /**
     * Views of the activity.
     */
    ImageView mUserPhotoImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_photo);
        initUi();
        updateUi();
    }

    /**
     * Initializes the user interface view bindings.
     */
    void initUi() {
        mUserPhotoImageView = (ImageView) findViewById(R.id.user_photo_image_view);
    }

    /**
     * Updates the user interface view bindings.
     */
    void updateUi() {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.circleCrop();
        Glide
                .with(this)
                .load(getIntent().getStringExtra(PROP_IN_USER_PHOTO_URL))
                .apply(requestOptions)
                .into(mUserPhotoImageView)
        ;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                finishAfterTransition();
                return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
