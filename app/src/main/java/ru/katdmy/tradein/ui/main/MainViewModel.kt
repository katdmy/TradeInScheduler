package ru.katdmy.tradein.ui.main

import Ad
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.katdmy.tradein.domain.MainRepository

class MainViewModel(
    private val repo: MainRepository
) : ViewModel() {

    val loadedStatus = MutableLiveData<String>()
    val loadedData = MutableLiveData<List<Ad>>()

    fun loadData() {
        viewModelScope.launch {
            val adListJson = repo.loadAds()
            when (adListJson.status) {
                "true" -> {
                    loadedStatus.value = ""
                    loadedData.value = adListJson.data!!
                }
                "false" -> {
                    loadedStatus.value = adListJson.descriprion!!
                    loadedData.value = emptyList()
                }
                else -> {
                    loadedStatus.value = "error"
                    loadedData.value = emptyList()
                }
            }
        }
    }

}