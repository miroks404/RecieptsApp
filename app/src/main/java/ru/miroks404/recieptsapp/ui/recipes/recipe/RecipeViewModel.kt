package ru.miroks404.recieptsapp.ui.recipes.recipe

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.miroks404.recieptsapp.model.Recipe

class RecipeViewModel : ViewModel() {

    data class RecipeUIState(
        val recipe: Recipe? = null,
        val isFavorite: Boolean = false,
        val stateOfSeekbar: Int = 1,
    )

    private val _uiState = MutableLiveData<RecipeUIState>()
    val uiState: LiveData<RecipeUIState>
        get() = _uiState

    init {
        Log.d("recipeViewModel", "viewmodel activated")
        _uiState.value = RecipeUIState(isFavorite = true)
    }

}