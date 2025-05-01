package ru.miroks404.recieptsapp.ui.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
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
        val imageUrl: String? = null,
    )

    private val _uiState = MutableLiveData(CategoriesListUIState())
    val uiState: LiveData<CategoriesListUIState>
        get() = _uiState

    private val data = RecipesRepository()

    fun loadCategories() {
        viewModelScope.launch {
            val categories = data.getAllCategories()
            if (categories != null) {
                _uiState.postValue(_uiState.value?.copy(listOfCategories = categories))
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