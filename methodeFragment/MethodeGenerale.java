package com.cedricakrou.covoiturage.general;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

/**
 * Created by cedricakrou on 04/04/19.
 */

public class MethodeGenerale
{

    /**
     *  Methode pour la navigation entre les fragmentes
     * @param fragment
     * @param context
     * @param idFrameLayout
     */

    public static void setFragment(Fragment fragment, Context context, int idFrameLayout)
    {
        FragmentTransaction fragmentTransaction = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();

        fragmentTransaction.replace(idFrameLayout, fragment, "");

        fragmentTransaction.commit();
    }

}
