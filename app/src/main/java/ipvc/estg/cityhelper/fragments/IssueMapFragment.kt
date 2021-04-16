package ipvc.estg.cityhelper.fragments

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import ipvc.estg.cityhelper.R
import ipvc.estg.cityhelper.ReportDescriptionActivity
import ipvc.estg.cityhelper.adapters.REPORT_ID
import ipvc.estg.cityhelper.adapters.ReportListAdapter
import ipvc.estg.cityhelper.api.ReportData
import ipvc.estg.cityhelper.api.endpoints.ReportEndPoint
import ipvc.estg.cityhelper.api.servicebuilder.ServiceBuilder
import kotlinx.android.synthetic.main.activity_user_report_list.*
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.InputStream
import java.net.URL

private lateinit var gMap: GoogleMap
private lateinit var allReports: List<ReportData>
private var markers: ArrayList<Marker> = ArrayList()
private lateinit var markersHashType: HashMap<Marker, String>
private lateinit var markersHashId: HashMap<Marker, Int>

//Variables for last known location
private lateinit var lastLocation: Location
private lateinit var fusedLocationClient: FusedLocationProviderClient

//Variables to implement periodic updates on user's location
private lateinit var locationCallback: LocationCallback
private lateinit var locationRequest: LocationRequest

//Buttons for filtering
private lateinit var refreshMarkersBtn: TextView
private lateinit var filterByInfrastucturesBtn: TextView
private lateinit var filterBySanitationBtn: TextView
private lateinit var filterBySignalingBtn: TextView

//Distance Filtering
private var selectedDistance = 100
private lateinit var distance100m: Button
private lateinit var distance500m: Button
private lateinit var distance1000m: Button
private lateinit var distance2000m: Button


const val LOCATION_PERMISSION_REQUEST_CODE = 1000
var once: Boolean = false
class IssueMapFragment : Fragment(), OnMapReadyCallback, View.OnClickListener, GoogleMap.OnInfoWindowClickListener {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_issue_map, container, false)

        //Obtaining the SupportMApFragment and get notified when the map is ready to be used
        val frag = childFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        frag.getMapAsync(this)

        //Initialize fusedLocationClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this.context!!)
        createLocationRequest()

        //Callback added for periodic location updates
        locationCallback = object: LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)
                lastLocation = p0.lastLocation
                var place = LatLng(lastLocation.latitude, lastLocation.longitude)
                if(!once){
                    gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place, 12.0f))
                    once = true
                }
                calculateDistance(lastLocation, markers)
            }
        }

        getAllMarkers()


        /**Type filtering actions********************/
        refreshMarkersBtn = root.findViewById(R.id.refreshMarkers)
        refreshMarkersBtn.setOnClickListener {
            showAllMarkers(markers)
        }
        filterByInfrastucturesBtn = root.findViewById(R.id.filterInfrastructures)
        filterByInfrastucturesBtn.setOnClickListener{
            showInfrastucturesOnly(markers)
        }
        filterBySanitationBtn = root.findViewById(R.id.filterSanitation)
        filterBySanitationBtn.setOnClickListener{
            showSanitationOnly(markers)
        }
        filterBySignalingBtn = root.findViewById(R.id.filterSignaling)
        filterBySignalingBtn.setOnClickListener{
            showSignalingOnly(markers)
        }
        /********************************************/
        /**Distance filtering*******************************/
        distance100m = root.findViewById(R.id.distance100m)
        distance500m = root.findViewById(R.id.distance500m)
        distance1000m = root.findViewById(R.id.distance1000m)
        distance2000m = root.findViewById(R.id.distance2000m)
        distance100m.setOnClickListener(this)
        distance500m.setOnClickListener(this)
        distance1000m.setOnClickListener(this)
        distance2000m.setOnClickListener(this)
        /**************************************************/
        // Inflate the layout for this fragment
        return root
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        gMap = googleMap!!
        setUpMap()
        gMap.setOnInfoWindowClickListener(this)
    }

    override fun onInfoWindowClick(selectedMarker: Marker) {
        val reportId = markersHashId.get(selectedMarker)

        val intent = Intent(this.context, ReportDescriptionActivity::class.java).apply {
            putExtra(REPORT_ID, reportId)
        }
        startActivity(intent)
    }

    private fun setUpMap(){
        if(ActivityCompat.checkSelfPermission(this.context!!, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this.activity!!, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)

            return
        }else{
            gMap.isMyLocationEnabled = true

            fusedLocationClient.lastLocation.addOnSuccessListener(this.activity!!) {
                location ->
                if(location != null){
                    lastLocation = location
                    val currentLatLng = LatLng(location.latitude, location.longitude)
                    gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))
                }
            }
        }
    }

    private fun startLocationUpdates(){
        if(ActivityCompat.checkSelfPermission(this.context!!, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this.activity!!, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            return
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    private fun showAllMarkers(markers: ArrayList<Marker>){
        for (marker in markers){
            marker.isVisible = true
        }
    }

    private fun showInfrastucturesOnly(markers: ArrayList<Marker>){
        for(marker in markers){
            marker.isVisible = markersHashType.get(marker) == "Infraestruturas"
        }
    }
    private fun showSanitationOnly(markers: ArrayList<Marker>){
        for(marker in markers){
            marker.isVisible = markersHashType.get(marker) == "Saneamento"
        }
    }

    private fun showSignalingOnly(markers: ArrayList<Marker>){
        for(marker in markers){
            marker.isVisible = markersHashType.get(marker) == "Sinalização"
        }
    }

    private fun calculateDistance(location: Location, markers: ArrayList<Marker>){
        for(marker in markers){
            var results = FloatArray(1)
            Location.distanceBetween(location.latitude, location.longitude, marker.position.latitude, marker.position.longitude, results)
            marker.isVisible = results[0] < selectedDistance
        }
    }
    private fun getAllMarkers(){
        /**Preparing request to web service - Getting all reports on database to place on de map *********/
        val request = ServiceBuilder.buildService(ReportEndPoint::class.java)
        val call = request.getReports()

        call.enqueue(object : Callback<List<ReportData>> {
            override fun onResponse(call: Call<List<ReportData>>, response: Response<List<ReportData>>){
                var markerPosition: LatLng
                markers = ArrayList<Marker>()
                markersHashType = HashMap()
                markersHashId = HashMap()
                if(response.isSuccessful){
                    allReports = response.body()!!

                    if (Build.VERSION.SDK_INT > 9) {
                        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
                        StrictMode.setThreadPolicy(policy)
                    }

                    for(report in allReports) {
                        var myBitmap: Bitmap
                        var imageUrl =
                            "https://cityhelpercommov.000webhostapp.com/COMMOV_APIS/uploads/" + report.report.problem_picture
                        var input: InputStream = URL(imageUrl).openStream()
                        myBitmap = BitmapFactory.decodeStream(input)
                        markerPosition = LatLng(
                            report.report.report_location_latitude,
                            report.report.report_location_longitude
                        )

                        val marker: Marker = gMap.addMarker(
                            MarkerOptions().position(markerPosition)
                                .title(report.report.report_title)
                                .snippet("Created by: " + report.user)
                                .icon(BitmapDescriptorFactory.fromBitmap(myBitmap))
                        )
                        marker.setIcon(BitmapDescriptorFactory.defaultMarker(report.type.problem_color))

                        markers.add(marker)
                        markersHashType.put(marker, report.type.problem_description)
                        markersHashId.put(marker, report.report.id)
                    }

                }
            }

            override fun onFailure(call: Call<List<ReportData>>, t: Throwable) {
                getAllMarkers()
            }
        })

        /********************************************/

    }

    private fun createLocationRequest(){
        locationRequest = LocationRequest()

        //Specify the interval rate when the update must to be receive
        locationRequest.interval = 6000 //ms
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    override fun onPause() {
        super.onPause()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    override fun onResume() {
        super.onResume()
        startLocationUpdates()
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.distance100m -> {selectedDistance = 100}
            R.id.distance500m -> {
                selectedDistance = 500}
            R.id.distance1000m -> {
                selectedDistance = 1000}
            R.id.distance2000m -> {
                selectedDistance = 2000}
        }
    }

    companion object {
        fun newInstance(): IssueMapFragment{
            return IssueMapFragment()
        }
    }
}