package ipvc.estg.cityhelper.map

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.example.notifications.NotificationHelper
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent
import ipvc.estg.cityhelper.fragments.IssueMapFragment

class GeofenceBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        var notificationHelper: NotificationHelper = NotificationHelper(context)
        var geofencingEvent: GeofencingEvent = GeofencingEvent.fromIntent(intent)

        if(geofencingEvent.hasError()){
            Toast.makeText(context, "Error", Toast.LENGTH_LONG).show()
        }

        val transitionType = geofencingEvent.geofenceTransition

        when(transitionType){
            Geofence.GEOFENCE_TRANSITION_ENTER -> {
                notificationHelper.sendHighPriorityNotification(IssueMapFragment.javaClass)
            }

        }
    }
}
