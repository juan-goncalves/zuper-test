package com.jcgds.zuper

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.jcgds.domain.entities.Operation
import com.jcgds.zuper.databinding.OperationListItemBinding

class OperationsAdapter : RecyclerView.Adapter<OperationsAdapter.ViewHolder>() {

    var data: List<Operation>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    private val differ: AsyncListDiffer<Operation> = AsyncListDiffer(this, OperationItemCallback())

    override fun getItemCount() = differ.currentList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = OperationListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val operation = differ.currentList[position]
        val context = holder.binding.root.context
        holder.binding.apply {
            idTextView.text = operation.id
            progressIndicator.setProgressCompat(operation.progress, true)
            stateTextView.text = when (operation.state) {
                Operation.State.Success -> context.getString(R.string.operation_success_state)
                Operation.State.Error -> context.getString(R.string.operation_error_state)
                else -> ""
            }

            val overrideIndicatorColor = getIndicatorColorOverride(operation, context)
            if (overrideIndicatorColor != null) {
                progressIndicator.setIndicatorColor(overrideIndicatorColor)
            }
        }
    }

    private fun getIndicatorColorOverride(
        operation: Operation,
        context: Context
    ) = when (operation.state) {
        Operation.State.Error -> ResourcesCompat.getColor(context.resources, R.color.red, null)
        Operation.State.Success -> ResourcesCompat.getColor(context.resources, R.color.green, null)
        else -> null
    }

    inner class ViewHolder(val binding: OperationListItemBinding) :
        RecyclerView.ViewHolder(binding.root)

}

class OperationItemCallback : DiffUtil.ItemCallback<Operation>() {

    override fun areItemsTheSame(oldItem: Operation, newItem: Operation): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Operation, newItem: Operation): Boolean {
        return oldItem == newItem
    }

}