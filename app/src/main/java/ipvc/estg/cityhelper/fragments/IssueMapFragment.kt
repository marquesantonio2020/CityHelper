package ipvc.estg.cityhelper.fragments

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
import ipvc.estg.cityhelper.adapters.ReportListAdapter
import ipvc.estg.cityhelper.api.ReportData
import ipvc.estg.cityhelper.api.endpoints.ReportEndPoint
import ipvc.estg.cityhelper.api.servicebuilder.ServiceBuilder
import kotlinx.android.synthetic.main.activity_user_report_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.InputStream
import java.net.URL

private lateinit var gMap: GoogleMap
private lateinit var allReports: List<ReportData>

//Variables for last known location
private lateinit var lastLocation: Location
private lateinit var fusedLocationClient: FusedLocationProviderClient

//Variables to implement periodic updates on user's location
private lateinit var locationCallback: LocationCallback
private lateinit var locationRequest: LocationRequest

const val LOCATION_PERMISSION_REQUEST_CODE = 1000
var once: Boolean = false
class IssueMapFragment : Fragment(), OnMapReadyCallback {
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
            }
        }

        /**Preparing request to web service - Getting all reports on database to place on de map *********/
        val request = ServiceBuilder.buildService(ReportEndPoint::class.java)
        val call = request.getReports()

        call.enqueue(object : Callback<List<ReportData>> {
            override fun onResponse(call: Call<List<ReportData>>, response: Response<List<ReportData>>){
                var markerPosition: LatLng
                if(response.isSuccessful){
                    allReports = response.body()!!

                    if (Build.VERSION.SDK_INT > 9) {
                        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
                        StrictMode.setThreadPolicy(policy)
                    }

                    for(report in allReports){
                        var myBitmap: Bitmap
                        var imageUrl = "https://cityhelpercommov.000webhostapp.com/COMMOV_APIS/uploads/" + report.report.problem_picture
                        var input: InputStream = URL(imageUrl).openStream()
                        myBitmap = BitmapFactory.decodeStream(input)
                        markerPosition = LatLng(report.report.report_location_latitude, report.report.report_location_longitude)

                            gMap.addMarker(MarkerOptions().position(markerPosition).title(report.report.report_title).snippet("Created by: " + report.user).icon(BitmapDescriptorFactory.fromBitmap(myBitmap))).setIcon(BitmapDescriptorFactory.defaultMarker(report.type.problem_color))

                    }
                }
            }

            override fun onFailure(call: Call<List<ReportData>>, t: Throwable) {
                Toast.makeText(this@IssueMapFragment.context, "${t.message}", Toast.LENGTH_LONG).show()
            }
        })

        /********************************************/
        // Inflate the layout for this fragment
        return root
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        gMap = googleMap!!
        setUpMap()
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

    companion object {
        fun newInstance(): IssueMapFragment{
            return IssueMapFragment()
        }
    }
}