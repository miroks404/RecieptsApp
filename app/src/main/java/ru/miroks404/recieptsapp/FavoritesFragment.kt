package ru.miroks404.recieptsapp

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import ru.miroks404.recieptsapp.Constants.KEY_SP
import ru.miroks404.recieptsapp.data.STUB
import ru.miroks404.recieptsapp.data.STUB.getRecipeById
import ru.miroks404.recieptsapp.databinding.FragmentFavoritesBinding
import ru.miroks404.recieptsapp.domain.Category

class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for ActivityMainBinding must be not null")

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

        initRecycler()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initRecycler() {
        val favoritesId = getFavorites().toSet()
        if (favoritesId.isEmpty()) {
            binding.rvRecipesList.visibility = View.GONE
            binding.tvEmpty.visibility = View.VISIBLE
        } else {
            val favoritesAdapter = FavoritesAdapter(STUB.getRecipesByIds(favoritesId))
            binding.rvRecipesList.adapter = favoritesAdapter
            favoritesAdapter.setOnItemClickListener(object : FavoritesAdapter.OnItemClickListener {
                override fun onItemClick(recipeId: Int) {
                    openRecipeByRecipeId(recipeId)
                }
            })
        }
    }

    private fun openRecipeByRecipeId(recipeId: Int) {

        val recipe = getRecipeById(recipeId)

        val bundle = bundleOf(Constants.KEY_RECIPE to recipe)

        fragmentManager?.commit {
            setReorderingAllowed(true)
            replace<RecipeFragment>(R.id.containerMain, args = bundle)
        }
    }

    private fun getFavorites(): HashSet<String> {
        val sharedPrefs = activity?.getPreferences(Context.MODE_PRIVATE)
        return sharedPrefs?.getStringSet(KEY_SP, null)?.toHashSet() ?: HashSet()
    }

}