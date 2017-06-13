package com.arieldiax.codelab.fireblood.ui.navigation.profile;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.arieldiax.codelab.fireblood.R;
import com.arieldiax.codelab.fireblood.models.firebase.User;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.HashMap;

public class EditProfileActivity extends AppCompatActivity {

    /**
     * Views of the activity.
     */
    ImageView mPhotoImageView;
    ImageView mEditProfileImageView;
    Spinner mProvinceSpinner;
    Spinner mBloodTypeSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        initUi();
        updateUi();
    }

    /**
     * Initializes the user interface view bindings.
     */
    void initUi() {
        mPhotoImageView = (ImageView) findViewById(R.id.photo_image_view);
        mEditProfileImageView = (ImageView) findViewById(R.id.edit_profile_image_view);
        mProvinceSpinner = (Spinner) findViewById(R.id.province_spinner);
        mBloodTypeSpinner = (Spinner) findViewById(R.id.blood_type_spinner);
    }

    /**
     * Updates the user interface view bindings.
     */
    void updateUi() {
        mPhotoImageView.setClipToOutline(true);
        HashMap<String, Object> user = (HashMap<String, Object>) getIntent().getSerializableExtra(User.CHILD_NODE);
        String userPhotoUrl = user.get(User.PROPERTY_PHOTO_URL).toString();
        if (!userPhotoUrl.isEmpty()) {
            RequestOptions requestOptions = new RequestOptions();
            requestOptions
                    .placeholder(R.mipmap.ic_launcher)
                    .circleCrop()
            ;
            Glide
                    .with(this)
                    .load(userPhotoUrl)
                    .apply(requestOptions)
                    .into(mPhotoImageView)
            ;
        }
        mEditProfileImageView.setClipToOutline(true);
        ArrayAdapter<CharSequence> provinceArrayAdapter = ArrayAdapter.createFromResource(this, R.array.array_provinces, android.R.layout.simple_spinner_item);
        provinceArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mProvinceSpinner.setAdapter(provinceArrayAdapter);
        ArrayAdapter<CharSequence> bloodTypeArrayAdapter = ArrayAdapter.createFromResource(this, R.array.array_blood_types, android.R.layout.simple_spinner_item);
        bloodTypeArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBloodTypeSpinner.setAdapter(bloodTypeArrayAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_profile, menu);
        return true;
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
