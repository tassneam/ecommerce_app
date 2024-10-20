package com.example.ecommerce_app.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ecommerce_app.databinding.ActivityNewPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class NewPasswordActivity : AppCompatActivity() {

        private lateinit var binding: ActivityNewPasswordBinding
        private lateinit var firebaseAuth: FirebaseAuth

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = ActivityNewPasswordBinding.inflate(layoutInflater)
            setContentView(binding.root)

            firebaseAuth = FirebaseAuth.getInstance()

            binding.confirmBtn.setOnClickListener {
                val newPassword = binding.newPassword.text.toString().trim()
                val ConfirmNewPassword = binding.ConfirmNewPassword.text.toString().trim()

                if (newPassword.isEmpty()) {
                    return@setOnClickListener
                }

                if (newPassword != ConfirmNewPassword) {
                    Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val user = firebaseAuth.currentUser

                if (user != null) {
                    user.updatePassword(newPassword)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val intent = Intent(this, SignInActivity::class.java)
                                intent.flags =
                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(intent)
                                finish()
                            } else {
                                val error = task.exception?.message.toString()
                                Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
                            }
                        }
                } else {
                    Toast.makeText(this, "An error occurred. Please try again.", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }
