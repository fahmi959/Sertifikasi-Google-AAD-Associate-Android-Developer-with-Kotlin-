package com.fahmi.dicodingtodoaad.ui

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.fahmi.dicodingtodoaad.data.Task
import com.fahmi.dicodingtodoaad.databinding.TaskItemBinding
import com.fahmi.dicodingtodoaad.ui.detail.DetailTaskActivity
import com.fahmi.dicodingtodoaad.utils.DateConverter
import com.fahmi.dicodingtodoaad.utils.TASK_ID

class TaskAdapter(
    private val onCheckedChangeListener: (Task, Boolean) -> Unit
) : ListAdapter<Task, TaskAdapter.TaskViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = TaskItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = getItem(position)
        task?.let {
            holder.bind(task)
        }
    }

    inner class TaskViewHolder(private val binding: TaskItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        lateinit var task: Task

        init {
            binding.root.setOnClickListener {
                val detailIntent = Intent(binding.root.context, DetailTaskActivity::class.java)
                detailIntent.putExtra(TASK_ID, task.id)
                binding.root.context.startActivity(detailIntent)
            }

            binding.itemCheckbox.setOnCheckedChangeListener { _, isChecked ->
                onCheckedChangeListener.invoke(task, isChecked)
            }
        }

        fun bind(task: Task) {
            this.task = task
            binding.itemTvTitle.text = task.title
            binding.itemTvDate.text = DateConverter.convertMillisToString(task.dueDateMillis)
            binding.itemCheckbox.isChecked = task.isCompleted

            val isOverdue = DateConverter.isPastDeadline(task.dueDateMillis)
            val taskStatus = getTaskStatus(task.isCompleted, isOverdue)
            binding.itemTvTitle.setStatus(taskStatus)
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Task>() {
            override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
                return oldItem == newItem
            }


        }

        private fun getTaskStatus(isCompleted: Boolean, isOverdue: Boolean): Int {
            return if (isOverdue) {
                TaskTitleView.OVERDUE
            } else if (isCompleted) {
                TaskTitleView.DONE
            } else {
                TaskTitleView.NORMAL
            }
        }
    }
}
