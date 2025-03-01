package ru.miroks404.recieptsapp.ui.recipes.favorites

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.miroks404.recieptsapp.Constants.KEY_SP
import ru.miroks404.recieptsapp.data.RecipesRepository
import ru.miroks404.recieptsapp.model.Recipe

class FavoritesViewModel(private val application: Application) : AndroidViewModel(application) {

    enum class FavoritesState {
        DEFAULT,
        ERROR
    }

    data class FavoritesUIState(
        val favoritesRecipes: List<Recipe>? = listOf(),
        val favoritesState: FavoritesState = FavoritesState.DEFAULT,
    )

    private val _uiState = MutableLiveData(FavoritesUIState())
    val uiState: LiveData<FavoritesUIState>
        get() = _uiState

    private val data = RecipesRepository()

    fun loadRecipes() {
        data.getAllRecipesByIds(loadFavoritesRecipes().joinToString { "," }) {
            if (it != null) {
                _uiState.postValue(_uiState.value?.copy(favoritesRecipes = it))
            } else {
                _uiState.postValue(
                    _uiState.value?.copy(
                        favoritesRecipes = null,
                        favoritesState = FavoritesState.ERROR
                    )
                )
            }
        }

    }

    private fun loadFavoritesRecipes(): HashSet<String> {
        val sharedPrefs = application.getSharedPreferences(KEY_SP, Context.MODE_PRIVATE)
        return sharedPrefs?.getStringSet(KEY_SP, null)?.toHashSet() ?: HashSet()
    }
}