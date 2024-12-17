package ru.miroks404.recieptsapp

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.miroks404.recieptsapp.databinding.FragmentRecipeBinding
import ru.miroks404.recieptsapp.domain.Recipe


class RecipeFragment : Fragment() {

    private var  _binding: FragmentRecipeBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("Binding for FragmentRecipesListBinding must be not null")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
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

        binding.tvRecipe.text = recipe?.title
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}