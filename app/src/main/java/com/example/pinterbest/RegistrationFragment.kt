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
import com.example.pinterbest.data.models.User
import com.example.pinterbest.data.repository.AuthRepository
import com.example.pinterbest.data.repository.Repository
import com.example.pinterbest.data.repository.SessionRepository
import com.example.pinterbest.databinding.FragmentRegistrationBinding
import com.example.pinterbest.utilities.ResourceProvider
import com.example.pinterbest.utilities.Validator
import com.example.pinterbest.viewmodels.RegistrationFactory
import com.example.pinterbest.viewmodels.RegistrationViewModel

class RegistrationFragment : Fragment() {
    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!

    private lateinit var sessionRepository: SessionRepository

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

        binding.registrationButton.setOnClickListener {
            if (validateUserFields()) {
                val model = ViewModelProvider(
                    requireActivity(),
                    RegistrationFactory(
                        requireActivity().application,
                        getUserData(),
                        Repository(
                            preferences = requireActivity().getSharedPreferences(
                                getString(R.string.login_info),
                                Context.MODE_PRIVATE
                            )
                        )
                    )
                ).get(RegistrationViewModel::class.java)

                model.signInCodeLiveData.observe(viewLifecycleOwner) { response ->
                    if (AuthRepository(response).authProvider(sessionRepository)) {
                        view.findNavController().navigate(R.id.homeFragment)
                        setUpBottomNavigationItem()
                    } else {
                        Toast.makeText(
                            context,
                            "Произошла ошибка при регистрации!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun setUpBottomNavigationItem() {
        (activity as MainActivity).binding.bottomNavigation.menu
            .getItem(MainActivity.HOME_POSITION_BNV).isChecked = true
    }

    private fun getUserData(): User {
        return User(
            binding.registrationUsernameBox.text.toString(),
            binding.registrationEmailBox.text.toString(),
            binding.registrationPasswordBox.text.toString()
        )
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
