package com.wadachirebandi.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wadachirebandi.data.MainRepository
import com.wadachirebandi.data.Resource
import com.wadachirebandi.data.villa.Villa
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class VillaViewModel @Inject constructor(private val mainRepository: MainRepository) : ViewModel() {

    val villa = MutableLiveData<Resource<Villa>>()

    fun villa() = viewModelScope.launch {
        villa.postValue(Resource.Loading())
        mainRepository.allVilla().let {
            villa.postValue(handleVillaResponse(it))
        }
    }

    private fun handleVillaResponse(response: Response<Villa>): Resource<Villa> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

}