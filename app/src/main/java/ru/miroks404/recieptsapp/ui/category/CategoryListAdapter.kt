package ru.miroks404.recieptsapp.ui.category

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.miroks404.recieptsapp.Constants
import ru.miroks404.recieptsapp.R
import ru.miroks404.recieptsapp.model.Category
import ru.miroks404.recieptsapp.databinding.ItemCategoryBinding

class CategoryListAdapter(private var dataSet: List<Category>, private val context: Context) :
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

        Glide.with(context)
            .load("${Constants.IMAGE_URL}${category.imageUrl}")
            .placeholder(R.drawable.img_placeholder)
            .error(R.drawable.img_error)
            .into(holder.imageView)

        holder.imageView.contentDescription = category.title

        holder.itemView.setOnClickListener {
            itemClickListener?.onItemClick(category.id)
        }

    }

    override fun getItemCount(): Int = dataSet.size

    fun setNewDataSet(dataSet: List<Category>) {
        this.dataSet = dataSet
        notifyDataSetChanged()
    }

}