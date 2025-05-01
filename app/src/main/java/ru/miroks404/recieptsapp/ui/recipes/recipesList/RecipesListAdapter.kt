package ru.miroks404.recieptsapp.ui.recipes.recipesList

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.miroks404.recieptsapp.Constants
import ru.miroks404.recieptsapp.R
import ru.miroks404.recieptsapp.databinding.ItemRecipeBinding
import ru.miroks404.recieptsapp.model.Recipe

class RecipesListAdapter(private var dataSet: List<Recipe>, private val context: Context) :
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

        Glide.with(context)
            .load("${Constants.IMAGE_URL}${recipe.imageUrl}")
            .placeholder(R.drawable.img_placeholder)
            .error(R.drawable.img_error)
            .into(holder.imageView)

        holder.imageView.contentDescription = recipe.title

        holder.itemView.setOnClickListener {
            itemClickListener?.onItemClick(recipe.id)
        }

    }

    override fun getItemCount(): Int = dataSet.size

    fun setNewDataSet(dataSet: List<Recipe>) {
        this.dataSet = dataSet
        notifyDataSetChanged()
    }

}