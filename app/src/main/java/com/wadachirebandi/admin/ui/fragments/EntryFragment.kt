package com.wadachirebandi.admin.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.util.Pair
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.android.material.datepicker.MaterialDatePicker
import com.wadachirebandi.admin.adapter.CustomerIdsAdapter
import com.wadachirebandi.admin.adapter.EntryAdapter
import com.wadachirebandi.admin.data.*
import com.wadachirebandi.admin.data.entry.SingleEntryData
import com.wadachirebandi.admin.databinding.FragmentEntryBinding
import com.wadachirebandi.admin.ui.viewmodels.EntryViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class EntryFragment : Fragment() {

    private var _binding: FragmentEntryBinding? = null

    private val binding get() = _binding!!

    private val viewModel by viewModels<EntryViewModel>()

    private val args: EntryFragmentArgs by navArgs()

    private lateinit var customerIdsAdapter: CustomerIdsAdapter

    private lateinit var entryAdapter: EntryAdapter

    private lateinit var dateRange: MaterialDatePicker<Pair<Long, Long>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.entryByVilla(args.villaId)

        viewModel.villaEntry.observe({lifecycle}){response->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    hideErrorMessage()
                    response.data?.let { entryResponse ->
                        if (entryResponse.date.entriesData.isNotEmpty()){
                            setAdapter(entryResponse.date.entriesData[0].documents)
                            setUi(entryResponse.date.entriesData[0])
                        }else{
                            noEntryUi()
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
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEntryBinding.inflate(layoutInflater, container, false)

        binding.noEntryCL.visibility = View.GONE

        binding.villaName.text = args.villaName

        entryAdapter = EntryAdapter(binding.root)

        customerIdsAdapter = CustomerIdsAdapter(binding.root)

        binding.rvEntries.adapter = entryAdapter

        viewModel.dateEntry.observe({lifecycle}){response->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    hideErrorMessage()
                    response.data?.let { entryResponse ->
                        if (!entryResponse.data.isNullOrEmpty()){
                            binding.entryCL.visibility = View.VISIBLE
                            binding.villaEntrySV.visibility = View.GONE
                            entryAdapter.differ.submitList(entryResponse.data)
                        }else{
                            noEntryUi()
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

        dateRange = MaterialDatePicker
            .Builder
            .dateRangePicker()
            .setTitleText("Select a date")
            .build()

        dateRange.addOnPositiveButtonClickListener {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val startDate = sdf.format(it.first)
            val endDate = sdf.format(it.second)
            Log.i("EntryDate", startDate + endDate)
            setDateEntry(startDate, endDate)
        }

        binding.btnDateFilterRV.setOnClickListener {
            dateRange.show(childFragmentManager, "DATE_RANGE_PICKER")
        }

        binding.btnDateFilterSV.setOnClickListener {
            dateRange.show(childFragmentManager, "DATE_RANGE_PICKER")
        }

        return binding.root
    }

    private fun noEntryUi() {
        binding.villaEntrySV.visibility = View.GONE
        binding.noEntryCL.visibility = View.VISIBLE
        binding.btnDateFilter.setOnClickListener {
            dateRange.show(childFragmentManager, "DATE_RANGE_PICKER")
        }
    }

    private fun setDateEntry(startDate: String, endDate: String) {
        viewModel.entryByDate(args.villaId, startDate, endDate)
    }

    private fun setUi(entryResponse: SingleEntryData) {
        binding.apply {
            villaEntrySV.visibility = View.VISIBLE
            binding.noEntryCL.visibility = View.GONE
            name.text = entryResponse.guestName
            customerNumber.text = entryResponse.contact
            guestNumber.text = entryResponse.guests
            paymentAmount.text = entryResponse.amount.toString()
            paymentMethod.text = entryResponse.paymentMethod
            checkInDate.text = entryResponse.checkIn
            checkOutDate.text = entryResponse.checkOut
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
        binding.noEntryCL.visibility = View.GONE
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