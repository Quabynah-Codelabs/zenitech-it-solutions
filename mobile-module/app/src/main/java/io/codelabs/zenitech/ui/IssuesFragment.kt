package io.codelabs.zenitech.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.codelabs.recyclerview.GridItemDividerDecoration
import io.codelabs.recyclerview.SlideInItemAnimator
import io.codelabs.zenitech.R
import io.codelabs.zenitech.core.datasource.repository.IssueRepository
import io.codelabs.zenitech.core.theme.BaseFragment
import io.codelabs.zenitech.databinding.FragmentIssuesBinding
import io.codelabs.zenitech.ui.recyclerview.IssueAdapter
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class IssuesFragment : BaseFragment() {
    private lateinit var binding: FragmentIssuesBinding
    private lateinit var adapter: IssueAdapter
    private val repository: IssueRepository by inject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_issues, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = IssueAdapter(requireContext(), repository)

        binding.grid.adapter = adapter
        binding.grid.layoutManager = LinearLayoutManager(requireContext()) as RecyclerView.LayoutManager?
        binding.grid.setHasFixedSize(true)
        binding.grid.addItemDecoration(
            GridItemDividerDecoration(
                requireContext(),
                R.dimen.divider_height,
                R.color.divider
            )
        )
        binding.grid.itemAnimator = SlideInItemAnimator()

        loadLiveData()
    }

    private fun loadLiveData() {
        repository.getAllIssues().observe(viewLifecycleOwner, Observer {
            if (it != null) adapter.addDataSource(it)
        })
    }

}