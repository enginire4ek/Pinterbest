package com.example.pinterbest

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.pinterbest.data.repository.AuthRepository
import com.example.pinterbest.data.repository.Repository
import com.example.pinterbest.data.repository.SessionRepository
import com.example.pinterbest.databinding.FragmentLoginBinding
import com.example.pinterbest.utilities.ResourceProvider
import com.example.pinterbest.utilities.Validator
import com.example.pinterbest.viewmodels.LoginFactory
import com.example.pinterbest.viewmodels.LoginViewModel

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var sessionRepository: SessionRepository

    private lateinit var model: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(
            inflater,
            container,
            false
        )

        sessionRepository = SessionRepository(
            preferences = requireActivity().getSharedPreferences(
                getString(R.string.login_info),
                Context.MODE_PRIVATE
            )
        )

        initViewModels()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObservers(view)

        binding.loginButton.setOnClickListener {
            if (validateUserFields()) {
                model.setLiveEvent(
                    binding.usernameBox.text.toString(),
                    binding.passwordBox.text.toString()
                )
            }
        }

        binding.emailAuth.setOnClickListener {
            it.findNavController().navigate(R.id.action_loginFragment_to_registrationFragment)
        }
    }

    private fun initViewModels() {
        model = ViewModelProvider(
            requireActivity(),
            LoginFactory(
                requireActivity().application,
                Repository(
                    preferences = requireActivity().getSharedPreferences(
                        getString(R.string.login_info),
                        Context.MODE_PRIVATE
                    )
                )
            )
        ).get(LoginViewModel::class.java)
    }

    private fun initObservers(view: View) {
        model.getLiveEvent().observe(viewLifecycleOwner) { response ->
            try {
                AuthRepository(sessionRepository).authProvider(response)
                view.findNavController().navigate(R.id.homeFragment)
                setUpBottomNavigationItem()
            } catch (t: IllegalStateException) {
                showErrorToast(model.getErrorCode(t))
            }
        }
    }

    private fun showErrorToast(messageId: Int) {
        Toast.makeText(
            context,
            ResourceProvider(resources).getString(messageId),
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUpBottomNavigationItem() {
        (activity as MainActivity).binding.bottomNavigation.menu
            .getItem(MainActivity.HOME_POSITION_BNV).isChecked = true
    }

    private fun validateUserFields(): Boolean {
        return Validator(ResourceProvider(resources))
            .isValidName(binding.usernameBox, true) &&
            Validator(ResourceProvider(resources))
                .isValidPassword(binding.passwordBox, true)
    }
}
