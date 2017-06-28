package com.arieldiax.codelab.fireblood.ui.navigation.search;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.arieldiax.codelab.fireblood.R;
import com.arieldiax.codelab.fireblood.utils.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class DonorViewHolder extends RecyclerView.ViewHolder {

    /**
     * Views of the holder.
     */
    private ImageView mDonorPhotoImageView;
    private TextView mDonorFirstNameTextView;
    private TextView mDonorLastNameTextView;
    private TextView mDonorUsernameTextView;
    private TextView mDonorCreatedAtTextView;

    /**
     * Instance of the RequestOptions class.
     */
    private RequestOptions mRequestOptions;

    /**
     * Creates a new DonorViewHolder object.
     */
    public DonorViewHolder(View itemView) {
        super(itemView);
        mDonorPhotoImageView = (ImageView) itemView.findViewById(R.id.donor_photo_image_view);
        mDonorFirstNameTextView = (TextView) itemView.findViewById(R.id.donor_first_name_text_view);
        mDonorLastNameTextView = (TextView) itemView.findViewById(R.id.donor_last_name_text_view);
        mDonorUsernameTextView = (TextView) itemView.findViewById(R.id.donor_username_text_view);
        mDonorCreatedAtTextView = (TextView) itemView.findViewById(R.id.donor_created_at_text_view);
        mRequestOptions = new RequestOptions()
                .placeholder(R.mipmap.ic_launcher)
                .circleCrop()
        ;
    }

    /**
     * Sets the photo URL of the donor.
     *
     * @param photoUrl Photo URL of the donor.
     */
    public void setDonorPhotoUrl(String photoUrl) {
        Glide
                .with(itemView.getContext())
                .load(photoUrl)
                .apply(mRequestOptions)
                .into(mDonorPhotoImageView)
        ;
    }

    /**
     * Sets the first name of the donor.
     *
     * @param firstName First name of the donor.
     */
    public void setDonorFirstName(String firstName) {
        mDonorFirstNameTextView.setText(firstName);
    }

    /**
     * Sets the last name of the donor.
     *
     * @param lastName Last name of the donor.
     */
    public void setDonorLastName(String lastName) {
        mDonorLastNameTextView.setText(lastName);
    }

    /**
     * Sets the username of the donor.
     *
     * @param username Username of the donor.
     */
    public void setDonorUsername(String username) {
        mDonorUsernameTextView.setText(username);
    }

    /**
     * Sets the create date of the donor.
     *
     * @param createdAt Create date of the donor.
     */
    public void setDonorCreateDate(long createdAt) {
        mDonorCreatedAtTextView.setText(Utils.formatDate(createdAt, "yyyy"));
    }
}
