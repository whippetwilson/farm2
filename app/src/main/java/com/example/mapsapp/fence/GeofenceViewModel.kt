package com.example.mapsapp.fence

import android.app.ProgressDialog
import android.os.AsyncTask
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.mapsapp.fence.model.FarmGeoPoint
import com.example.mapsapp.fence.model.FarmPostBean
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.SphericalUtil
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class GeofenceViewModel: ViewModel(), IGeofenceViewModel{
    private var view: IGeofenceFragment? = null
    private var farmDataSource: IFarmDataSource? = null
    private var homeDataRepository: IHomeDataRepository? = null

    override fun attachView(view: IGeofenceFragment?) {
        this.view = view
        farmDataSource = FarmDataSource()
        homeDataRepository = HomeDataRepository()
    }

    override fun getFarms(farmerId: String?): List<FarmResponseBean>? {
        return farmDataSource.getFarmByFarmerId(farmerId)
    }

    override fun displayFarmAreas() {
        val dialog = ProgressDialog(view.getView().getContext())
        view.toggleProgressDialog(dialog, true)
        AsyncTask.execute {
            val farms: List<FarmResponseBean>? =
                getFarms(view.getArguments().getString(Utils.FARMER_ID_ARG))
            view.runOnUiThread({
                if (farms != null) {
                    val farmPoints: MutableList<Map<Int, List<LatLng>>> =
                        ArrayList()
                    for (farmGeoPoints in farms) {
                        val latLngs: MutableList<LatLng> = ArrayList()
                        for (point in farmGeoPoints.geo) {
                            latLngs.add(LatLng(point.getLatitude(), point.getLongitude()))
                        }
                        val map: MutableMap<Int, List<LatLng>> =
                            HashMap()
                        map[farmGeoPoints.farm.getId()] = latLngs
                        farmPoints.add(map)
                    }
                    view.displayFarmArea(farmPoints)
                }
                view.toggleProgressDialog(dialog, false)
            })
        }
    }

    fun saveFarmArea(polygon: Array<LatLng>, farmId: Int) {
        val dialog = ProgressDialog(view.getView().getContext())
        view.toggleProgressDialog(dialog, true)
        AsyncTask.execute {
            val points: MutableList<FarmGeoPoint> = ArrayList()
            for (latLng in polygon) {
                points.add(
                    FarmGeoPoint(
                        latLng.latitude, latLng.longitude,
                        view.getArguments().getString(FARMER_ID_ARG), farmId
                    )
                )
            }
            val status = farmDataSource.saveFarmArea(
                FarmPostBean(
                    points, SphericalUtil.computeArea(Arrays.asList(polygon)),
                    view.getArguments().getString(Utils.FARMER_ID_ARG)
                )
            )
            view.runOnUiThread({
                view.toggleProgressDialog(dialog, false)
                if (status != null && status.success) {
                    displayFarmAreas()
                } else {
                    Toast.makeText(
                        view.getView().getContext(),
                        "Error! while saving farm area",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
    }

    override fun deleteFarmArea(farmId: Int) {
        val dialog = ProgressDialog(view.getView().getContext())
        view.toggleProgressDialog(dialog, true)
        AsyncTask.execute {
            val status = farmDataSource.deleteFarmArea(farmId)
            view.runOnUiThread({
                view.toggleProgressDialog(dialog, false)
                if (status != null && status.success) {
                    displayFarmAreas()
                } else {
                    Toast.makeText(
                        view.getView().getContext(),
                        "Error! while deleting farm area",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
    }
}