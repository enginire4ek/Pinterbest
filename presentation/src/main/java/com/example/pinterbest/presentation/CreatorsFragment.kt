package com.example.pinterbest.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pinterbest.domain.entities.Profile
import com.example.pinterbest.presentation.adapters.CreatorsAdapter
import com.example.pinterbest.presentation.common.getAppComponent
import com.example.pinterbest.presentation.databinding.FragmentCreatorsBinding
import com.example.pinterbest.presentation.mappers.MapToViewData
import com.example.pinterbest.presentation.viewmodels.CreatorsViewModel

class CreatorsFragment : Fragment() {
    private var _binding: FragmentCreatorsBinding? = null
    private val binding get() = _binding!!

    private val appComponent by lazy {
        getAppComponent()
    }

    private lateinit var creatorsAdapter: CreatorsAdapter

    private val viewModel: CreatorsViewModel by viewModels {
        appComponent.viewModelsFactory()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreatorsBinding.inflate(
            inflater,
            container,
            false
        )
        viewModel.getCreators()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.back.setOnClickListener {
            val navHostFragment = requireActivity().supportFragmentManager
                .findFragmentById(R.id.NavHostFragment) as NavHostFragment
            navHostFragment.navController.navigateUp()
        }

        creatorsAdapter = CreatorsAdapter()
        binding.rvCreators.apply {
            adapter = creatorsAdapter
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL,
                false
            )
        }

        initObservers()
    }

    private fun initObservers() {
        viewModel.creators.observe(
            viewLifecycleOwner
        ) {
            it?.let {
                showCreators(it)
            }
        }

        viewModel.error.observe(
            viewLifecycleOwner
        ) {
            showError(it)
        }

        viewModel.loadingState.observe(
            viewLifecycleOwner
        ) { loading ->
            binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        }
    }

    private fun showCreators(list: List<Profile>) {
        creatorsAdapter.updateList(
            list.map {
                MapToViewData.mapToProfileViewData(it)
            }
        )
    }

    private fun showError(it: String) {
        Toast.makeText(requireActivity(), it, Toast.LENGTH_SHORT).show()
    }
}
