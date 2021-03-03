package com.example.mapsapp.fence.model

class FarmPostBean {
    var geo: List<FarmGeoPoint>? = null
    var farmSize: Double? = null
    var farmerId: String? = null

    constructor(
        geo: List<FarmGeoPoint>?,
        farmSize: Double?,
        farmerId: String?
    ) {
        this.geo = geo
        this.farmSize = farmSize
        this.farmerId = farmerId
    }

    constructor() {}
}