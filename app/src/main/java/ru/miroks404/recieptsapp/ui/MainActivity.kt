package ru.miroks404.recieptsapp.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.miroks404.recieptsapp.R
import ru.miroks404.recieptsapp.databinding.ActivityMainBinding
import ru.miroks404.recieptsapp.model.Category
import java.net.HttpURLConnection
import java.net.URL

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

        val thread = Thread {
            val url = URL("https://recipes.androidsprint.ru/api/category")
            val connection = url.openConnection() as HttpURLConnection
            connection.connect()

            val categoriesJsonString = connection.inputStream.bufferedReader().readText()

            Log.d("!!!", "Выполняю запрос на потоке: ${Thread.currentThread().name}")
            Log.d("!!!", categoriesJsonString)

            val gson = Gson()

            val listType = object : TypeToken<List<Category>>() {}.type
            val listOfCategories = gson.fromJson<List<Category>>(categoriesJsonString, listType)

            listOfCategories.forEach {
                Log.d("category", "$it")
            }
        }
        thread.start()

        Log.d("!!!", "Метод onCreate() выполняется на потоке: ${Thread.currentThread().name}")
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

