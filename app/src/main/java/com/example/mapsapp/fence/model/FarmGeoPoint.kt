package com.example.mapsapp.fence.model

import androidx.room.Entity

@Entity(tableName = "FarmGeoPoints")
class FarmGeoPoint {
    var id = 0
    var latitude: Double? = null
    var longitude: Double? = null
    var farmId = 0
    var farmerId: String? = null

    constructor(
        latitude: Double?,
        longitude: Double?,
        farmerId: String?,
        farmId: Int
    ) {
        this.latitude = latitude
        this.longitude = longitude
        this.farmerId = farmerId
        this.farmId = farmId
    }

    constructor() {}

}
