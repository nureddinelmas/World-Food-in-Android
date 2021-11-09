package com.se.iths.app21.grupp1.myapplication.view

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.firebase.ui.auth.AuthUI.getApplicationContext
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.se.iths.app21.grupp1.myapplication.Constants
import com.se.iths.app21.grupp1.myapplication.R
import com.se.iths.app21.grupp1.myapplication.adapter.CommentRecyclerAdapter
import com.se.iths.app21.grupp1.myapplication.model.Comments
import com.se.iths.app21.grupp1.myapplication.model.Place
import kotlinx.android.synthetic.main.activity_places.*
import kotlinx.android.synthetic.main.fragment_comment.*
import kotlinx.android.synthetic.main.fragment_comment.view.*
import java.util.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CommentFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CommentFragment : DialogFragment() {
    var placeslist : MutableList<Place> = mutableListOf()
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val db : FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    var lat : Double? = null
    var long : Double? = null
    var docId : String? = null
    private var userName : String? = null
    private var userDocumentId: String? = null
    private var profileImage: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view = inflater.inflate(R.layout.fragment_comment, container, false)

        if (arguments != null){
            docId = requireArguments().getString("docId")
        }

        view.cancelButton.setOnClickListener {
            dismiss()
        }
        view.saveCommentButton.setOnClickListener {
            getUserData()
            dismiss()
        }
        return view
    }


    @SuppressLint("RestrictedApi")
    fun addComment(){

        println("profile image 1->${profileImage}")
        val comments = hashMapOf<String, Any>()

        if (userDocumentId == null || docId == null){
            Toast.makeText(this.context, "något gick fel!", Toast.LENGTH_LONG).show()
        }else{

            if(commentText.text.isNotEmpty()){

                comments["comment"] = commentText.text.toString()
                comments["userDocumentId"] = userDocumentId.toString()
                comments["placeId"] = docId.toString()
                comments["userName"] = userName.toString()
                comments["email"] = auth.currentUser!!.email.toString()
                comments["date"] = Timestamp.now()
                comments["rating"] = commentRatingBar.rating.toString()
                println("profile image->${profileImage}")
                comments[Constants.CHILD_PIMAGE] = profileImage.toString()

                db.collection("Comments").add(comments).addOnSuccessListener { Toast.makeText(getApplicationContext(), "Successfully", Toast.LENGTH_LONG).show()

                }.addOnFailureListener { Toast.makeText(getApplicationContext(), it.localizedMessage, Toast.LENGTH_LONG).show() }
            }else{
                commentText.error = "Please enter your comment"
            }

        }

}


    @SuppressLint("RestrictedApi")
    fun getUserData(){

        if(auth.currentUser != null){
            val currentUser = auth.currentUser!!.uid
            var name  = ""
            var surname =""
            var email =""

            println("currentUser ->$currentUser")
            db.collection("Users").document(currentUser).addSnapshotListener { value, error ->
                if(error != null){
                    Toast.makeText(getApplicationContext(), error.localizedMessage, Toast.LENGTH_LONG).show()

                }else{
                    if(value != null){
                        userDocumentId = value.id.toString()
                        name = value.get("name").toString()
                        surname = value.get("surname").toString()
                        email = value.get("email").toString()
                        profileImage = value.get(Constants.CHILD_PIMAGE).toString()
                        println("profileImage ->$profileImage")
                        userName = value.get("userName").toString()

                        addComment()

                    }

                }
            }
        }

    }
}