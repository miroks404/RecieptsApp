package ru.miroks404.recieptsapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import ru.miroks404.recieptsapp.R
import ru.miroks404.recieptsapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for ActivityMainBinding must be not null")

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()

        navController = findNavController(binding.navHostFragmentContainer.id)

        binding.bCategory.setOnClickListener {

            if (navController.currentDestination?.id != R.id.categoriesListFragment) {
                navController.navigate(R.id.action_global_to_categories)
            }

        }

        binding.bFavorites.setOnClickListener {

            if (navController.currentDestination?.id != R.id.favoritesFragment) {
                navController.navigate(R.id.action_global_to_favorites)
            }

        }
    }
}

