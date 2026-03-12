package com.example.evaluation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ConfirmationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirmation)

        // Initialize Views
        val ivConfirmedImage = findViewById<ImageView>(R.id.ivConfirmedImage)
        val tvDispName = findViewById<TextView>(R.id.tvDispName)
        val tvDispPhone = findViewById<TextView>(R.id.tvDispPhone)
        val tvDispEmail = findViewById<TextView>(R.id.tvDispEmail)
        val tvDispType = findViewById<TextView>(R.id.tvDispType)
        val tvDispDate = findViewById<TextView>(R.id.tvDispDate)
        val tvDispGender = findViewById<TextView>(R.id.tvDispGender)
        val btnDone = findViewById<Button>(R.id.btnDone)

        // Get Data from Intent
        val name = intent.getStringExtra("FULL_NAME")
        val phone = intent.getStringExtra("PHONE")
        val email = intent.getStringExtra("EMAIL")
        val eventType = intent.getStringExtra("EVENT_TYPE")
        val eventDate = intent.getStringExtra("EVENT_DATE")
        val gender = intent.getStringExtra("GENDER")
        val imageUriString = intent.getStringExtra("IMAGE_URI")

        // Display Data
        tvDispName.text = name
        tvDispPhone.text = "Phone: $phone"
        tvDispEmail.text = "Email: $email"
        tvDispType.text = "Event: $eventType"
        tvDispDate.text = "Date: $eventDate"
        tvDispGender.text = "Gender: $gender"

        // Display Image if available
        if (!imageUriString.isNullOrEmpty() && imageUriString != "null") {
            try {
                val imageUri = Uri.parse(imageUriString)
                ivConfirmedImage.setImageURI(imageUri)
            } catch (e: Exception) {
                // Fallback to default launcher icon if URI is invalid
                ivConfirmedImage.setImageResource(R.mipmap.ic_launcher)
            }
        }

        btnDone.setOnClickListener {
            // Navigate back to Main Screen and clear the activity stack
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
    }
}