package com.example.mapsapp.fence

import android.app.ProgressDialog
import android.os.Bundle
import android.view.View
import androidx.lifecycle.LifecycleOwner
import com.google.android.gms.maps.model.LatLng


interface IGeofenceFragment {
    fun getView(): View?
    fun runOnUiThread(runnable: () -> Unit)
    fun getViewLifecycleOwner(): LifecycleOwner?
    fun displayFarmArea(geofence: List<Map<Int?, List<LatLng?>?>?>?)
    fun getArguments(): Bundle?
    fun toggleProgressDialog(dialog: ProgressDialog?, show: Boolean)
}