package ru.miroks404.recieptsapp.ui.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.miroks404.recieptsapp.data.RecipesRepository
import ru.miroks404.recieptsapp.model.Category

class CategoriesListViewModel : ViewModel() {

    enum class CategoriesListState {
        DEFAULT,
        ERROR,
    }

    data class CategoriesListUIState(
        val listOfCategories: List<Category> = listOf(),
        val categoriesListState: CategoriesListState = CategoriesListState.DEFAULT,
    )

    private val _uiState = MutableLiveData(CategoriesListUIState())
    val uiState: LiveData<CategoriesListUIState>
        get() = _uiState

    private val data = RecipesRepository()

    fun loadCategories() {
        data.getAllCategories {
            if (it != null) {
                _uiState.postValue(_uiState.value?.copy(listOfCategories = it))
            } else {
                _uiState.postValue(
                    _uiState.value?.copy(
                        listOfCategories = listOf(),
                        categoriesListState = CategoriesListState.ERROR
                    )
                )
            }
        }
    }

}