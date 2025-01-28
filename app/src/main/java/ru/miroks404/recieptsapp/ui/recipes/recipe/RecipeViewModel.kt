package ru.miroks404.recieptsapp.ui.recipes.recipe

import android.app.Application
import android.content.Context
import android.graphics.drawable.Drawable
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
        val recipe: Recipe?,
        val isFavorite: Boolean,
        val stateOfSeekbar: Int,
        val recipeImage: Drawable?,
    )

    private val _uiState = MutableLiveData<RecipeUIState>()
    val uiState: LiveData<RecipeUIState>
        get() = _uiState

    fun loadRecipe(recipeId: Int) {
        _uiState.value = RecipeUIState(null, false, 1, null)
        _uiState.value = _uiState.value?.copy(recipe = STUB.getRecipeById(recipeId))
        _uiState.value = _uiState.value?.copy(
            isFavorite = recipeId.toString() in getFavorites()
        )
        _uiState.value = _uiState.value?.copy(
            recipeImage =
            try {
                Drawable.createFromStream(
                    this.application.assets?.open(_uiState.value?.recipe?.imageUrl ?: ""),
                    null
                )
            } catch (e: Exception) {
                Log.d("Not found", "Image not found: ${_uiState.value?.recipe?.imageUrl}")
                null
            }
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
        _uiState.value = _uiState.value?.copy(
            isFavorite =
            if (_uiState.value?.isFavorite == true) {
                favoritesSet.remove(_uiState.value?.recipe?.id?.toString())
                saveFavorites(favoritesSet)
                false
            } else {
                favoritesSet.add(_uiState.value?.recipe?.id?.toString() ?: "")
                saveFavorites(favoritesSet)
                true
            }
        )
    }

}