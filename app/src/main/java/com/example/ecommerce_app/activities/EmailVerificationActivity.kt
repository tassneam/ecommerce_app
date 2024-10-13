package com.example.ecommerce_app.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ecommerce_app.databinding.ActivityEmailVerificationBinding
import com.google.firebase.auth.FirebaseAuth

class EmailVerificationActivity : AppCompatActivity() {

        private lateinit var binding: ActivityEmailVerificationBinding
        private lateinit var firebaseAuth: FirebaseAuth

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = ActivityEmailVerificationBinding.inflate(layoutInflater)
            setContentView(binding.root)

            firebaseAuth = FirebaseAuth.getInstance()

            binding.verificationBtn.setOnClickListener {
                val code = retrieveEnteredCode()

                if (code.isEmpty()) {
                    return@setOnClickListener
                }

                val user = firebaseAuth.currentUser

                if (user != null) {
                    val verificationTask = user.sendEmailVerification()
                    verificationTask.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val intent = Intent(this, NewPasswordActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            val error = task.exception?.message.toString()
                            Log.e("Firebase", "Verification failed: $error")
                        }
                    }
                } else {
                    Toast.makeText(this, "An error occurred. Please try again.", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

        private fun retrieveEnteredCode(): String {
            val digit1 = binding.digitsCodeLayout.digit1.text.toString()
            val digit2 = binding.digitsCodeLayout.digit2.text.toString()
            val digit3 = binding.digitsCodeLayout.digit3.text.toString()
            val digit4 = binding.digitsCodeLayout.digit4.text.toString()
            return digit1 + digit2 + digit3 + digit4
        }
    }
