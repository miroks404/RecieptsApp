package ru.miroks404.recieptsapp.ui.recipes.recipe

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.divider.MaterialDividerItemDecoration
import ru.miroks404.recieptsapp.Constants
import ru.miroks404.recieptsapp.Constants.KEY_SP
import ru.miroks404.recieptsapp.R
import ru.miroks404.recieptsapp.data.STUB
import ru.miroks404.recieptsapp.databinding.FragmentRecipeBinding
import ru.miroks404.recieptsapp.model.Recipe


class RecipeFragment : Fragment() {

    private var _binding: FragmentRecipeBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for FragmentRecipesListBinding must be not null")

    private val viewModel: RecipeViewModel by viewModels()

    private var isFavorite = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentRecipeBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recipe = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireArguments().getParcelable(Constants.KEY_RECIPE, Recipe::class.java)
        } else {
            requireArguments().getParcelable(Constants.KEY_RECIPE)
        }

        viewModel.uiState.observe(viewLifecycleOwner) {
            Log.d("!!!", "onViewCreated: isFavorite: ${it.isFavorite}")
        }

        recipe?.let { initUI(it) }

        recipe?.id?.let { initRecycler(it) }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initUI(recipe: Recipe) {

        val drawable = try {
            Drawable.createFromStream(this.context?.assets?.open(recipe.imageUrl), null)
        } catch (e: Exception) {
            Log.d("Not found", "Image not found: ${recipe.imageUrl}")
            null
        }

        with(binding) {
            ivRecipe.setImageDrawable(drawable)

            tvRecipe.text = recipe.title

            val favoritesSet = getFavorites()

            ibFavorite.setImageResource(
                if (recipe.id.toString() in favoritesSet) {
                    isFavorite = true
                    R.drawable.ic_heart
                } else {
                    isFavorite = false
                    R.drawable.ic_favorite
                }
            )

            ibFavorite.setOnClickListener {

                if (isFavorite) {
                    ibFavorite.setImageResource(R.drawable.ic_favorite)

                    favoritesSet.remove(recipe.id.toString())
                    saveFavorites(favoritesSet.toMutableSet())
                } else {
                    ibFavorite.setImageResource(R.drawable.ic_heart)

                    favoritesSet.add(recipe.id.toString())
                    saveFavorites(favoritesSet.toMutableSet())
                }

                isFavorite = !isFavorite

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

    private fun saveFavorites(setOfId: MutableSet<String>) {
        val sharedPrefs = activity?.getPreferences(Context.MODE_PRIVATE)
        sharedPrefs?.edit {
            putStringSet(KEY_SP, setOfId)
            Log.d("!!!", "saveFavorites: correct save")
            apply()
        }
    }

    private fun getFavorites(): HashSet<String> {
        val sharedPrefs = activity?.getPreferences(Context.MODE_PRIVATE)
        return sharedPrefs?.getStringSet(KEY_SP, null)?.toHashSet() ?: HashSet()
    }

}