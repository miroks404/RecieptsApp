package ru.miroks404.recieptsapp.data

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.miroks404.recieptsapp.Constants
import ru.miroks404.recieptsapp.model.Category
import ru.miroks404.recieptsapp.model.Recipe
import java.util.concurrent.Executors

class RecipesRepository {

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val service = retrofit.create(RecipeApiService::class.java)
    private val threadPool = Executors.newFixedThreadPool(10)

    fun getAllCategories(callback: (List<Category>?) -> Unit) {
        threadPool.submit {
            val listOfCategories = try {
                service.getAllCategories().execute().body()
            } catch (e: Exception) {
                null
            }
            callback(listOfCategories)
        }
    }

    fun getCategoryByCategoryId(id: Int, callback: (Category?) -> Unit) {
        threadPool.submit {
            val category = try {
                service.getCategoryByCategoryId(id).execute().body()
            } catch (e: Exception) {
                null
            }
            callback(category)
        }
    }

    fun getAllRecipesByCategoryId(id: Int, callback: (List<Recipe>?) -> Unit) {
        threadPool.submit {
            val recipes = try {
                service.getAllRecipesByCategoryId(id).execute().body()
            } catch (e: Exception) {
                null
            }
            callback(recipes)
        }
    }

    fun getAllRecipesByIds(ids: String, callback: (List<Recipe>?) -> Unit) {
        threadPool.submit {
            val recipes = try {
                service.getAllRecipesByIds(ids).execute().body()
            } catch (e: Exception) {
                null
            }
            callback(recipes)
        }
    }

    fun getRecipeByRecipeId(id: Int, callback: (Recipe?) -> Unit) {
        threadPool.submit {
            val recipe = try {
                service.getRecipeByRecipeId(id).execute().body()
            } catch (e: Exception) {
                null
            }
            callback(recipe)
        }
    }

}