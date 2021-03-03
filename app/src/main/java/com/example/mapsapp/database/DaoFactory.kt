package com.datatools.applybpo.data.database

import android.content.Context


class DaoFactory constructor (application: Context) {
    var db: Any = AppDatabase.invoke(application)
    var farmGeoPointDao: Any

    init {
        farmGeoPointDao = (db as AppDatabase).farmGeoPointDao()
    }
}