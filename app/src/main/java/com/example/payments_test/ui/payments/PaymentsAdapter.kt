package com.example.payments_test.ui.payments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.movies_app.R
import com.example.movies_app.databinding.ItemPaymentBinding
import com.example.payments_test.domain.Payment

class PaymentsAdapter : ListAdapter<Payment, PaymentsAdapter.PaymentViewHolder>(DiffCallback) {

    private lateinit var binding: ItemPaymentBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentViewHolder {
        binding = ItemPaymentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PaymentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PaymentViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class PaymentViewHolder(
        private val binding: ItemPaymentBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(payment: Payment) {
            with (binding) {
                title.text = payment.title
                val amountFormatted = if (payment.amount == "N/A") {
                    "N/A"
                } else {
                    binding.root.context.getString(R.string.amount, payment.amount)
                }
                amount.text = amountFormatted
                created.text = payment.created
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Payment>() {
        override fun areItemsTheSame(oldItem: Payment, newItem: Payment): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Payment, newItem: Payment): Boolean =
            oldItem == newItem
    }
}