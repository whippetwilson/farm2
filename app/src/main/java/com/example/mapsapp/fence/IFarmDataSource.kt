package com.example.mapsapp.fence

import com.example.mapsapp.fence.model.FarmPostBean

import com.example.mapsapp.fence.model.FarmResponseBean




interface IFarmDataSource {
    fun getFarmByFarmerId(id: String?): List<FarmResponseBean?>?
    fun saveFarmArea(farmGeoPoints: FarmPostBean?): Any?
    fun deleteFarmArea(farmId: Int): Any?
}