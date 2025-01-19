package ru.miroks404.recieptsapp.ui.category

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.miroks404.recieptsapp.model.Category
import ru.miroks404.recieptsapp.databinding.ItemCategoryBinding

class CategoryListAdapter(private val dataSet: List<Category>) :
    RecyclerView.Adapter<CategoryListAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(id: Int)
    }

    private var itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    class ViewHolder(binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
        val imageView: ImageView = binding.ivCategory
        val titleTextView: TextView = binding.tvCategory
        val descriptionTextView: TextView = binding.tvCategoryDescription
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding =
            ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
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
        holder.imageView.contentDescription = category.title

        holder.itemView.setOnClickListener {
            itemClickListener?.onItemClick(category.id)
        }

    }

    override fun getItemCount(): Int = dataSet.size

}