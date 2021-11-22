package ru.katdmy.tradein.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.compose.runtime.Composable
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.katdmy.tradein.R

class MainFragment : Fragment(R.layout.main_fragment) {
    private var loadButton: Button? = null
    private var message: TextView? = null
    private var rv: RecyclerView? = null
    private val adapter = AdsAdapter()

    companion object {
        fun newInstance() = MainFragment()
    }

    private val viewModel: MainViewModel by activityViewModels { ViewModelFactory(requireActivity()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews(view)
        setRecyclerView()
        setupClickListener()

        viewModel.loadedStatus.observe(viewLifecycleOwner, { message?.text = it })
        viewModel.loadedData.observe(viewLifecycleOwner, { adapter.loadAds(it) })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        loadButton = null
        message = null
    }

    private fun initViews(view: View) {
        loadButton = view.findViewById(R.id.button_load)
        message = view.findViewById(R.id.message)
        rv = view.findViewById(R.id.rv)
    }

    private fun setRecyclerView() {
        rv?.layoutManager = LinearLayoutManager(requireContext())
        rv?.adapter = adapter
    }

    private fun setupClickListener() {
        loadButton?.setOnClickListener { viewModel.loadData() }
    }

}
