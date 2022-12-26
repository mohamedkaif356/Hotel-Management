package com.wadachirebandi.ui.fragments

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.AnimatedVectorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.wadachirebandi.R
import com.wadachirebandi.adapter.ViewPagerAdapter
import com.wadachirebandi.data.entry.Entry
import com.wadachirebandi.data.Resource
import com.wadachirebandi.databinding.FragmentAddEntryBinding
import com.wadachirebandi.ui.viewmodels.EntryViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class AddEntryFragment : Fragment() {

    private var _binding: FragmentAddEntryBinding? = null

    private val binding get() = _binding!!

    private val viewModel by viewModels<EntryViewModel>()

    private val args: AddEntryFragmentArgs by navArgs()

    private lateinit var villaId: String

    private lateinit var villaName: String

    private var guestNumber = "2"

    private var paymentMethod = "Cash"

    private lateinit var cameraLauncher: ActivityResultLauncher<Intent>

    private lateinit var galleryLauncher: ActivityResultLauncher<Intent>

    private lateinit var adapter: ViewPagerAdapter

    private var storageRef = Firebase.storage.reference

    private var documentUrl = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddEntryBinding.inflate(layoutInflater, container, false)

        villaId = args.villaId

        villaName = args.villaName

        setUi()

        viewModel.entryResource.observe({ lifecycle }) { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    hideErrorMessage()
                    entryAddedSuccessfully()
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

    private fun entryAddedSuccessfully() {
        binding.entryAddedCL.visibility = View.VISIBLE
        animation()
        binding.name.setText("")
        binding.number.setText("")
        binding.email.setText("")
        binding.checkInDate.setText("")
        binding.checkOutDate.setText("")
        binding.amount.setText("")
        documentUrl.clear()
        setViewPager()
        binding.addEntrySV.visibility = View.GONE
        binding.home.setOnClickListener {
            findNavController().navigateUp()
        }

    }

    private fun animation() {
        val drawable = binding.done.drawable

        if (drawable is AnimatedVectorDrawableCompat) {
            val avd: AnimatedVectorDrawableCompat = drawable
            avd.start()
        } else if (drawable is AnimatedVectorDrawable) {
            val avd2: AnimatedVectorDrawable = drawable
            avd2.start()
        }
    }

    private fun setUi() {

        cameraLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                    binding.progressBarImage.visibility = View.VISIBLE
                    binding.noDocumentTV.visibility = View.GONE
                    val thumbnail: Bitmap = result.data!!.extras!!.get("data") as Bitmap
                    val uri: Uri = saveImageToInternalStorage(thumbnail)
                    uri.let {
                        val imageRef = storageRef
                            .child("CustomerID/ + ${it.lastPathSegment}")

                        val uploadTask = it.let { it1 -> imageRef.putFile(it1) }
                        uploadTask.addOnSuccessListener {
                            val url = imageRef.downloadUrl
                            url.addOnSuccessListener { uri ->
                                documentUrl.add(uri.toString())
                                setViewPager()
                            }
                            binding.progressBarImage.visibility = View.GONE
                        }
                        uploadTask.addOnFailureListener { exception ->
                            Toast.makeText(
                                requireContext(),
                                exception.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                            binding.progressBarImage.visibility = View.GONE
                        }
                    }
                }
            }

        galleryLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                    binding.progressBarImage.visibility = View.VISIBLE
                    binding.noDocumentTV.visibility = View.GONE
                    val uri: Uri = result.data!!.data!!.normalizeScheme()
                    uri.let {
                        val imageRef = storageRef
                            .child("CustomerID/ + ${it.lastPathSegment}")

                        val uploadTask = it.let { it1 -> imageRef.putFile(it1) }
                        uploadTask.addOnSuccessListener {
                            val url = imageRef.downloadUrl
                            url.addOnSuccessListener { uri ->
                                documentUrl.add(uri.toString())
                                setViewPager()
                            }
                            binding.progressBarImage.visibility = View.GONE
                        }
                        uploadTask.addOnFailureListener { exception ->
                            Toast.makeText(
                                requireContext(),
                                exception.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                            binding.progressBarImage.visibility = View.GONE
                        }
                    }
                }
            }

        setProductImage()

        val checkIn = MaterialDatePicker
            .Builder
            .datePicker()
            .setTitleText("Select Check-In Date")
            .build()

        val checkOut = MaterialDatePicker
            .Builder
            .datePicker()
            .setTitleText("Select Check-Out Date")
            .build()


        binding.checkInDate.setOnClickListener {
            checkIn.show(childFragmentManager, "DATE_PICKER")
        }

        binding.checkOutDate.setOnClickListener {
            checkOut.show(childFragmentManager, "DATE_PICKER")
        }

        checkIn.addOnPositiveButtonClickListener {
            val sdf = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
            val date = sdf.format(it)
            binding.checkInDate.setText(date)
        }

        checkOut.addOnPositiveButtonClickListener {
            val sdf = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
            val date = sdf.format(it)
            binding.checkOutDate.setText(date)
        }

        binding.guestSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    val number = p0?.getItemAtPosition(p2).toString()
                    guestNumber = number
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }
            }

        binding.paymentSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    val method = p0?.getItemAtPosition(p2).toString()
                    paymentMethod = method
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }
            }

        setPaymentSpinner()

        setGuestSpinner()

        binding.addEntry.setOnClickListener {
            if (binding.name.text.toString() != "" && binding.number.text.toString() != ""
                && binding.checkInDate.text.toString() != ""
                && binding.checkOutDate.text.toString() != ""
                && binding.amount.text.toString() != ""
            ) {
                addEntry()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Please entry all information properly",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }

    private fun addEntry() {
        val entry = Entry(
            amount = binding.amount.text.toString().toInt(),
            arrivingDate = binding.checkInDate.text.toString(),
            checkIn = "${binding.checkInDate.text.toString()}Z",
            checkOut = "${binding.checkOutDate.text.toString()}Z",
            contact = binding.number.text.toString().toLong(),
            guestName = binding.name.text.toString(),
            documents = documentUrl,
            breakfast = binding.breakFast.isChecked,
            lunch = binding.lunch.isChecked,
            dinner = binding.dinner.isChecked,
            guests = guestNumber,
            paymentMethod = paymentMethod,
            villaId = villaId,
            villaNo = villaName,

            )
        viewModel.entry(entry)
    }

    private fun setGuestSpinner() {
        val guest = mutableListOf("2", "2+1", "2+2", "4+1")

        val arrayAdapter: ArrayAdapter<Any?> = ArrayAdapter<Any?>(
            requireContext(),
            R.layout.spinner_list,
            guest as List<Any?>
        )

        arrayAdapter.setDropDownViewResource(R.layout.spinner_list)

        binding.guestSpinner.adapter = arrayAdapter
    }

    private fun setPaymentSpinner() {
        val payment = mutableListOf("Cash", "Google Pay", "Phone Pay", "Online")

        val arrayAdapter: ArrayAdapter<Any?> = ArrayAdapter<Any?>(
            requireContext(),
            R.layout.spinner_list,
            payment as List<Any?>
        )

        arrayAdapter.setDropDownViewResource(R.layout.spinner_list)

        binding.paymentSpinner.adapter = arrayAdapter
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

    private fun setProductImage() {
        binding.camearImage.setOnClickListener {
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also {
                cameraLauncher.launch(it)
            }
        }

        binding.galleryImage.setOnClickListener {
            Intent(Intent.ACTION_GET_CONTENT).also {
                it.type = "image/*"
                galleryLauncher.launch(it)
            }
        }
    }

    private fun setViewPager() {
        if (documentUrl.isNotEmpty()) {
            binding.noDocumentTV.visibility = View.GONE
        } else {
            binding.noDocumentTV.visibility = View.VISIBLE
        }

        adapter = ViewPagerAdapter(documentUrl, binding.root)

        binding.imageViewPager.adapter = adapter
    }

    private fun saveImageToInternalStorage(bitmap: Bitmap): Uri {

        val file = File(context?.filesDir, "${UUID.randomUUID()}.jpg")

        try {
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream)
            stream.flush()
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return Uri.fromFile(file)
    }
}