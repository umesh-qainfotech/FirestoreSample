package com.example.myfirestore

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    // Access a Cloud Firestore instance from your Activity
    val db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        save.setOnClickListener { onSave() }
        load.setOnClickListener { onLoad() }
        update.setOnClickListener { onUpdate() }
        deleteField.setOnClickListener { onDeleteField() }
        deleteDocument.setOnClickListener { onDelete() }


    }

    override fun onStart() {
        super.onStart()
        db.document("User data/data").addSnapshotListener { documentSnapshot, firebaseFirestoreException ->
            if (firebaseFirestoreException != null)
            {
                Toast.makeText(this,"Error while loading.",Toast.LENGTH_SHORT).show()
                Log.e("TAG", "$firebaseFirestoreException")
            }
             if (documentSnapshot != null) {
                val Title : String? = documentSnapshot.get("Title") as? String
                val Desc : String? = documentSnapshot.get("Description") as? String
                val rs = "$Title : $Desc"
                load_text.text = rs
            }
            else
            {
                load_text.text =""
            }
        }
    }

    private fun onSave()
    {
        val title : String = findViewById<TextView>(R.id.Title_text).text.toString()
        var desc : String = findViewById<TextView>(R.id.description).text.toString()
        val data = hashMapOf(
            "Title" to title,
             "Description" to desc
        )

        db.collection("User data").document("data").set(data)
            .addOnSuccessListener { documentReference ->
                Toast.makeText(this,"$title : $desc",Toast.LENGTH_LONG).show()
                Log.d("TAG", "DocumentSnapshot added with ID: ${documentReference}")
            }
            .addOnFailureListener { e ->
                Log.w("TAG", "Error adding document", e)
            }
    }

    private fun onLoad()
    {
        db.document("User data/data").get()
            .addOnSuccessListener { result : DocumentSnapshot ->
                val Title : String = result.get("Title") as String
                val Desc : String = result.get("Description") as String
                val rs = "$Title : $Desc"
                load_text.text = rs
            }
            .addOnFailureListener{ e ->
                Log.i("TAG",e.toString())
            }
    }

    private fun onUpdate(){
        val Desc : String = description.text.toString()
        db.document("User data/data").update("Description",Desc)
    }

    private fun onDeleteField()
    {
        db.document("User data/data").update("Description",FieldValue.delete()).addOnSuccessListener {
            Toast.makeText(this,"Description deleted",Toast.LENGTH_SHORT).show()
        }
    }

    private fun onDelete()
    {
        db.document("User data/data").delete()
    }
}
