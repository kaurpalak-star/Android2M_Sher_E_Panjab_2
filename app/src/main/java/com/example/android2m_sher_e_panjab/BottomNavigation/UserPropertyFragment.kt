package com.example.android2m_sher_e_panjab.BottomNavigation

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.android2m_sher_e_panjab.Property
import com.example.android2m_sher_e_panjab.R
import com.example.android2m_sher_e_panjab.databinding.FragmentUserPropertyBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.database.FirebaseDatabase

class UserPropertyFragment : Fragment() {

    private var _binding: FragmentUserPropertyBinding? = null
    private val binding get() = _binding!!

    private var property: Property? = null
    private val database = FirebaseDatabase.getInstance().getReference("Properties")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentUserPropertyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Setup Toolbar
        (activity as? AppCompatActivity)?.setSupportActionBar(binding.toolbar)
        binding.toolbar.setNavigationOnClickListener { parentFragmentManager.popBackStack() }

        // 2. Get Data
        property = arguments?.getSerializable("property_data") as? Property

        // 3. Populate UI
        property?.let {
            binding.etName.setText(it.name)
            binding.etPrice.setText(it.price)
            binding.etArea.setText(it.area)
            binding.etLocation.setText(it.location)
            binding.etDescription.setText(it.description)
        }

        // 4. Button Listeners
        binding.btnUpdate.setOnClickListener { updateProperty() }
        binding.btnDelete.setOnClickListener { confirmDelete() }
    }

    private fun updateProperty() {
        val id = property?.id ?: return
        val updatedMap = mapOf(
            "name" to binding.etName.text.toString(),
            "price" to binding.etPrice.text.toString(),
            "area" to binding.etArea.text.toString(),
            "location" to binding.etLocation.text.toString(),
            "description" to binding.etDescription.text.toString()
        )

        database.child(id).updateChildren(updatedMap).addOnSuccessListener {
            Toast.makeText(context, "Property Updated", Toast.LENGTH_SHORT).show()
            parentFragmentManager.popBackStack()
        }
    }

    private fun confirmDelete() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Delete Property")
            .setMessage("Are you sure you want to delete this listing permanently?")
            .setPositiveButton("Delete") { _, _ ->
                property?.id?.let { id ->
                    database.child(id).removeValue().addOnSuccessListener {
                        Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_SHORT).show()
                        parentFragmentManager.popBackStack()
                    }
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}