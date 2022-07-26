package jmmh.thefive.m513.view.ui.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.FirebaseFirestore
import jmmh.ibcpt.control.QuantityDialog
import jmmh.thefive.m513.R
import jmmh.thefive.m513.data.local.LocationDatabase
import jmmh.thefive.m513.data.utils.Constants
import jmmh.thefive.m513.data.utils.NetworkHelper
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener,
    GoogleMap.OnMyLocationClickListener {
    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val db = FirebaseFirestore.getInstance()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        supportActionBar?.hide()
        notification()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        createFragment()
    }

    private fun createFragment() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        //createMarker()
        //notification()
        map.setOnMyLocationButtonClickListener(this)
        map.setOnMyLocationClickListener(this)
        enableMyLocation()
        if (NetworkHelper.isNetworkConnection(this)) {
            getCurrentLocation()
        } else {
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show()
            readLocationFromLocalData()
        }
        updateLocationCountDown()
    }

    private fun createMarker(lat: Double, lng: Double, msg: String) {
        val cordinates = LatLng(lat, lng)
        val marker = MarkerOptions().position(cordinates).title(msg)
        map.addMarker(marker)
        //Zoom
        map.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                cordinates, 18f
            ), 4000, null
        )
    }

    fun readLocationFromLocalData() {
        val room =
            Room.databaseBuilder(this, LocationDatabase::class.java, "location").build()
        lifecycleScope.launch {
            var locations = room.daoLocation().getLocation()
            for (item in locations) {
                createMarker(item.latitude.toDouble(), item.location.toDouble(), "Last location")
            }
        }
    }
    private fun getCurrentLocation() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { miLocation: Location? ->
                if (miLocation != null) {
                    val datetime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM dd yyyy, hh:mm:ss a"))
                    writeDataToFirebase(miLocation.latitude, miLocation.longitude, datetime)
                    createMarker(miLocation.latitude, miLocation.longitude, "You are here")
                    writeToLocalDatabase(datetime,miLocation.latitude.toString(),miLocation.longitude.toString())
                }
            }
    }

    //Check permission
    private fun isLocationPermissionsGranted() = ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    @SuppressLint("MissingPermission")
    private fun enableMyLocation() {
        if (!::map.isInitialized) return

        if (isLocationPermissionsGranted()) {
            map.isMyLocationEnabled = true
        } else {
            requestLocationPermission()
        }
    }

    private fun requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            Toast.makeText(
                this,
                "Ve a ajustes y acepta los permisos",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                Constants.REQUEST_CODE_LOCATION
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            Constants.REQUEST_CODE_LOCATION -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                map.isMyLocationEnabled = true
            } else {
                Toast.makeText(
                    this,
                    "Para activar la localización ve a ajustes y acepta los permisos",
                    Toast.LENGTH_SHORT
                ).show()
            }
            else -> {}
        }
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        if (!::map.isInitialized) return
        if (!isLocationPermissionsGranted()) {
            map.isMyLocationEnabled = false
            Toast.makeText(this, "Para activar la localización ve a ajustes y acepta los permisos", Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun onMyLocationButtonClick(): Boolean {
        Toast.makeText(this, "Actualizando...", Toast.LENGTH_SHORT).show()
        readLocationFromLocalData()
        return false
    }

    override fun onMyLocationClick(miLocation: Location) {
        QuantityDialog().show(supportFragmentManager, "dialogo")
    }

    fun writeDataToFirebase(lat: Double, lon: Double, dateTime: String) {
        val cordinatesId = lat.toString() + lon.toString()
        val location = hashMapOf(
            "id_location" to cordinatesId,
            "datetime" to dateTime,
            "latitude" to lat,
            "longitud" to lon
        )
        db.collection(Constants.LOCATION_DATABASE).document("cordinatesId")
            .set(location)
            .addOnSuccessListener {
                Toast.makeText(this, "Updated", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error " + e, Toast.LENGTH_SHORT).show()
            }
    }

    private fun notification() {
        val chanelID = "M513 CHANEL ID"
        val chanelName = "M513"
        val importancia = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(chanelID, chanelName, importancia)
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
        val notification = NotificationCompat.Builder(this, chanelID).also { noti ->
            noti.setContentTitle("Servicio de ubicación")
            noti.setContentText("M513 está accediendo a tu ubicación.")
            noti.setSmallIcon(R.drawable.movie_icon)
        }.build()
        val notificationManager = NotificationManagerCompat.from(applicationContext)
        notificationManager.notify(1, notification)
    }

    private fun updateLocationCountDown() {
        object : CountDownTimer(50000, 1000) {

            override fun onTick(millisUntilFinished: Long) {
            }

            override fun onFinish() {
                onMapReady(map)
            }
        }.start()
    }

    private fun writeToLocalDatabase(dateT: String, lat: String, lon: String) {
        val room =
            Room.databaseBuilder(this, LocationDatabase::class.java, "location").build()
        lifecycleScope.launch {
            room.daoLocation().insertLocation(
                jmmh.thefive.m513.data.local.entities.Location(
                    0,
                    dateT,
                    lat,
                    lon
                )
            )
        }
    }
}