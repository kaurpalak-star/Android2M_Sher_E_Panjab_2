package com.example.android2m_sher_e_panjab.BottomNavigation

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
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
import java.util.Locale
class HomeFragment : Fragment(), ItemAdapter.OnItemClick, HomeAdapter.OnItemClick {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var categoryAdapter: ItemAdapter
    private lateinit var propertyAdapter: HomeAdapter

    private val categoryList = arrayListOf<Item>()
    private val masterPropertyList = arrayListOf<Property>()
    private val filteredPropertyList = arrayListOf<Property>()
    private val userFavIds = arrayListOf<String>() // Track favorite IDs

    private var currentCategory = "All"
    private var currentSearchQuery = ""

    private val databaseReference = FirebaseDatabase.getInstance().getReference("Properties")
    private val favReference = FirebaseDatabase.getInstance().getReference("favourites")
    private val auth = FirebaseAuth.getInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerViews()
        addCategories()
        setupSearchListener()
        fetchFavIdsAndProperties() // Unified fetch call
    }

    private fun setupRecyclerViews() {
        categoryAdapter = ItemAdapter(categoryList, this)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerView.adapter = categoryAdapter

        // Initialized with empty fav list
        propertyAdapter = HomeAdapter(filteredPropertyList, userFavIds, this)
        binding.recyclerView2.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView2.adapter = propertyAdapter
    }

    private fun fetchFavIdsAndProperties() {
        val uid = auth.currentUser?.uid ?: return

        // 1. Listen to Favourites in Real-time
        favReference.child(uid).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userFavIds.clear()
                for (data in snapshot.children) {
                    userFavIds.add(data.key ?: "") // The key is the property ID
                }
                // Refresh properties once favs are loaded
                fetchPropertiesFromFirebase()
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun fetchPropertiesFromFirebase() {
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                masterPropertyList.clear()
                for (data in snapshot.children) {
                    data.getValue(Property::class.java)?.let { masterPropertyList.add(it) }
                }
                applyFilters()
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun applyFilters() {
        val filtered = masterPropertyList.filter { property ->
            val categoryMatches = if (currentCategory == "All") true
            else property.propertyType.equals(currentCategory, ignoreCase = true) ||
                    (currentCategory == "Rent" && property.propertyType.equals("On Rent", ignoreCase = true))

            val searchMatches = if (currentSearchQuery.isEmpty()) true
            else property.name.lowercase().contains(currentSearchQuery) ||
                    property.propertyType.lowercase().contains(currentSearchQuery)

            categoryMatches && searchMatches
        }

        filteredPropertyList.clear()
        filteredPropertyList.addAll(filtered)
        // Pass both filtered list and current fav IDs to adapter
        propertyAdapter.updateData(filteredPropertyList, userFavIds)
    }

    override fun onFavouriteClick(property: Property, position: Int) {
        val uid = auth.currentUser?.uid ?: return
        val propertyId = property.id ?: return
        val itemRef = favReference.child(uid).child(propertyId)

        if (userFavIds.contains(propertyId)) {
            // Already in favs -> Remove it
            itemRef.removeValue().addOnSuccessListener {
                Toast.makeText(context, "Removed from Favourites", Toast.LENGTH_SHORT).show()
            }
        } else {
            // Not in favs -> Add it
            itemRef.setValue(property).addOnSuccessListener {
                Toast.makeText(context, "Added to Favourites!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // ... Keep existing setupSearchListener, addCategories, onClick, onItemClick, onDestroyView ...

    private fun setupSearchListener() {
        binding.EdTx1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                currentSearchQuery = s.toString().trim().lowercase(Locale.getDefault())
                applyFilters()
            }
            override fun afterTextChanged(s: Editable?) {}
        })
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

    override fun onClick(item: String) {
        currentCategory = item
        applyFilters()
    }

    override fun onItemClick(currentItem: Property) {
        Log.d("HomeFragment", "Navigating to: ${currentItem.name}")

        // 1. Prepare the data bundle
        val bundle = Bundle().apply {
            // Ensure Property class implements java.io.Serializable
            putSerializable("property_data", currentItem)
        }

        // 2. Navigate using NavController
        // Ensure R.id.viewFragment matches the ID in your navigation_graph.xml
        try {
            findNavController().navigate(R.id.viewFragment, bundle)
        } catch (e: Exception) {
            Log.e("HomeFragment", "Navigation failed: ${e.message}")
            Toast.makeText(context, "Navigation error", Toast.LENGTH_SHORT).show()
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}