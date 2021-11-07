package com.se.iths.app21.grupp1.myapplication.view

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.se.iths.app21.grupp1.myapplication.R
import com.se.iths.app21.grupp1.myapplication.adapter.CommentRecyclerAdapter
import com.se.iths.app21.grupp1.myapplication.model.Comments
import com.se.iths.app21.grupp1.myapplication.model.Place
import com.se.iths.app21.grupp1.myapplication.databinding.ActivityPlacesBinding
import com.se.iths.app21.grupp1.myapplication.model.Places
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_places.*
import java.util.*


class PlacesActivity : AppCompatActivity(){

    lateinit var binding: ActivityPlacesBinding

    var commentslist : MutableList<Comments> = mutableListOf()
    var placeslist : MutableList<Place> = mutableListOf()
    lateinit var auth: FirebaseAuth
    lateinit var db : FirebaseFirestore
    lateinit var storage : FirebaseStorage
    lateinit var collectionRef: CollectionReference
    var lat : Double? = null
    var long : Double? = null
    var docId : String? = null
    private var placeId: String? = null
    private var userName : String? = null
    private var userDocumentId: String? = null
    private var commentList = ArrayList<Comments>()
    private lateinit var commentAdapter : CommentRecyclerAdapter

    var totalRatingBar : Float? = 0f

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlacesBinding.inflate(layoutInflater)
        setContentView(binding.root)


        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()

        supportActionBar?.setBackgroundDrawable(ColorDrawable(R.drawable.background_color))
        actionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val currentUser = auth.currentUser
        if (currentUser != null){
            commentList = ArrayList<Comments>()

            commentText.visibility= View.GONE
            saveCommentButton.visibility = View.GONE
            cancelButton.visibility = View.GONE
            commentRatingBar.visibility = View.GONE

            getUserData()


        }else{

            commentText.visibility= View.GONE
            saveCommentButton.visibility = View.GONE
            cancelButton.visibility = View.GONE
            addCommentButton.visibility = View.GONE
            Snackbar.make(binding.root, "Please first sign in to type a comment ", Snackbar.LENGTH_INDEFINITE).setAction("Go to inloggning sida",){
                val intent = Intent(this, InloggningActivity::class.java)
                startActivity(intent)
            }.show()
        }



         docId = intent.getStringExtra("docId")



        getPlacesInfo()

        commentRecyclerView.layoutManager = LinearLayoutManager(this)
        commentAdapter = CommentRecyclerAdapter(commentList)
        commentRecyclerView.adapter = commentAdapter
        getCommentsData()

      addCommentButton.setOnClickListener {
            commentRecyclerView.visibility = View.GONE
            commentText.visibility= View.VISIBLE
            addCommentButton.visibility = View.GONE
            saveCommentButton.visibility = View.VISIBLE
            cancelButton.visibility = View.VISIBLE
            commentRatingBar.visibility = View.VISIBLE
        }

        cancelButton.setOnClickListener {

            commentRecyclerView.visibility = View.VISIBLE
            commentText.visibility= View.GONE
            commentRatingBar.visibility = View.GONE
            addCommentButton.visibility = View.VISIBLE
            saveCommentButton.visibility = View.GONE
            cancelButton.visibility = View.GONE
        }

    }

   private fun getPlacesInfo() {

       docId = intent.getStringExtra("docId")

       val uuid = UUID.randomUUID()
       val imageName = "$uuid"
       val reference = storage.reference
       val imageReference = reference.child("images").child(imageName)


       if (docId != null) {
           db.collection("Places").document(docId!!)
               .get()
               .addOnSuccessListener {  task ->
                   if (task != null)
                        {
                            val place = task.toObject(Places::class.java)
                            supportActionBar?.title= place!!.name!!.toUpperCase() + " RESTAURANGEN "
                            landPlacesTextListView.text = place!!.land
                            beskrivningPlacesText.text = place.beskrivning

                             db.collection("Places").document(docId!!)
                                 .get()
                                 .addOnCompleteListener {
                                     if (task != null) {
                                         val result: StringBuffer = StringBuffer()
                                         if(it.isSuccessful) {
                                             var url = result.append(it.result!!.data!!.getValue("image")).toString()
                                             Picasso.get().load(url).into(selectImage)
                                                }
                                     }
                                 }
                        }
               }
       }
   }


fun addComment(view: View){

    val comments = hashMapOf<String, Any>()

    if (userDocumentId == null || docId == null || userDocumentId == null){
        Toast.makeText(this, "något gick fel!", Toast.LENGTH_LONG).show()
    }else{

        comments["comment"] = commentText.text.toString()
        comments["userDocumentId"] = userDocumentId.toString()
        comments["placeId"] = docId.toString()
        comments["userName"] = userName.toString()
        comments["email"] = auth.currentUser!!.email.toString()
        comments["date"] = Timestamp.now()
        comments["rating"] = commentRatingBar.rating.toString()

        db.collection("Comments").add(comments).addOnSuccessListener {
            Toast.makeText(this, "Successfully", Toast.LENGTH_LONG).show()

            commentRecyclerView.visibility = View.VISIBLE
            commentText.visibility= View.GONE
            addCommentButton.visibility = View.VISIBLE
            saveCommentButton.visibility = View.GONE
            cancelButton.visibility = View.GONE
            commentRatingBar.visibility = View.GONE
            commentRatingBar.rating = 0.0F
            commentText.setText("")

        }.addOnFailureListener {
            Toast.makeText(this, it.localizedMessage, Toast.LENGTH_LONG).show()
        }
    }


}


    @SuppressLint("NotifyDataSetChanged")
    private fun getCommentsData(){

        db.collection("Comments").orderBy("date", Query.Direction.DESCENDING).whereEqualTo("placeId", docId).addSnapshotListener { value, error ->

            if (error != null){
                Log.d("!!!", error.localizedMessage.toString())
                Toast.makeText(this, error.localizedMessage, Toast.LENGTH_LONG).show()
            }else{
                if (value != null){
                    commentList.clear()
                    val documents = value.documents
                    for(document in documents){

                        val comment = document.get("comment") as String
                        val nameH = document.get("userName") as String
                        val placeId = document.get("placeId") as String
                        val ratingBar = document.get("rating") as? String

                        val commentAdd = Comments(nameH, comment, placeId, ratingBar)
                        commentList.add(commentAdd)

                    }
                    commentAdapter!!.notifyDataSetChanged()
                }
            }


        }

    }


    fun getUserData(){
        val currentUser = auth.currentUser!!.uid
        var name  = ""
        var surname =""
        var email =""

        db.collection("Users").whereEqualTo("userid", currentUser).addSnapshotListener { value, error ->
            if(error != null){
                Toast.makeText(this, error.localizedMessage, Toast.LENGTH_LONG).show()

                println(error.localizedMessage)
            }else{
                if(value != null){
                    val documents = value.documents
                    for(document in documents){
                        userDocumentId = document.id.toString()
                        name = document.get("name") as String
                        surname = document.get("surname") as String
                        email = document.get("email") as String
                        userName = "$name $surname"

                    }

                }

            }
        }


    }

}