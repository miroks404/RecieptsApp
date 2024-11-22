package ru.miroks404.recieptsapp

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.miroks404.recieptsapp.Domain.Category
import ru.miroks404.recieptsapp.databinding.ItemCategoryBinding

class CategoryListAdapter(private val dataSet: List<Category>) :
    RecyclerView.Adapter<CategoryListAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val binding = ItemCategoryBinding.bind(view)

        val imageView: ImageView = binding.ivCategory
        val titleTextView: TextView = binding.tvCategory
        val descriptionTextView: TextView = binding.tvCategoryDescription
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val inflater = LayoutInflater.from(parent.context)

        val view = inflater.inflate(R.layout.item_category, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val category: Category = dataSet[position]

        holder.titleTextView.text = category.title
        holder.descriptionTextView.text = category.description

        val drawable = try {
                Drawable.createFromStream(holder.itemView.context.assets.open(category.imageUrl), null)
        } catch (e: Exception) {
            Log.d("Not found", "Image not found: ${category.imageUrl}")
            null
        }

        holder.imageView.setImageDrawable(drawable)

    }

    override fun getItemCount(): Int = dataSet.size

}