package com.example.pinterbest.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.pinterbest.presentation.common.getAppComponent
import com.example.pinterbest.presentation.databinding.FragmentBoardCreationBinding
import com.example.pinterbest.presentation.utilities.ResourceProvider
import com.example.pinterbest.presentation.utilities.Validator
import com.example.pinterbest.presentation.viewmodels.BoardCreationViewModel
import com.example.pinterbest.presentation.viewmodels.ProfileViewModel

class BoardCreationFragment : Fragment() {
    private val appComponent by lazy {
        getAppComponent()
    }

    private var _binding: FragmentBoardCreationBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BoardCreationViewModel by viewModels {
        appComponent.viewModelsFactory()
    }

    private val profileViewModel: ProfileViewModel by viewModels {
        appComponent.viewModelsFactory()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBoardCreationBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profileViewModel.getAuthStatus()
        initAuthObservers()

        binding.back.setOnClickListener {
            val navHostFragment = requireActivity().supportFragmentManager
                .findFragmentById(R.id.NavHostFragment) as NavHostFragment
            navHostFragment.navController.navigateUp()
        }
    }

    private fun initAuthObservers() {
        profileViewModel.loggedIn.observe(viewLifecycleOwner) { loggedIn ->
            when (loggedIn) {
                true -> {
                    binding.uploadButton.setOnClickListener {
                        if (validateUserFields()) {
                            viewModel.postNewBoard(binding.titleBox, binding.descriptionBox)
                        }
                    }
                    initObservers()
                }
                false -> {
                    val loginArgs = LoginFragmentArgs.Builder()
                    loginArgs.returnFragmentId = R.id.profileFragment
                    findNavController().navigate(R.id.loginFragment, loginArgs.build().toBundle())
                }
            }
        }
        profileViewModel.checkAuthError.observe(viewLifecycleOwner) { response ->
            if (response != null) {
                showError(ResourceProvider(resources).getString(response))
            }
        }
        profileViewModel.checkAuthState.observe(viewLifecycleOwner) { loading ->
            if (loading) {
                binding.progressBar.visibility = View.VISIBLE
            }
        }
    }

    private fun initObservers() {
        viewModel.run {
            board.observe(viewLifecycleOwner) { response ->
                if (response != null) {
                    showSuccess()
                }
            }
            error.observe(viewLifecycleOwner) { response ->
                if (response != null) {
                    showError(response)
                }
            }
            loadingState.observe(viewLifecycleOwner) { loading ->
                if (!loading) {
                    binding.progressBar.visibility = View.GONE
                }
            }
        }
    }

    private fun showSuccess() {
        Toast.makeText(
            requireActivity(),
            ResourceProvider(resources).getString(R.string.success_create_board),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun showError(response: String) {
        Toast.makeText(requireActivity(), response, Toast.LENGTH_SHORT).show()
    }

    private fun validateUserFields(): Boolean {
        return Validator(ResourceProvider(resources))
            .isValidName(binding.titleBox, true) &&
            Validator(ResourceProvider(resources))
                .isValidName(binding.descriptionBox, true)
    }
}
