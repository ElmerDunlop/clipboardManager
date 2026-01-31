package com.clipboard.manager.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.clipboard.manager.database.ClipboardDatabase
import com.clipboard.manager.database.ClipboardEntry
import com.clipboard.manager.database.ClipboardRepository
import kotlinx.coroutines.launch

class ClipboardViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ClipboardRepository
    val allEntries: LiveData<List<ClipboardEntry>>

    init {
        val database = ClipboardDatabase.getDatabase(application)
        repository = ClipboardRepository(database.clipboardDao())
        allEntries = repository.allEntries
    }

    fun delete(entry: ClipboardEntry) = viewModelScope.launch {
        repository.delete(entry)
    }

    fun deleteAll() = viewModelScope.launch {
        repository.deleteAll()
    }
}
