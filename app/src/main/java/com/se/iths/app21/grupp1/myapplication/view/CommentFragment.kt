package com.se.iths.app21.grupp1.myapplication.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.se.iths.app21.grupp1.myapplication.R
import com.se.iths.app21.grupp1.myapplication.adapter.CommentRecyclerAdapter
import com.se.iths.app21.grupp1.myapplication.model.Comments
import com.se.iths.app21.grupp1.myapplication.model.Place
import kotlinx.android.synthetic.main.activity_places.*
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

        val currentUser = auth.currentUser
        if (currentUser != null){
            getUserData()
        }

        println(docId)

        view.cancelButton.setOnClickListener {
            dismiss()
        }
        view.saveCommentButton.setOnClickListener {
            addComment()
            dismiss()
        }
        return view
    }


    fun addComment(){

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

                db.collection("Comments").add(comments).addOnSuccessListener {
                   // Toast.makeText(activity, "Successfully", Toast.LENGTH_LONG).show()

                }.addOnFailureListener {
                    //Toast.makeText(activity, it.localizedMessage, Toast.LENGTH_LONG).show()
                }
            }else{
                commentText.error = "Please enter your comment"
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
                Toast.makeText(this.context, error.localizedMessage, Toast.LENGTH_LONG).show()

            }else{
                if(value != null){
                    val documents = value.documents
                    for(document in documents){
                        userDocumentId = document.id
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