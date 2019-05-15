package io.codelabs.zenitech.core.datasource.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.codelabs.zenitech.core.datasource.room.RoomAppDao
import io.codelabs.zenitech.data.Issue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class IssueRepository constructor(private val dao: RoomAppDao) {

    fun addIssue(vararg issue: Issue) = GlobalScope.launch(Dispatchers.IO) {
        dao.addIssue(*issue)
    }

    fun removeIssue(issue: Issue) = GlobalScope.launch(Dispatchers.IO) {
        dao.removeIssue(issue)
    }

    fun getIssueById(key: String): LiveData<Issue> {
        val _issue = MutableLiveData<Issue>()
        GlobalScope.launch(Dispatchers.IO) {
            _issue.postValue(dao.getIssue(key))
        }
        return _issue
    }

    fun getAllIssues(): LiveData<MutableList<Issue>> {
        val _issues = MutableLiveData<MutableList<Issue>>()

        GlobalScope.launch(Dispatchers.IO) {
            _issues.postValue(dao.getAllIssues())
        }

        return _issues
    }

}