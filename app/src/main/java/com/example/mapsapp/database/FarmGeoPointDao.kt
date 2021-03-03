package com.datatools.applybpo.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.mapsapp.fence.model.FarmGeoPoint

@Dao
interface FarmGeoPointDao {
    @Query("SELECT * FROM FarmGeoPoints")
    fun getAll(): List<FarmGeoPoint>

    @Query("SELECT * FROM FarmGeoPoints WHERE id = :userId")
    fun getLiveData(userId: Int): LiveData<List<FarmGeoPoint>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg step: FarmGeoPoint)

    @Query("SELECT * FROM FarmGeoPoints WHERE id = :id")
    fun getItemById(id: Int) : FarmGeoPoint?

    @Query("DELETE FROM FarmGeoPoints WHERE id LIKE :string")
    fun deleteItem(string: Int)

    @Delete
    fun delete(user: FarmGeoPoint)

    @Query("DELETE FROM FarmGeoPoints")
    fun delete()

    @Update
    fun updateTodo(vararg step: FarmGeoPoint)
}