package ci.esatic.master2.tp.testcarte.ClasseGeolocalisation.classeMetier;

/**
 * Created by cedricakrou on 28/11/18.
 * cette classe sert à
 */

public class CoordonneesGps
{
    private double longitude;
    private double latitude;

    public CoordonneesGps(double longitude, double latitude)
    {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
}
