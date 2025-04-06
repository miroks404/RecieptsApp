package ru.miroks404.recieptsapp.ui.recipes.recipe

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.core.content.edit
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.miroks404.recieptsapp.Constants.KEY_SP
import ru.miroks404.recieptsapp.data.RecipesRepository
import ru.miroks404.recieptsapp.model.Recipe

class RecipeViewModel(private val application: Application) : AndroidViewModel(application) {

    enum class RecipeState {
        DEFAULT,
        ERROR
    }

    data class RecipeUIState(
        val recipe: Recipe? = null,
        val isFavorite: Boolean = false,
        val stateOfSeekbar: Int = 1,
        val recipeImage: String? = null,
        val recipeState: RecipeState = RecipeState.DEFAULT,
    )

    private val _uiState = MutableLiveData(RecipeUIState())
    val uiState: LiveData<RecipeUIState>
        get() = _uiState

    private val data = RecipesRepository()

    fun loadRecipe(recipeId: Int) {
        _uiState.value = _uiState.value?.copy(
            isFavorite = recipeId.toString() in getFavorites(),
        )
        data.getRecipeByRecipeId(recipeId) {
            if (it != null) {
                _uiState.postValue(_uiState.value?.copy(recipe = it, recipeImage = it.imageUrl))
            } else {
                _uiState.postValue(
                    _uiState.value?.copy(
                        recipe = null,
                        recipeState = RecipeState.ERROR
                    )
                )
            }
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

    fun updateStateOfSeekbar(newState: Int) {
        _uiState.value = _uiState.value?.copy(stateOfSeekbar = newState)
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

}