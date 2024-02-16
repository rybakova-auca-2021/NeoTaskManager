package com.example.neotaskmanager.presentation.ui.main

import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.neotaskmanager.R
import com.example.neotaskmanager.data.model.Task
import com.example.neotaskmanager.data.model.TaskData
import com.example.neotaskmanager.databinding.ItemCategoryCardBinding

class CategoryAdapter(var items: MutableList<Task?>) : RecyclerView.Adapter<CategoryAdapter.TaskViewHolder>(){
    private var itemClickListener: OnItemClickListener? = null


    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    interface OnItemClickListener {
        fun onTaskItemClick(item: Task?)
        fun onSpinnerClickListener()
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

    inner class TaskViewHolder(private val binding: ItemCategoryCardBinding) : RecyclerView.ViewHolder(binding.root) {
        var isClicked = true
        init {
            val taskAdapter = TaskAdapter(mutableListOf())
            binding.rvTasks.layoutManager = LinearLayoutManager(binding.root.context)
            binding.rvTasks.adapter = taskAdapter
            taskAdapter.attachItemTouchHelper(binding.rvTasks)

            // Spinner Setup
            val spinnerItems = arrayOf(R.drawable.spinner_item_1, R.drawable.spinner_item_2, R.drawable.spinner_item_3, R.drawable.spinner_item_4, R.drawable.spinner_item_5)
            val adapter = object : ArrayAdapter<Int>(binding.root.context, R.layout.item_circle, spinnerItems) {
                override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                    val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_circle, parent, false)
                    val drawable = ContextCompat.getDrawable(binding.root.context, spinnerItems[position])
                    view.background = drawable
                    return view
                }

                override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                    return getView(position, convertView, parent)
                }
            }

            binding.spinner.adapter = adapter

            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val clickedItem = items[position]
                    itemClickListener?.onTaskItemClick(clickedItem)
                    val drawable = ContextCompat.getDrawable(binding.root.context, R.drawable.rounded_red_card_background)
                    binding.card.background = drawable
                    binding.btnDelete.visibility = View.VISIBLE
                    binding.btnDelete.setOnClickListener {
                        deleteItem(position)
                    }
                }
            }

            binding.categoryName.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    val text = s.toString().trim()
                    binding.addTaskBtn.visibility = if (text.isNotEmpty()) View.VISIBLE else View.GONE
                    binding.star.visibility = if (text.isNotEmpty()) View.VISIBLE else View.GONE
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })

            binding.addTaskBtn.setOnClickListener {
                binding.cardTasksInProcess.visibility = View.VISIBLE
                val emptyTask = TaskData("", false)
                taskAdapter.items.add(emptyTask)
                taskAdapter.notifyItemInserted(taskAdapter.items.size - 1)
            }

            binding.star.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    if(isClicked) {
                        binding.star.setImageResource(R.drawable.star_clicked)
                        moveToTop(position)
                    } else {
                        binding.star.setImageResource(R.drawable.icon_star)
                        moveToOriginalPosition(position)
                    }
                    isClicked = !isClicked
                }
            }
        }

        fun bind(item: Task?) {

        }
    }

    fun moveToTop(position: Int) {
        val item = items.removeAt(position)
        items.add(0, item)
        notifyItemMoved(position, 0)
    }

    fun moveToOriginalPosition(position: Int) {
        val item = items.removeAt(position)
        items.add(item)
        notifyItemMoved(position, items.size - 1)
    }

    fun deleteItem(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCategoryCardBinding.inflate(inflater, parent, false)
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
