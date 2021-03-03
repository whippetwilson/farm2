package com.example.mapsapp.fence

import com.datatools.applybpo.data.database.DaoFactory
import com.example.mapsapp.fence.model.Farm
import com.example.mapsapp.fence.model.FarmGeoPoint
import com.example.mapsapp.fence.model.FarmPostBean
import com.example.mapsapp.fence.model.FarmResponseBean

class FarmDataSource: IFarmDataSource {
    override fun getFarmByFarmerId(id: String?, factory: DaoFactory): List<FarmResponseBean?>? {
        val t = FarmResponseBean()
        t.farm = Farm().also {
            it.id = id!!.toInt()
        }
        t.geo = id?.let { factory.farmGeoPointDao.getAll(farmId = it) }
        return mutableListOf(t)
    }

    override fun saveFarmArea(farmGeoPoints: FarmPostBean?, factory: DaoFactory): Any? {
        for (point in farmGeoPoints!!.geo!!){
            factory.farmGeoPointDao.insert(point)
        }
        return farmGeoPoints
    }

    override fun deleteFarmArea(farmId: Int, factory: DaoFactory): Any? {
        factory.farmGeoPointDao.deleteItem(farmId)
        return true
    }


}