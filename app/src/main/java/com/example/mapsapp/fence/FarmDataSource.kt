package com.example.mapsapp.fence

import com.datatools.applybpo.data.database.DaoFactory
import com.example.mapsapp.fence.model.FarmGeoPoint
import com.example.mapsapp.fence.model.FarmPostBean

class FarmDataSource: IFarmDataSource {
    override fun getFarmByFarmerId(id: String?, factory: DaoFactory): List<FarmGeoPoint?>? {
        return factory.farmGeoPointDao.getAll()
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