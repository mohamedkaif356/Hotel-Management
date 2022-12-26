package com.wadachirebandi.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wadachirebandi.data.entry.Entry
import com.wadachirebandi.data.MainRepository
import com.wadachirebandi.data.Resource
import com.wadachirebandi.data.entry.TodayEntry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class EntryViewModel @Inject constructor(private val mainRepository: MainRepository) : ViewModel() {

    val entryResource = MutableLiveData<Resource<Any>>()

    val dailyEntry = MutableLiveData<Resource<TodayEntry>>()

    val deleteEntryResource = MutableLiveData<Resource<Any>>()

    fun entry(entry: Entry) = viewModelScope.launch {
        entryResource.postValue(Resource.Loading())
        mainRepository.entry(entry).let {
            entryResource.postValue(handleEntryResponse(it))
        }
    }

    private fun handleEntryResponse(response: Response<Any>): Resource<Any> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun dailyEntry() = viewModelScope.launch {
        dailyEntry.postValue(Resource.Loading())
        mainRepository.todayEntry().let {
            dailyEntry.postValue(handleDailyEntryResponse(it))
        }
    }

    private fun handleDailyEntryResponse(response: Response<TodayEntry>): Resource<TodayEntry> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun deleteEntry(villaId: String) = viewModelScope.launch {
        deleteEntryResource.postValue(Resource.Loading())
        mainRepository.deleteEntry(villaId).let {
            deleteEntryResource.postValue(handleEntryResponse(it))
        }
    }

}