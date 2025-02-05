package ru.miroks404.recieptsapp.ui.recipes.favorites

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.miroks404.recieptsapp.Constants.KEY_SP
import ru.miroks404.recieptsapp.data.STUB
import ru.miroks404.recieptsapp.model.Recipe

class FavoritesViewModel(private val application: Application) : AndroidViewModel(application) {
    data class FavoritesUIState(
        val favoritesRecipes: List<Recipe> = listOf(),
    )

    private val _uiState = MutableLiveData(FavoritesUIState())
    val uiState: LiveData<FavoritesUIState>
        get() = _uiState

    fun loadRecipes() {
        _uiState.value = _uiState.value?.copy(
            favoritesRecipes = STUB.getRecipesByIds(loadFavoritesRecipes())
        )
    }

    private fun loadFavoritesRecipes() : HashSet<String> {
        val sharedPrefs = application.getSharedPreferences(KEY_SP , Context.MODE_PRIVATE)
        return sharedPrefs?.getStringSet(KEY_SP, null)?.toHashSet() ?: HashSet()
    }
}