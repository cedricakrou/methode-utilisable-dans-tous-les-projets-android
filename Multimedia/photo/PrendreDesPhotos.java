package ci.esatic.master2.tp.testcarte.multimedia.photo;

import android.content.Context;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import ci.esatic.master2.tp.testcarte.R;

/**
 * Created by cedricakrou on 29/11/18.
 * cette classe sert à
 */

public class PrendreDesPhotos
{


    private int codePrisePhoto = 1;
    private File photoPrise = null;

    private Context context;

    /**
     * cette fonction sert à ouvrir la camera
     * @return
     */

    public Camera OuvertureCamera()
    {
        return Camera.open();
    }


    public PrendreDesPhotos(Context context) {
        this.context = context;
    }

    /**
     *  fonction pour recuperer les parametres de la camera
     * @return
     */




    public Camera.Parameters RecuperationDesParametresCamera(Camera camera)
    {

        // les parametres de la camera
        Camera.Parameters parameters = camera.getParameters();

        // pour connaitre les modes de flash supportés
//        List<String> listeDesFlashs = parameters.getSupportedFlashModes();

        // pour connaitre les tailles supportées par le tel
//        List<Camera.Size> liste = parameters.getSupportedPictureSizes();

        return parameters;
    }

    /**
     * parametre pour prendre une photo
     */

    public void CaptureDePhoto(final Context context, Camera camera)
    {
        camera.takePicture( priseDePhoto, null, recuperationPhoto);
    }


    // variable servant à la prise de la photo quand on clique sur le bouton pour lancer l'application

    private Camera.ShutterCallback priseDePhoto = new Camera.ShutterCallback()
    {
        @Override
        public void onShutter()
        {
            lancementSonCamera(context);

        }
    };

    // variable servant à recuperer l'image prise par l'utilisateur qui est stocké

    private Camera.PictureCallback recuperationPhoto = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera)
        {
            FileOutputStream outStream = null;

            // Write to SD Card
            try {
                File sdCard = Environment.getExternalStorageDirectory();
                File dir = new File (sdCard.getAbsolutePath() + "/camtest");
                dir.mkdirs();

                String fileName = String.format("%d.jpg", System.currentTimeMillis());
                File outFile = new File(dir, fileName);

                outStream = new FileOutputStream(outFile);
                outStream.write(data[0]);
                outStream.flush();
                outStream.close();


            } catch (FileNotFoundException e)
            {

                Toast.makeText(context, "Impossible de trouver l'image", Toast.LENGTH_SHORT).show();

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
            }
        }

    };


    /**
     * lancement du son pour la prise de la photo
     * @param context
     */

    private void lancementSonCamera(Context context)
    {
        // creation du son
        MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.sonnerie);

        // lancement du son
        mediaPlayer.start();

        // une fois que le son terminé
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp)
            {
                mp.release();
            }
        });
    }



}
