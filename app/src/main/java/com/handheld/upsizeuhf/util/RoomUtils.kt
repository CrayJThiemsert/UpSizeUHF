package com.handheld.upsizeuhf.util

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.room.Room
import com.handheld.upsizeuhf.dao.CostumeDao
import com.handheld.upsizeuhf.database.CostumeRoomDatabase
import com.handheld.upsizeuhf.model.Actor
import com.handheld.upsizeuhf.model.Costume
import kotlinx.coroutines.*
import java.util.ArrayList
import kotlin.system.measureTimeMillis


class RoomUtils {
    companion object {
        private val TAG = this.javaClass.simpleName
        private lateinit var todoDao: CostumeDao
        private lateinit var localDb: CostumeRoomDatabase


        fun importLocalCostumeDB(context: Context, networkCostumes: MutableList<out Costume>) {
            val time = measureTimeMillis {
//                localDb = Room.databaseBuilder(context, CostumeRoomDatabase::class.java, "costumedb").allowMainThreadQueries().build()
                localDb = Room.databaseBuilder(context, CostumeRoomDatabase::class.java, "costumedb").build()

                val zero = somethingUsefulZeroAsync()
                runBlocking { zero.await() }

                var i: Int = 0
                networkCostumes.forEach { costume ->
                    val one = doRefreshLocalCostumesAsync(i, costume)
                    runBlocking {
                        one.await()
                        i++
                    }
                }
            }
            println("Completed in $time ms")
            Thread {
//                val costumeDao = localDb.costumeDao()
//                val costumeList: List<com.handheld.upsizeuhf.entity.Costume> = costumeDao.getAllCostumes()
                val costumeList = loadLocalCostumeList(context)

                Log.d(TAG, "costumesxxx size=" + costumeList.size)
                Log.d(TAG, "costumesxxx actor=" + costumeList[0].actor)
            }.start()

        }

        fun loadLocalCostumeList(context: Context): MutableList<Costume> {
            localDb = Room.databaseBuilder(context, CostumeRoomDatabase::class.java, "costumedb").build()
            val costumeDao = localDb.costumeDao()
            val costumeList: List<com.handheld.upsizeuhf.entity.Costume> = costumeDao.getAllCostumes()

            Log.d(TAG, "loadLocalCostumeList size=" + costumeList.size)
            var returnList: MutableList<Costume> = mutableListOf<Costume>()
            costumeList.forEach { localCostume ->
                val costume: Costume = Costume(
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

            Log.d(TAG, "costumesxxx size=" + returnList.size)
            Log.d(TAG, "costumesxxx actor=" + returnList[0].actor)

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
            Log.d(TAG, "1st actor=" + returnList[0].name)

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

//            returnList = returnList.toMutableSet().toMutableList()
            Log.d(TAG, "item code filter size=" + returnList.size)
            Log.d(TAG, "1st code filter=" + returnList[0].code)

            return returnList
        }

        @OptIn(DelicateCoroutinesApi::class)
        fun somethingUsefulZeroAsync() = GlobalScope.async {
            doSomethingUsefulZero()
        }

        @OptIn(DelicateCoroutinesApi::class)
        fun doRefreshLocalCostumesAsync(i: Int, networkCostume: Costume) = GlobalScope.async {
            doRefreshLocalCostumes(i, networkCostume)
        }

        suspend fun doSomethingUsefulZero(): Boolean {
            val costumeDao = localDb.costumeDao()
            costumeDao.deleteAll()
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
            costumeDao.insert(localCostume);
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