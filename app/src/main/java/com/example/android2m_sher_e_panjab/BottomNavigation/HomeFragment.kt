package com.example.android2m_sher_e_panjab.BottomNavigation

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android2m_sher_e_panjab.HomeAdapter
import com.example.android2m_sher_e_panjab.Property
import com.example.android2m_sher_e_panjab.R
import com.example.android2m_sher_e_panjab.com.example.android2m_sher_e_panjab.Item
import com.example.android2m_sher_e_panjab.com.example.android2m_sher_e_panjab.ItemAdapter
import com.example.android2m_sher_e_panjab.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeFragment : Fragment(), ItemAdapter.OnItemClick, HomeAdapter.OnItemClick {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val TAG = "HomeFragment"

    // Adapters
    private lateinit var categoryAdapter: ItemAdapter
    private lateinit var propertyAdapter: HomeAdapter

    // Lists
    private val categoryList = arrayListOf<Item>()
    private val masterPropertyList = arrayListOf<Property>() // Holds all data from Firebase
    private val filteredPropertyList = arrayListOf<Property>() // Holds data shown to user

    // Firebase
    private val databaseReference = FirebaseDatabase.getInstance().getReference("Properties")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerViews()
        addCategories()
        fetchPropertiesFromFirebase()
    }

    private fun setupRecyclerViews() {
        // 1. Setup Category RecyclerView (Horizontal)
        categoryAdapter = ItemAdapter(categoryList, this)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerView.adapter = categoryAdapter

        // 2. Setup Property RecyclerView (Vertical)
        propertyAdapter = HomeAdapter(filteredPropertyList, this)
        binding.recyclerView2.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView2.adapter = propertyAdapter
    }

    private fun fetchPropertiesFromFirebase() {
        // This listener will trigger every time data changes in the "Properties" node
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                masterPropertyList.clear()
                for (data in snapshot.children) {
                    val property = data.getValue(Property::class.java)
                    if (property != null) {
                        masterPropertyList.add(property)
                    }
                }
                // Once data is fetched, default view is "All"
                filterProperties("All")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(TAG, "Database Error: ${error.message}")
                Toast.makeText(context, "Failed to load data", Toast.LENGTH_SHORT).show()
            }
        })
    }

    /**
     * Category selection from ItemAdapter (Horizontal list)
     */
    override fun onClick(item: String) {
        Log.d(TAG, "Selected Category: $item")
        filterProperties(item)
    }

    private fun filterProperties(category: String) {
        filteredPropertyList.clear()

        if (category == "All") {
            filteredPropertyList.addAll(masterPropertyList)
        } else {
            // Filter logic:
            // 1. Matches exact category
            // 2. Handles "Rent" vs "On Rent" difference between Spinner and Category list
            val filtered = masterPropertyList.filter {
                it.propertyType.equals(category, ignoreCase = true) ||
                        (category == "Rent" && it.propertyType.equals("On Rent", ignoreCase = true))
            }
            filteredPropertyList.addAll(filtered)
        }

        propertyAdapter.notifyDataSetChanged()
    }

    private fun addCategories() {
        categoryList.apply {
            clear()
            add(Item("All"))
            add(Item("Agricultural"))
            add(Item("Residential"))
            add(Item("Commercial"))
            add(Item("Rent"))
        }
        categoryAdapter.notifyDataSetChanged()
    }

    /**
     * Handle adding to favourites in Firebase
     * Path: favourites -> currentUserId -> propertyId -> property data
     */
    override fun onFavouriteClick(property: Property, position: Int) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            Toast.makeText(context, "Please login to add favourites", Toast.LENGTH_SHORT).show()
            return
        }

        val propertyId = property.id ?: property.name.hashCode().toString()
        val favRef = FirebaseDatabase.getInstance().getReference("favourites")
            .child(currentUser.uid)
            .child(propertyId)

        favRef.setValue(property).addOnSuccessListener {
            Toast.makeText(context, "Added to Favourites!", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(context, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Handle item click to view details (optional)
     */
    override fun onItemClick(currentItem: Property) {
        // Implementation for opening a detail screen would go here
        Log.d(TAG, "Clicked on: ${currentItem.name}")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}