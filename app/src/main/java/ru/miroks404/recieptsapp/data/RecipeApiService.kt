package ru.miroks404.recieptsapp.data

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.miroks404.recieptsapp.model.Category
import ru.miroks404.recieptsapp.model.Recipe

interface RecipeApiService {

    @GET("category")
    fun getAllCategories(): Call<List<Category>>

    @GET("category/{id}")
    fun getCategoryByCategoryId(@Path("id") id: Int): Call<Category>

    @GET("category/{id}/recipes")
    fun getAllRecipesByCategoryId(@Path("id") id: Int): Call<List<Recipe>>

    @GET("recipes")
    fun getAllRecipesByIds(@Query("ids") ids: String): Call<List<Recipe>>

    @GET("recipe/{id}")
    fun getRecipeByRecipeId(@Path("id") id: Int): Call<Recipe>

}