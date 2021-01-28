package com.jcgds.zuper

import android.view.LayoutInflater
import android.view.ViewGroup
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
        holder.binding.apply {
            idTextView.text = operation.id
            stateTextView.text = operation.state
            progressTextView.text = "(${operation.progress})%"
        }
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