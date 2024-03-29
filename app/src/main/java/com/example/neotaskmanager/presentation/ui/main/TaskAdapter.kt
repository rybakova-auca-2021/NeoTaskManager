package com.example.neotaskmanager.presentation.ui.main

import android.graphics.Paint
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.neotaskmanager.R
import com.example.neotaskmanager.data.model.Task
import com.example.neotaskmanager.data.model.TaskData
import com.example.neotaskmanager.databinding.ItemCardTaskBinding
import java.util.Collections

class TaskAdapter(var items: MutableList<TaskData?>) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>(){

    private var itemTouchHelper: ItemTouchHelper? = null
    private var itemClickListener: OnItemClickListener? = null


    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    interface OnItemClickListener {
        fun onDeleteClick(item: TaskData?)
    }

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

        init {
            binding.root.setOnClickListener {
                binding.btnTaskDelete.visibility = View.VISIBLE
                binding.btnTaskDelete.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val deletedItem = items[position]
                        items.removeAt(position)
                        notifyItemRemoved(position)
                        itemClickListener?.onDeleteClick(deletedItem)
                    }
                }
            }

            binding.etTask.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        items[position] = TaskData(0, s.toString(), false)
                    }
                }


                override fun afterTextChanged(s: Editable?) {}
            })
        }

        fun bind(task: TaskData?) {
            binding.etTask.text = task?.title.toEditable()
            if (task?.completed == true) {
                binding.etTask.setTextColor(ContextCompat.getColor(binding.root.context, R.color.grey))
                binding.etTask.paintFlags = binding.etTask.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            }
            binding.checkBox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    items[adapterPosition]?.completed = true
                    binding.etTask.setTextColor(ContextCompat.getColor(binding.root.context, R.color.grey))
                    binding.etTask.paintFlags = binding.etTask.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                } else {
                    items[adapterPosition]?.completed = false
                    binding.etTask.setTextColor(ContextCompat.getColor(binding.root.context, R.color.black))
                    binding.etTask.paintFlags = binding.etTask.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                }
            }
        }
    }

    fun updateData(newList: MutableList<TaskData?>) {
        val diffResult = DiffUtil.calculateDiff(
            DisplayableItemDiffCallback(
                items,
                newList
            )
        )
        items = newList
        diffResult.dispatchUpdatesTo(this)
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

    class DisplayableItemDiffCallback(
        private val oldList: List<TaskData?>,
        private val newList: List<TaskData?>
    ) : DiffUtil.Callback() {

        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}
