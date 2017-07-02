package com.arieldiax.codelab.fireblood.ui.navigation.search;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.arieldiax.codelab.fireblood.R;

import java.util.Random;

public class RequestBloodDialogFragment extends DialogFragment {

    /**
     * Properties of the fragment.
     */
    public static final String PROP_IN_PROVINCE = "province";
    public static final String PROP_IN_BLOOD_TYPE = "blood_type";
    public static final String PROP_IN_LOCATION = "location";

    /**
     * Values of the litres.
     */
    private static final float[] BLOOD_QUANTITY_VALUES_LITRES = {
            0.00f,
            0.25f,
            0.50f,
            0.75f,
            1.00f,
            1.25f,
            1.50f,
            1.75f,
            2.00f,
            2.25f,
            2.50f,
            2.75f,
            3.00f,
            3.25f,
            3.50f,
            3.75f,
            4.00f,
            4.25f,
            4.50f,
            4.75f,
            5.00f,
    };

    /**
     * Content view of the fragment.
     */
    View mContentView;

    /**
     * Views of the fragment.
     */
    TextView mBloodQuantityTextView;
    ProgressBar mRequestBloodProgressBar;
    SeekBar mBloodQuantitySeekBar;
    CheckBox mRequiresPlasmaCheckBox;
    CheckBox mRequiresPlateletsCheckBox;
    EditText mMessageEditText;
    Button mCancelButton;
    Button mRequestBloodButton;

    /**
     * Instance of the Toast class.
     */
    Toast mToast;

    /**
     * Touch state of the blood quantity seek bar.
     */
    boolean mBloodQuantitySeekBarTouchState;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, R.style.AppDialogTheme);
    }

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater,
            @Nullable ViewGroup container,
            Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.dialog_fragment_request_blood, container);
    }

    @Override
    public void onViewCreated(
            View view,
            @Nullable Bundle savedInstanceState
    ) {
        super.onViewCreated(view, savedInstanceState);
        mContentView = view;
        getDialog().getWindow().getAttributes().windowAnimations = R.style.AppDialogAnimation;
        initUi();
        init();
        initListeners();
        updateUi();
    }

    /**
     * Initializes the user interface view bindings.
     */
    void initUi() {
        mBloodQuantityTextView = (TextView) mContentView.findViewById(R.id.blood_quantity_text_view);
        mRequestBloodProgressBar = (ProgressBar) mContentView.findViewById(R.id.request_blood_progress_bar);
        mBloodQuantitySeekBar = (SeekBar) mContentView.findViewById(R.id.blood_quantity_seek_bar);
        mRequiresPlasmaCheckBox = (CheckBox) mContentView.findViewById(R.id.requires_plasma_check_box);
        mRequiresPlateletsCheckBox = (CheckBox) mContentView.findViewById(R.id.requires_platelets_check_box);
        mMessageEditText = (EditText) mContentView.findViewById(R.id.message_edit_text);
        mCancelButton = (Button) mContentView.findViewById(R.id.cancel_button);
        mRequestBloodButton = (Button) mContentView.findViewById(R.id.request_blood_button);
    }

    /**
     * Initializes the back end logic bindings.
     */
    void init() {
        mToast = Toast.makeText(getContext(), "", Toast.LENGTH_LONG);
        mBloodQuantitySeekBarTouchState = false;
    }

    /**
     * Initializes the event listener view bindings.
     */
    void initListeners() {
        mBloodQuantitySeekBar.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(
                    View view,
                    MotionEvent event
            ) {
                return mBloodQuantitySeekBarTouchState;
            }
        });
        mBloodQuantitySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(
                    SeekBar seekBar,
                    int progress,
                    boolean fromUser
            ) {
                mBloodQuantityTextView.setText(getString(R.string.request_information_blood_quantity, BLOOD_QUANTITY_VALUES_LITRES[progress], BLOOD_QUANTITY_VALUES_LITRES[getResources().getInteger(R.integer.blood_request_quantity_max_index)]));
                boolean enabled = (progress > 0);
                mRequestBloodButton.setEnabled(enabled);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        mCancelButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        mRequestBloodButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                attemptToRequestBlood();
            }
        });
    }

    /**
     * Updates the user interface view bindings.
     */
    void updateUi() {
        mBloodQuantityTextView.setText(getString(R.string.request_information_blood_quantity, BLOOD_QUANTITY_VALUES_LITRES[mBloodQuantitySeekBar.getProgress()], BLOOD_QUANTITY_VALUES_LITRES[getResources().getInteger(R.integer.blood_request_quantity_max_index)]));
    }

    @Override
    public void onStart() {
        super.onStart();
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    /**
     * Attempts to request the blood.
     */
    void attemptToRequestBlood() {
        toggleFragmentInteractionsState(false);
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                if (!(new Random()).nextBoolean()) {
                    toggleFragmentInteractionsState(true);
                    mToast.setText(R.string.message_an_error_has_occurred);
                    mToast.show();
                    return;
                }
                dismiss();
                mToast.setText(R.string.message_blood_successfully_requested);
                mToast.show();
            }
        }, DateUtils.SECOND_IN_MILLIS * 2);
    }

    /**
     * Toggles the fragment interactions state.
     *
     * @param intractable Whether or not the fragment should allow interactions.
     */
    void toggleFragmentInteractionsState(boolean intractable) {
        setCancelable(intractable);
        int visibility = (!intractable)
                ? View.VISIBLE
                : View.INVISIBLE;
        mRequestBloodProgressBar.setVisibility(visibility);
        mBloodQuantitySeekBarTouchState = !intractable;
        mRequiresPlasmaCheckBox.setClickable(intractable);
        mRequiresPlateletsCheckBox.setClickable(intractable);
        mMessageEditText.setEnabled(intractable);
        mCancelButton.setEnabled(intractable);
        mRequestBloodButton.setEnabled(intractable);
    }
}
