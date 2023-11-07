package com.inksy.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mbs.mbsapp.Database.Entities.ActivityDetailEntity
import com.mbs.mbsapp.Database.Entities.ActivityLog
import com.mbs.mbsapp.Database.Entities.ActivityMaster
import com.mbs.mbsapp.Database.Entities.AnswerDetailEntity
import com.mbs.mbsapp.Database.Entities.AnswerMasterEntity
import com.mbs.mbsapp.Database.Entities.BaPitchEntity
import com.mbs.mbsapp.Database.Entities.BrandAmbassadorEntity
import com.mbs.mbsapp.Database.Entities.BrandEntity
import com.mbs.mbsapp.Database.Entities.CampaignChannel
import com.mbs.mbsapp.Database.Entities.CampaignEntity
import com.mbs.mbsapp.Database.Entities.CityEntity
import com.mbs.mbsapp.Database.Entities.LocationEntity
import com.mbs.mbsapp.Database.Entities.MediaEntity
import com.mbs.mbsapp.Database.Entities.ProductEntity
import com.mbs.mbsapp.Database.Entities.ProductStock
import com.mbs.mbsapp.Database.Entities.QuestionEntity
import com.mbs.mbsapp.Database.Entities.QuestionSectionEntity
import com.mbs.mbsapp.Database.Entities.QuestionnaireEntity
import com.mbs.mbsapp.Database.Entities.StoreEntity
import com.mbs.mbsapp.Database.Entities.UserEntity
import com.mbs.mbsapp.Database.iMBSSave

@Database(
    entities = [UserEntity::class, ActivityMaster::class,
        ActivityDetailEntity::class, BrandEntity::class, CampaignEntity::class,
        CityEntity::class, LocationEntity::class, StoreEntity::class,
        QuestionnaireEntity::class, QuestionEntity::class, CampaignChannel::class,
        ActivityLog::class, QuestionSectionEntity::class, ProductEntity::class,
        AnswerMasterEntity::class, AnswerDetailEntity::class, MediaEntity::class, ProductStock::class,
        BaPitchEntity::class, BrandAmbassadorEntity::class],
    version = 36,
    exportSchema = false
)
abstract class MBSDatabase : RoomDatabase() {

    abstract fun getMBSData(): iMBSSave

    companion object {
        // Singleton prevents multiple
        // instances of database opening at the
        // same time.

        private var mbsDatabase: MBSDatabase? = null

        fun getInstance(context: Context): MBSDatabase? {
            if (null == mbsDatabase) {
                mbsDatabase = buildDatabaseInstance(context)
            }
            return mbsDatabase
        }

        private fun buildDatabaseInstance(context: Context): MBSDatabase {
            return Room.databaseBuilder(
                context,
                MBSDatabase::class.java,
                "MBSDatabase"
            ).allowMainThreadQueries().fallbackToDestructiveMigration().build()
        }
    }

    fun cleanUp() {
        mbsDatabase = null
    }
}