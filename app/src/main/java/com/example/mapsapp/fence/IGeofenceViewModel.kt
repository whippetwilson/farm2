package com.example.mapsapp.fence

import com.google.android.gms.maps.model.LatLng




interface IGeofenceViewModel {
    fun attachView(view: IGeofenceFragment?)
    fun getFarms(id: String?): List<Any?>?
    fun displayFarmAreas()
    fun saveFarmArea(polygon: Array<LatLng?>?, id: Int)
    fun deleteFarmArea(id: Int)
}