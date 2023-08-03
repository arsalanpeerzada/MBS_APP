package com.mbs.mbsapp.Database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mbs.mbsapp.Database.Entities.BrandEntity
import com.mbs.mbsapp.Database.Entities.CampaignEntity
import com.mbs.mbsapp.Database.Entities.CityEntity
import com.mbs.mbsapp.Database.Entities.LocationEntity
import com.mbs.mbsapp.Database.Entities.StoreEntity
import com.mbs.mbsapp.Database.Entities.UserEntity

@Dao
interface iMBSSave {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBrands(brand: BrandEntity?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCampaign(campaignEntity: CampaignEntity?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCities(cityEntity: CityEntity?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocation(locationEntity: LocationEntity?)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStores(storeEntity: StoreEntity?)


    @Query("Select * from brands order by mid ASC")
    fun getAllBrands(): List<BrandEntity>



}