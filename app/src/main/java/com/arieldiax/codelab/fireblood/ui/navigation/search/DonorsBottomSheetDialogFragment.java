package com.arieldiax.codelab.fireblood.ui.navigation.search;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.arieldiax.codelab.fireblood.R;
import com.arieldiax.codelab.fireblood.models.firebase.Donor;
import com.arieldiax.codelab.fireblood.models.firebase.User;
import com.arieldiax.codelab.fireblood.ui.navigation.profile.ProfileActivity;
import com.arieldiax.codelab.fireblood.utils.FirebaseUtils;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class DonorsBottomSheetDialogFragment extends BottomSheetDialogFragment {

    /**
     * Properties of the fragment.
     */
    public static final String PROP_IN_PROVINCE = "province";
    public static final String PROP_IN_BLOOD_TYPE = "blood_type";
    public static final String PROP_IN_LOCATION = "location";

    /**
     * Content view of the fragment.
     */
    View mContentView;

    /**
     * Views of the fragment.
     */
    Toolbar mDonorsToolbar;
    ProgressBar mDonorsProgressBar;
    TextView mRequestBloodTextView;
    RecyclerView mDonorsRecyclerView;
    TextView mDonorsEmptyRecyclerView;

    /**
     * Instance of the DatabaseReference class.
     */
    DatabaseReference mDatabaseReference;

    /**
     * Firebase recycler adapter of the donors.
     */
    FirebaseRecyclerAdapter mDonorsFirebaseRecyclerAdapter;

    @Override
    public void setupDialog(
            Dialog dialog,
            int style
    ) {
        // noinspection RestrictedApi
        super.setupDialog(dialog, style);
        mContentView = View.inflate(getContext(), R.layout.bottom_sheet_dialog_fragment_donors, null);
        dialog.setContentView(mContentView);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        initUi();
        init();
        initListeners();
        updateUi();
    }

    /**
     * Initializes the user interface view bindings.
     */
    void initUi() {
        mDonorsToolbar = (Toolbar) mContentView.findViewById(R.id.donors_toolbar);
        mDonorsProgressBar = (ProgressBar) mContentView.findViewById(R.id.donors_progress_bar);
        mRequestBloodTextView = (TextView) mContentView.findViewById(R.id.request_blood_text_view);
        mDonorsRecyclerView = (RecyclerView) mContentView.findViewById(R.id.donors_recycler_view);
        mDonorsEmptyRecyclerView = (TextView) mContentView.findViewById(R.id.donors_empty_recycler_view);
    }

    /**
     * Initializes the back end logic bindings.
     */
    void init() {
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mDonorsFirebaseRecyclerAdapter = null;
    }

    /**
     * Initializes the event listener view bindings.
     */
    void initListeners() {
        mDonorsToolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        final String argumentProvince = getArguments().getString(PROP_IN_PROVINCE);
        final String argumentBloodType = getArguments().getString(PROP_IN_BLOOD_TYPE);
        final String argumentLocation = getArguments().getString(PROP_IN_LOCATION);
        mRequestBloodTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                RequestBloodDialogFragment requestBloodDialogFragment = new RequestBloodDialogFragment();
                Bundle arguments = new Bundle();
                arguments.putString(RequestBloodDialogFragment.PROP_IN_PROVINCE, argumentProvince);
                arguments.putString(RequestBloodDialogFragment.PROP_IN_BLOOD_TYPE, argumentBloodType);
                arguments.putString(RequestBloodDialogFragment.PROP_IN_LOCATION, argumentLocation);
                requestBloodDialogFragment.setArguments(arguments);
                requestBloodDialogFragment.show(getActivity().getSupportFragmentManager(), requestBloodDialogFragment.getTag());
            }
        });
        String donorsDatabasePath = FirebaseUtils.normalizePath(Donor
                .sDatabasePath
                .replace(Donor.PATH_SEGMENT_PROVINCE, argumentProvince)
                .replace(Donor.PATH_SEGMENT_BLOOD_TYPE, argumentBloodType)
                .replace(Donor.PATH_SEGMENT_LOCATION, argumentLocation));
        Query donorsQuery = mDatabaseReference
                .child(donorsDatabasePath)
                .orderByChild(User.PROPERTY_FULL_NAME);
        // TODO: Implement infinite scroll, once the feature gets released as part of `master`.
        // @see https://github.com/firebase/FirebaseUI-Android/pull/26
        mDonorsFirebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Donor, DonorViewHolder>(Donor.class, R.layout.list_item_donor, DonorViewHolder.class, donorsQuery) {

            @Override
            protected void populateViewHolder(
                    DonorViewHolder viewHolder,
                    Donor donor,
                    final int position
            ) {
                viewHolder.setDonorPhotoUrl(donor.photoUrl);
                viewHolder.setDonorFirstName(donor.firstName);
                viewHolder.setDonorLastName(donor.lastName);
                viewHolder.setDonorUsername(donor.username);
                viewHolder.setDonorCreateDate(donor.createdAt);
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        Intent activityIntent = new Intent(getContext(), ProfileActivity.class);
                        activityIntent.putExtra(ProfileActivity.PROP_IN_USER_UID, getRef(position).getKey());
                        startActivity(activityIntent);
                    }
                });
            }

            @Override
            public void onDataChanged() {
                super.onDataChanged();
                int visibility = (mDonorsFirebaseRecyclerAdapter.getItemCount() > 0)
                        ? View.GONE
                        : View.VISIBLE;
                int inverseVisibility = (mDonorsFirebaseRecyclerAdapter.getItemCount() == 0)
                        ? View.GONE
                        : View.VISIBLE;
                mDonorsProgressBar.setVisibility(visibility);
                mRequestBloodTextView.setVisibility(inverseVisibility);
                mDonorsEmptyRecyclerView.setVisibility(visibility);
            }
        };
    }

    /**
     * Updates the user interface view bindings.
     */
    void updateUi() {
        mDonorsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mDonorsRecyclerView.setAdapter(mDonorsFirebaseRecyclerAdapter);
        mDonorsRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDonorsFirebaseRecyclerAdapter.cleanup();
    }
}
