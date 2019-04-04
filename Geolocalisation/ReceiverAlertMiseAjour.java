package ci.esatic.master2.tp.testcarte.ClasseGeolocalisation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;

/**
 * Created by cedricakrou on 28/11/18.
 * cette classe sert à
 */

public class ReceiverAlertMiseAjour extends BroadcastReceiver
{

    @Override
    public void onReceive(Context context, Intent intent)
    {
        boolean entrer = intent.getBooleanExtra(LocationManager.KEY_PROXIMITY_ENTERING, true);

    }
}
