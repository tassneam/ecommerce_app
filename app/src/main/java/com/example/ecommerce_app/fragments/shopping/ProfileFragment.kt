package com.example.ecommerce_app.fragments.shopping

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.RoundedCorner
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.ecommerce_app.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth

import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import jp.wasabeef.glide.transformations.RoundedCornersTransformation


class ProfileFragment : Fragment() {
    lateinit var binding: FragmentProfileBinding
    private lateinit var imageUri: Uri
    private val CAMERA_PERMISSION_REQUEST_CODE = 100
    private lateinit var auth: FirebaseAuth
    private val storage = FirebaseStorage.getInstance() // Initialize FirebaseStorage
    private val database = FirebaseDatabase.getInstance() // Initialize Firebase Realtime Database

    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            binding.profileImage.setImageURI(imageUri)
            uploadImageToFirebase(imageUri) // Call upload function
        } else {
            Toast.makeText(requireContext(), "Camera operation was cancelled", Toast.LENGTH_SHORT).show()
        }
    }
    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            val data = result.data
            if (data != null) {
                imageUri = data.data ?: return@registerForActivityResult
                binding.profileImage.setImageURI(imageUri)
                uploadImageToFirebase(imageUri) // Call upload function
            }
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (auth.currentUser != null) {
            // User is authenticated
            //ensure the user's profile image is loaded
            loadProfileImage()
            // Edit Profile Image action
            binding.editprofileimage.setOnClickListener {
                if (isCameraAvailable()) {
                    showImageSourceDialog()
                } else {
                    Toast.makeText(requireContext(), "Camera is not available on this device", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(context, "User not authenticated. Please sign in.", Toast.LENGTH_SHORT).show()
        }
    }
    private fun showImageSourceDialog() {
        val options = arrayOf("Camera", "Gallery")
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Choose Image Source")
        builder.setItems(options) { _, which ->
            when (which) {
                0 -> checkPermissionsAndOpenCamera() // Open camera
                1 -> openGallery() // Open gallery
            }
        }
        builder.show()
    }
    private fun checkPermissionsAndOpenCamera() {
        val cameraPermission = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CAMERA), 100)
        } else {
            openCamera() // Open camera if permission granted
        }
    }
    private fun openCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(requireActivity().packageManager) != null) {
            // Create a file to save the image
            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.TITLE, "New Picture")
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            }
            imageUri = requireActivity().contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)!!
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
            cameraLauncher.launch(takePictureIntent) // Launch camera
        } else {
            Toast.makeText(requireContext(), "No camera application found", Toast.LENGTH_SHORT).show()
        }
    }
    private fun openGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryLauncher.launch(galleryIntent) // Launch gallery
    }
    private fun setProfileImage(imageUri: Uri) {
        Glide.with(requireContext())
            .load(imageUri)
            .apply(RequestOptions.bitmapTransform(RoundedCornersTransformation(30, 0)))
            .into(binding.profileImage)
    }
    private fun uploadImageToFirebase(uri: Uri) {
        val userId = auth.currentUser?.uid ?: return
        val storageRef = storage.reference.child("profileImages/$userId.jpg")

        storageRef.putFile(uri)
            .addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                    saveProfileImageUrlToDatabase(downloadUri.toString())
                    Toast.makeText(requireContext(), "Image Uploaded Successfully", Toast.LENGTH_SHORT).show()
                    setProfileImage(uri) // Load the image into ImageView
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Firebase", "Failed to upload image: ${exception.message}")
                Toast.makeText(requireContext(), "${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }
    private fun saveProfileImageUrlToDatabase(imageUrl: String) {
        val userId = auth.currentUser?.uid ?: return
        val databaseRef = database.reference.child("users").child(userId)
        databaseRef.child("profileImageUrl").setValue(imageUrl)
            .addOnSuccessListener {
                Log.d("Firebase", "Profile image URL saved to database")
            }
            .addOnFailureListener {
                Log.e("Firebase", "Failed to save profile image URL: ${it.message}")
            }
    }
    //Load Image from Database
    private fun loadProfileImage() {
        val userId = auth.currentUser?.uid ?: return
        val databaseRef = database.reference.child("users").child(userId)
        databaseRef.child("profileImageUrl").get().addOnSuccessListener { snapshot ->
            val imageUrl = snapshot.value as? String
            if (imageUrl != null) {
                Glide.with(this)
                    .load(imageUrl)
                    .apply(RequestOptions.bitmapTransform(RoundedCornersTransformation(30, 0)))
                    .into(binding.profileImage)
            }
        }.addOnFailureListener {
            Log.e("Firebase", "Failed to load profile image")
        }
    }
    private fun isCameraAvailable(): Boolean {
        return requireContext().packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera() // Open camera if permission granted
            } else {
                Toast.makeText(requireContext(), "Camera permission is required", Toast.LENGTH_SHORT).show()
            }
        }
    }
}