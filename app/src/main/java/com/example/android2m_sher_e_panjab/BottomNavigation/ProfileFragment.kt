package com.example.android2m_sher_e_panjab.BottomNavigation

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.android2m_sher_e_panjab.LoginActivity
import com.example.android2m_sher_e_panjab.Property
import com.example.android2m_sher_e_panjab.R
import com.example.android2m_sher_e_panjab.Users
import com.example.android2m_sher_e_panjab.databinding.FragmentProfileBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ProfileFragment : Fragment(), FavGridAdapter.OnItemClick {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val auth = FirebaseAuth.getInstance()
    private val userDatabase = FirebaseDatabase.getInstance().getReference("users")
    private val propertiesDatabase = FirebaseDatabase.getInstance().getReference("Properties")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        fetchUserData()
        fetchUserProperties()
        setupListeners()
    }

    private fun setupRecyclerView() {
        binding.userPostsRecyclerView.layoutManager = GridLayoutManager(context, 2)
        binding.userPostsRecyclerView.setHasFixedSize(true)
        binding.userPostsRecyclerView.isNestedScrollingEnabled = false
    }

    private fun setupListeners() {
        binding.logOut.setOnClickListener {
            auth.signOut()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            activity?.finish()
        }

        binding.profileEditBTN.setOnClickListener {
            showEnhancedEditDialog()
        }
    }

    private fun showEnhancedEditDialog() {
        val uid = auth.currentUser?.uid ?: return

        // Root layout for the dialog with nice padding
        val layout = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(50, 40, 50, 10)
        }

        // --- Name Input Field ---
        val nameLayout = TextInputLayout(requireContext(), null, com.google.android.material.R.style.Widget_MaterialComponents_TextInputLayout_OutlinedBox).apply {
            hint = "Full Name"
//            boxCornerRadiusTopStart = 15f
//            boxCornerRadiusTopEnd = 15f
//            boxCornerRadiusBottomStart = 15f
//            boxCornerRadiusBottomEnd = 15f
        }
        val nameET = TextInputEditText(nameLayout.context).apply {
            setText(binding.profileName.text.toString())
        }
        nameLayout.addView(nameET)

        // --- Phone Input Field ---
        val phoneLayout = TextInputLayout(requireContext(), null, com.google.android.material.R.style.Widget_MaterialComponents_TextInputLayout_OutlinedBox).apply {
            hint = "Phone Number"
//            boxCornerRadiusTopStart = 15f
//            boxCornerRadiusTopEnd = 15f
//            boxCornerRadiusBottomStart = 15f
//            boxCornerRadiusBottomEnd = 15f
            val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            params.setMargins(0, 30, 0, 0)
            layoutParams = params
        }
        val phoneET = TextInputEditText(phoneLayout.context).apply {
            setText(binding.profileBio.text.toString())
            inputType = android.text.InputType.TYPE_CLASS_PHONE
        }
        phoneLayout.addView(phoneET)

        layout.addView(nameLayout)
        layout.addView(phoneLayout)

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Edit Profile")
            .setMessage("Update your display name and contact details.")
            .setView(layout)
            .setPositiveButton("Update") { dialog, _ ->
                val newName = nameET.text.toString().trim()
                val newPhone = phoneET.text.toString().trim()

                if (newName.isNotEmpty() && newPhone.isNotEmpty()) {
                    updateFirebaseProfile(uid, newName, newPhone)
                } else {
                    Toast.makeText(context, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun updateFirebaseProfile(uid: String, name: String, phone: String) {
        val updates = mapOf(
            "name" to name,
            "phnNum" to phone
        )

        userDatabase.child(uid).updateChildren(updates)
            .addOnSuccessListener {
                Toast.makeText(context, "Profile updated!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun fetchUserData() {
        val uid = auth.currentUser?.uid ?: return
        userDatabase.child(uid).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists() && _binding != null) {
                    val user = snapshot.getValue(Users::class.java)
                    user?.let {
                        binding.profileName.text = it.name
                        binding.profileEmail.text = it.email
                        binding.profileBio.text = it.phnNum
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun fetchUserProperties() {
        val currentUid = auth.currentUser?.uid ?: return
        val query = propertiesDatabase.orderByChild("userid").equalTo(currentUid)

        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val propertyList = mutableListOf<Property>()
                for (data in snapshot.children) {
                    val property = data.getValue(Property::class.java)
                    property?.let { propertyList.add(it) }
                }

                if (_binding != null) {
                    val adapter = FavGridAdapter(propertyList, this@ProfileFragment)
                    binding.userPostsRecyclerView.adapter = adapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                context?.let { Toast.makeText(it, error.message, Toast.LENGTH_SHORT).show() }
            }
        })
    }

    override fun onPropertyClick(property: Property) {
        // Create the new Fragment
//        val userListFragment = UserPropertyFragment()

        // Pass the property data via Bundle
        val bundle = Bundle()
        bundle.putSerializable("property_data", property) // Ensure Property implements Serializable
//        userListFragment.arguments = bundle

        findNavController().navigate(R.id.userPropertyFragment,bundle)

        // Fragment Transaction
//        parentFragmentManager.beginTransaction()
//            .replace(R.id.fragment_container, userListFragment) // Replace with your actual container ID
//            .addToBackStack(null)
//            .commit()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}