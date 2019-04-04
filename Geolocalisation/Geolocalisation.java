package ci.esatic.master2.tp.testcarte.ClasseGeolocalisation;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;

import ci.esatic.master2.tp.testcarte.ClasseGeolocalisation.classeMetier.CoordonneesGps;


/**
 * Created by cedricakrou on 28/11/18.
 * cette classe sert à
 */

public class Geolocalisation {
    private Context context;

    public static final String nomFournisseur = LocationManager.GPS_PROVIDER;
    public static final int minDinstance = 1000;
    public static final int minTime = 1000;

    private LocationManager locationManager;

    public Geolocalisation(Context context) {
        this.context = context;
    }

    public Geolocalisation(LocationManager locationManager) {
        this.locationManager = locationManager;
    }

    // récuperation du locationManager afin de recuperer les providers et les
//    private LocationManager locationManager = (LocationManager) this.context.getSystemService(Context.LOCATION_SERVICE);

    // les fournisseurs de position sont les providers qui sont le GPS, le wifi et les antennes relais


    /**
     * fonction pour recuperer tous les providers à tavers leur nom meme ceux qui sont desactivés si
     * @param typeProviders == true
     */

    public List<String> RecuperationDesProviders(Boolean typeProviders) {
        List<String> listeProviders = new ArrayList<String>();
        listeProviders.clear();

        if (typeProviders) {
            listeProviders = locationManager.getAllProviders();
        } else {
            listeProviders = locationManager.getProviders(true);
        }


        return listeProviders;
    }


    /**
     *  Methode servant à recuperer toutes les locations providers disponibles
     * @param typeProviders
     * @return
     */

    public List<LocationProvider> RecuperationTousLesLocationsProviders(boolean typeProviders) {
        List<LocationProvider> listeLocationProviders = new ArrayList<LocationProvider>();

        List<String> listeProviders = RecuperationDesProviders(typeProviders);

        for (String providers : listeProviders) {
            listeLocationProviders.add(locationManager.getProvider(providers));
        }

        return listeLocationProviders;
    }


    /**
     * recuperation d'un provider à l'aide du
     * @param name
     * @return un LocationProvider
     */

    public LocationProvider RecuperationDunLocationProvider(String name)
    {
        return locationManager.getProvider(name);
    }

    /**
     * fonction permettant de recuperer les coordonnées GPS
     * @param name
     * @param minimumTemps
     * @param minimumDistance
     * @return
     */

    public CoordonneesGps RecuperationCoordonnéesGpsEnCours(String name, int minimumTemps, int minimumDistance) {

        final CoordonneesGps[] coordonneesGps = {null};

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return null ;
        }

        locationManager.requestLocationUpdates(name, minimumTemps, minimumDistance, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                coordonneesGps[0] = new CoordonneesGps(location.getLongitude(), location.getLatitude());
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        });


        return coordonneesGps[0];
    }


    public void RecuperationDesAlertesDeProximite(double longitude, double latitude, float rayon, long expiration) {
        Intent intent = new Intent(this.context, ReceiverAlertMiseAjour.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (ActivityCompat.checkSelfPermission(this.context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.addProximityAlert(longitude, latitude, rayon, expiration, pendingIntent);
    }


}
