package ru.miroks404.recieptsapp.ui.recipes.recipesList

import android.app.Application
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.miroks404.recieptsapp.data.RecipesRepository
import ru.miroks404.recieptsapp.model.Category
import ru.miroks404.recieptsapp.model.Recipe

class RecipesListViewModel(private val application: Application) : AndroidViewModel(application) {

    enum class RecipesListState {
        DEFAULT,
        ERROR,
    }

    data class RecipesListUIState(
        val category: Category? = null,
        val recipesList: List<Recipe> = listOf(),
        val categoryImage: Drawable? = null,
        val recipesListState: RecipesListState = RecipesListState.DEFAULT,
    )

    private val _uiState = MutableLiveData(RecipesListUIState())
    val uiState: LiveData<RecipesListUIState>
        get() = _uiState

    private val data = RecipesRepository()

    fun loadRecipes(categoryId: Int) {
        data.getCategoryByCategoryId(categoryId) {
            if (it != null) {
                _uiState.postValue(_uiState.value?.copy(category = it))
            } else {
                _uiState.postValue(
                    _uiState.value?.copy(
                        category = null,
                        recipesListState = RecipesListState.ERROR
                    )
                )
            }
        }
        data.getAllRecipesByCategoryId(categoryId) {
            if (it != null) {
                _uiState.postValue(_uiState.value?.copy(recipesList = it))
            } else {
                _uiState.postValue(
                    _uiState.value?.copy(
                        recipesList = listOf(),
                        recipesListState = RecipesListState.ERROR
                    )
                )
            }
        }
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