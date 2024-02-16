package com.example.neotaskmanager.presentation.ui.main

import android.graphics.Paint
import android.text.Editable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.neotaskmanager.R
import com.example.neotaskmanager.data.model.TaskData
import com.example.neotaskmanager.databinding.ItemCardTaskBinding
import java.util.Collections

class TaskAdapter(var items: MutableList<TaskData>) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>(){

    private var itemTouchHelper: ItemTouchHelper? = null

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
            binding.etTask.text = task.title.toEditable()
            binding.checkBox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    binding.etTask.setTextColor(ContextCompat.getColor(binding.root.context, R.color.grey))
                    binding.etTask.paintFlags = binding.etTask.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                } else {
                    binding.etTask.setTextColor(ContextCompat.getColor(binding.root.context, R.color.black))
                    binding.etTask.paintFlags = binding.etTask.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                }
            }
        }
    }

    fun String?.toEditable(): Editable? {
        return this?.let { Editable.Factory.getInstance().newEditable(this) }
    }

    fun attachItemTouchHelper(recyclerView: RecyclerView) {
        itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val fromPosition = viewHolder.adapterPosition
                val toPosition = target.adapterPosition
                if (fromPosition < toPosition) {
                    for (i in fromPosition until toPosition) {
                        Collections.swap(items, i, i + 1)
                    }
                } else {
                    for (i in fromPosition downTo toPosition + 1) {
                        Collections.swap(items, i, i - 1)
                    }
                }

                notifyItemMoved(fromPosition, toPosition)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            }
        })

        itemTouchHelper?.attachToRecyclerView(recyclerView)
    }
}