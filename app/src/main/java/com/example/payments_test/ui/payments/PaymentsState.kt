package com.example.payments_test.ui.payments

import com.example.payments_test.domain.Payment

sealed interface PaymentsState {
    data object Loading : PaymentsState
    data class NewPayments(val payments: List<Payment>) : PaymentsState
}