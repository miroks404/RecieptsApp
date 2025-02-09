package ru.miroks404.recieptsapp.ui.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.miroks404.recieptsapp.data.STUB
import ru.miroks404.recieptsapp.model.Category

class CategoriesListViewModel : ViewModel() {

    data class CategoriesListUIState(
        val listOfCategories: List<Category> = listOf()
    )

    private val _uiState = MutableLiveData(CategoriesListUIState())
    val uiState: LiveData<CategoriesListUIState>
        get() = _uiState

    fun loadCategories() {
        _uiState.value = _uiState.value?.copy(
            listOfCategories = STUB.getCategories()
        )
    }

}