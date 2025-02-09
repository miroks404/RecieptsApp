package ru.miroks404.recieptsapp.ui.recipes.recipesList

import android.app.Application
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.miroks404.recieptsapp.data.STUB
import ru.miroks404.recieptsapp.model.Category
import ru.miroks404.recieptsapp.model.Recipe

class RecipesListViewModel(private val application: Application) : AndroidViewModel(application) {

    data class RecipesListUIState(
        val category: Category? = null,
        val recipesList: List<Recipe>? = null,
        val categoryImage: Drawable? = null,
    )

    private val _uiState = MutableLiveData(RecipesListUIState())
    val uiState: LiveData<RecipesListUIState>
        get() = _uiState

    fun loadRecipes(categoryId: Int) {
        _uiState.value = _uiState.value?.copy(
            category = STUB.getCategoryById(categoryId),
            recipesList = STUB.getRecipesByCategoryId(categoryId)
        )
        _uiState.value = _uiState.value?.copy(
            categoryImage = try {
                Drawable.createFromStream(
                    application.assets?.open(
                        _uiState.value?.category?.imageUrl ?: ""
                    ), null
                )
            } catch (e: Exception) {
                Log.d("Not found", "Image not found: ${_uiState.value?.category?.imageUrl}")
                null
            }
        )
    }
}