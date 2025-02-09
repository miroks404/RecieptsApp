package ru.miroks404.recieptsapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import ru.miroks404.recieptsapp.R
import ru.miroks404.recieptsapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var  _binding: ActivityMainBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("Binding for ActivityMainBinding must be not null")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bCategory.setOnClickListener {

            findNavController(R.id.nav_host_fragment_container).navigate(R.id.categoriesListFragment)

        }

        binding.bFavorites.setOnClickListener {

            findNavController(R.id.nav_host_fragment_container).navigate(R.id.favoritesFragment)

        }
    }
}

