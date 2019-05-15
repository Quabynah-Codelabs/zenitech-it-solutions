package io.codelabs.zenitech.ui.recyclerview

import android.content.Context
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.codelabs.zenitech.R
import io.codelabs.zenitech.core.datasource.repository.IssueRepository
import io.codelabs.zenitech.data.Issue
import kotlinx.android.synthetic.main.item_empty_issue.view.*
import kotlinx.android.synthetic.main.item_issue.view.*

class IssueAdapter constructor(
    private val context: Context,
    private val repository: IssueRepository
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val EMPTY = R.layout.item_empty_issue
        private const val ISSUE = R.layout.item_issue
    }

    private val dataSource = mutableListOf<Issue>()
    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var parent: ViewGroup? = null
    private var tappedIssue: Issue? = null

    override fun getItemViewType(position: Int): Int = if (dataSource.isEmpty()) EMPTY else ISSUE

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        this.parent = parent

        return when (viewType) {
            EMPTY -> EmptyViewHolder(inflater.inflate(EMPTY, parent, false))
            ISSUE -> IssueViewHolder(inflater.inflate(ISSUE, parent, false))
            else -> throw IllegalArgumentException("No viewholder defined")
        }
    }

    override fun getItemCount(): Int = if (dataSource.isEmpty()) 1 else dataSource.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            EMPTY -> bindEmptyView(holder as EmptyViewHolder)
            ISSUE -> bindIssues(holder as IssueViewHolder, position)
        }
    }

    private fun bindEmptyView(holder: EmptyViewHolder) {
        holder.v.issue_shimmer_container.startShimmer()
    }

    private fun bindIssues(holder: IssueViewHolder, position: Int) {
        val issue = dataSource[position]

        holder.v.issue_name.text = issue.description
        holder.v.issue_timestamp.text = DateUtils.getRelativeTimeSpanString(issue.timestamp)

        holder.v.setOnClickListener {  }
    }

    fun addDataSource(issues: MutableList<Issue>) {
        this.dataSource.clear()
        this.dataSource.addAll(issues)
        notifyDataSetChanged()
    }
}