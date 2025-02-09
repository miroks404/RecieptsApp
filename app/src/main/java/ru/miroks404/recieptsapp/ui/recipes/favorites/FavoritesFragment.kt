package ru.miroks404.recieptsapp.ui.recipes.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import ru.miroks404.recieptsapp.Constants
import ru.miroks404.recieptsapp.R
import ru.miroks404.recieptsapp.databinding.FragmentFavoritesBinding
import ru.miroks404.recieptsapp.ui.recipes.recipe.RecipeFragment

class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for ActivityMainBinding must be not null")

    private val viewModel: FavoritesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentFavoritesBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loadRecipes()

        initUI()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initUI() {
        val favoritesAdapter = FavoritesAdapter(listOf())

        binding.rvRecipesList.adapter = favoritesAdapter

        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            if (state.favoritesRecipes.isEmpty()) {
                binding.rvRecipesList.visibility = View.GONE
                binding.tvEmpty.visibility = View.VISIBLE
            } else {
                binding.rvRecipesList.visibility = View.VISIBLE
                favoritesAdapter.setNewDataSet(state.favoritesRecipes)
            }
        }

        favoritesAdapter.setOnItemClickListener(object : FavoritesAdapter.OnItemClickListener {
            override fun onItemClick(recipeId: Int) {
                openRecipeByRecipeId(recipeId)
            }

        })
    }

    private fun openRecipeByRecipeId(recipeId: Int) {
        val bundle = bundleOf(Constants.KEY_RECIPE to recipeId)

        fragmentManager?.commit {
            setReorderingAllowed(true)
            replace<RecipeFragment>(R.id.containerMain, args = bundle)
        }
    }

}