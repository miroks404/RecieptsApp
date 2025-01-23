package ru.miroks404.recieptsapp.ui.recipes.recipe

import androidx.lifecycle.ViewModel
import ru.miroks404.recieptsapp.model.Recipe

class RecipeViewModel : ViewModel() {

    data class RecipeUIState(
        val recipe: Recipe? = null,
        val isFavorite: Boolean = false,
        val stateOfSeekbar: Int = 1,
    )

}