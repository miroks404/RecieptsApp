package ru.miroks404.recieptsapp

import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.divider.MaterialDividerItemDecoration
import ru.miroks404.recieptsapp.data.STUB
import ru.miroks404.recieptsapp.databinding.FragmentRecipeBinding
import ru.miroks404.recieptsapp.domain.Recipe


class RecipeFragment : Fragment() {

    private var _binding: FragmentRecipeBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for FragmentRecipesListBinding must be not null")

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

        initUI(recipe!!)

        initRecycler(recipe.id)
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

        binding.ivRecipe.setImageDrawable(drawable)

        binding.tvRecipe.text = recipe.title
    }

    private fun initRecycler(recipeId: Int) {
        val ingredientsAdapter = IngredientsAdapter(STUB.getRecipeById(recipeId).ingredients)
        binding.rvIngredients.adapter = ingredientsAdapter

        val methodsAdapter = MethodsAdapter(STUB.getRecipeById(recipeId).method)
        binding.rvMethod.adapter = methodsAdapter

        val divider =
            MaterialDividerItemDecoration(this.requireContext(), LinearLayoutManager.VERTICAL)

        binding.rvIngredients.addItemDecoration(divider)
        binding.rvMethod.addItemDecoration(divider)
    }

}