package ru.miroks404.recieptsapp.ui.recipes.recipesList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.miroks404.recieptsapp.data.RecipesRepository
import ru.miroks404.recieptsapp.model.Category
import ru.miroks404.recieptsapp.model.Recipe

class RecipesListViewModel : ViewModel() {

    enum class RecipesListState {
        DEFAULT,
        ERROR,
    }

    data class RecipesListUIState(
        val category: Category? = null,
        val recipesList: List<Recipe> = listOf(),
        val categoryImage: String? = null,
        val recipesListState: RecipesListState = RecipesListState.DEFAULT,
    )

    private val _uiState = MutableLiveData(RecipesListUIState())
    val uiState: LiveData<RecipesListUIState>
        get() = _uiState

    private val data = RecipesRepository()

    fun loadRecipes(categoryId: Int) {
        viewModelScope.launch {
            val category = data.getCategoryByCategoryId(categoryId)
            if (category != null) {
                _uiState.postValue(_uiState.value?.copy(category = category, categoryImage = category.imageUrl))
            } else {
                _uiState.postValue(
                    _uiState.value?.copy(
                        category = null,
                        recipesListState = RecipesListState.ERROR
                    )
                )
            }

            val allRecipes = data.getAllRecipesByCategoryId(categoryId)
            if (allRecipes != null) {
                _uiState.postValue(_uiState.value?.copy(recipesList = allRecipes))
            } else {
                _uiState.postValue(
                    _uiState.value?.copy(
                        recipesList = listOf(),
                        recipesListState = RecipesListState.ERROR
                    )
                )
            }
        }
    }

}