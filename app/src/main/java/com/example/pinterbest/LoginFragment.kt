package com.example.pinterbest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.pinterbest.databinding.FragmentLoginBinding
import com.example.pinterbest.utilities.Validator

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_login,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginButton.setOnClickListener {
            if (isValidLogIn()) {
                Toast.makeText(context, "Correct", Toast.LENGTH_SHORT).show()
                it.findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
            } else {
                Toast.makeText(context, "Not correct", Toast.LENGTH_SHORT).show()
                // Для облегчения входа в приложение
                it.findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun isValidLogIn(): Boolean {
        return Validator.isValidName(
            data = binding.usernameBox.text.toString().trim(),
            updateUI = true
        ) && Validator.isValidPassword(
            data = binding.passwordBox.text.toString().trim(),
            updateUI = true
        )
    }
}
