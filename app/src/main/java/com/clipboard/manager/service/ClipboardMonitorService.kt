package com.clipboard.manager.service

import android.app.Service
import android.content.ClipboardManager
import android.content.Intent
import android.os.IBinder
import androidx.lifecycle.ViewModelProvider
import com.clipboard.manager.database.ClipboardDatabase
import com.clipboard.manager.database.ClipboardEntry
import com.clipboard.manager.database.ClipboardRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class ClipboardMonitorService : Service() {

    private lateinit var clipboardManager: ClipboardManager
    private lateinit var repository: ClipboardRepository
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val clipboardListener = ClipboardManager.OnPrimaryClipChangedListener {
        handleClipboardChange()
    }

    override fun onCreate() {
        super.onCreate()
        clipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        
        val database = ClipboardDatabase.getDatabase(applicationContext)
        repository = ClipboardRepository(database.clipboardDao())
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        clipboardManager.addPrimaryClipChangedListener(clipboardListener)
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        clipboardManager.removePrimaryClipChangedListener(clipboardListener)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun handleClipboardChange() {
        val clipData = clipboardManager.primaryClip
        if (clipData != null && clipData.itemCount > 0) {
            val text = clipData.getItemAt(0).text
            if (!text.isNullOrEmpty()) {
                serviceScope.launch {
                    val entry = ClipboardEntry(content = text.toString())
                    repository.insert(entry)
                }
            }
        }
    }
}
