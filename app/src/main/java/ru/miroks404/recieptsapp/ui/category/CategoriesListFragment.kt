package ru.miroks404.recieptsapp.ui.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.miroks404.recieptsapp.Constants
import ru.miroks404.recieptsapp.R
import ru.miroks404.recieptsapp.databinding.FragmentListCategoriesBinding

class CategoriesListFragment : Fragment() {

    private var _binding: FragmentListCategoriesBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for ActivityMainBinding must be not null")

    private val viewModel: CategoriesListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListCategoriesBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loadCategories()

        initUI()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initUI() {
        val categoriesAdapter = CategoryListAdapter(listOf())

        binding.rvCategories.adapter = categoriesAdapter

        viewModel.uiState.observe(viewLifecycleOwner) { state ->

            categoriesAdapter.setNewDataSet(state.listOfCategories)

        }

        categoriesAdapter.setOnItemClickListener(object : CategoryListAdapter.OnItemClickListener {
            override fun onItemClick(id: Int) {
                openRecipesByCategoryId(id)
            }
        })
    }

    private fun openRecipesByCategoryId(categoryId: Int) {

        val bundle = bundleOf(
            Constants.KEY_CATEGORY to categoryId,
        )

        findNavController().navigate(R.id.action_categoriesListFragment_to_recipesListFragment, bundle)
    }

}
