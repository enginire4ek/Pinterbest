package com.example.pinterbest.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.pinterbest.presentation.common.getAppComponent
import com.example.pinterbest.presentation.databinding.FragmentRegistrationBinding
import com.example.pinterbest.presentation.utilities.ResourceProvider
import com.example.pinterbest.presentation.utilities.Validator
import com.example.pinterbest.presentation.viewmodels.SignUpViewModel

class RegistrationFragment : Fragment() {
    private val appComponent by lazy {
        getAppComponent()
    }

    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SignUpViewModel by viewModels {
        appComponent.viewModelsFactory()
    }

    private var _returnFragmentID: Int = 0
    private val returnFragmentID get() = _returnFragmentID

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegistrationBinding.inflate(
            inflater,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _returnFragmentID = LoginFragmentArgs.fromBundle(requireArguments()).returnFragmentId
        if (returnFragmentID == 0) {
            _returnFragmentID = R.id.homeFragment
        }

        // If the user presses the back button, bring them back to the home screen
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val inclusive = (returnFragmentID != R.id.homeFragment)
                view.findNavController().popBackStack(returnFragmentID, inclusive)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        initObservers(view)

        binding.registrationButton.setOnClickListener {
            if (validateUserFields()) {
                viewModel.postSigUp(
                    binding.registrationUsernameBox,
                    binding.registrationEmailBox,
                    binding.registrationPasswordBox
                )
            }
        }
    }

    private fun showErrorToast(messageId: Int?) {
        if (messageId != null) {
            Toast.makeText(
                context,
                ResourceProvider(resources).getString(messageId),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun initObservers(view: View) {
        viewModel.response.observe(viewLifecycleOwner) {
            view.findNavController().popBackStack(returnFragmentID, false)
        }
        viewModel.error.observe(viewLifecycleOwner) {
            showErrorToast(viewModel.error.value)
        }
    }

    private fun validateUserFields(): Boolean {
        return Validator(ResourceProvider(resources))
            .isValidName(binding.registrationUsernameBox, true) &&
            Validator(ResourceProvider(resources))
                .isValidEmail(binding.registrationEmailBox, true) &&
            Validator(ResourceProvider(resources))
                .isValidPassword(binding.registrationPasswordBox, true)
    }
}
