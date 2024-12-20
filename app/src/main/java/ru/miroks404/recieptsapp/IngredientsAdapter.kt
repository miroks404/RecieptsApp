package ru.miroks404.recieptsapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.miroks404.recieptsapp.databinding.ItemIngredientBinding
import ru.miroks404.recieptsapp.domain.Ingredient

class IngredientsAdapter(private val dataSet: List<Ingredient>) :
    RecyclerView.Adapter<IngredientsAdapter.ViewHolder>() {

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

        val ingredient: Ingredient = dataSet[position]

        holder.textView.text = ingredient.description
        holder.quantity.text = "${ingredient.quantity} ${ingredient.unitOfMeasure}"

    }

    override fun getItemCount(): Int = dataSet.size

}