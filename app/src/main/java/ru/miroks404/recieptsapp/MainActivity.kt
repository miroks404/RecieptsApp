package ru.miroks404.recieptsapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.add
import androidx.fragment.app.commit
import ru.miroks404.recieptsapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var  _binding: ActivityMainBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("Binding for ActivityMainBinding must be not null")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add<CategoriesListFragment>(R.id.containerMain)
        }

        binding.bCategory.setOnClickListener {

            supportFragmentManager.commit {
                setReorderingAllowed(true)
                replace(R.id.containerMain, CategoriesListFragment())
            }

        }

        binding.bFavorites.setOnClickListener {

            supportFragmentManager.commit {
                setReorderingAllowed(true)
                replace(R.id.containerMain, FavoritesFragment())
            }

        }
    }
}

