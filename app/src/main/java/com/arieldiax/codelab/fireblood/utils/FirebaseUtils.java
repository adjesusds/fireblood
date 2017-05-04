package com.arieldiax.codelab.fireblood.utils;

import com.google.firebase.database.DataSnapshot;

public final class FirebaseUtils {

    /**
     * Creates a new FirebaseUtils object (no, it won't).
     */
    private FirebaseUtils() {
        // Required empty private constructor (to prevent instantiation).
    }

    /**
     * Gets the child of the data snapshot.
     *
     * @param dataSnapshot Instance of the DataSnapshot class.
     * @return The child of the data snapshot.
     */
    public static DataSnapshot getDataSnapshotChild(DataSnapshot dataSnapshot) {
        if (!dataSnapshot.hasChildren()) {
            return dataSnapshot;
        }
        for (DataSnapshot datumSnapshot : dataSnapshot.getChildren()) {
            return datumSnapshot;
        }
        return dataSnapshot;
    }
}
