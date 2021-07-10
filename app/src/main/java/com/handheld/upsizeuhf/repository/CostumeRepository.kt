package com.handheld.upsizeuhf.repository

import androidx.annotation.WorkerThread
import com.handheld.upsizeuhf.dao.CostumeDao
import com.handheld.upsizeuhf.entity.Costume
import com.handheld.upsizeuhf.entity.PlayBox
import com.handheld.upsizeuhf.entity.ShipBox
import com.handheld.upsizeuhf.entity.StorageBox
import kotlinx.coroutines.flow.Flow


/**
 * Abstracted Repository as promoted by the Architecture Guide.
 * https://developer.android.com/topic/libraries/architecture/guide.html
 */
class CostumeRepository(private val costumeDao: CostumeDao) {

    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    val allCostumesFlow: Flow<List<Costume>> = costumeDao.getAllCostumesFlow()
    val allCostumes: List<Costume> = costumeDao.getAllCostumes()

    val allShipBoxesFlow: Flow<List<ShipBox>> = costumeDao.getAllShipBoxesFlow()
    val allShipBoxes: List<ShipBox> = costumeDao.getAllShipBoxes()

    val allStorageBoxesFlow: Flow<List<StorageBox>> = costumeDao.getAllStorageBoxesFlow()
    val allStorageBoxes: List<StorageBox> = costumeDao.getAllStorageBoxes()

    val allPlayBoxesFlow: Flow<List<PlayBox>> = costumeDao.getAllPlayBoxesFlow()
    val allPlayBoxes: List<PlayBox> = costumeDao.getAllPlayBoxes()


    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(costume: Costume) {
        costumeDao.insertCostume(costume)
    }
}