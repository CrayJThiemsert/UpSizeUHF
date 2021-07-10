package com.handheld.upsizeuhf.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.handheld.upsizeuhf.entity.Costume
import com.handheld.upsizeuhf.entity.PlayBox
import com.handheld.upsizeuhf.entity.ShipBox
import com.handheld.upsizeuhf.entity.StorageBox
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
    // start tblcsvimport ==================================================
    // The flow always holds/caches latest version of data. Notifies its observers when the
    // data has changed.
    @Query("SELECT * FROM tblcsvimport ORDER BY actor ASC, actScence ASC")
    fun getAllCostumesFlow(): Flow<List<Costume>>

    @Query("SELECT * FROM tblcsvimport ORDER BY actor ASC, actScence ASC")
    fun getAllCostumes(): List<Costume>

//    @Insert(onConflict = OnConflictStrategy.IGNORE)
    @Insert
    fun insertCostume(costume: Costume)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllCostume(vararg costume: Costume)

    @Query("DELETE FROM tblcsvimport")
    suspend fun deleteAllCostume()

    // end tblcsvimport ==================================================
    // start tblshipbox ==================================================
    @Query("SELECT * FROM tblshipbox ORDER BY name ASC")
    fun getAllShipBoxesFlow(): Flow<List<ShipBox>>

    @Query("SELECT * FROM tblshipbox ORDER BY name ASC")
    fun getAllShipBoxes(): List<ShipBox>

    @Insert
    fun insertShipBox(shipBox: ShipBox)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllShipBox(vararg shipBox: ShipBox)

    @Query("DELETE FROM tblshipbox")
    suspend fun deleteAllShipBox()
    // end tblshipbox ==================================================
    // start tblstoragebox ==================================================
    @Query("SELECT * FROM tblstoragebox ORDER BY name ASC")
    fun getAllStorageBoxesFlow(): Flow<List<StorageBox>>

    @Query("SELECT * FROM tblstoragebox ORDER BY name ASC")
    fun getAllStorageBoxes(): List<StorageBox>

    @Insert
    fun insertStorageBox(shipBox: StorageBox)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllStorageBox(vararg storageBox: StorageBox)

    @Query("DELETE FROM tblstoragebox")
    suspend fun deleteAllStorageBox()
    // end tblstoragebox ==================================================
    // start tblplaybox ==================================================
    @Query("SELECT * FROM tblplaybox ORDER BY name ASC")
    fun getAllPlayBoxesFlow(): Flow<List<PlayBox>>

    @Query("SELECT * FROM tblplaybox ORDER BY name ASC")
    fun getAllPlayBoxes(): List<PlayBox>

    @Insert
    fun insertPlayBox(playBox: PlayBox)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllPlayBox(vararg playBox: PlayBox)

    @Query("DELETE FROM tblplaybox")
    suspend fun deleteAllPlayBox()
    // end tblplaybox ==================================================

}