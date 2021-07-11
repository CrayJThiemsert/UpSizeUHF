package com.handheld.upsizeuhf.util

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.room.Room
import com.handheld.upsizeuhf.dao.CostumeDao
import com.handheld.upsizeuhf.database.CostumeRoomDatabase
import com.handheld.upsizeuhf.model.Actor
import com.handheld.upsizeuhf.model.Box
import com.handheld.upsizeuhf.model.Costume
import kotlinx.coroutines.*
import java.lang.Exception
import java.util.ArrayList
import kotlin.system.measureTimeMillis


class RoomUtils {
    companion object {
        private val TAG = this.javaClass.simpleName
        private lateinit var todoDao: CostumeDao
        private lateinit var localDb: CostumeRoomDatabase


        fun importLocalCostumeDB(context: Context, networkCostumes: MutableList<Costume>) {
            val time = measureTimeMillis {
                try {


                    //                localDb = Room.databaseBuilder(context, CostumeRoomDatabase::class.java, "costumedb").allowMainThreadQueries().build()
                    localDb = Room.databaseBuilder(context, CostumeRoomDatabase::class.java, "costumedb").build()

                    // Clear
                    val deleteCostumeTask = deleteAllLocalCostumeAsync()
                    runBlocking { deleteCostumeTask.await() }

                    // =========================================
                    // Insert all new
                    // Run batch insert, fast
                    val insertAllCostumesTask = doBatchInsertAllLocalCostumesAsync(networkCostumes)
                    runBlocking {
                        insertAllCostumesTask.await()
                    }
                }catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    // localDb.close()
                }
            }
            println("Completed in $time ms")
//            Thread {
//                val costumeList = loadLocalCostumeList(context)
//
//                Log.d(TAG, "costumesxx1 size=" + costumeList.size)
//            }.start()

        }

        fun importLocalShipBoxDB(context: Context, networkShipBoxes: MutableList<Box>) {
            val time = measureTimeMillis {
                try {
                    localDb = Room.databaseBuilder(context, CostumeRoomDatabase::class.java, "costumedb").build()

                    // Clear
                    val deleteShipBoxTask = deleteAllLocalShipBoxAsync()
                    runBlocking { deleteShipBoxTask.await() }

                    // =========================================
                    // Insert all new
                    // Run batch insert, fast
                    val insertAllShipBoxesTask = doBatchInsertAllLocalShipBoxesAsync(networkShipBoxes)
                    runBlocking {
                        insertAllShipBoxesTask.await()
                    }
                }catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    // localDb.close()
                }

            }
            println("Completed in $time ms")
        }

        fun importLocalStorageBoxDB(context: Context, networkStorageBoxes: MutableList<Box>) {
            val time = measureTimeMillis {
                try {


                    localDb = Room.databaseBuilder(context, CostumeRoomDatabase::class.java, "costumedb").build()

                    // Clear
                    val deleteStorageBoxTask = deleteAllLocalStorageBoxAsync()
                    runBlocking { deleteStorageBoxTask.await() }

                    // =========================================
                    // Insert all new
                    // Run batch insert, fast
                    val insertAllStorageBoxesTask = doBatchInsertAllLocalStorageBoxesAsync(networkStorageBoxes)
                    runBlocking {
                        insertAllStorageBoxesTask.await()
                    }
                }catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    // localDb.close()
                }

            }
            println("Completed in $time ms")
        }

        fun importLocalPlayBoxDB(context: Context, networkPlayBoxes: MutableList<Box>) {
            val time = measureTimeMillis {
                try {
                    localDb = Room.databaseBuilder(context, CostumeRoomDatabase::class.java, "costumedb").build()

                    // Clear
                    val deletePlayBoxTask = deleteAllLocalPlayBoxAsync()
                    runBlocking { deletePlayBoxTask.await() }

                    // =========================================
                    // Insert all new
                    // Run batch insert, fast
                    val insertAllPlayBoxesTask = doBatchInsertAllLocalPlayBoxesAsync(networkPlayBoxes)
                    runBlocking {
                        insertAllPlayBoxesTask.await()
                    }
                }catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    // localDb.close()
                }

            }
            println("Completed in $time ms")
        }

        fun loadLocalCostumeList(context: Context): MutableList<Costume> {
            var returnList: MutableList<Costume> = mutableListOf<Costume>()
            try {
                localDb = Room.databaseBuilder(context, CostumeRoomDatabase::class.java, "costumedb").build()
                val costumeDao = localDb.costumeDao()
                val costumeList: List<com.handheld.upsizeuhf.entity.Costume> = costumeDao.getAllCostumes()

                Log.d(TAG, "loadLocalCostumeList size=" + costumeList.size)

                costumeList.forEach { localCostume ->
                    val costume: Costume = Costume(
                            localCostume.uid,
                            localCostume.runningNo,
                            localCostume.actor,
                            localCostume.actScence,
                            localCostume.code,
                            localCostume.type,
                            localCostume.size,
                            localCostume.codeNo,
                            localCostume.epcHeader,
                            localCostume.epcRun,
                            localCostume.shipBox,
                            localCostume.storageBox,
                            localCostume.playBox
                    )
                    returnList.add(costume)
                }
            }catch (e: Exception) {
                e.printStackTrace()
            } finally {
                // localDb.close()
            }

            Log.d(TAG, "costumesxx2 size=" + returnList.size)
//            Log.d(TAG, "costumesxxx actor=" + returnList[0].actor)

            return returnList
        }

        fun loadLocalShipBoxList(context: Context): MutableList<Box> {
            var returnList: MutableList<Box> = mutableListOf<Box>()

            try {
                localDb = Room.databaseBuilder(context, CostumeRoomDatabase::class.java, "costumedb").build()
                val costumeDao = localDb.costumeDao()
                val shipBoxList: List<com.handheld.upsizeuhf.entity.ShipBox> = costumeDao.getAllShipBoxes()

                Log.d(TAG, "loadLocalShipBoxList size=" + shipBoxList.size)

                shipBoxList.forEach { localShipBox ->
                    val box: Box = Box(
                            localShipBox.uid,
                            localShipBox.name,
                            localShipBox.epc,
                            localShipBox.epcHeader,
                            localShipBox.epcRun
                    )
                    returnList.add(box)
                }
            }catch (e: Exception) {
                e.printStackTrace()
            } finally {
                // localDb.close()
            }

            Log.d(TAG, "shipBox list size=" + returnList.size)

            return returnList
        }

        fun loadLocalStorageBoxList(context: Context): MutableList<Box> {
            var returnList: MutableList<Box> = mutableListOf<Box>()
            try {
                localDb = Room.databaseBuilder(context, CostumeRoomDatabase::class.java, "costumedb").build()
                val costumeDao = localDb.costumeDao()
                val storageBoxList: List<com.handheld.upsizeuhf.entity.StorageBox> = costumeDao.getAllStorageBoxes()

                Log.d(TAG, "loadLocalStorageBoxList size=" + storageBoxList.size)

                storageBoxList.forEach { localStorageBox ->
                    val box: Box = Box(
                            localStorageBox.uid,
                            localStorageBox.name,
                            localStorageBox.epc,
                            localStorageBox.epcHeader,
                            localStorageBox.epcRun
                    )
                    returnList.add(box)
                }
            }catch (e: Exception) {
                e.printStackTrace()
            } finally {
                // localDb.close()
            }

            Log.d(TAG, "storageBox list size=" + returnList.size)

            return returnList
        }

        fun loadLocalPlayBoxList(context: Context): MutableList<Box> {
            var returnList: MutableList<Box> = mutableListOf<Box>()
            try {
                localDb = Room.databaseBuilder(context, CostumeRoomDatabase::class.java, "costumedb").build()
                val costumeDao = localDb.costumeDao()
                val playBoxList: List<com.handheld.upsizeuhf.entity.PlayBox> = costumeDao.getAllPlayBoxes()

                Log.d(TAG, "loadLocalPlayBoxList size=" + playBoxList.size)

                playBoxList.forEach { localPlayBox ->
                    val box: Box = Box(
                            localPlayBox.uid,
                            localPlayBox.name,
                            localPlayBox.epc,
                            localPlayBox.epcHeader,
                            localPlayBox.epcRun
                    )
                    returnList.add(box)
                }
            }catch (e: Exception) {
                e.printStackTrace()
            } finally {
                // localDb.close()
            }

            Log.d(TAG, "playBox list size=" + returnList.size)

            return returnList
        }

        fun loadActorList(context: Context, costumeArrayList: ArrayList<Costume>): MutableList<Actor> {
            Log.d(TAG, "costumeArrayList size=" + costumeArrayList.size)
            var nameList: MutableList<String> = mutableListOf<String>()
            var returnList: MutableList<Actor> = mutableListOf<Actor>()

            costumeArrayList.forEach { localCostume ->

                nameList.add(localCostume.actor.trim())
            }
            Log.d(TAG, "before nameList size=" + nameList.size)
            nameList = nameList.distinct().toMutableList()
            Log.d(TAG, "after distinct nameList size=" + nameList.size)
            var i = 1
            nameList.forEach { name ->
                val actor: Actor = Actor(
                        i.toString(),
                        name
                )
                i++
                returnList.add(actor)
            }

            returnList = returnList.toMutableSet().toMutableList()
            Log.d(TAG, "actors size=" + returnList.size)
//            Log.d(TAG, "1st actor=" + returnList[0].name)

            return returnList//.toMutableSet().toMutableList()
        }

        fun loadItemCodeFilterList(context: Context, costumeArrayList: ArrayList<Costume>): MutableList<Costume> {
            Log.d(TAG, "costumeArrayList size=" + costumeArrayList.size)
            var nameList: MutableList<String> = mutableListOf<String>()
            var returnList: MutableList<Costume> = mutableListOf<Costume>()

            costumeArrayList.forEach { localCostume ->
                nameList.add(localCostume.code.trim()+"^"+localCostume.type.trim()+"^"+localCostume.size.trim()+"^"+localCostume.codeNo.trim())
            }
            Log.d(TAG, "before nameList size=" + nameList.size)
            nameList = nameList.distinct().toMutableList()
            nameList.sort()
            Log.d(TAG, "after distinct nameList size=" + nameList.size)
            nameList.forEach { name ->
                val codeSplit = name.split("^")
                if(codeSplit.size == 4) {
                    var costumeItemCode = Costume(
                            -1,
                            "",
                            "",
                            "",
                            codeSplit[0],
                            codeSplit[1],
                            codeSplit[2],
                            codeSplit[3],
                            "",
                            "",
                            "",
                            "",
                            ""
                    )
                    returnList.add(costumeItemCode)
                }
            }

            Log.d(TAG, "item code filter size=" + returnList.size)
            return returnList
        }

        @OptIn(DelicateCoroutinesApi::class)
        fun deleteAllLocalCostumeAsync() = GlobalScope.async {
            doDeleteAllLocalCostume()
        }

        @OptIn(DelicateCoroutinesApi::class)
        fun deleteAllLocalShipBoxAsync() = GlobalScope.async {
            doDeleteAllLocalShipBox()
        }

        @OptIn(DelicateCoroutinesApi::class)
        fun deleteAllLocalStorageBoxAsync() = GlobalScope.async {
            doDeleteAllLocalStorageBox()
        }

        @OptIn(DelicateCoroutinesApi::class)
        fun deleteAllLocalPlayBoxAsync() = GlobalScope.async {
            doDeleteAllLocalPlayBox()
        }

        @OptIn(DelicateCoroutinesApi::class)
        fun doRefreshLocalCostumesAsync(i: Int, networkCostume: Costume) = GlobalScope.async {
            doRefreshLocalCostumes(i, networkCostume)
        }

        @OptIn(DelicateCoroutinesApi::class)
        fun doBatchInsertAllLocalCostumesAsync(networkCostumes: MutableList<Costume>) = GlobalScope.async {
            doBatchInsertAllLocalCostumes(networkCostumes)
        }

        @OptIn(DelicateCoroutinesApi::class)
        fun doBatchInsertAllLocalShipBoxesAsync(networkShipBoxes: MutableList<Box>) = GlobalScope.async {
            doBatchInsertAllLocalShipBoxes(networkShipBoxes)
        }

        @OptIn(DelicateCoroutinesApi::class)
        fun doBatchInsertAllLocalStorageBoxesAsync(networkStorageBoxes: MutableList<Box>) = GlobalScope.async {
            doBatchInsertAllLocalStorageBoxes(networkStorageBoxes)
        }

        @OptIn(DelicateCoroutinesApi::class)
        fun doBatchInsertAllLocalPlayBoxesAsync(networkPlayBoxes: MutableList<Box>) = GlobalScope.async {
            doBatchInsertAllLocalPlayBoxes(networkPlayBoxes)
        }

        suspend fun doDeleteAllLocalCostume(): Boolean {
            val costumeDao = localDb.costumeDao()
            costumeDao.deleteAllCostume()
            return true
        }

        suspend fun doDeleteAllLocalShipBox(): Boolean {
            val costumeDao = localDb.costumeDao()
            costumeDao.deleteAllShipBox()
            return true
        }

        suspend fun doDeleteAllLocalStorageBox(): Boolean {
            val costumeDao = localDb.costumeDao()
            costumeDao.deleteAllStorageBox()
            return true
        }

        suspend fun doDeleteAllLocalPlayBox(): Boolean {
            val costumeDao = localDb.costumeDao()
            costumeDao.deleteAllPlayBox()
            return true
        }

        suspend fun doBatchInsertAllLocalCostumes(networkCostumes: MutableList<Costume>): Boolean {
            val costumeDao = localDb.costumeDao()
//            print(networkCostume.actor)
            var localCostumes: MutableList<com.handheld.upsizeuhf.entity.Costume> = mutableListOf<com.handheld.upsizeuhf.entity.Costume>()
            var i: Int = 1
            networkCostumes.forEach { networkCostumes ->
                var localCostume = com.handheld.upsizeuhf.entity.Costume(
                        i,
                        networkCostumes.runningNo,
                        networkCostumes.actor,
                        networkCostumes.actScence,
                        networkCostumes.code,
                        networkCostumes.type,
                        networkCostumes.size,
                        networkCostumes.codeNo,
                        networkCostumes.epcHeader,
                        networkCostumes.epcRun,
                        networkCostumes.shipBox,
                        networkCostumes.storageBox,
                        networkCostumes.playBox
                );
                i++
                localCostumes.add(localCostume)
            }

            costumeDao.insertAllCostume(*localCostumes.toTypedArray());
            return true
        }

        suspend fun doBatchInsertAllLocalShipBoxes(networkShipBoxes: MutableList<Box>): Boolean {
            val costumeDao = localDb.costumeDao()
            var localShipBoxes: MutableList<com.handheld.upsizeuhf.entity.ShipBox> = mutableListOf<com.handheld.upsizeuhf.entity.ShipBox>()
            var i: Int = 1
            networkShipBoxes.forEach { networkShipBox ->
                var localShipBox = com.handheld.upsizeuhf.entity.ShipBox(
                        i,
                        networkShipBox.name,
                        networkShipBox.epc,
                        networkShipBox.epcHeader,
                        networkShipBox.epcRun,
                        true
                );
                i++
                localShipBoxes.add(localShipBox)
            }

            costumeDao.insertAllShipBox(*localShipBoxes.toTypedArray());
            return true
        }

        suspend fun doBatchInsertAllLocalStorageBoxes(networkStorageBoxes: MutableList<Box>): Boolean {
            val costumeDao = localDb.costumeDao()
            var localStorageBoxes: MutableList<com.handheld.upsizeuhf.entity.StorageBox> = mutableListOf<com.handheld.upsizeuhf.entity.StorageBox>()
            var i: Int = 1
            networkStorageBoxes.forEach { networkStorageBox ->
                var localStorageBox = com.handheld.upsizeuhf.entity.StorageBox(
                        i,
                        networkStorageBox.name,
                        networkStorageBox.epc,
                        networkStorageBox.epcHeader,
                        networkStorageBox.epcRun,
                        true
                );
                i++
                localStorageBoxes.add(localStorageBox)
            }

            costumeDao.insertAllStorageBox(*localStorageBoxes.toTypedArray());
            return true
        }

        suspend fun doBatchInsertAllLocalPlayBoxes(networkPlayBoxes: MutableList<Box>): Boolean {
            val costumeDao = localDb.costumeDao()
            var localPlayBoxes: MutableList<com.handheld.upsizeuhf.entity.PlayBox> = mutableListOf<com.handheld.upsizeuhf.entity.PlayBox>()
            var i: Int = 1
            networkPlayBoxes.forEach { networkPlayBox ->
                var localPlayBox = com.handheld.upsizeuhf.entity.PlayBox(
                        i,
                        networkPlayBox.name,
                        networkPlayBox.epc,
                        networkPlayBox.epcHeader,
                        networkPlayBox.epcRun,
                        true
                );
                i++
                localPlayBoxes.add(localPlayBox)
            }

            costumeDao.insertAllPlayBox(*localPlayBoxes.toTypedArray());
            return true
        }

        suspend fun doRefreshLocalCostumes(i: Int, networkCostume: Costume): Boolean {
            val costumeDao = localDb.costumeDao()
//            print(networkCostume.actor)

            var localCostume = com.handheld.upsizeuhf.entity.Costume(
                    i,
                    networkCostume.runningNo,
                    networkCostume.actor,
                    networkCostume.actScence,
                    networkCostume.code,
                    networkCostume.type,
                    networkCostume.size,
                    networkCostume.codeNo,
                    networkCostume.epcHeader,
                    networkCostume.epcRun,
                    networkCostume.shipBox,
                    networkCostume.storageBox,
                    networkCostume.playBox
            );
            costumeDao.insertCostume(localCostume);
            return true
        }

        fun createDb(activity: Activity) {

            val db = Room.inMemoryDatabaseBuilder(
                    activity, CostumeRoomDatabase::class.java).build()

            var todoDao = db.costumeDao()
            Log.d(TAG, "createDbxxx size=")

//            Handler().postDelayed(
//                    {
//                        todoDao.getAllCostumes().observe(
//                                this,
//                                Observer<List<Costume>> { t ->
//                                    t?.let {
//                                        Log.v(TAG, "here")
//                                        for (word in t) {
//                                            Log.v(TAG, word.actor)
//                                        }
//                                    }
//                                }
//                        )
//                    },
//                    3000
//            )

//            var costumes = todoDao.getAllCostumes()
//
//            Log.d(TAG, "costumesxxx size=" + costumes.size)
        }

    }
}