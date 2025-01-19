package ru.miroks404.recieptsapp.ui.recipes

import androidx.lifecycle.ViewModel
import ru.miroks404.recieptsapp.model.Recipe

data class RecipeUIState(
    val recipe: Recipe? = null
)

class RecipeViewModel : ViewModel() {
}