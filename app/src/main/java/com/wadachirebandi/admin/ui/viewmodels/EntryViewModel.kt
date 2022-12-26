package com.wadachirebandi.admin.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wadachirebandi.admin.data.entry.Entry
import com.wadachirebandi.admin.data.MainRepository
import com.wadachirebandi.admin.data.Resource
import com.wadachirebandi.admin.data.entry.SingleEntry
import com.wadachirebandi.admin.data.villa.VillaEntry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class EntryViewModel @Inject constructor(private val mainRepository: MainRepository) : ViewModel() {

    val dateEntry = MutableLiveData<Resource<Entry>>()

    val villaEntry = MutableLiveData<Resource<VillaEntry>>()

    val singleEntry = MutableLiveData<Resource<SingleEntry>>()

    fun entryByDate(villaId: String, startDate: String, endDate: String) = viewModelScope.launch {
        dateEntry.postValue(Resource.Loading())
        mainRepository.entryByDate(villaId, startDate, endDate).let {
            dateEntry.postValue(handleDateEntryResponse(it))
        }
    }

    private fun handleDateEntryResponse(response: Response<Entry>): Resource<Entry> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun entryByVilla(villaId: String) = viewModelScope.launch {
        villaEntry.postValue(Resource.Loading())
        mainRepository.entryByVilla(villaId).let {
            villaEntry.postValue(handleVillaEntryResponse(it))
        }
    }

    private fun handleVillaEntryResponse(response: Response<VillaEntry>): Resource<VillaEntry> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun singleEntry(entryId: String) = viewModelScope.launch {
        singleEntry.postValue(Resource.Loading())
        mainRepository.singleEntry(entryId).let {
            singleEntry.postValue(handleSingleEntryResponse(it))
        }
    }

    private fun handleSingleEntryResponse(response: Response<SingleEntry>): Resource<SingleEntry> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }
}