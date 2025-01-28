package ru.miroks404.recieptsapp.ui.recipes.recipe

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.core.content.edit
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.miroks404.recieptsapp.Constants.KEY_SP
import ru.miroks404.recieptsapp.data.STUB
import ru.miroks404.recieptsapp.model.Recipe

class RecipeViewModel(private val application: Application) : AndroidViewModel(application) {

    data class RecipeUIState(
        val recipe: Recipe? = null,
        val isFavorite: Boolean = false,
        val stateOfSeekbar: Int = 1,
    )

    private val _uiState = MutableLiveData<RecipeUIState>()
    val uiState: LiveData<RecipeUIState>
        get() = _uiState

    fun loadRecipe(recipeId: Int) {
        _uiState.value = RecipeUIState(STUB.getRecipeById(recipeId))
        _uiState.value = _uiState.value?.copy(
            isFavorite = recipeId.toString() in getFavorites()
        )
    }

    private fun getFavorites(): HashSet<String> {
        val sharedPrefs = application.getSharedPreferences(
            KEY_SP, Context.MODE_PRIVATE
        )
        return sharedPrefs?.getStringSet(KEY_SP, null)?.toHashSet() ?: HashSet()
    }

    private fun saveFavorites(setOfId: MutableSet<String>) {
        val sharedPrefs = application.getSharedPreferences(
            KEY_SP, Context.MODE_PRIVATE
        )
        sharedPrefs?.edit {
            putStringSet(KEY_SP, setOfId)
            Log.d("!!!", "saveFavorites: correct save")
            apply()
        }
    }

    fun onFavoritesClicked() {
        val favoritesSet = getFavorites().toMutableSet()
        if (_uiState.value?.isFavorite == true) {
            _uiState.value = _uiState.value?.copy(isFavorite = false)
            favoritesSet.remove(_uiState.value?.recipe?.id?.toString())
            saveFavorites(favoritesSet)
        } else {
            _uiState.value = _uiState.value?.copy(isFavorite = true)
            favoritesSet.add(_uiState.value?.recipe?.id?.toString() ?: "")
            saveFavorites(favoritesSet)
        }
    }

}