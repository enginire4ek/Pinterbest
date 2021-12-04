package com.example.pinterbest.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.pinterbest.domain.entities.PinsList
import com.example.pinterbest.presentation.adapters.PinFeedHomeAdapter
import com.example.pinterbest.presentation.common.getAppComponent
import com.example.pinterbest.presentation.databinding.FragmentHomeBinding
import com.example.pinterbest.presentation.utilities.ResourceProvider
import com.example.pinterbest.presentation.viewmodels.HomeViewModel

class HomeFragment : Fragment() {
    private val appComponent by lazy {
        getAppComponent()
    }

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var pinFeedHomeAdapter: PinFeedHomeAdapter

    private val viewModel: HomeViewModel by viewModels {
        appComponent.viewModelsFactory()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(
            inflater,
            container,
            false
        )
        viewModel.getPins()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObservers()

        pinFeedHomeAdapter = PinFeedHomeAdapter()
        binding.rvPins.apply {
            adapter = pinFeedHomeAdapter
            layoutManager = StaggeredGridLayoutManager(
                GRID_COLUMNS,
                StaggeredGridLayoutManager.VERTICAL
            )
        }

        binding.creators.setOnClickListener {
            val navHostFragment = requireActivity().supportFragmentManager
                .findFragmentById(R.id.NavHostFragment) as NavHostFragment
            val navController: NavController = navHostFragment.navController
            navController.navigate(R.id.action_homeFragment_to_creatorsFragment)
        }
    }

    private fun initObservers() {
        viewModel.pins.observe(
            viewLifecycleOwner,
            {
                it?.let {
                    showPins(it)
                }
            }
        )

        viewModel.error.observe(
            viewLifecycleOwner,
            {
                showError(it)
            }
        )

        viewModel.state.observe(
            viewLifecycleOwner,
            { loading ->
                when (loading) {
                    true -> binding.progressBar.visibility = View.VISIBLE
                    false -> binding.progressBar.visibility = View.GONE
                }
            }
        )
    }

    // Helper functions for working with UI
    private fun showPins(response: PinsList) {
        hideEmptyView()
        pinFeedHomeAdapter.updateList(response)
    }

    private fun showError(messageId: Int) {
        if (binding.emptyViewLinear.visibility != View.VISIBLE) {
            binding.emptyViewLinear.visibility = View.VISIBLE
        }
        if (binding.rvPins.visibility != View.GONE) {
            binding.rvPins.visibility = View.GONE
        }
        binding.errorText.text = ResourceProvider(resources).getString(messageId)
    }

    private fun hideEmptyView() {
        if (binding.emptyViewLinear.visibility != View.GONE) {
            binding.emptyViewLinear.visibility = View.GONE
        }
        if (binding.rvPins.visibility != View.VISIBLE) {
            binding.rvPins.visibility = View.VISIBLE
        }
    }

    companion object {
        private const val GRID_COLUMNS = 2
    }
}