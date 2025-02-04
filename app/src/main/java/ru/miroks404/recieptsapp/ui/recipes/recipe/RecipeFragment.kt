package ru.miroks404.recieptsapp.ui.recipes.recipe

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.divider.MaterialDividerItemDecoration
import ru.miroks404.recieptsapp.Constants
import ru.miroks404.recieptsapp.R
import ru.miroks404.recieptsapp.databinding.FragmentRecipeBinding


class RecipeFragment : Fragment() {

    private var _binding: FragmentRecipeBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for FragmentRecipesListBinding must be not null")

    private val viewModel: RecipeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentRecipeBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recipeId = arguments?.getInt(Constants.KEY_RECIPE)

        recipeId?.let {
            viewModel.loadRecipe(it)
            initUI()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initUI() {

        val ingredientsAdapter = IngredientsAdapter(listOf())
        binding.rvIngredients.adapter = ingredientsAdapter

        val methodsAdapter = MethodsAdapter(listOf())
        binding.rvMethod.adapter = methodsAdapter

        viewModel.uiState.observe(viewLifecycleOwner) { state ->

            binding.ivRecipe.setImageDrawable(state.recipeImage)

            binding.tvRecipe.text = state.recipe?.title

            binding.ibFavorite.setImageResource(
                if (state.isFavorite) R.drawable.ic_heart
                else R.drawable.ic_favorite
            )

            binding.tvPortionQuantity.text = state.stateOfSeekbar.toString()

            state.recipe?.let {
                ingredientsAdapter.dataSet = it.ingredients
                ingredientsAdapter.notifyDataSetChanged()
                methodsAdapter.dataSet = it.method
                methodsAdapter.notifyDataSetChanged()
            }

            ingredientsAdapter.updateIngredients(state.stateOfSeekbar)
        }

        binding.ibFavorite.setOnClickListener {

            viewModel.onFavoritesClicked()

        }

        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                viewModel.updateStateOfSeekbar(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        val divider =
            MaterialDividerItemDecoration(
                this@RecipeFragment.requireContext(),
                LinearLayoutManager.VERTICAL
            ).apply {
                isLastItemDecorated = false
                dividerInsetStart = 24
                dividerInsetEnd = 24
                dividerColor = ContextCompat.getColor(requireContext(), R.color.divider_color)
                dividerThickness = 3
            }

        binding.rvIngredients.addItemDecoration(divider)
        binding.rvMethod.addItemDecoration(divider)
    }

}
