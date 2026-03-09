package com.example.android2m_sher_e_panjab.BottomNavigation

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android2m_sher_e_panjab.HomeAdapter
import com.example.android2m_sher_e_panjab.Property
import com.example.android2m_sher_e_panjab.R
import com.example.android2m_sher_e_panjab.com.example.android2m_sher_e_panjab.Item
import com.example.android2m_sher_e_panjab.com.example.android2m_sher_e_panjab.ItemAdapter
import com.example.android2m_sher_e_panjab.databinding.FragmentHomeBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment(), ItemAdapter.OnItemClick, HomeAdapter.OnItemClick {

    private lateinit var binding: FragmentHomeBinding

    var TAG = "Home Fragment"

    // Adapters
    private lateinit var categoryAdapter: ItemAdapter
    private lateinit var propertyAdapter: HomeAdapter

    // Lists
    private val categoryList = arrayListOf<Item>()
    private val masterPropertyList = arrayListOf<Property>() // All properties
    private val filteredPropertyList = arrayListOf<Property>() // Currently visible properties

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerViews()
        addCategories()
        addPropertyData() // Load your initial data
    }

    private fun setupRecyclerViews() {
        // 1. Setup Category RecyclerView (Horizontal)
        categoryAdapter = ItemAdapter(categoryList, this)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerView.adapter = categoryAdapter

        // 2. Setup Property RecyclerView (Vertical)
        // Ensure you have a second RecyclerView in your XML, e.g., binding.recyclerViewProperties
        propertyAdapter = HomeAdapter(filteredPropertyList, this)
        binding.recyclerView2.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView2.adapter = propertyAdapter
    }

    // This triggers when you click "Agricultural", "Residential", etc.
    override fun onItemClick(currentItem: Property) {
        filterProperties(currentItem.propertyType.toString())
    }

    override fun onFavouriteClick(
        property: Property,
        position: Int
    ) {
    }


    private fun filterProperties(category: String) {
        filteredPropertyList.clear()

        if (category == "All") {
            filteredPropertyList.addAll(masterPropertyList)
        } else {
            // Filter based on propertyType
            val filtered = masterPropertyList.filter {
                it.propertyType.equals(category, ignoreCase = true)
            }
            filteredPropertyList.addAll(filtered)
        }

        Log.d(TAG,filteredPropertyList.toString())

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

    private fun addPropertyData() {
        // Mock Data - In a real app, this comes from Firebase or an API
        masterPropertyList.add(Property("Farm Land", "Jalandhar", "5000 sqft", "Agricultural", "₹50L", arrayListOf("url1").toString()))
        masterPropertyList.add(Property("Modern Villa", "Model Town", "2500 sqft", "Residential", "₹1.2Cr", arrayListOf("url2").toString()))
        masterPropertyList.add(Property("Shop Space", "Civil Lines", "800 sqft", "Commercial", "₹30L", arrayListOf("url3").toString()))

        // Initially show all
        filterProperties("All")
    }

    override fun onClick(item: String) {

        Log.d(TAG,"on item clicked")
        filterProperties(item)

    }
}