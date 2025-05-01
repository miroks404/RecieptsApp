package ru.miroks404.recieptsapp.ui.recipes.recipe

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.miroks404.recieptsapp.databinding.ItemIngredientBinding
import ru.miroks404.recieptsapp.model.Ingredient
import java.math.BigDecimal
import java.math.RoundingMode

class IngredientsAdapter(private var dataSet: List<Ingredient>) :
    RecyclerView.Adapter<IngredientsAdapter.ViewHolder>() {

    private var quantity: Int = 1

    class ViewHolder(binding: ItemIngredientBinding) : RecyclerView.ViewHolder(binding.root) {
        val textView: TextView = binding.tvIngredient
        val quantity: TextView = binding.tvQuantity
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding =
            ItemIngredientBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val ingredient: Ingredient = dataSet[position]

        holder.textView.text = ingredient.description

        holder.quantity.text = try {
            val totalQuantity = BigDecimal(ingredient.quantity) * BigDecimal(quantity)
            val displayTotalQuantity =
                totalQuantity.setScale(1, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString()
            "$displayTotalQuantity ${ingredient.unitOfMeasure}"
        } catch (e: Exception) {
            ingredient.quantity
        }
    }

    override fun getItemCount(): Int = dataSet.size

    fun updateIngredients(progress: Int) {
        quantity = progress
        notifyDataSetChanged()
    }

    fun setNewDataSet(dataSet: List<Ingredient>) {
        this.dataSet = dataSet
        notifyDataSetChanged()
    }

}