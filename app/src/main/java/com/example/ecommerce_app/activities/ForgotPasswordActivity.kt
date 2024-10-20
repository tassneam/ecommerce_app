package com.example.ecommerce_app.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ecommerce_app.R
import com.example.ecommerce_app.databinding.ActivityForgotPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding
    private lateinit var firebaseAuth: FirebaseAuth

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
            setContentView(binding.root)

            firebaseAuth = FirebaseAuth.getInstance()

            binding.buttonResetPassword.setOnClickListener {
                val email = binding.editTextEmail.text.toString().trim()

                if (email.isEmpty()) {
                    binding.textViewErrorMessage.text = getString(R.string.email_required)
                    binding.textViewErrorMessage.visibility = View.VISIBLE
                    return@setOnClickListener
                }

                firebaseAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, getString(R.string.reset_email_sent), Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, EmailVerificationActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            val error = task.exception?.message.toString()
                            binding.textViewErrorMessage.text = error
                            binding.textViewErrorMessage.visibility = View.VISIBLE
                        }
                    }
            }
        }
    }