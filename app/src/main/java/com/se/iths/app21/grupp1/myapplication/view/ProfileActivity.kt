package com.se.iths.app21.grupp1.myapplication.view

import android.Manifest
import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.SyncStateContract
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.se.iths.app21.grupp1.myapplication.Constants
import com.se.iths.app21.grupp1.myapplication.Constants.CHILD_PIMAGE
import com.se.iths.app21.grupp1.myapplication.R
import com.se.iths.app21.grupp1.myapplication.databinding.ActivityProfileBinding
import com.se.iths.app21.grupp1.myapplication.gone
import com.se.iths.app21.grupp1.myapplication.visible
import kotlinx.android.synthetic.main.activity_profile.*
import java.io.FileNotFoundException
import java.lang.Exception
import java.util.*

class ProfileActivity : AppCompatActivity() {

    private val auth : FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val db : FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    private val storage : StorageReference by lazy { FirebaseStorage.getInstance().reference }
    private val currentUser: FirebaseUser by lazy { auth.currentUser!! }
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var permissionLaunher: ActivityResultLauncher<String>
    private var selectedPicture: Uri? = null

    private lateinit var progressDialog: ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        supportActionBar?.setBackgroundDrawable(ColorDrawable(R.drawable.background_color))
        actionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setUserDatas()

         registerLauncher()

        change_image_fab.setOnClickListener {
            givePermission()
            profile_ProgressBar.gone()
        }

    }

    @SuppressLint("CheckResult")
    private fun setUserDatas(){
        val userId = currentUser.uid

        db.collection(Constants.CHILD_USERS).document(userId).addSnapshotListener { value, error ->
            val username = value!!.get(Constants.CHILD_USERNAME).toString()
            val profileImage = value!!.get(Constants.CHILD_PIMAGE).toString()
            val name = value.get(Constants.CHILD_NAME).toString()
            val surname = value.get(Constants.CHILD_SURNAME).toString()
            val age = value.get(Constants.CHILD_AGE).toString()
            val email = value.get(Constants.CHILD_EMAIL).toString()

            profile_page_cp.title = username
            profile_page_toolbar.title = username
            textAgeInlogning.text = age
            textSurnameInlogning.text = surname
            textNameInlogning.text = name
            textEmailInlogning.text = email


            if(profileImage == "no_image" || profileImage == null){
                Glide.with(this).load(R.drawable.ic_baseline_person).into(profile_page_pimage)
            }else{
                Glide.with(this.applicationContext).load(profileImage).into(profile_page_pimage)
            }
        }

    }


    private fun saveProfile(){

        val filePath = storage.child(Constants.PPFOLDER).child("${UUID.randomUUID()}${currentUser.uid}.jpg")

        if(selectedPicture != null && auth.currentUser != null){
            filePath.putFile(selectedPicture!!).addOnSuccessListener {

                filePath.downloadUrl.addOnSuccessListener {
                    val downloadUrl = it.toString()

                    db.collection(Constants.CHILD_USERS).document(currentUser.uid).update(Constants.CHILD_PIMAGE, downloadUrl).addOnSuccessListener {
                        Toast.makeText(this, "Successfully", Toast.LENGTH_LONG).show()
                        profile_ProgressBar.gone()
                    }
                }

            }.addOnFailureListener {
                Toast.makeText(this, "${it.localizedMessage}", Toast.LENGTH_LONG).show()
            }


        }

    }



    private fun givePermission() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
              /*
                Snackbar.make(
                    this,
                    "Permission needed for gallery",
                    Snackbar.LENGTH_INDEFINITE
                ).setAction("Give Permission") {
                    permissionLaunher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
                }.show()

               */

            } else {
                permissionLaunher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        } else {

            val intentToGallery =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            activityResultLauncher.launch(intentToGallery)
        }
    }


    private fun registerLauncher() {

        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {

                    profile_ProgressBar.visible()
                    val intentFromResult = result.data
                    if (intentFromResult != null) {
                        selectedPicture = intentFromResult.data
                        selectedPicture?.let {
                            saveProfile()
                            profile_page_pimage.setImageURI(it)
                        }
                    }
                }

            }

        permissionLaunher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
                if (result) {
                    val intentToGallery =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    activityResultLauncher.launch(intentToGallery)
                } else {
                    Toast.makeText(this, "Permission needed", Toast.LENGTH_LONG).show()
                }

            }
    }

}
