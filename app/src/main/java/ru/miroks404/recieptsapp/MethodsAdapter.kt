package ru.miroks404.recieptsapp

import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.miroks404.recieptsapp.databinding.ItemIngredientBinding

class MethodsAdapter(private val dataSet: List<String>) :
    RecyclerView.Adapter<MethodsAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val binding = ItemIngredientBinding.bind(view)

        val textView: TextView = binding.tvIngredient
        val quantity: TextView = binding.tvQuantity
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding =
            ItemIngredientBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val method: String = dataSet[position]

        holder.textView.text = method
        holder.quantity.visibility = GONE

    }

    override fun getItemCount(): Int = dataSet.size

}