package io.codelabs.zenitech.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import io.codelabs.sdk.util.toast
import io.codelabs.zenitech.R
import io.codelabs.zenitech.core.datasource.repository.IssueRepository
import io.codelabs.zenitech.core.util.isNotEmpty
import io.codelabs.zenitech.data.Issue
import io.codelabs.zenitech.data.Product
import kotlinx.android.synthetic.main.bottomsheet_post_issue.*
import org.koin.android.ext.android.inject

class PostIssueFragment : BottomSheetDialogFragment() {
    private val repository: IssueRepository by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottomsheet_post_issue, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        description.addTextChangedListener {
            post_issue.isEnabled = it != null && it.isNotEmpty()
        }

        post_issue.setOnClickListener {
            if (description.isNotEmpty()) {
                val category: String = when (button_group.checkedButtonId) {
                    R.id.button_laptop -> Product.Category.LAPTOP
                    R.id.button_phone -> Product.Category.MOBILE
                    else -> Product.Category.OTHER
                }

                repository.addIssue(
                    Issue(
                        System.currentTimeMillis().toString(),
                        description.text.toString(), category
                    )
                )

                //todo: post to online data source
                Snackbar.make(
                    requireActivity().findViewById(R.id.container),
                    " Posting your issue to ${getString(R.string.zenitech_app_name)}",
                    Snackbar.LENGTH_LONG
                ).show()
                dismiss()
            } else requireContext().toast("Enter a valid category name and a short description", true)
        }
    }

    companion object {
        const val FRAGMENT_TAG = "bottom_sheet_fragment_tag"
    }

}