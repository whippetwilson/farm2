package com.example.mapsapp.fence

import com.example.mapsapp.fence.model.FarmPostBean
import com.example.mapsapp.fence.model.FarmResponseBean

class FarmDataSource: IFarmDataSource {
    override fun getFarmByFarmerId(id: String?): List<FarmResponseBean?>? {
        TODO("Not yet implemented")
    }

    override fun saveFarmArea(farmGeoPoints: FarmPostBean?): Any? {
        TODO("Not yet implemented")
    }

    override fun deleteFarmArea(farmId: Int): Any? {
        TODO("Not yet implemented")
    }
}