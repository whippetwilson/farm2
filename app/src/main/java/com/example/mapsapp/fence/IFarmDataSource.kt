package com.example.mapsapp.fence

import com.datatools.applybpo.data.database.DaoFactory
import com.example.mapsapp.fence.model.FarmGeoPoint
import com.example.mapsapp.fence.model.FarmPostBean

import com.example.mapsapp.fence.model.FarmResponseBean




interface IFarmDataSource {
    fun getFarmByFarmerId(id: String?, factory: DaoFactory): List<FarmGeoPoint?>?
    fun saveFarmArea(farmGeoPoints: FarmPostBean?, factory: DaoFactory): Any?
    fun deleteFarmArea(farmId: Int, factory: DaoFactory): Any?
}