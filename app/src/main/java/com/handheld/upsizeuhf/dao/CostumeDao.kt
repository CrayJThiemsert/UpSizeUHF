package com.handheld.upsizeuhf.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.handheld.upsizeuhf.entity.Costume
import kotlinx.coroutines.flow.Flow

/**
 * The Room Magic is in this file, where you map a method call to an SQL query.
 *
 * When you are using complex data types, such as Date, you have to also supply type converters.
 * To keep this example basic, no types that require type converters are used.
 * See the documentation at
 * https://developer.android.com/topic/libraries/architecture/room.html#type-converters
 */

@Dao
interface CostumeDao {

    // The flow always holds/caches latest version of data. Notifies its observers when the
    // data has changed.
    @Query("SELECT * FROM tblcsvimport ORDER BY actor ASC, actScence ASC")
    fun getAllCostumesFlow(): Flow<List<Costume>>

    @Query("SELECT * FROM tblcsvimport ORDER BY actor ASC, actScence ASC")
    fun getAllCostumes(): List<Costume>

//    @Insert(onConflict = OnConflictStrategy.IGNORE)
    @Insert
    fun insert(costume: Costume)

    @Query("DELETE FROM tblcsvimport")
    suspend fun deleteAll()
}