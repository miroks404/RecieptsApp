package ru.miroks404.recieptsapp.ui.recipes.recipesList

import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import ru.miroks404.recieptsapp.Constants
import ru.miroks404.recieptsapp.R
import ru.miroks404.recieptsapp.data.STUB
import ru.miroks404.recieptsapp.data.STUB.getRecipeById
import ru.miroks404.recieptsapp.databinding.FragmentRecipesListBinding
import ru.miroks404.recieptsapp.model.Category
import ru.miroks404.recieptsapp.ui.recipes.recipe.RecipeFragment

class RecipesListFragment: Fragment(R.layout.fragment_recipes_list) {

    private var  _binding: FragmentRecipesListBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("Binding for FragmentRecipesListBinding must be not null")

    private var categoryId: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipesListBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val category = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requireArguments().getParcelable(Constants.KEY_CATEGORY, Category::class.java)
        } else {
            requireArguments().getParcelable(Constants.KEY_CATEGORY)
        }

        categoryId = category?.id

        initRecycler()

        category?.let { initUI(it) }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initRecycler() {
        val recipesListAdapter = RecipesListAdapter(STUB.getRecipesByCategoryId(categoryId ?: -1))
        binding.rvRecipesList.adapter = recipesListAdapter
        recipesListAdapter.setOnItemClickListener(object : RecipesListAdapter.OnItemClickListener {
            override fun onItemClick(recipeId: Int) {
                openRecipeByRecipeId(recipeId)
            }
        })
    }

    private fun initUI(category: Category) {
        val drawable = try {
            Drawable.createFromStream(view?.context?.assets?.open(category.imageUrl), null)
        } catch (e: Exception) {
            Log.d("Not found", "Image not found: ${category.imageUrl}")
            null
        }

        binding.ivCategory.setImageDrawable(drawable)

        binding.tvCategory.text = category.title

    }

    private fun openRecipeByRecipeId(recipeId: Int) {

        val recipe = getRecipeById(recipeId)

        val bundle = bundleOf(Constants.KEY_RECIPE to recipe)

        fragmentManager?.commit {
            setReorderingAllowed(true)
            replace<RecipeFragment>(R.id.containerMain, args = bundle)
        }
    }

}