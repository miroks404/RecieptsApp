package ru.miroks404.recieptsapp.ui.recipes.recipe

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.divider.MaterialDividerItemDecoration
import ru.miroks404.recieptsapp.Constants
import ru.miroks404.recieptsapp.R
import ru.miroks404.recieptsapp.data.STUB
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
        }

        initUI()

        recipeId?.let {
            initRecycler(it)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initUI() {

        viewModel.uiState.observe(viewLifecycleOwner) {
            state ->
            val drawable = try {
                Drawable.createFromStream(
                    this.context?.assets?.open(state.recipe?.imageUrl ?: ""),
                    null
                )
            } catch (e: Exception) {
                Log.d("Not found", "Image not found: ${state.recipe?.imageUrl}")
                null
            }

            with(binding) {
                ivRecipe.setImageDrawable(drawable)

                tvRecipe.text = state.recipe?.title

                ibFavorite.setImageResource(
                    if (state.isFavorite) R.drawable.ic_heart
                    else R.drawable.ic_favorite
                )

                ibFavorite.setOnClickListener {

                    viewModel.onFavoritesClicked()

                }
            }
        }

    }

    private fun initRecycler(recipeId: Int) {
        val ingredientsAdapter = IngredientsAdapter(STUB.getRecipeById(recipeId).ingredients)
        binding.rvIngredients.adapter = ingredientsAdapter

        val methodsAdapter = MethodsAdapter(STUB.getRecipeById(recipeId).method)
        binding.rvMethod.adapter = methodsAdapter

        val divider =
            MaterialDividerItemDecoration(
                this.requireContext(),
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

        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                ingredientsAdapter.updateIngredients(progress)
                binding.tvPortionQuantity.text = progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
    }

}