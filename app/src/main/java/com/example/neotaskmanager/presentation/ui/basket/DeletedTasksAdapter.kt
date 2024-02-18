package com.example.neotaskmanager.presentation.ui.basket

import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.neotaskmanager.data.model.Task
import com.example.neotaskmanager.databinding.ItemDeletedCardBinding
import com.example.neotaskmanager.presentation.ui.main.DeleteTaskViewModel
import com.example.neotaskmanager.presentation.ui.main.TaskAdapter

class DeletedTasksAdapter(var items: MutableList<Task?>, val restoreViewModel: DeleteTaskViewModel, private val lifecycleScope: LifecycleCoroutineScope) : RecyclerView.Adapter<DeletedTasksAdapter.TaskViewHolder>(){
    private var itemClickListener: OnItemClickListener? = null


    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    interface OnItemClickListener {
        fun onRecoverClick(task: Task)
    }

    fun updateData(newList: MutableList<Task?>) {
        val diffResult = DiffUtil.calculateDiff(
            DisplayableItemDiffCallback(
                items,
                newList
            )
        )
        items = newList
        diffResult.dispatchUpdatesTo(this)
    }

    inner class TaskViewHolder(private val binding: ItemDeletedCardBinding) : RecyclerView.ViewHolder(binding.root) {
        var color: Int? = null
        private val taskAdapter = TaskAdapter(mutableListOf())

        init {
            setupRecyclerView()
        }

        private fun setupRecyclerView() {
            binding.rvTasks.layoutManager = LinearLayoutManager(binding.root.context)
            binding.rvTasks.adapter = taskAdapter
            taskAdapter.attachItemTouchHelper(binding.rvTasks)
        }

        fun bind(item: Task?) {
            binding.cardTasksInProcess.visibility = View.GONE

            if (item?.subTasks != null) {
                binding.cardTasksInProcess.visibility = View.VISIBLE
            }
            binding.rvTasks.layoutManager = LinearLayoutManager(binding.root.context)
            binding.rvTasks.adapter = taskAdapter
            taskAdapter.attachItemTouchHelper(binding.rvTasks)
            item?.subTasks?.let { taskAdapter.updateData(it) }
            binding.categoryName.text = item?.category.toEditable()

            binding.btnRestore.setOnClickListener {
                if (item != null) {
                    itemClickListener?.onRecoverClick(item)
                }
            }
        }
    }

    fun String?.toEditable(): Editable? {
        return this?.let { Editable.Factory.getInstance().newEditable(this) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemDeletedCardBinding.inflate(inflater, parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    class DisplayableItemDiffCallback(
        private val oldList: List<Task?>,
        private val newList: List<Task?>
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
