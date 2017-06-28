package com.arieldiax.codelab.fireblood.ui.navigation.search;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.arieldiax.codelab.fireblood.R;
import com.arieldiax.codelab.fireblood.models.firebase.Donor;
import com.arieldiax.codelab.fireblood.models.firebase.User;
import com.arieldiax.codelab.fireblood.models.widgets.ConfirmBottomSheetDialog;
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
     * Instance of the Toast class.
     */
    Toast mToast;

    /**
     * Instance of the ConfirmBottomSheetDialog class.
     */
    ConfirmBottomSheetDialog mConfirmBottomSheetDialog;

    /**
     * Instance of the ProgressDialog class.
     */
    ProgressDialog mProgressDialog;

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
        mToast = Toast.makeText(getContext(), "", Toast.LENGTH_LONG);
        mConfirmBottomSheetDialog = new ConfirmBottomSheetDialog(getActivity());
        mProgressDialog = new ProgressDialog(getContext(), R.style.AppProgressDialogTheme);
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
        mRequestBloodTextView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                mConfirmBottomSheetDialog.show();
            }
        });
        String donorsDatabasePath = FirebaseUtils.normalizePath(Donor
                .sDatabasePath
                .replace(Donor.PATH_SEGMENT_PROVINCE, getArguments().getString(PROP_IN_PROVINCE))
                .replace(Donor.PATH_SEGMENT_BLOOD_TYPE, getArguments().getString(PROP_IN_BLOOD_TYPE))
                .replace(Donor.PATH_SEGMENT_LOCATION, getArguments().getString(PROP_IN_LOCATION)));
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
        View.OnClickListener positiveButtonListener = new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                mConfirmBottomSheetDialog.dismiss();
                attemptToRequestBlood();
            }
        };
        mConfirmBottomSheetDialog
                .setTitle(R.string.title_request_blood)
                .setMessage(R.string.message_are_you_sure)
                .setPositiveButtonListener(positiveButtonListener)
        ;
        mProgressDialog.setTitle(R.string.title_requesting_blood);
        mProgressDialog.setMessage(getString(R.string.message_please_wait_a_few_seconds));
        mProgressDialog.setCancelable(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDonorsFirebaseRecyclerAdapter.cleanup();
    }

    /**
     * Attempts to request the blood.
     */
    void attemptToRequestBlood() {
        mProgressDialog.show();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                mProgressDialog.dismiss();
                mToast.setText(R.string.message_blood_successfully_requested);
                mToast.show();
            }
        }, DateUtils.SECOND_IN_MILLIS * 2);
    }
}
