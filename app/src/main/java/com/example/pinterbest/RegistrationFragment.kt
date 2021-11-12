package com.example.pinterbest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.pinterbest.api.ApiClient
import com.example.pinterbest.api.ApiService
import com.example.pinterbest.data.models.User
import com.example.pinterbest.databinding.FragmentRegistrationBinding
import com.example.pinterbest.utilities.SessionManager
import com.example.pinterbest.utilities.Validator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RegistrationFragment : Fragment() {
    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!

    private lateinit var sessionManager: SessionManager

    private val apiService: ApiService = ApiClient().retrofit.create(ApiService::class.java)

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
        sessionManager = SessionManager(requireActivity())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.registrationButton.setOnClickListener {
            if (validateUserFields()) {
                lifecycleScope.launch {
                    val response = withContext(Dispatchers.IO) {
                        apiService.postSignUp(getUserData())
                    }
                    when (response.code()) {
                        SUCCESS -> {
                            val regex =
                                """['session_id=']+(?<=session_id=).{56}""".toRegex()
                            val cookie = regex.find(response.headers().values("Set-Cookie")[0])
                            sessionManager.saveUserData(
                                cookie?.value ?: "",
                                binding.registrationUsernameBox.text.toString(),
                                binding.registrationPasswordBox.text.toString(),
                                true
                            )
                            it.findNavController().navigate(R.id.homeFragment)
                            setUpBottomNavigationItem()
                        }
                        INVALID_DATA -> Toast.makeText(
                            context,
                            "Предоставлены неверные учетные данные",
                            Toast.LENGTH_SHORT
                        ).show()
                        ALREADY_AUTHORIZED -> Toast.makeText(
                            context,
                            "Вы уже авторизованы. Сначала выйдите из системы",
                            Toast.LENGTH_SHORT
                        ).show()
                        USER_EXISTS -> Toast.makeText(
                            context,
                            "Пользователь уже существует",
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
        return Validator.isValidName(binding.registrationUsernameBox, true) &&
            Validator.isValidEmail(binding.registrationEmailBox, true) &&
            Validator.isValidPassword(binding.registrationPasswordBox, true)
    }

    companion object {
        const val SUCCESS = 201
        const val INVALID_DATA = 400
        const val ALREADY_AUTHORIZED = 403
        const val USER_EXISTS = 409
    }
}
