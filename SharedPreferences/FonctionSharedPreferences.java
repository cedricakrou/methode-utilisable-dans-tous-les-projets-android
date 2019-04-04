package com.cedricakrou.covoiturage.general;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

/**
 * Created by cedricakrou on 02/04/19.
 */

public class FonctionSharedPreferences
{
    public static final String sharedPreferencesUtilisateur = "sharedPreferencesUtilisateur";
    public static final String sharedPreferencesRememberMe = "sharedPreferencesRememberMe";


    /**
     *  Fonction d'enregsiter d'un user dans le shared preferences
     * @param user
     * @param context
     */

    public static void EnregistrerSharedPreferences(Object user, Context context)
    {
        // sauvegarde de l'utilisateur

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences( context );
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();

        String json = gson.toJson(user);
        editor.putString(sharedPreferencesUtilisateur, json);

        editor.apply();

    }

    public static Object RecupererSharedPreferences( Class classe , Context context)
    {

        // recuperation de l'utilisateur

        SharedPreferences sharedPreferences =  PreferenceManager.getDefaultSharedPreferences(context);

        Gson gson = new Gson();
        String json = sharedPreferences.getString(FonctionSharedPreferences.sharedPreferencesUtilisateur, null);

        return gson.fromJson(json, classe);

    }

}
