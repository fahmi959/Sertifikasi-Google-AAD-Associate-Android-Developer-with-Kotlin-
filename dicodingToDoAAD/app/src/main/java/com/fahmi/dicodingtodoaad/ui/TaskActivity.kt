package com.fahmi.dicodingtodoaad.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fahmi.dicodingtodoaad.R
import com.fahmi.dicodingtodoaad.databinding.ActivityTaskBinding
import com.fahmi.dicodingtodoaad.notification.NotificationWorker.Companion.createNotificationChannel
import com.fahmi.dicodingtodoaad.setting.SettingsActivity
import com.fahmi.dicodingtodoaad.ui.add.AddTaskActivity
import com.fahmi.dicodingtodoaad.utils.TasksFilterType
import com.google.android.material.snackbar.Snackbar



class TaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTaskBinding
    private lateinit var taskViewModel: TaskViewModel

    private fun showSnackBar(message: Int) {
        Snackbar.make(
            binding.coordinatorLayout,
            message,
            Snackbar.LENGTH_SHORT
        ).show()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        // Memanggil metode createNotificationChannel
        createNotificationChannel(this)

        val layoutManager = LinearLayoutManager(this)
        binding.rvTask.layoutManager = layoutManager

        val factory = ViewModelFactory.getInstance(this)
        taskViewModel = ViewModelProvider(this, factory).get(TaskViewModel::class.java)

        val adapter = TaskAdapter { task, isChecked ->
            taskViewModel.completeTask(task, isChecked)
        }
        binding.rvTask.adapter = adapter

        taskViewModel.tasks.observe(this, { tasks ->
            adapter.submitList(tasks)
        })

        taskViewModel.snackbarText.observe(this, { eventMessage ->
            eventMessage.getContentIfNotHandled()?.let { message ->
                showSnackBar(message)
            }
        })

        binding.fab.setOnClickListener {
            val addIntent = Intent(this, AddTaskActivity::class.java)
            startActivity(addIntent)
        }

        initAction()
    }

    private fun initAction() {
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.Callback() {
            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                return makeMovementFlags(0, ItemTouchHelper.RIGHT)
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val task = (viewHolder as TaskAdapter.TaskViewHolder).task
                task?.let { taskViewModel.deleteTask(it) }
            }
        })
        itemTouchHelper.attachToRecyclerView(binding.rvTask)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                val settingIntent = Intent(this, SettingsActivity::class.java)
                startActivity(settingIntent)
                true
            }
            R.id.action_filter -> {
                showFilteringPopupMenu()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    private fun showFilteringPopupMenu() {
        val view = binding.appBarLayout
        PopupMenu(this, view).run {
            menuInflater.inflate(R.menu.filter_tasks, menu)

            setOnMenuItemClickListener { menuItem ->
                val filterType = when (menuItem.itemId) {
                    R.id.active -> TasksFilterType.ACTIVE_TASKS
                    R.id.completed -> TasksFilterType.COMPLETED_TASKS
                    else -> TasksFilterType.ALL_TASKS
                }
                taskViewModel.filter(filterType)
                true
            }
            show()
        }
    }

}