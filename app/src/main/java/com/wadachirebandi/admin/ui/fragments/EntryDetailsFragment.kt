package com.wadachirebandi.admin.ui.fragments

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.wadachirebandi.admin.adapter.CustomerIdsAdapter
import com.wadachirebandi.admin.data.Resource
import com.wadachirebandi.admin.data.entry.SingleEntryData
import com.wadachirebandi.admin.databinding.FragmentEntryDetailsBinding
import com.wadachirebandi.admin.ui.viewmodels.EntryViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

@AndroidEntryPoint
class EntryDetailsFragment : Fragment() {

    private var _binding: FragmentEntryDetailsBinding? = null

    private val binding get() = _binding!!

    private val viewModel by viewModels<EntryViewModel>()

    private val args: EntryDetailsFragmentArgs by navArgs()

    private lateinit var customerIdsAdapter: CustomerIdsAdapter

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEntryDetailsBinding.inflate(layoutInflater, container, false)

        viewModel.singleEntry(args.entryId)

        customerIdsAdapter = CustomerIdsAdapter(binding.root)

        viewModel.singleEntry.observe({ lifecycle }) { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    hideErrorMessage()
                    response.data?.let { entryResponse ->
                        setAdapter(entryResponse.date.entriesData.documents)
                        setUi(entryResponse.date.entriesData)
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setUi(entriesData: SingleEntryData) {
        binding.apply {
            villaEntrySV.visibility = View.VISIBLE
            name.text = entriesData.guestName
            customerNumber.text = entriesData.contact
            guestNumber.text = entriesData.guests
            paymentAmount.text = entriesData.amount.toString()
            paymentMethod.text = entriesData.paymentMethod
            val inputFormatter: DateTimeFormatter =
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)
            val outputFormatter: DateTimeFormatter =
                DateTimeFormatter.ofPattern("dd-MM-yyy", Locale.ENGLISH)
            val checkIn: LocalDate = LocalDate.parse(entriesData.checkIn, inputFormatter)
            val checkOut: LocalDate = LocalDate.parse(entriesData.checkOut, inputFormatter)
            val checkInFinal: String = outputFormatter.format(checkIn)
            val checkOutFinal: String = outputFormatter.format(checkOut)

            checkInDate.text = checkInFinal
            checkOutDate.text = checkOutFinal
        }
    }

    private fun setAdapter(data: List<String>) {
        customerIdsAdapter.differ.submitList(data)
        binding.rvCustomerIds.adapter = customerIdsAdapter
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