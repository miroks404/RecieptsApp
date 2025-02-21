package ru.miroks404.recieptsapp.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import ru.miroks404.recieptsapp.R
import ru.miroks404.recieptsapp.databinding.ActivityMainBinding
import ru.miroks404.recieptsapp.model.Category
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("Binding for ActivityMainBinding must be not null")

    private lateinit var navController: NavController

    private val threadPool = Executors.newFixedThreadPool(10)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val thread = Thread {
            val listOfCategories = mutableListOf<Category>()
            val idCategories = mutableMapOf<String, Int>()

            val loggingInterceptor =
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

            val client = OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()
            val requestCategories = Request.Builder()
                .url("https://recipes.androidsprint.ru/api/category")
                .build()

            val gson = Gson()

            client.newCall(requestCategories).execute().use { response ->
                Log.d("!!!", "Выполняю запрос на потоке: ${Thread.currentThread().name}")
                val listType = object : TypeToken<List<Category>>() {}.type
                response.body?.string()?.let {
                    Log.d("!!!", it)
                    listOfCategories.addAll(
                        gson.fromJson<List<Category>>(
                            it,
                            listType
                        )
                    )
                }
            }

            listOfCategories.forEach {
                idCategories[it.title] = it.id
            }

            idCategories.forEach {
                threadPool.submit {
                    val requestRecipes = Request.Builder()
                        .url("https://recipes.androidsprint.ru/api/category/${it.value}/recipes")
                        .build()
                    client.newCall(requestRecipes).execute().use { response ->
                        response.body?.string()?.let { it1 -> Log.d("!!!", it1) }
                    }
                }
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

