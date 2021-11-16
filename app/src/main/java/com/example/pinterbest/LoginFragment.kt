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
import com.example.pinterbest.data.models.UserLogin
import com.example.pinterbest.data.repository.AuthRepository
import com.example.pinterbest.data.repository.Repository
import com.example.pinterbest.data.repository.SessionRepository
import com.example.pinterbest.databinding.FragmentLoginBinding
import com.example.pinterbest.utilities.ResourceProvider
import com.example.pinterbest.utilities.Validator
import com.example.pinterbest.viewmodels.LoginFactory
import com.example.pinterbest.viewmodels.LoginViewModel
import java.lang.IllegalStateException

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var sessionRepository: SessionRepository

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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginButton.setOnClickListener {
            if (validateUserFields()) {
                val model = ViewModelProvider(
                    requireActivity(),
                    LoginFactory(
                        requireActivity().application,
                        getUserData(),
                        Repository(
                            preferences = requireActivity().getSharedPreferences(
                                getString(R.string.login_info),
                                Context.MODE_PRIVATE
                            )
                        )
                    )
                ).get(LoginViewModel::class.java)

                model.logInCodeLiveData.observe(viewLifecycleOwner) { response ->
                    try {
                        AuthRepository(sessionRepository).authProvider(response)
                        view.findNavController()
                            .navigate(R.id.action_loginFragment_to_homeFragment)
                        setUpBottomNavigationItem()
                    } catch (t: IllegalStateException) {
                        showErrorToast(t)
                    }
                }
            }
        }

        binding.emailAuth.setOnClickListener {
            it.findNavController().navigate(R.id.action_loginFragment_to_registrationFragment)
        }
    }

    private fun showErrorToast(t: IllegalStateException) {
        when (t.message) {
            INVALID_DATA -> {
                Toast.makeText(
                    context,
                    resources.getString(R.string.error_invalid_data),
                    Toast.LENGTH_SHORT
                ).show()
            }
            WRONG_PASSWORD -> {
                Toast.makeText(
                    context,
                    resources.getString(R.string.error_wrong_password),
                    Toast.LENGTH_SHORT
                ).show()
            }
            ALREADY_AUTHORIZED -> {
                Toast.makeText(
                    context,
                    resources.getString(R.string.error_already_authorized),
                    Toast.LENGTH_SHORT
                ).show()
            }
            USER_NOT_FOUND -> {
                Toast.makeText(
                    context,
                    resources.getString(R.string.error_user_not_found),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUpBottomNavigationItem() {
        (activity as MainActivity).binding.bottomNavigation.menu
            .getItem(MainActivity.HOME_POSITION_BNV).isChecked = true
    }

    private fun getUserData(): UserLogin {
        return UserLogin(
            binding.usernameBox.text.toString(),
            binding.passwordBox.text.toString()
        )
    }

    private fun validateUserFields(): Boolean {
        return Validator(ResourceProvider(resources))
            .isValidName(binding.usernameBox, true) &&
            Validator(ResourceProvider(resources))
                .isValidPassword(binding.passwordBox, true)
    }

    companion object {
        const val INVALID_DATA = "400"
        const val WRONG_PASSWORD = "401"
        const val ALREADY_AUTHORIZED = "403"
        const val USER_NOT_FOUND = "404"
    }
}
