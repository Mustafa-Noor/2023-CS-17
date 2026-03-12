package com.example.evaluation

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Patterns
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var etFullName: EditText
    private lateinit var etPhoneNumber: EditText
    private lateinit var etEmail: EditText
    private lateinit var spinnerEventType: Spinner
    private lateinit var btnPickDate: Button
    private lateinit var tvSelectedDate: TextView
    private lateinit var rgGender: RadioGroup
    private lateinit var ivPreview: ImageView
    private lateinit var btnUploadImage: Button
    private lateinit var cbTerms: CheckBox
    private lateinit var btnSubmit: Button

    private var selectedDate: String = ""
    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Initialize views
        etFullName = findViewById(R.id.etFullName)
        etPhoneNumber = findViewById(R.id.etPhoneNumber)
        etEmail = findViewById(R.id.etEmail)
        spinnerEventType = findViewById(R.id.spinnerEventType)
        btnPickDate = findViewById(R.id.btnPickDate)
        tvSelectedDate = findViewById(R.id.tvSelectedDate)
        rgGender = findViewById(R.id.rgGender)
        ivPreview = findViewById(R.id.ivPreview)
        btnUploadImage = findViewById(R.id.btnUploadImage)
        cbTerms = findViewById(R.id.cbTerms)
        btnSubmit = findViewById(R.id.btnSubmit)

        setupSpinner()
        setupDatePicker()
        setupImagePicker()

        btnSubmit.setOnClickListener {
            if (validateForm()) {
                showConfirmationDialog()
            }
        }
    }

    private fun setupSpinner() {
        val eventTypes = arrayOf("Seminar", "Workshop", "Conference", "Webinar", "Cultural Event")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, eventTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerEventType.adapter = adapter
    }

    private fun setupDatePicker() {
        btnPickDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                tvSelectedDate.text = "Selected Date: $selectedDate"
            }, year, month, day)

            datePickerDialog.show()
        }
    }

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            ivPreview.setImageURI(it)
            ivPreview.visibility = View.VISIBLE
        }
    }

    private fun setupImagePicker() {
        btnUploadImage.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }
    }

    private fun validateForm(): Boolean {
        val name = etFullName.text.toString().trim()
        val phone = etPhoneNumber.text.toString().trim()
        val email = etEmail.text.toString().trim()

        if (name.isEmpty()) {
            Toast.makeText(this, "Full name cannot be empty", Toast.LENGTH_SHORT).show()
            return false
        }
        if (phone.isEmpty()) {
            Toast.makeText(this, "Phone number cannot be empty", Toast.LENGTH_SHORT).show()
            return false
        }
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Enter a valid email address", Toast.LENGTH_SHORT).show()
            return false
        }
        if (selectedDate.isEmpty()) {
            Toast.makeText(this, "Please select an event date", Toast.LENGTH_SHORT).show()
            return false
        }
        if (rgGender.checkedRadioButtonId == -1) {
            Toast.makeText(this, "Please select your gender", Toast.LENGTH_SHORT).show()
            return false
        }
        if (!cbTerms.isChecked) {
            Toast.makeText(this, "You must accept the terms and conditions", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun showConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Confirm Registration")
            .setMessage("Are you sure you want to submit your registration?")
            .setPositiveButton("Yes") { _, _ ->
                navigateToConfirmation()
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun navigateToConfirmation() {
        val selectedGenderId = rgGender.checkedRadioButtonId
        val gender = findViewById<RadioButton>(selectedGenderId).text.toString()
        val eventType = spinnerEventType.selectedItem.toString()

        val intent = Intent(this, ConfirmationActivity::class.java).apply {
            putExtra("FULL_NAME", etFullName.text.toString())
            putExtra("PHONE", etPhoneNumber.text.toString())
            putExtra("EMAIL", etEmail.text.toString())
            putExtra("EVENT_TYPE", eventType)
            putExtra("EVENT_DATE", selectedDate)
            putExtra("GENDER", gender)
            putExtra("IMAGE_URI", selectedImageUri.toString())
        }
        startActivity(intent)
    }
}