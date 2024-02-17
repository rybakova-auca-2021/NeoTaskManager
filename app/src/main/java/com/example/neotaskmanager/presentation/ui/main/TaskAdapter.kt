package com.example.neotaskmanager.presentation.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.neotaskmanager.data.model.TaskData
import com.example.neotaskmanager.databinding.ItemCardTaskBinding

class TaskAdapter(var items: MutableList<TaskData>) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCardTaskBinding.inflate(inflater, parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class TaskViewHolder(private val binding: ItemCardTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(task: TaskData) {
            binding.checkBox.text = task.title
        }
    }
}
