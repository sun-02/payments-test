package com.example.payments_test.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.commit
import com.example.movies_app.R
import com.example.payments_test.PaymentsApplication
import com.example.payments_test.ui.login.LoginFragment
import com.example.payments_test.ui.payments.PaymentsFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (PaymentsApplication.get().sessionManager.authToken == null) {
            navigeteToLogin()
        } else {
            navigateToPayments()
        }
    }

    private fun navigeteToLogin() {
        supportFragmentManager.commit {
            add(R.id.container, LoginFragment::class.java, null)
        }
    }

    private fun navigateToPayments() {
        supportFragmentManager.commit {
            add(R.id.container, PaymentsFragment::class.java, null)
        }
    }
}