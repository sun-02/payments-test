package com.example.payments_test.ui.payments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.movies_app.R
import com.example.movies_app.databinding.FragmentPaymentsBinding
import com.example.payments_test.ui.extensions.observe
import com.example.payments_test.ui.extensions.observeAsEvent

class PaymentsFragment : Fragment() {
    private var _binding: FragmentPaymentsBinding? = null
    private val binding get() = _binding!!

    private var _adapter: PaymentsAdapter? = null
    private val adapter get() = _adapter!!

    private val viewModel: PaymentsViewModel by viewModels {
        PaymentsViewModel.factory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPaymentsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindViews()
    }

    private fun bindViews() {
        bindPaymentsList()

        observeState()
    }

    private fun observeState() {
        observe(viewModel.state) { state ->
            if (state != null) {
                when (state) {
                    is PaymentsState.Loading -> {
                        binding.paymentsList.isVisible = false
                        binding.progress.isVisible = true
                        binding.alert.isVisible = false
                    }
                    is PaymentsState.NewPayments -> {
                        binding.paymentsList.isVisible = true
                        binding.progress.isVisible = false
                        binding.alert.isVisible = false
                        adapter.submitList(state.payments)
                    }
                }

            }
        }

        observeAsEvent(viewModel.responseError) { error ->
            binding.paymentsList.isVisible = false
            binding.progress.isVisible = false
            binding.alert.isVisible = true
            Toast.makeText(requireContext(), getString(
                R.string.error_message,
                error.errorCode,
                error.errorMsg
            ), Toast.LENGTH_LONG).show()
        }

        observeAsEvent(viewModel.exceptionMsg) { message ->
            binding.paymentsList.isVisible = false
            binding.progress.isVisible = false
            binding.alert.isVisible = true
            Toast.makeText(requireContext(), getString(
                R.string.exception_message,
                message
            ), Toast.LENGTH_LONG).show()
        }
    }

    private fun bindPaymentsList() {
        val dividerDrawable =
            AppCompatResources.getDrawable(requireContext(), R.drawable.divider_for_vertical)!!
        val itemDivider = DividerItemDecoration(context, LinearLayoutManager.VERTICAL)
            .apply { setDrawable(dividerDrawable) }
        binding.paymentsList.addItemDecoration(itemDivider)
        _adapter = PaymentsAdapter()
        binding.paymentsList.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
        _adapter = null
    }
}