package com.mobiiworld.mvvm.ui.detail

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.mobiiworld.mvvm.data.image.ImageRepo
import javax.inject.Inject


class DetailsViewModel @ViewModelInject
constructor(private val imageRepo: ImageRepo) : ViewModel() {

    val imageModelResponse =
        imageRepo.getImages(true)
            .asLiveData(viewModelScope.coroutineContext)

}