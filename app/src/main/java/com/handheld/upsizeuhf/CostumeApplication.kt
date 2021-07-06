package com.handheld.upsizeuhf

import android.app.Application
import com.handheld.upsizeuhf.database.CostumeRoomDatabase
import com.handheld.upsizeuhf.repository.CostumeRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob


class CostumeApplication : Application() {
    // No need to cancel this scope as it'll be torn down with the process
    val applicationScope = CoroutineScope(SupervisorJob())

    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts
    val database by lazy { CostumeRoomDatabase.getDatabase(this, applicationScope) }
    val repository by lazy { CostumeRepository(database.costumeDao()) }
}