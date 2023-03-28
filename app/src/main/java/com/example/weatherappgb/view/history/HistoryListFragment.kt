package com.example.weatherappgb.view.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherappgb.databinding.FragmentHistoryListBinding
import com.example.weatherappgb.viewmodel.AppState
import com.example.weatherappgb.viewmodel.HistoryViewModel
import com.google.android.material.snackbar.Snackbar

class HistoryListFragment : Fragment() {
    private val adapter = HistoryListAdapter()

    private var _binding: FragmentHistoryListBinding? = null
    private val binding: FragmentHistoryListBinding
        get() {
            return _binding!!
        }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    private val viewModel: HistoryViewModel by lazy {
        ViewModelProvider(this).get(HistoryViewModel::class.java)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHistoryListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recycleViewHistory.also {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(requireContext())
        }

        val observer = {data: AppState -> renderData(data)}
        viewModel.getData().observe(viewLifecycleOwner, observer)
        viewModel.getAll()
    }
    private fun renderData(data: AppState){
        when(data){
            is AppState.Error ->  Snackbar.make(binding.root, "error", Snackbar.LENGTH_LONG)
                .show()
            AppState.Loading -> TODO()
            is AppState.Success ->  adapter.setData(data.weatherList)
        }
    }
    companion object {
        @JvmStatic
        fun newInstance() = HistoryListFragment()
    }


}
