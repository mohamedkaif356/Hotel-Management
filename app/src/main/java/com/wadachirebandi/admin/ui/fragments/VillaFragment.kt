package com.wadachirebandi.admin.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.wadachirebandi.admin.adapter.VillaAdapter
import com.wadachirebandi.admin.data.Resource
import com.wadachirebandi.admin.data.villa.VillaData
import com.wadachirebandi.admin.databinding.FragmentVillaBinding
import com.wadachirebandi.admin.ui.viewmodels.VillaViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VillaFragment : Fragment() {

    private var _binding: FragmentVillaBinding? = null

    private val binding get() = _binding!!

    private val viewModel by viewModels<VillaViewModel>()

    private lateinit var adapter: VillaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.villa()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVillaBinding.inflate(layoutInflater, container, false)

        adapter = VillaAdapter(binding.root)

        viewModel.villa.observe({ lifecycle }) { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    hideErrorMessage()
                    response.data?.let { villaResponse ->
                        setAdapter(villaResponse.data)

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

        binding.itemErrorMessage.btnRetry.setOnClickListener {
            viewModel.villa()
        }

        return binding.root
    }

    private fun setAdapter(data: List<VillaData>?) {
        adapter.differ.submitList(data)
        binding.rvBreakingNews.adapter = adapter
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