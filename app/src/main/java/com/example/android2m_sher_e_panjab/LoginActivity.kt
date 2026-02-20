package com.example.android2m_sher_e_panjab

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.android2m_sher_e_panjab.BottomNavigation.BottomNavigationActivity
import com.example.android2m_sher_e_panjab.databinding.ActivityLoginBinding
import com.example.android2m_sher_e_panjab.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding


    var auth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.BTN1.setOnClickListener{
            val email = binding.ET1.text.toString()
            val password = binding.ET2.text.toString()

            if (email.isEmpty()) {
                Toast.makeText(this, "Please fill email", Toast.LENGTH_SHORT).show()
            } else if (password.isEmpty()) {
                Toast.makeText(this, "Please fill password", Toast.LENGTH_SHORT).show()
            } else {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Login successfully!", Toast.LENGTH_SHORT)
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
        }



