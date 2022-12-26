package com.wadachirebandi.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.wadachirebandi.adapter.EntryAdapter
import com.wadachirebandi.data.Resource
import com.wadachirebandi.data.entry.SingleEntry
import com.wadachirebandi.databinding.FragmentEditEntryBinding
import com.wadachirebandi.ui.viewmodels.EntryViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditEntryFragment : Fragment() {

    private var _binding: FragmentEditEntryBinding? = null

    private val binding get() = _binding!!

    private val viewModel by viewModels<EntryViewModel>()

    private lateinit var entryAdapter: EntryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditEntryBinding.inflate(layoutInflater, container, false)

        entryAdapter = EntryAdapter(viewModel)

        viewModel.dailyEntry()

        viewModel.dailyEntry.observe({ lifecycle }) { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    hideErrorMessage()
                    response.data?.let { entryResponse ->
                        if (entryResponse.data.isNotEmpty()) {
                            setAdapter(entryResponse.data)
                        } else {
                            binding.noEntryTextView.visibility = View.VISIBLE
                        }

                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Toast.makeText(activity, "An error occurred: $message", Toast.LENGTH_LONG)
                            .show()
                        showErrorMessage(message)
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }

        }

        viewModel.deleteEntryResource.observe({ lifecycle }) { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    hideErrorMessage()
                    response.data?.let {
                        Toast.makeText(
                            requireContext(),
                            "Entry Deleted Successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                        findNavController().navigateUp()
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Toast.makeText(activity, "An error occurred: $message", Toast.LENGTH_LONG)
                            .show()
                        showErrorMessage(message)
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        }

        return binding.root
    }

    private fun setAdapter(Entry: List<SingleEntry>) {
        entryAdapter.differ.submitList(Entry)
        binding.rvEntry.visibility = View.VISIBLE
        binding.rvEntry.adapter = entryAdapter
    }

    private fun hideProgressBar() {
        binding.progressBarCL.visibility = View.GONE
        binding.progressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        binding.progressBarCL.visibility = View.VISIBLE
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideErrorMessage() {
        binding.errorMessageCL.visibility = View.INVISIBLE
    }

    private fun showErrorMessage(message: String) {
        binding.errorMessageCL.visibility = View.VISIBLE
        binding.itemErrorMessage.tvErrorMessage.text = message
    }

}