package com.clipboard.manager

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.clipboard.manager.adapter.ClipboardAdapter
import com.clipboard.manager.database.ClipboardEntry
import com.clipboard.manager.databinding.ActivityMainBinding
import com.clipboard.manager.service.ClipboardMonitorService
import com.clipboard.manager.ui.ClipboardViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: ClipboardViewModel
    private lateinit var adapter: ClipboardAdapter
    private var isMonitoring = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[ClipboardViewModel::class.java]

        setupRecyclerView()
        setupButtons()
        observeData()
    }

    private fun setupRecyclerView() {
        adapter = ClipboardAdapter(
            onItemClick = { entry -> copyToClipboard(entry) },
            onItemLongClick = { entry -> showDeleteDialog(entry) }
        )
        
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    private fun setupButtons() {
        binding.buttonToggleMonitoring.setOnClickListener {
            toggleMonitoring()
        }

        binding.buttonClearHistory.setOnClickListener {
            showClearHistoryDialog()
        }
    }

    private fun observeData() {
        viewModel.allEntries.observe(this) { entries ->
            adapter.submitList(entries)
            updateEmptyState(entries.isEmpty())
        }
    }

    private fun toggleMonitoring() {
        isMonitoring = !isMonitoring
        val intent = Intent(this, ClipboardMonitorService::class.java)
        
        if (isMonitoring) {
            startService(intent)
            binding.buttonToggleMonitoring.text = getString(R.string.stop_monitoring)
            binding.statusText.text = getString(R.string.monitoring_active)
            Toast.makeText(this, getString(R.string.monitoring_active), Toast.LENGTH_SHORT).show()
        } else {
            stopService(intent)
            binding.buttonToggleMonitoring.text = getString(R.string.start_monitoring)
            binding.statusText.text = getString(R.string.monitoring_inactive)
            Toast.makeText(this, getString(R.string.monitoring_inactive), Toast.LENGTH_SHORT).show()
        }
    }

    private fun copyToClipboard(entry: ClipboardEntry) {
        val clipboard = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("clipboard", entry.content)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(this, getString(R.string.copy_to_clipboard), Toast.LENGTH_SHORT).show()
    }

    private fun showDeleteDialog(entry: ClipboardEntry) {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.delete))
            .setMessage("确定要删除这条记录吗？")
            .setPositiveButton("删除") { _, _ ->
                viewModel.delete(entry)
                Toast.makeText(this, "已删除", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("取消", null)
            .show()
    }

    private fun showClearHistoryDialog() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.clear_history))
            .setMessage("确定要清空所有历史记录吗？")
            .setPositiveButton("清空") { _, _ ->
                viewModel.deleteAll()
                Toast.makeText(this, "已清空历史", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("取消", null)
            .show()
    }

    private fun updateEmptyState(isEmpty: Boolean) {
        if (isEmpty) {
            binding.emptyText.visibility = android.view.View.VISIBLE
            binding.recyclerView.visibility = android.view.View.GONE
        } else {
            binding.emptyText.visibility = android.view.View.GONE
            binding.recyclerView.visibility = android.view.View.VISIBLE
        }
    }
}
