package com.handheld.upsizeuhf.viewmodel

import androidx.lifecycle.*
import com.handheld.upsizeuhf.entity.Costume
import com.handheld.upsizeuhf.entity.PlayBox
import com.handheld.upsizeuhf.entity.ShipBox
import com.handheld.upsizeuhf.entity.StorageBox
import com.handheld.upsizeuhf.repository.CostumeRepository
import kotlinx.coroutines.launch


/**
 * View Model to keep a reference to the word repository and
 * an up-to-date list of all words.
 */

class CostumeViewModel(private val repository: CostumeRepository) : ViewModel() {

    // Using LiveData and caching what allCostumes returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allCostumesFlow: LiveData<List<Costume>> = repository.allCostumesFlow.asLiveData()
    val allCostumes: List<Costume> = repository.allCostumes

    val allShipBoxesFlow: LiveData<List<ShipBox>> = repository.allShipBoxesFlow.asLiveData()
    val allShipBoxes: List<ShipBox> = repository.allShipBoxes

    val allStorageBoxesFlow: LiveData<List<StorageBox>> = repository.allStorageBoxesFlow.asLiveData()
    val allStorageBoxes: List<StorageBox> = repository.allStorageBoxes

    val allPlayBoxesFlow: LiveData<List<PlayBox>> = repository.allPlayBoxesFlow.asLiveData()
    val allPlayBoxes: List<PlayBox> = repository.allPlayBoxes

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(word: Costume) = viewModelScope.launch {
        repository.insert(word)
    }
}

class CostumeViewModelFactory(private val repository: CostumeRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CostumeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CostumeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}