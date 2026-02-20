package com.example.android2m_sher_e_panjab

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.android2m_sher_e_panjab.BottomNavigation.BottomNavigationActivity
import com.example.android2m_sher_e_panjab.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth

class registerActivity : AppCompatActivity() {


    lateinit var binding: ActivityRegisterBinding


    var auth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btn1.setOnClickListener {

            val email = binding.Et1.text.toString()
            val password = binding.Et2.text.toString()


            if (email.isEmpty()) {
                Toast.makeText(this, "Please fill email", Toast.LENGTH_SHORT).show()
            } else if (password.isEmpty()) {
                Toast.makeText(this, "Please fill password", Toast.LENGTH_SHORT).show()
            } else {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Account created successfully!", Toast.LENGTH_SHORT)
                            .show()
                        val intent = Intent(this, BottomNavigationActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, e.localizedMessage, Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }
}