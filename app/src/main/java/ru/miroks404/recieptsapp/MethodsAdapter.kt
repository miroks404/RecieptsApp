package ru.miroks404.recieptsapp

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.miroks404.recieptsapp.databinding.ItemMethodBinding

class MethodsAdapter(private val dataSet: List<String>) :
    RecyclerView.Adapter<MethodsAdapter.ViewHolder>() {

    class ViewHolder(binding: ItemMethodBinding) : RecyclerView.ViewHolder(binding.root) {
        val textView: TextView = binding.tvMethod
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding =
            ItemMethodBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val method: String = dataSet[position]

        holder.textView.text = "${position + 1}. $method"

    }

    override fun getItemCount(): Int = dataSet.size

}