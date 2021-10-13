package com.se.iths.app21.grupp1.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.se.iths.app21.grupp1.myapplication.databinding.ActivityInloggningBinding

class InloggningActivity : AppCompatActivity() {
    private lateinit var binding : ActivityInloggningBinding

    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInloggningBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()

        val currentUser = auth.currentUser

        if(currentUser != null){
            val intent = Intent(this@InloggningActivity, MapsActivity::class.java)
            startActivity(intent)
        }
    }

    fun signUpClicked(view: View){

        val email = binding.emailNameText.text.toString()
        val password = binding.passwordText.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty()){
            auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
                val intent = Intent(this@InloggningActivity, MapsActivity::class.java)
                startActivity(intent)
            }.addOnFailureListener {
                Toast.makeText(this@InloggningActivity, it.localizedMessage, Toast.LENGTH_LONG).show()
            }
        }else{
            Toast.makeText(this@InloggningActivity, "Enter email and password", Toast.LENGTH_LONG).show()
        }
    }

    fun signInClicked(view: View){

        val email = binding.emailNameText.text.toString()
        val password = binding.passwordText.text.toString()

        if(email.isNotEmpty() && password.isNotEmpty()){
            auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
                val intent = Intent(this@InloggningActivity, MapsActivity::class.java)
                startActivity(intent)
            }.addOnFailureListener {
                Toast.makeText(this@InloggningActivity, it.localizedMessage, Toast.LENGTH_LONG).show()
            }

        }else{
            Toast.makeText(this@InloggningActivity, "Enter email and password", Toast.LENGTH_LONG).show()
        }
    }
}