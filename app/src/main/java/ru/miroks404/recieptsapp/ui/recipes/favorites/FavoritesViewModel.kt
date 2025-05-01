package ru.miroks404.recieptsapp.ui.recipes.favorites

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
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
        val favoritesRecipes = loadFavoritesRecipes().joinToString(",")
        if (favoritesRecipes.isNotEmpty()) {
            viewModelScope.launch {
                val allRecipes =data.getAllRecipesByIds(favoritesRecipes)
                if (allRecipes != null) {
                    _uiState.postValue(_uiState.value?.copy(favoritesRecipes = allRecipes))
                } else {
                    _uiState.postValue(
                        _uiState.value?.copy(
                            favoritesRecipes = null,
                            favoritesState = FavoritesState.ERROR
                        )
                    )
                }
            }
        } else {
            _uiState.postValue(
                uiState.value?.copy(
                    favoritesRecipes = null
                )
            )
        }


    }

    private fun loadFavoritesRecipes(): HashSet<String> {
        val sharedPrefs = application.getSharedPreferences(KEY_SP, Context.MODE_PRIVATE)
        return sharedPrefs?.getStringSet(KEY_SP, null)?.toHashSet() ?: HashSet()
    }
}