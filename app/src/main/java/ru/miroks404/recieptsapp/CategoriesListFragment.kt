package ru.miroks404.recieptsapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.miroks404.recieptsapp.databinding.ActivityMainBinding

class CategoriesListFragment : Fragment(R.layout.fragment_list_categories) {

    private var  _binding: ActivityMainBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("Binding for ActivityMainBinding must be not null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ActivityMainBinding.inflate(layoutInflater)

        return binding.root
    }

}
