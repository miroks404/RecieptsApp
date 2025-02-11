package ru.miroks404.recieptsapp.ui.recipes.recipesList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.miroks404.recieptsapp.R
import ru.miroks404.recieptsapp.databinding.FragmentRecipesListBinding

class RecipesListFragment : Fragment(R.layout.fragment_recipes_list) {

    private var _binding: FragmentRecipesListBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for FragmentRecipesListBinding must be not null")

    private val viewModel: RecipesListViewModel by viewModels()

    private val args: RecipesListFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentRecipesListBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val categoryId = args.categoryId

        viewModel.loadRecipes(categoryId)
        initUI()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initUI() {
        val recipesListAdapter = RecipesListAdapter(listOf())

        binding.rvRecipesList.adapter = recipesListAdapter

        viewModel.uiState.observe(viewLifecycleOwner) { state ->

            binding.ivCategory.setImageDrawable(state.categoryImage)

            binding.tvCategory.text = state.category?.title

            state.recipesList?.let {
                recipesListAdapter.setNewDataSet(it)
            }

        }

        recipesListAdapter.setOnItemClickListener(object : RecipesListAdapter.OnItemClickListener {
            override fun onItemClick(recipeId: Int) {
                openRecipeByRecipeId(recipeId)
            }
        })

    }

    private fun openRecipeByRecipeId(recipeId: Int) {
        findNavController().navigate(
            RecipesListFragmentDirections.actionRecipesListFragmentToRecipeFragment(
                recipeId
            )
        )
    }

}