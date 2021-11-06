package com.example.pinterbest

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.pinterbest.adapters.PinFeedHomeAdapter
import com.example.pinterbest.data.models.PinsFeed
import com.example.pinterbest.data.network.ApiExecutionRequests
import com.example.pinterbest.data.states.NetworkState
import com.example.pinterbest.databinding.FragmentHomeBinding
import com.example.pinterbest.utilities.HandlerError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pinFeedHomeAdapter = PinFeedHomeAdapter()
        binding.rvPins.apply {
            adapter = pinFeedHomeAdapter
            layoutManager = GridLayoutManager(context, GRID_COLUMNS)
        }

        getPinsFeed()

        binding.creators.setOnClickListener {
            val navHostFragment = requireActivity().supportFragmentManager
                .findFragmentById(R.id.NavHostFragment) as NavHostFragment
            val navController: NavController = navHostFragment.navController
            navController.navigate(R.id.action_homeFragment_to_creatorsFragment)
        }
    }

    private fun getPinsFeed() {
        lifecycleScope.launch {
            val state = async { ApiExecutionRequests.fetchNetworkState() }
            handleResult(state.await())
        }
    }

    private suspend fun handleResult(networkState: NetworkState) = withContext(Dispatchers.Main) {
        when (networkState) {
            is NetworkState.Success -> showPins(networkState.data)
            is NetworkState.HttpErrors.ResourceForbidden -> handleError(networkState.exception)
            is NetworkState.HttpErrors.ResourceNotFound ->
                HandlerError
                    .handleResourceNotFoundError(binding.errorText, networkState.exception)
            is NetworkState.HttpErrors.InternalServerError ->
                HandlerError
                    .handleInternalServerError(binding.errorText, networkState.exception)
            is NetworkState.HttpErrors.BadGateWay -> handleError(networkState.exception)
            is NetworkState.HttpErrors.ResourceRemoved -> handleError(networkState.exception)
            is NetworkState.HttpErrors.RemovedResourceFound -> handleError(networkState.exception)
            is NetworkState.InvalidData -> handleInvalidDataError()
            is NetworkState.Error -> handleError(networkState.error)
            is NetworkState.NetworkException ->
                HandlerError
                    .handleNetworkExceptionError(binding.errorText, networkState.error)
        }
    }

    private fun handleInvalidDataError() {
        if (binding.emptyViewLinear.visibility != View.VISIBLE) {
            binding.emptyViewLinear.visibility = View.VISIBLE
        }
        if (binding.rvPins.visibility != View.INVISIBLE) {
            binding.rvPins.visibility = View.INVISIBLE
        }
        binding.errorText.text = "Извините, ничего не найдено!"
    }

    private fun hideEmptyView() {
        if (binding.emptyViewLinear.visibility != View.INVISIBLE) {
            binding.emptyViewLinear.visibility = View.INVISIBLE
        }
        if (binding.rvPins.visibility != View.VISIBLE) {
            binding.rvPins.visibility = View.VISIBLE
        }
    }

    private fun handleError(message: String) {
        Log.d("PINTEREST", message)
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun showPins(response: PinsFeed) {
        hideEmptyView()
        pinFeedHomeAdapter.updateList(response.pins)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val GRID_COLUMNS = 2
    }
}
