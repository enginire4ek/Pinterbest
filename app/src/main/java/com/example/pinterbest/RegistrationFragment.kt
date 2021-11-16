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
import com.example.pinterbest.data.repository.Repository
import com.example.pinterbest.data.repository.SessionRepository
import com.example.pinterbest.databinding.FragmentRegistrationBinding
import com.example.pinterbest.utilities.ResourceProvider
import com.example.pinterbest.utilities.Validator
import com.example.pinterbest.viewmodels.RegistrationFactory
import com.example.pinterbest.viewmodels.RegistrationViewModel
import okhttp3.ResponseBody
import retrofit2.Response

class RegistrationFragment : Fragment() {
    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!

    private lateinit var sessionManager: SessionRepository

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
        sessionManager = SessionRepository(
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
                    registrationProvider(view, response)
                }
            }
        }
    }

    private fun saveSession(response: Response<ResponseBody>) {
        val regex =
            """['session_id=']+(?<=session_id=).{56}""".toRegex()
        val cookie = regex.find(response.headers().values("Set-Cookie")[0])
        sessionManager.saveUserData(cookie?.value ?: "")
    }

    private fun setUpBottomNavigationItem() {
        (activity as MainActivity).binding.bottomNavigation.menu
            .getItem(MainActivity.HOME_POSITION_BNV).isChecked = true
    }

    private fun registrationProvider(view: View, response: Response<ResponseBody>) {
        when (response.code()) {
            SUCCESS -> {
                saveSession(response)
                view.findNavController().navigate(R.id.homeFragment)
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

    companion object {
        const val SUCCESS = 201
        const val INVALID_DATA = 400
        const val ALREADY_AUTHORIZED = 403
        const val USER_EXISTS = 409
    }
}
