package ru.miroks404.recieptsapp.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.miroks404.recieptsapp.Constants
import ru.miroks404.recieptsapp.model.Category
import ru.miroks404.recieptsapp.model.Recipe

class RecipesRepository(private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO) {

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val service = retrofit.create(RecipeApiService::class.java)

    suspend fun getAllCategories() : List<Category>? =
        withContext(ioDispatcher) {
            val listOfCategories = try {
                service.getAllCategories()
            } catch (e: Exception) {
                null
            }
            listOfCategories
        }

    suspend fun getCategoryByCategoryId(id: Int) : Category? =
        withContext(ioDispatcher) {
            val category = try {
                service.getCategoryByCategoryId(id)
            } catch (e: Exception) {
                null
            }
            category
        }

    suspend fun getAllRecipesByCategoryId(id: Int) : List<Recipe>? =
        withContext(ioDispatcher) {
            val recipes = try {
                service.getAllRecipesByCategoryId(id)
            } catch (e: Exception) {
                null
            }
            recipes
        }

    suspend fun getAllRecipesByIds(ids: String) : List<Recipe>? =
        withContext(ioDispatcher) {
            val recipes = try {
                service.getAllRecipesByIds(ids)
            } catch (e: Exception) {
                null
            }
            recipes
        }

    suspend fun getRecipeByRecipeId(id: Int) : Recipe? =
        withContext(ioDispatcher) {
            val recipe = try {
                service.getRecipeByRecipeId(id)
            } catch (e: Exception) {
                null
            }
            recipe
        }

}