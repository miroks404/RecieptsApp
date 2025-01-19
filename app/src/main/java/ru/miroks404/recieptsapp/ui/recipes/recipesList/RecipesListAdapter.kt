package ru.miroks404.recieptsapp.ui.recipes.recipesList

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.miroks404.recieptsapp.databinding.ItemRecipeBinding
import ru.miroks404.recieptsapp.model.Recipe

class RecipesListAdapter(private val dataSet: List<Recipe>) :
    RecyclerView.Adapter<RecipesListAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(recipeId: Int)
    }

    private var itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    class ViewHolder(binding: ItemRecipeBinding) : RecyclerView.ViewHolder(binding.root) {
        val imageView: ImageView = binding.ivRecipe
        val titleTextView: TextView = binding.tvRecipe
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding =
            ItemRecipeBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val recipe: Recipe = dataSet[position]

        holder.titleTextView.text = recipe.title

        val drawable = try {
            Drawable.createFromStream(holder.itemView.context.assets.open(recipe.imageUrl), null)
        } catch (e: Exception) {
            Log.d("Not found", "Image not found: ${recipe.imageUrl}")
            null
        }

        holder.imageView.setImageDrawable(drawable)
        holder.imageView.contentDescription = recipe.title

        holder.itemView.setOnClickListener {
            itemClickListener?.onItemClick(recipe.id)
        }

    }

    override fun getItemCount(): Int = dataSet.size

}