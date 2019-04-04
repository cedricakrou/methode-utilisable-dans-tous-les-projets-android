package com.cergi.cantiqueemuci.general;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.cergi.cantiqueemuci.R;
import com.cergi.cantiqueemuci.vue.MenuPrincipale;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;
import org.xmlpull.v1.XmlPullParserException;

import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by cedricakrou on 25/03/19.
 */

public class MethodeGenerale
{

    /**
     *  Methode pour justifier les textes
     * @param textView
     */

    public static void justify(final TextView textView) {

        final AtomicBoolean isJustify = new AtomicBoolean(false);

        final String textString = textView.getText().toString();

        final TextPaint textPaint = textView.getPaint();

        final SpannableStringBuilder builder = new SpannableStringBuilder();

        textView.post(new Runnable() {
            @Override
            public void run() {

                if (!isJustify.get()) {

                    final int lineCount = textView.getLineCount();
                    final int textViewWidth = textView.getWidth();

                    for (int i = 0; i < lineCount; i++) {

                        int lineStart = textView.getLayout().getLineStart(i);
                        int lineEnd = textView.getLayout().getLineEnd(i);

                        String lineString = textString.substring(lineStart, lineEnd);

                        if (i == lineCount - 1) {
                            builder.append(new SpannableString(lineString));
                            break;
                        }

                        String trimSpaceText = lineString.trim();
                        String removeSpaceText = lineString.replaceAll(" ", "");

                        float removeSpaceWidth = textPaint.measureText(removeSpaceText);
                        float spaceCount = trimSpaceText.length() - removeSpaceText.length();

                        float eachSpaceWidth = (textViewWidth - removeSpaceWidth) / spaceCount;

                        SpannableString spannableString = new SpannableString(lineString);
                        for (int j = 0; j < trimSpaceText.length(); j++) {
                            char c = trimSpaceText.charAt(j);
                            if (c == ' ') {
                                Drawable drawable = new ColorDrawable(0x00ffffff);
                                drawable.setBounds(0, 0, (int) eachSpaceWidth, 0);
                                ImageSpan span = new ImageSpan(drawable);
                                spannableString.setSpan(span, j, j + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            }
                        }

                        builder.append(spannableString);
                    }

                    textView.setText(builder);
                    isJustify.set(true);
                }
            }
        });
    }

    /**
     *  Methode incomplete pour la gestion des erreurs liées au JSON
     * @param context
     * @param error
     */

    public static void MethodeErrorJsonDebut(Context context, VolleyError error)
    {


        String texteMessage =  context.getResources().getString(R.string.texteErrorNoConnectionError);

        if (error instanceof TimeoutError || error instanceof NoConnectionError)
        {

            texteMessage = context.getResources().getString(R.string.texteErrorNoConnectionError);

        } else if (error instanceof AuthFailureError)
        {

            texteMessage = context.getResources().getString(R.string.texteErrorAuthFailure);

        } else if (error instanceof ServerError)
        {
            texteMessage = context.getResources().getString(R.string.texteErrorServerError);

        } else if (error instanceof NetworkError)
        {
            texteMessage = context.getResources().getString(R.string.texteErrorNetworkError);

        } else if (error instanceof ParseError)
        {
            texteMessage = context.getResources().getString(R.string.texteErrorParseError);
        }

        Toast.makeText(context, texteMessage , Toast.LENGTH_LONG).show();


    }


    /**
     * Methode Complete pour la gestion des erreurs liées au JSON
     */

    public static void MethodeErrorJsonComplete(Context context, VolleyError error)
    {

        String texteMessage = "";


        try
        {

            // verifiation de la connexion

            if (error instanceof NoConnectionError)
            {
                ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo networkInfo = null;

                // on recureper les infos de la connections actives

                if ( connectivityManager != null )
                {
                    networkInfo = connectivityManager.getActiveNetworkInfo();
                }

                if ( networkInfo != null && networkInfo.isConnectedOrConnecting() )
                {
                    texteMessage = context.getResources().getString(R.string.texteErrorServeurNonConnecteInternet);
                }
                else
                {
                    texteMessage = context.getResources().getString(R.string.texteErrorAppareilNonConnecteInternet);
                }


            } else if (error instanceof NetworkError || error.getCause() instanceof ConnectException || (error.getCause().getMessage() != null && error.getCause().getMessage().contains("connection")))
            {

                texteMessage = context.getResources().getString(R.string.texteErrorAppareilNonConnecteInternet);

            }


            else if (error.getCause() instanceof MalformedURLException)
            {

                texteMessage = context.getResources().getString(R.string.texteErrorUrlMalForme);

            }
            else if (error instanceof ParseError || error.getCause() instanceof IllegalStateException || error.getCause() instanceof JSONException || error.getCause() instanceof XmlPullParserException)
            {

                texteMessage = context.getResources().getString(R.string.texteErrorJsonXmlInvalid);

            } else if (error.getCause() instanceof OutOfMemoryError)
            {

                texteMessage = context.getResources().getString(R.string.texteErrorOutMemory);

            }
            else if (error instanceof AuthFailureError)
            {

                texteMessage = context.getResources().getString(R.string.texteErrorAuthFailure);

            }  else if (error instanceof ServerError || error.getCause() instanceof ServerError)
            {
                texteMessage = context.getResources().getString(R.string.texteErrorServerError);
            }
            else if (error instanceof TimeoutError || error.getCause() instanceof SocketTimeoutException || error.getCause() instanceof ConnectTimeoutException || error.getCause() instanceof SocketException || (error.getCause().getMessage() != null && error.getCause().getMessage().contains("Connection timed out")))
            {
                texteMessage = context.getResources().getString(R.string.texteErrorServerError);
            }
            else
            {
                texteMessage = context.getResources().getString(R.string.texteErrorUnkown);
            }

        }
        catch (Exception e)
        {
            texteMessage = context.getResources().getString(R.string.texteErrorUnkown);

        }

        Toast.makeText(context, texteMessage , Toast.LENGTH_LONG).show();


    }


    /**
     *  Methode Complete pour la gestion des erreurs liées au JSON avec les options pour cacher et afficher le relativeLayout d'erreur
     * @param context
     * @param error
     * @param relativeLayoutChargement
     * @param relativeLayoutRecyclerview
     * @param relativeLayoutErreur
     * @param textView pour mettre à jour le message d'erreur
     * @return
     */

    public static void MethodeErrorJsonComplete(Context context, VolleyError error, RelativeLayout relativeLayoutChargement, RelativeLayout relativeLayoutRecyclerview, RelativeLayout relativeLayoutErreur, TextView textView )
    {

        String texteMessage = "";


        try
        {

            // verifiation de la connexion

            if (error instanceof NoConnectionError)
            {
                ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo networkInfo = null;

                // on recureper les infos de la connections actives

                if ( connectivityManager != null )
                {
                    networkInfo = connectivityManager.getActiveNetworkInfo();
                }

                if ( networkInfo != null && networkInfo.isConnectedOrConnecting() )
                {
                    texteMessage = context.getResources().getString(R.string.texteErrorServeurNonConnecteInternet);
                }
                else
                {
                    texteMessage = context.getResources().getString(R.string.texteErrorAppareilNonConnecteInternet);
                }


            } else if (error instanceof NetworkError || error.getCause() instanceof ConnectException || (error.getCause().getMessage() != null && error.getCause().getMessage().contains("connection")))
            {

                texteMessage = context.getResources().getString(R.string.texteErrorAppareilNonConnecteInternet);

            }


            else if (error.getCause() instanceof MalformedURLException)
            {

                texteMessage = context.getResources().getString(R.string.texteErrorUrlMalForme);

            }
            else if (error instanceof ParseError || error.getCause() instanceof IllegalStateException || error.getCause() instanceof JSONException || error.getCause() instanceof XmlPullParserException)
            {

                texteMessage = context.getResources().getString(R.string.texteErrorJsonXmlInvalid);

            } else if (error.getCause() instanceof OutOfMemoryError)
            {

                texteMessage = context.getResources().getString(R.string.texteErrorOutMemory);

            }
            else if (error instanceof AuthFailureError)
            {

                texteMessage = context.getResources().getString(R.string.texteErrorAuthFailure);

            }  else if (error instanceof ServerError || error.getCause() instanceof ServerError)
            {
                texteMessage = context.getResources().getString(R.string.texteErrorServerError);
            }
            else if (error instanceof TimeoutError || error.getCause() instanceof SocketTimeoutException || error.getCause() instanceof ConnectTimeoutException || error.getCause() instanceof SocketException || (error.getCause().getMessage() != null && error.getCause().getMessage().contains("Connection timed out")))
            {
                texteMessage = context.getResources().getString(R.string.texteErrorServerError);
            }
            else
            {
                texteMessage = context.getResources().getString(R.string.texteErrorUnkown);
            }

        }
        catch (Exception e)
        {
            texteMessage = context.getResources().getString(R.string.texteErrorUnkown);

        }


        textView.setText(texteMessage);

        AfficherLesRelativesErreur(relativeLayoutChargement, relativeLayoutRecyclerview, relativeLayoutErreur);
    }


    /**
     *  Methode pour afficher les relatives layout quqnd il y'a un succes des recherches
     * @param relativeLayoutChargement
     * @param relativeLayoutRecyclerview
     * @param relativeLayoutErreur
     */


    public static void AfficherLesRelativesRecherche(RelativeLayout relativeLayoutChargement, RelativeLayout relativeLayoutRecyclerview, RelativeLayout relativeLayoutErreur)
    {

        relativeLayoutChargement.setVisibility(View.VISIBLE);
        relativeLayoutRecyclerview.setVisibility(View.GONE);
        relativeLayoutErreur.setVisibility(View.GONE);
    }


    /**
     *  Methode pour afficher les relatives layout quqnd il y'a un succes des recherches
     * @param relativeLayoutChargement
     * @param relativeLayoutRecyclerview
     * @param relativeLayoutErreur
     */


    public static void AfficherLesRelativesSucces(RelativeLayout relativeLayoutChargement, RelativeLayout relativeLayoutRecyclerview, RelativeLayout relativeLayoutErreur)
    {

        relativeLayoutChargement.setVisibility(View.GONE);
        relativeLayoutRecyclerview.setVisibility(View.VISIBLE);
        relativeLayoutErreur.setVisibility(View.GONE);
    }

    /**
     *  Methode pour afficher les relatives layout quqnd il y'a une erreur lors des recherches
     * @param relativeLayoutChargement
     * @param relativeLayoutRecyclerview
     * @param relativeLayoutErreur
     */


    public static void AfficherLesRelativesErreur(RelativeLayout relativeLayoutChargement, RelativeLayout relativeLayoutRecyclerview, RelativeLayout relativeLayoutErreur)
    {

        relativeLayoutChargement.setVisibility(View.GONE);
        relativeLayoutRecyclerview.setVisibility(View.GONE);


        relativeLayoutErreur.setVisibility(View.VISIBLE);

    }




}
