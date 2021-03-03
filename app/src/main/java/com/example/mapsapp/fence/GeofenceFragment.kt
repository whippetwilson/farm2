package com.example.mapsapp.fence


import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mapsapp.R
import com.github.bkhezry.mapdrawingtools.model.DataModel
import com.github.bkhezry.mapdrawingtools.model.DrawingOption
import com.github.bkhezry.mapdrawingtools.model.DrawingOptionBuilder
import com.github.bkhezry.mapdrawingtools.ui.MapsActivity
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.tbruyelle.rxpermissions2.RxPermissions
import java.util.*


class GeofenceFragment : Fragment(), IGeofenceFragment, GoogleMap.OnPolygonClickListener {

    private var mViewModel: GeofenceViewModel? = null
    private var googleMap: GoogleMap? = null
    private val REQUEST_CODE = 1
    private var zoom = false
    private var polygonFarmId = 0

    @SuppressLint("CheckResult")
    private val callback = OnMapReadyCallback { googleMap: GoogleMap ->
        val kampala = LatLng(0.347596, 32.582520)
        this@GeofenceFragment.googleMap = googleMap
        this@GeofenceFragment.googleMap!!.setOnPolygonClickListener(this)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(kampala, 14f))
        zoom = true
        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            RxPermissions(requireActivity())
                .request(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION)
                .subscribe({ granted: Boolean -> showCurrentLocation(granted) })
        } else {
            showCurrentLocation(true)
        }
        init()
    }

    @Nullable
    override fun onCreateView(
        inflater: LayoutInflater,
        @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_geofence, container, false)
    }

    override fun onViewCreated(
        view: View,
        @Nullable savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    override fun onActivityCreated(@Nullable savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val drawFarmPolygon: FloatingActionButton = requireView().findViewById(R.id.draw_polygon)
        drawFarmPolygon.setOnClickListener { v: View? -> drawPolygon() }
    }

    private fun drawPolygon() {
        val currentDrawingType: DrawingOption.DrawingType = DrawingOption.DrawingType.POLYGON
        val intent: Intent = DrawingOptionBuilder()
            .withLocation(35.744502, 51.368966)
            .withMapZoom(14F)
            .withFillColor(Color.argb(60, 0, 0, 255))
            .withStrokeColor(Color.argb(100, 255, 0, 0))
            .withStrokeWidth(3)
            .withRequestGPSEnabling(false)
            .withDrawingType(currentDrawingType)
            .build(requireActivity())
        startActivityForResult(intent, REQUEST_CODE)
    }

    private fun init() {
        mViewModel = ViewModelProvider(this).get(GeofenceViewModel::class.java)
        mViewModel?.attachView(this)
        mViewModel?.displayFarmAreas()
    }

    @SuppressLint("MissingPermission")
    private fun showCurrentLocation(granted: Boolean) {
        val locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 0
        locationRequest.fastestInterval = 0
        locationRequest.numUpdates = 1
        LocationServices.getFusedLocationProviderClient(requireContext())
            .requestLocationUpdates(
                locationRequest, locationCallback, Looper.getMainLooper()
            )
    }

    private val locationCallback: LocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val location: Location = locationResult.lastLocation
            val latLng = LatLng(location.latitude, location.longitude)
            googleMap!!.addMarker(MarkerOptions().position(latLng))
            if (zoom) googleMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14f))
            zoom = false
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE && data != null) {
            val dataModel: DataModel = data.extras!!.getParcelable(MapsActivity.POINTS)!!
            val points: Array<LatLng> = dataModel.getPoints()
            val farmId = polygonFarmId
            mViewModel.saveFarmArea(points, farmId)
            polygonFarmId = 0
        }
    }

    private val PATTERN_GAP_LENGTH_PX = 20
    private val DOT: PatternItem = Dot()
    private val GAP: PatternItem = Gap(PATTERN_GAP_LENGTH_PX.toFloat())

    // Create a stroke pattern of a gap followed by a dot.
    private val PATTERN_POLYLINE_DOTTED: List<PatternItem> =
        Arrays.asList(GAP, DOT)
    private val COLOR_BLACK_ARGB = -0x1000000
    private val COLOR_WHITE_ARGB = -0x1
    private val COLOR_GREEN_ARGB = -0xc771c4
    private val COLOR_PURPLE_ARGB = -0x7e387c
    private val COLOR_ORANGE_ARGB = -0xa80e9
    private val COLOR_BLUE_ARGB = -0x657db

    private val POLYGON_STROKE_WIDTH_PX = 4
    private val PATTERN_DASH_LENGTH_PX = 0
    private val DASH: PatternItem = Dash(PATTERN_DASH_LENGTH_PX.toFloat())

    // Create a stroke pattern of a gap followed by a dash.
    private val PATTERN_POLYGON_ALPHA: List<PatternItem> =
        Arrays.asList(GAP, DASH)

    // Create a stroke pattern of a dot followed by a gap, a dash, and another gap.
    private val PATTERN_POLYGON_BETA: List<PatternItem> =
        Arrays.asList(DOT, GAP, DASH, GAP)

    /**
     * Styles the polygon, based on type.
     *
     * @param polygon The polygon object that needs styling.
     */
    private fun stylePolygon(polygon: Polygon) {
        var type = ""
        // Get the data object stored with the polygon.
        if (polygon.tag != null) {
            type = polygon.tag.toString()
        }
        val pattern: List<PatternItem>
        val strokeColor: Int
        val fillColor: Int
        pattern = PATTERN_POLYGON_ALPHA
        strokeColor = COLOR_GREEN_ARGB
        fillColor = COLOR_PURPLE_ARGB
        polygon.strokePattern = pattern
        polygon.setStrokeWidth(POLYGON_STROKE_WIDTH_PX.toFloat())
        polygon.strokeColor = strokeColor
        polygon.fillColor = fillColor
    }

    override fun runOnUiThread(runnable: Runnable?) {
        requireActivity().runOnUiThread(runnable)
    }

    override fun displayFarmArea(geofence: List<Map<Int?, List<LatLng?>?>?>?) {
        googleMap!!.clear()
        zoom = false
        showCurrentLocation(true)
        if (geofence != null) {
            for (_polygon in geofence) {
                if (_polygon != null) {
                    for ((key, value) in _polygon) {
                        if (value != null) {
                            if (!value.isEmpty()) {
                                val polygon: Polygon = googleMap!!.addPolygon(
                                    PolygonOptions()
                                        .clickable(true)
                                        .addAll(value)
                                )
                                polygon.tag = key
                                stylePolygon(polygon)
                                googleMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(value[0], 14f))
                            }
                        }
                    }
                }
            }
        }
    }

    override fun toggleProgressDialog(dialog: ProgressDialog?, show: Boolean) {
        if (show) {
            dialog.setMessage("Loading....")
            dialog.show()
        } else {
            dialog.dismiss()
        }
    }

    override fun onPolygonClick(polygon: Polygon) {
        AlertDialog.Builder(requireActivity())
            .setTitle("Farm Options")
            .setSingleChoiceItems(
                arrayOf(
                    "Edit farm boundary",
                    "Delete farm boundaries"
                ), 0
            ) { dialogInterface, i ->
                when (i) {
                    0 -> {
                        polygonFarmId =
                            Objects.requireNonNull(polygon.tag).toString().toInt()
                        drawPolygon()
                        dialogInterface.dismiss()
                    }
                    1 -> {
                        dialogInterface.dismiss()
                        AlertDialog.Builder(requireActivity())
                            .setMessage("You're about to delete farm boundaries.\nPlease confirm.")
                            .setPositiveButton(
                                "Confirm",
                                { dialogInterface1, i1 ->
                                    mViewModel?.deleteFarmArea(
                                        Objects.requireNonNull(polygon.tag).toString()
                                            .toInt()
                                    )
                                })
                            .setNegativeButton(
                                "Cancel",
                                { dialogInterface12, i12 -> dialogInterface12.dismiss() })
                            .show()
                    }
                }
            }
            .setNegativeButton("Close", { dialogInterface, i -> dialogInterface.dismiss() })
            .show()
    }
}