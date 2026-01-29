package com.clipboard.manager.database

import androidx.lifecycle.LiveData

class ClipboardRepository(private val clipboardDao: ClipboardDao) {

    val allEntries: LiveData<List<ClipboardEntry>> = clipboardDao.getAllEntries()

    suspend fun insert(entry: ClipboardEntry) {
        // Check if content already exists to avoid duplicates
        val existing = clipboardDao.findByContent(entry.content)
        if (existing == null) {
            clipboardDao.insert(entry)
        }
    }

    suspend fun delete(entry: ClipboardEntry) {
        clipboardDao.delete(entry)
    }

    suspend fun deleteAll() {
        clipboardDao.deleteAll()
    }
}
