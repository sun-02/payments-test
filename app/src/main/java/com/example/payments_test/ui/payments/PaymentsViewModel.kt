package com.example.payments_test.ui.payments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.movies_app.data.core.remote.RetrofitClient
import com.example.payments_test.data.dto.PaymentsResponse
import com.example.payments_test.data.dto.ResponseError
import com.example.payments_test.domain.Payment
import com.example.payments_test.ui.util.DateTimeConverter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import okio.IOException
import timber.log.Timber

class PaymentsViewModel : ViewModel() {

    private val _state: MutableStateFlow<PaymentsState?> = MutableStateFlow(null)
    val state: StateFlow<PaymentsState?> = _state.asStateFlow()

    private val responseErrorChannel = Channel<ResponseError>()
    val responseError get() = responseErrorChannel.receiveAsFlow()

    private val exceptionMsgChannel = Channel<String>()
    val exceptionMsg get() = exceptionMsgChannel.receiveAsFlow()

    init {
        getPayments()
    }

    fun getPayments() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.emit(PaymentsState.Loading)
            kotlin.runCatching {
                RetrofitClient.paymentsApi.fetchPayments()
            }.onSuccess {
                handleResponse(it)
            }.onFailure { e ->
                if (e is IOException) {
                    e.message?.let {
                        delay(100)
                        exceptionMsgChannel.send(it)
                    }
                } else {
                    throw e
                }
            }
        }
    }

    private suspend fun handleResponse(response: PaymentsResponse) {
        Timber.d("Payments response: $response")
        if (response.success == "true") {
            response.paymentDto?.let { payments ->
                _state.emit(PaymentsState.NewPayments(
                    payments.map { payment ->
                        val amount = if (payment.amount.isNullOrBlank()) {
                            "N/A"
                        } else {
                            payment.amount
                        }
                        val created = payment.created?.let { DateTimeConverter.getDateTimeString(it.toLong()) } ?: "N/A"
                        Payment(
                            id = payment.id,
                            title = payment.title,
                            amount = amount,
                            created = created
                        )
                    }
                ))
            }
        } else {
            response.error?.let {
                delay(100)
                responseErrorChannel.send(it)
            }
        }
    }

    companion object {
        val factory: ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    PaymentsViewModel()
                }
            }
    }
}