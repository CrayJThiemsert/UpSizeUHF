package com.handheld.upsizeuhf.database

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.handheld.upsizeuhf.dao.CostumeDao
import com.handheld.upsizeuhf.entity.Costume
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * This is the backend. The database. This used to be done by the OpenHelper.
 * The fact that this has very few comments emphasizes its coolness.
 */
@Database(entities = [Costume::class], version = 1)
abstract class CostumeRoomDatabase : RoomDatabase() {

    abstract fun costumeDao(): CostumeDao


    companion object {
        @Volatile
        private var INSTANCE: CostumeRoomDatabase? = null
        private val TAG = this.javaClass.simpleName

        fun getDatabase(
                context: Context,
                scope: CoroutineScope
        ): CostumeRoomDatabase {
            Log.d(TAG, "Hello getDatabase()")
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        CostumeRoomDatabase::class.java,
                        "costumedb"
                )
                        // Wipes and rebuilds instead of migrating if no Migration object.
                        // Migration is not part of this codelab.
                        .fallbackToDestructiveMigration()
                        .addCallback(CostumeDatabaseCallback(scope))
                        .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }

        private class CostumeDatabaseCallback(
                private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            /**
             * Override the onCreate method to populate the database.
             */
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                // If you want to keep the data through app restarts,
                // comment out the following line.
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateDatabase(database.costumeDao())
                    }
                }
            }
        }

        /**
         * Populate the database in a new coroutine.
         * If you want to start with more costumes, just add them.
         */
        suspend fun populateDatabase(costumeDao: CostumeDao) {
            // Start the app with a clean database every time.
            // Not needed if you only populate on creation.
            costumeDao.deleteAll()

//            var costume = Costume(runningNo = "99999",
//                    actor = "Doctor K",
//                    actScence = "Operation V",
//                    code = "code V",
//                    type = "type V",
//                    size = "v",
//                    codeNo = "X",
//                    epcHeader = "4567894545678945",
//                    epcRun = "12345612",
//                    shipBox = "",
//                    storageBox = "",
//                    playBox = ""
//                    )
//            costumeDao.insert(costume)

//            var costumes = costumeDao.getAllCostumes()
//            Log.d(TAG, "VVV costumes.size=" + costumes.size)


        }
    }
}