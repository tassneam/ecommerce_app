package com.example.ecommerce_app.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ecommerce_app.databinding.ActivitySignUpBinding
import com.example.ecommerce_app.fragments.shopping.HomeFragment
import com.google.firebase.auth.FirebaseAuth


class SignUpActivity : AppCompatActivity() {

        private lateinit var binding: ActivitySignUpBinding
        private lateinit var firebaseAuth: FirebaseAuth

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            binding = ActivitySignUpBinding.inflate(layoutInflater)
            setContentView(binding.root)

            firebaseAuth = FirebaseAuth.getInstance()

            binding.toSignIn.setOnClickListener {
                val intent = Intent(this, SignInActivity::class.java)
                startActivity(intent)
            }

            binding.signUpBtn.setOnClickListener {
                val username = binding.usernameEditText.text.toString()
                val phone = binding.phoneEditText.text.toString()
                val email = binding.emailEditText.text.toString()
                val password = binding.passwordEditText.text.toString()
                val confirmPassword = binding.confirmPasswordEditText.text.toString()

                if (username.isNotEmpty() && phone.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
                    if (password == confirmPassword) {

                        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                                if (it.isSuccessful) {
                                    val intent = Intent(this, HomeFragment::class.java)
                                    startActivity(intent)
                                } else {
                                    Toast.makeText(
                                        this,
                                        it.exception.toString(),
                                        Toast.LENGTH_SHORT
                                    ).show()

                                }
                            }
                    } else {
                        Toast.makeText(this, "Password is not matching", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "Empty Fields Are not Allowed !!", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
}