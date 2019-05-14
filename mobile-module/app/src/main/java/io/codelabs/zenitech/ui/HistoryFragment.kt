package io.codelabs.zenitech.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import io.codelabs.zenitech.R
import io.codelabs.zenitech.core.theme.BaseFragment
import io.codelabs.zenitech.databinding.FragmentHistoryBinding
import io.codelabs.zenitech.databinding.FragmentShopBinding

class HistoryFragment : BaseFragment() {
    private lateinit var binding: FragmentHistoryBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_history,container, false)
        return binding.root
    }

}