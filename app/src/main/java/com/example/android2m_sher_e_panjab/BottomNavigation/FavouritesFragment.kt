package com.example.android2m_sher_e_panjab.BottomNavigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.android2m_sher_e_panjab.Property
import com.example.android2m_sher_e_panjab.R
import com.example.android2m_sher_e_panjab.databinding.FragmentFavouritesBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class FavouritesFragment : Fragment(), FavGridAdapter.OnItemClick {

    private var _binding: FragmentFavouritesBinding? = null
    private val binding get() = _binding!!

    private lateinit var gridAdapter: FavGridAdapter
    private val favList = arrayListOf<Property>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentFavouritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        gridAdapter = FavGridAdapter(favList, this)
        binding.favRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.favRecyclerView.adapter = gridAdapter

        fetchFavourites()
    }

    private fun fetchFavourites() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val ref = FirebaseDatabase.getInstance().getReference("favourites").child(userId)

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                favList.clear()
                for (data in snapshot.children) {
                    val p = data.getValue(Property::class.java)
                    p?.let { favList.add(it) }


                    print("this is the favourite list : ${favList.toString()}")
                }
                gridAdapter.notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    override fun onPropertyClick(property: Property) {
        // Create the options for the Alert Dialog
        val options = arrayOf("View Details", "Remove from Favourites")

        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Property Options")
        builder.setItems(options) { dialog, which ->
            when (which) {
                0 -> navigateToViewFragment(property) // "View Details" clicked
                1 -> removeFromFavourites(property)   // "Remove" clicked
            }
        }
        builder.show()
    }

    private fun navigateToViewFragment(property: Property) {
        // Pass the property data to the ViewFragment
        // Usually done using a Bundle or SafeArgs
        val bundle = Bundle().apply {
            putSerializable("property_data", property) // Ensure Property class implements Serializable
        }

        findNavController().navigate(R.id.viewFragment,bundle)
        Toast.makeText(context, "Navigating to ${property.name}", Toast.LENGTH_SHORT).show()
    }

    private fun removeFromFavourites(property: Property) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val propertyId = property.id ?: return

        val ref = FirebaseDatabase.getInstance().getReference("favourites")
            .child(userId)
            .child(propertyId)

        ref.removeValue().addOnSuccessListener {
            Toast.makeText(context, "Removed from Favourites", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(context, "Failed to remove: ${it.message}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}