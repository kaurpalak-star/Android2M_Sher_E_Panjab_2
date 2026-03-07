package com.example.android2m_sher_e_panjab.BottomNavigation

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.android2m_sher_e_panjab.Property
import com.example.android2m_sher_e_panjab.R
import com.example.android2m_sher_e_panjab.com.example.android2m_sher_e_panjab.AppwriteManager
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.launch

// Data model for Firebase


class AddFragment : Fragment(R.layout.fragment_add) {

    private lateinit var appwriteManager: AppwriteManager
    private val database = FirebaseDatabase.getInstance().getReference("Properties")

    // To store selected image URIs
    private var selectedImageUris = mutableListOf<Uri>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        appwriteManager = AppwriteManager.getInstance(requireContext())

        // 1. Initialize UI Elements
        val nameET = view.findViewById<TextInputEditText>(R.id.EdTx1)
        val soilET = view.findViewById<TextInputEditText>(R.id.SoilType)
        val areaET = view.findViewById<TextInputEditText>(R.id.Area)
        val contactET = view.findViewById<TextInputEditText>(R.id.Contact)
        val priceET = view.findViewById<TextInputEditText>(R.id.Price)
        val descET = view.findViewById<TextInputEditText>(R.id.Description)
        val btnPost = view.findViewById<Button>(R.id.btnPost) // Ensure you added this ID in XML
        val imageContainer = view.findViewById<TextInputEditText>(R.id.Images)

        // 2. Setup Spinners
        val locationSpinner = view.findViewById<Spinner>(R.id.locationSpinner)
        val locations = arrayListOf("Amritsar", "Barnala", "Bathinda", "Faridkot", "Fatehgarh Sahib", "Fazilka", "Ferozpur", "Gurdaspur", "Hoshiarpur", "Jalandhar", "Kapurthala", "Ludhiana", "Malerkotla", "Mansa", "Moga", "Pathankot", "Patiala", "Rupnagar", "Mohali", "Sangrur", "Nawanshahr", "Sri Muktsar Sahib", "Tarn Taran")
        setupSpinner(locationSpinner, locations)

        val propertySpinner = view.findViewById<Spinner>(R.id.propertySpinner)
        val propertyTypes = arrayListOf("Agricultural", "Commercial", "Residential", "On Rent")
        setupSpinner(propertySpinner, propertyTypes)

        val areaSpinner = view.findViewById<Spinner>(R.id.AreaSpinner)
        val areaUnits = arrayListOf("Marla", "Killa", "SqFt", "BHK")
        setupSpinner(areaSpinner, areaUnits)

        // 3. Image Picker Logic
        val pickImages = registerForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris ->
            if (uris.isNotEmpty()) {
                selectedImageUris.clear()
                selectedImageUris.addAll(uris)
                imageContainer.setText("${uris.size} Images Selected")
                Toast.makeText(context, "Selected ${uris.size} images", Toast.LENGTH_SHORT).show()
            }
        }

        imageContainer.setOnClickListener {
            pickImages.launch("image/*")
        }

        // 4. Post Button Click
        btnPost.setOnClickListener {
            val name = nameET.text.toString().trim()
            val contact = contactET.text.toString().trim()
            val price = priceET.text.toString().trim()

            // Basic Validation
            if (name.isEmpty() || contact.isEmpty() || price.isEmpty()) {
                Toast.makeText(context, "Please fill required fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (selectedImageUris.size < 2) {
                Toast.makeText(context, "Please select at least 2 images", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Start the upload process
            uploadAndSaveData(
                name = name,
                soil = soilET.text.toString(),
                area = areaET.text.toString(),
                contact = contact,
                price = price,
                desc = descET.text.toString(),
                loc = locationSpinner.selectedItem.toString(),
                pType = propertySpinner.selectedItem.toString(),
                aUnit = areaSpinner.selectedItem.toString()
            )
        }
    }

    private fun setupSpinner(spinner: Spinner, items: List<String>) {
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    private fun uploadAndSaveData(
        name: String, soil: String, area: String, contact: String,
        price: String, desc: String, loc: String, pType: String, aUnit: String
    ) {
        // Show a loading indicator if you have one
        Toast.makeText(context, "Uploading property details...", Toast.LENGTH_SHORT).show()

        lifecycleScope.launch {
            try {
                val uploadedUrls = mutableListOf<String>()

                // 1. Upload to Appwrite Storage
                selectedImageUris.forEach { uri ->
                    val url = appwriteManager.uploadImageFromUri(uri)
                    if (url != null) {
                        uploadedUrls.add(url)
                    }
                }

                // 2. Create Firebase Object
                val propertyId = database.push().key ?: return@launch
                val property = Property(
                    id = propertyId,
                    name = name,
                    location = loc,
                    propertyType = pType,
                    soilType = soil,
                    area = area,
                    areaUnit = aUnit,
                    contact = contact,
                    price = price,
                    description = desc,
                    imageUrls = uploadedUrls,
                    userid = FirebaseAuth.getInstance().currentUser?.uid
                )

                // 3. Save to Realtime Database
                database.child(propertyId).setValue(property)
                    .addOnSuccessListener {
                        Toast.makeText(context, "Property Posted Successfully!", Toast.LENGTH_LONG).show()
                        // Optional: Clear fields or navigate away
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(context, "Firebase Error: ${e.message}", Toast.LENGTH_SHORT).show()
                        Log.e("Error",e.toString())
                    }

            } catch (e: Exception) {
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}