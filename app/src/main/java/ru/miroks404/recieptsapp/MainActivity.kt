package ru.miroks404.recieptsapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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

        val fragment = CategoriesListFragment()

        val fragmentManager = supportFragmentManager

        fragmentManager.commit {
            val fragmentTransaction = fragmentManager.beginTransaction()

            fragmentTransaction.add(R.id.mainContainer, fragment)

            fragmentTransaction.commit()
        }
    }
}