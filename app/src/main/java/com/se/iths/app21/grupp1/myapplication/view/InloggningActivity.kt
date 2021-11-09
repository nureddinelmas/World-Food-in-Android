package com.se.iths.app21.grupp1.myapplication.view

import android.annotation.SuppressLint
import android.app.ActionBar
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.se.iths.app21.grupp1.myapplication.R
import com.se.iths.app21.grupp1.myapplication.databinding.ActivityInloggningBinding
import kotlinx.android.synthetic.main.activity_add_place.*
import kotlinx.android.synthetic.main.activity_inloggning.*
import kotlinx.android.synthetic.main.activity_inloggning.ageText
import kotlinx.android.synthetic.main.activity_inloggning.emailText
import kotlinx.android.synthetic.main.activity_inloggning.nameText
import kotlinx.android.synthetic.main.activity_inloggning.passText
import kotlinx.android.synthetic.main.activity_inloggning.searchText


class InloggningActivity : AppCompatActivity() {
    private lateinit var binding : ActivityInloggningBinding

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val db: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInloggningBinding.inflate(layoutInflater)
        setContentView(binding.root)


        supportActionBar?.setBackgroundDrawable(ColorDrawable(R.drawable.background_color))
        actionBar?.setDisplayHomeAsUpEnabled(true)

        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar?.setCustomView(R.layout.abs_layout)

        frameLayout2.visibility = View.GONE

        val currentUser = auth.currentUser

        if(currentUser != null){
            val intent = Intent(this@InloggningActivity, MapsActivity::class.java)
            startActivity(intent)
        }


    }

    fun signUpClicked(view: View){
        frameLayout1.visibility = View.GONE
        frameLayout2.visibility = View.VISIBLE

    }

    fun backButton(view: View){
        frameLayout1.visibility = View.VISIBLE
        frameLayout2.visibility = View.GONE

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

    fun saveDetails(view: View){

        val userName = userNameText.text.toString()
        val email = emailText.text.toString()
        val password = passText.text.toString()
        val name = nameText.text.toString()
        val surname = searchText.text.toString()
        val age = ageText.text.toString()

        val users = hashMapOf<String, Any>()


        if (email.isNotEmpty() && password.isNotEmpty()){
            auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
                users["userName"] = userName
                users["name"] = name
                users["surname"] = surname
                users["email"] = email
                users["age"] = age
                users["profileImage"] = "no_image"

                db.collection("Users").document(auth.currentUser!!.uid).set(users).addOnSuccessListener {
                    Toast.makeText(this, "Successfully", Toast.LENGTH_LONG).show()
                    val intent = Intent(this@InloggningActivity, MapsActivity::class.java)
                    startActivity(intent)
                }

            }.addOnFailureListener {
                Toast.makeText(this@InloggningActivity, it.localizedMessage, Toast.LENGTH_LONG).show()
            }
        }else{
            Toast.makeText(this@InloggningActivity, "Enter email and password", Toast.LENGTH_LONG).show()
        }
    }
}