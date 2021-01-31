package com.jcgds.zuper

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.jcgds.domain.entities.Operation
import com.jcgds.domain.entities.Operation.State
import com.jcgds.zuper.databinding.OperationListItemBinding
import com.jcgds.zuper.extensions.getThemeColor

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
            stateTextView.text = getStatusMessage(operation, context)

            val overrideIndicatorColor = getIndicatorColorOverride(operation, context)
            progressIndicator.setIndicatorColor(overrideIndicatorColor)
        }
    }

    private fun getStatusMessage(operation: Operation, context: Context) = when (operation.state) {
        State.Ongoing -> context.getString(R.string.operation_ongoing_state)
        State.Completed.Error -> context.getString(R.string.operation_completed_error_state)
        State.Completed.Success -> context.getString(R.string.operation_completed_success_state)
    }

    private fun getIndicatorColorOverride(operation: Operation, context: Context) =
        when (operation.state) {
            State.Completed.Error -> context.getThemeColor(R.attr.colorError)
            State.Completed.Success -> context.getThemeColor(R.attr.colorSuccess)
            else -> context.getThemeColor(R.attr.colorPrimary)
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
