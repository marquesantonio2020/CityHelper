package ipvc.estg.cityhelper.map

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import ipvc.estg.cityhelper.R

class CustomInfoWindowForGoogleMap(context: Context, mapImageHash: HashMap<String, Bitmap>) : GoogleMap.InfoWindowAdapter {

    var mContext = context
    var imageUrl = mapImageHash
    var mWindow = (context as Activity).layoutInflater.inflate(R.layout.marker_custom_info_window, null)

    private fun rendowWindowText(marker: Marker, view: View){

        val tvTitle = view.findViewById<TextView>(R.id.title)
        val tvSnippet = view.findViewById<TextView>(R.id.snippet)
        val tvImage = view.findViewById<ImageView>(R.id.windowImage)

        tvTitle.text = marker.title
        tvSnippet.text = marker.snippet
        tvImage.setImageBitmap(imageUrl[marker.id])

    }

    override fun getInfoContents(marker: Marker): View {
        rendowWindowText(marker, mWindow)
        return mWindow
    }

    override fun getInfoWindow(marker: Marker): View? {
        rendowWindowText(marker, mWindow)
        return mWindow
    }
}