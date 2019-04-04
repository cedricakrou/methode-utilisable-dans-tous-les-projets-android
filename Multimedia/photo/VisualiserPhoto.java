package ci.esatic.master2.tp.testcarte.multimedia.photo;

import android.content.Context;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;
import java.util.List;

/**
 * Created by cedricakrou on 02/12/18.
 * cette classe sert à
 */

public class VisualiserPhoto extends SurfaceView implements SurfaceHolder.Callback
{
    private int anglePortrait = 90;
    private int angleLandscape = 0;

    private PrendreDesPhotos prendreDesPhotos = null;
    private Camera camera = null;
    private SurfaceHolder surfaceHolder;

    public VisualiserPhoto(Context context, Camera camera, PrendreDesPhotos prendreDesPhotos)
    {
        super(context);
        this.camera = camera;
        this.prendreDesPhotos = prendreDesPhotos;
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
    }

    /**
     * This is called immediately after the surface is first created.
     * Implementations of this should start up whatever rendering code
     * they desire.  Note that only one thread can ever draw into
     * a {@link Surface}, so you should not draw into the Surface here
     * if your normal rendering will be in another thread.
     *
     * @param holder The SurfaceHolder whose surface is being created.
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {


        Camera.Parameters parameters = prendreDesPhotos.RecuperationDesParametresCamera(camera);

        // recuperation des tailles de l'ecran

        List<Camera.Size> listeSize = parameters.getSupportedPictureSizes();

        Camera.Size size = null;

        for (Camera.Size taille: listeSize )
        {
            size = taille;
        }

        //
        // on verifie si le telephone est tourn" en mode portrait ou en mode paysage pour adapter la camera

        if (this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE)
        {
            parameters.set("orientation", "portrait");
            camera.setDisplayOrientation(anglePortrait);
            parameters.setRotation(anglePortrait);
        }
        else
        {
            parameters.set("orientation", "landscape");
            camera.setDisplayOrientation(angleLandscape);
            parameters.setRotation(angleLandscape);
        }

        // taille de la photo

        parameters.setPictureSize(size.width, size.height);

        // on associe les parametres recueillis à la camera
        camera.setParameters(parameters);

        try
        {
            // on visualise l'image
            camera.setPreviewDisplay(holder);
            camera.startPreview();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This is called immediately after any structural changes (format or
     * size) have been made to the surface.  You should at this point update
     * the imagery in the surface.  This method is always called at least
     * once, after {@link #surfaceCreated}.
     *
     * @param holder The SurfaceHolder whose surface has changed.
     * @param format The new PixelFormat of the surface.
     * @param width  The new width of the surface.
     * @param height The new height of the surface.
     */
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {

    }

    /**
     * This is called immediately before a surface is being destroyed. After
     * returning from this call, you should no longer try to access this
     * surface.  If you have a rendering thread that directly accesses
     * the surface, you must ensure that thread is no longer touching the
     * Surface before returning from this function.
     *
     * @param holder The SurfaceHolder whose surface is being destroyed.
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        camera.stopPreview();
        camera.release();
    }

}
