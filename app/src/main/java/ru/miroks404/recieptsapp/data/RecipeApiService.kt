package ru.miroks404.recieptsapp.data

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.miroks404.recieptsapp.model.Category
import ru.miroks404.recieptsapp.model.Recipe

interface RecipeApiService {

    @GET("category")
    suspend fun getAllCategories(): List<Category>

    @GET("category/{id}")
    suspend fun getCategoryByCategoryId(@Path("id") id: Int): Category

    @GET("category/{id}/recipes")
    suspend fun getAllRecipesByCategoryId(@Path("id") id: Int): List<Recipe>

    @GET("recipes")
    suspend fun getAllRecipesByIds(@Query("ids") ids: String): List<Recipe>

    @GET("recipe/{id}")
    suspend fun getRecipeByRecipeId(@Path("id") id: Int): Recipe

}