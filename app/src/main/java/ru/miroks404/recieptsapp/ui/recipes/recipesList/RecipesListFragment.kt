package ru.miroks404.recieptsapp.ui.recipes.recipesList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import ru.miroks404.recieptsapp.Constants
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
        val recipesListAdapter =
            RecipesListAdapter(listOf(), this@RecipesListFragment.requireContext())

        binding.rvRecipesList.adapter = recipesListAdapter

        viewModel.uiState.observe(viewLifecycleOwner) { state ->

            when (state.recipesListState) {
                RecipesListViewModel.RecipesListState.DEFAULT -> {
                    binding.tvCategory.text = state.category?.title
                    recipesListAdapter.setNewDataSet(state.recipesList)

                    if (state.categoryImage != null) {
                        Glide.with(this@RecipesListFragment.requireContext())
                            .load("${Constants.IMAGE_URL}${state.categoryImage}")
                            .placeholder(R.drawable.img_placeholder)
                            .error(R.drawable.img_error)
                            .into(binding.ivCategory)
                    }
                }

                RecipesListViewModel.RecipesListState.ERROR -> Toast.makeText(
                    this@RecipesListFragment.requireContext(),
                    R.string.data_error_text,
                    Toast.LENGTH_SHORT
                ).show()

            }
        }

        recipesListAdapter.setOnItemClickListener(object :
            RecipesListAdapter.OnItemClickListener {
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