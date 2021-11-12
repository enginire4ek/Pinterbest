package com.example.pinterbest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.pinterbest.adapters.PinFeedHomeAdapter
import com.example.pinterbest.api.ApiClient
import com.example.pinterbest.api.ApiService
import com.example.pinterbest.data.models.PinsFeed
import com.example.pinterbest.data.repository.Repository
import com.example.pinterbest.data.states.NetworkState
import com.example.pinterbest.databinding.FragmentHomeBinding
import com.example.pinterbest.viewmodels.HomeFactory
import com.example.pinterbest.viewmodels.HomeViewModel

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var pinFeedHomeAdapter: PinFeedHomeAdapter

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
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val model = ViewModelProvider(
            requireActivity(),
            HomeFactory(
                requireActivity().application,
                Repository(
                    ApiClient().retrofit.create(
                        ApiService::class.java
                    )
                )
            )
        ).get(HomeViewModel::class.java)

        pinFeedHomeAdapter = PinFeedHomeAdapter()
        binding.rvPins.apply {
            adapter = pinFeedHomeAdapter
            layoutManager = StaggeredGridLayoutManager(
                GRID_COLUMNS,
                StaggeredGridLayoutManager.VERTICAL
            )
        }

        model.pinsFeedLiveData.observe(viewLifecycleOwner) { result ->
            when (result) {
                is NetworkState.Success -> showPins(result.data!!)
                is NetworkState.Error -> showError(result.error)
                is NetworkState.Loading -> binding.progressBar.visibility = View.VISIBLE
                is NetworkState.NetworkException -> showError(result.error)
                is NetworkState.InternalServerError -> showError(result.exception)
            }
        }

        binding.creators.setOnClickListener {
            val navHostFragment = requireActivity().supportFragmentManager
                .findFragmentById(R.id.NavHostFragment) as NavHostFragment
            val navController: NavController = navHostFragment.navController
            navController.navigate(R.id.action_homeFragment_to_creatorsFragment)
        }
    }

    // Helper functions for working with UI
    private fun showPins(response: PinsFeed) {
        hideEmptyView()
        pinFeedHomeAdapter.updateList(response.mapToViewData())
    }

    private fun showError(error: String) {
        if (binding.emptyViewLinear.visibility != View.VISIBLE) {
            binding.emptyViewLinear.visibility = View.VISIBLE
        }
        if (binding.progressBar.visibility != View.GONE) {
            binding.progressBar.visibility = View.GONE
        }
        if (binding.rvPins.visibility != View.GONE) {
            binding.rvPins.visibility = View.GONE
        }
        binding.errorText.text = "$error"
    }

    private fun hideEmptyView() {
        if (binding.emptyViewLinear.visibility != View.GONE) {
            binding.emptyViewLinear.visibility = View.GONE
        }
        if (binding.progressBar.visibility != View.GONE) {
            binding.progressBar.visibility = View.GONE
        }
        if (binding.rvPins.visibility != View.VISIBLE) {
            binding.rvPins.visibility = View.VISIBLE
        }
    }

    companion object {
        private const val GRID_COLUMNS = 2
    }
}
