package ru.miroks404.recieptsapp.ui.recipes.recipe

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.divider.MaterialDividerItemDecoration
import ru.miroks404.recieptsapp.Constants
import ru.miroks404.recieptsapp.R
import ru.miroks404.recieptsapp.databinding.FragmentRecipeBinding

private class PortionSeekbarListener(private val onChangeIngredients: (Int) -> Unit) :
    OnSeekBarChangeListener {
    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        onChangeIngredients(progress)
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {

    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {

    }

}

class RecipeFragment : Fragment() {

    private var _binding: FragmentRecipeBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for FragmentRecipesListBinding must be not null")

    private val viewModel: RecipeViewModel by viewModels()

    private val args: RecipeFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentRecipeBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recipeId = args.recipeId

        viewModel.loadRecipe(recipeId)
        initUI()

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

            when (state.recipeState) {
                RecipeViewModel.RecipeState.DEFAULT -> {

                    if (state.recipeImage != null) {
                        Glide.with(this@RecipeFragment.requireContext())
                            .load("${Constants.IMAGE_URL}${state.recipeImage}")
                            .placeholder(R.drawable.img_placeholder)
                            .error(R.drawable.img_error)
                            .into(binding.ivRecipe)
                    }

                    binding.tvRecipe.text = state.recipe?.title

                    binding.ibFavorite.setImageResource(
                        if (state.isFavorite) R.drawable.ic_heart
                        else R.drawable.ic_favorite
                    )

                    binding.tvPortionQuantity.text = state.stateOfSeekbar.toString()

                    state.recipe?.let {
                        ingredientsAdapter.setNewDataSet(it.ingredients)
                        methodsAdapter.setNewDataSet(it.method)
                    }

                    ingredientsAdapter.updateIngredients(state.stateOfSeekbar)

                }

                RecipeViewModel.RecipeState.ERROR -> Toast.makeText(
                    this@RecipeFragment.requireContext(),
                    R.string.data_error_text,
                    Toast.LENGTH_SHORT
                ).show()
            }

        }

        binding.ibFavorite.setOnClickListener {

            viewModel.onFavoritesClicked()

        }

        binding.seekBar.setOnSeekBarChangeListener(PortionSeekbarListener {
            viewModel.updateStateOfSeekbar(it)
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

        val widthOfScreen = resources.displayMetrics.widthPixels
        val marginRightInDp = 16
        val marginRightInPix = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            marginRightInDp.toFloat(),
            resources.displayMetrics
        ).toInt()
        binding.tvRecipe.maxWidth = widthOfScreen - marginRightInPix * 2
    }

}
