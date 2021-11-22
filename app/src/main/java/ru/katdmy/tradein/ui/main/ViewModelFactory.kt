package ru.katdmy.tradein.ui.main

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import ru.katdmy.tradein.domain.MainRepository
import ru.katdmy.tradein.domain.retrofit.RetrofitClient

class ViewModelFactory(
    activity: FragmentActivity,
    defaultArgs: Bundle? = null
): AbstractSavedStateViewModelFactory(activity, defaultArgs) {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T = when (modelClass) {
        MainViewModel::class.java -> {
            MainViewModel(
                MainRepository(
                    RetrofitClient.smkhApi
                )
            )
        }
        else -> throw IllegalArgumentException("$modelClass is not registered ViewModel.")
    } as T

}