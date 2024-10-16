package com.example.ecommerce_app.activities

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ecommerce_app.databinding.ActivitySignUpBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.firestore.FirebaseFirestore
import java.io.File

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var progressBar: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        // Initialize Firebase App
        FirebaseApp.initializeApp(this)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Initialize ProgressBar and set to invisible
        progressBar = binding.progressbarSignup
        progressBar.visibility = View.GONE
        // Initialize Firebase Auth and Firestore
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        // Enable Firestore logging for more information
        FirebaseFirestore.setLoggingEnabled(true)

        binding.signUpBtn.setOnClickListener {
            // Ensure directory is created before trying to access it
            val file = File(getExternalFilesDir(null), "example.txt")
            if (!file.exists()) {
                file.mkdirs()
            }
            ////////////////
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()
            val confirmPassword = binding.confirmPasswordEditText.text.toString().trim()
            val username = binding.usernameEditText.text.toString().trim()
            val phone = binding.phoneEditText.text.toString().trim()

            // Check for network connectivity before making a request
            if (!isNetworkAvailable()) {
                Toast.makeText(this, "No internet connection available", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Input validation
            if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || username.isEmpty() || phone.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.length < 6) {
                Toast.makeText(this, "Password must be at least 6 characters long", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Show the ProgressBar
            progressBar.visibility = View.VISIBLE

            // Firebase create user with email and password
            // Firebase create user with email and password
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    // Hide the ProgressBar after task completion
                    progressBar.visibility = View.GONE
                    if (task.isSuccessful) {
                        // User created successfully, now save additional user info
                        val userId = auth.currentUser?.uid
                        Log.d("Auth", "User ID: $userId") // Check if userId is valid

                        // Proceed if userId is valid
                        if (userId != null) {
                            val user = hashMapOf(
                                "username" to username,
                                "email" to email,
                                "phone" to phone
                            )

                            // Save user data to Firestore
                            firestore.collection("users").document(userId)
                                .set(user)
                                .addOnSuccessListener {
                                    Toast.makeText(this, "Sign-up successful", Toast.LENGTH_SHORT).show()
                                    val intent = Intent(this, SignInActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                                .addOnFailureListener { e ->
                                    Log.e("Firestore", "Failed to save user data: ${e.message}", e)
                                    Toast.makeText(this, "Failed to save user data: ${e.message}", Toast.LENGTH_LONG).show()
                                }
                        } else {
                            Log.e("AuthError", "User ID is null after sign up.")
                            Toast.makeText(this, "Unexpected error occurred. Please try again.", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        // Handle specific authentication errors
                        handleAuthError(task.exception)
                    }
                }

        }
        binding.toSignIn.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }

    }

    private fun handleAuthError(exception: Exception?) {
        when (exception) {
            is FirebaseAuthUserCollisionException -> {
                Toast.makeText(this, "This email already exists. Please use another email.", Toast.LENGTH_SHORT).show()
            }
            else -> {
                Log.e("AuthError", "Failed: ${exception?.message}", exception)
                Toast.makeText(this, "Error: ${exception?.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork?.let {
                connectivityManager.getNetworkCapabilities(it)
            }
            return networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
        } else {
            val networkInfo = connectivityManager.activeNetworkInfo
            return networkInfo?.isConnected == true
        }
    }
}
//////////////////////////////////////////

       /* binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

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
                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Save additional user information to Firestore
                            val userId = firebaseAuth.currentUser?.uid
                            val userMap = hashMapOf(
                                "username" to username,
                                "phone" to phone,
                                "email" to email
                            )

                            if (userId != null) {
                                firestore.collection("users").document(userId).set(userMap).addOnCompleteListener { firestoreTask ->
                                    if (firestoreTask.isSuccessful) {
                                        Log.d("Firestore", "User data saved successfully!")
                                        val intent = Intent(this, MainActivity::class.java)
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                        startActivity(intent)
                                        finish()
                                    } else {
                                        Log.e("Firestore", "Failed to save user data: ${firestoreTask.exception?.message}", firestoreTask.exception)
                                        Toast.makeText(this, "Failed to save user data: ${firestoreTask.exception?.message}", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            } else {
                                Log.e("AuthError", "User ID is null after sign up.")
                                Toast.makeText(this, "Unexpected error occurred. Please try again.", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            handleAuthError(task.exception)
                        }
                    }
                } else {
                    Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun handleAuthError(exception: Exception?) {
        when (exception) {
            is FirebaseAuthWeakPasswordException -> {
                Toast.makeText(this, "Weak password, please use at least 6 characters.", Toast.LENGTH_SHORT).show()
            }
            is FirebaseAuthInvalidCredentialsException -> {
                Toast.makeText(this, "Invalid email format.", Toast.LENGTH_SHORT).show()
            }
            is FirebaseAuthUserCollisionException -> {
                Toast.makeText(this, "This email is already registered.", Toast.LENGTH_SHORT).show()
            }
            else -> {
                Toast.makeText(this, "Error: ${exception?.message}", Toast.LENGTH_SHORT).show()
            }
        }
        */
