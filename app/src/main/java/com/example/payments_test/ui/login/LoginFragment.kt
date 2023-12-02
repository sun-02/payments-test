package com.example.payments_test.ui.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import com.example.movies_app.R
import com.example.movies_app.data.core.remote.RetrofitClient
import com.example.movies_app.databinding.FragmentLoginBinding
import com.example.payments_test.PaymentsApplication
import com.example.payments_test.data.dto.LoginRequest
import com.example.payments_test.data.dto.LoginResponse
import com.example.payments_test.ui.payments.PaymentsFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okio.IOException
import timber.log.Timber

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindViews()
    }

    private fun bindViews() {
        binding.loginField.doAfterTextChanged {
            showLoginError(true)
        }
        binding.passwordField.doAfterTextChanged {
            showPasswordError(true)
        }
        binding.logInButton.setOnClickListener {
            if (verifyFields()) {
                requestLogin()
            } else {
                showErrors()
            }
        }
    }

    private fun requestLogin() {
        val request = LoginRequest(
            binding.loginField.text?.toString() ?: "",
            binding.passwordField.text?.toString() ?: ""
        )
        lifecycleScope.launch(Dispatchers.IO) {
            enableLoadingState(true)
            kotlin.runCatching {
                RetrofitClient.loginApi.login(request)
            }.onSuccess {
                handleResponse(it)
            }.onFailure { e ->
                if (e is IOException) {
                    Timber.d(e)
                    e.message?.let { msg ->
                        showExceptionToast(msg)
                    }
                } else {
                    throw e
                }
            }
            enableLoadingState(false)
        }
    }

    private suspend fun showExceptionToast(message: String) {
        withContext(Dispatchers.Main) {
            Toast.makeText(requireContext(), getString(
                R.string.exception_message,
                message
            ), Toast.LENGTH_LONG).show()
        }
    }

    private suspend fun handleResponse(response: LoginResponse) {
        Timber.d("Login response: $response")
        if (response.success == "true") {
            PaymentsApplication.get().sessionManager.saveToken(response.token?.value)
            navigateToPayments()
        } else {
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    requireContext(), getString(
                        R.string.error_message,
                        response.error?.errorCode,
                        response.error?.errorMsg
                    ), Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun navigateToPayments() {
        parentFragmentManager.commit {
            replace(R.id.container, PaymentsFragment::class.java, null)
        }
    }

    private suspend fun enableLoadingState(show: Boolean) {
        withContext(Dispatchers.Main) {
            if (show) {
                binding.progress.isVisible = true
                binding.logInButton.isEnabled = false
            } else {
                binding.progress.isVisible = false
                binding.logInButton.isEnabled = true
            }
        }
    }

    private fun showErrors() {
        showLoginError()
        showPasswordError()
    }

    private fun showLoginError(hideErrorOnly: Boolean = false) {
        if (verifyLogin()) {
            binding.loginLayout.setBoxBackgroundColorResource(android.R.color.transparent)
            binding.loginError.isVisible = false
        } else if (!hideErrorOnly) {
            binding.loginLayout.setBoxBackgroundColorResource(R.color.error)
            binding.loginError.isVisible = true
        }
    }

    private fun showPasswordError(hideErrorOnly: Boolean = false) {
        if (verifyPassword()) {
            binding.passwordLayout.setBoxBackgroundColorResource(android.R.color.transparent)
            binding.passwordError.isVisible = false
        } else if (!hideErrorOnly) {
            binding.passwordLayout.setBoxBackgroundColorResource(R.color.error)
            binding.passwordError.isVisible = true
        }
    }

    private fun verifyLogin() = binding.loginField.text?.isNotBlank() ?: false

    private fun verifyPassword() = binding.passwordField.text?.isNotBlank() ?: false

    private fun verifyFields() = verifyLogin() && verifyPassword()

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

}