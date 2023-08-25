package com.example.adminapp.fragment

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.adminapp.R
import com.example.adminapp.databinding.FragmentCategoryBinding

import com.example.adminapp.databinding.FragmentSliderBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class sliderFragment : Fragment() {
    private lateinit var binding:FragmentSliderBinding

    private var imageUrl:Uri? = null
    private lateinit var dialog:Dialog


    private var launchGalleryActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            imageUrl = it.data!!.data
            binding.imageView.setImageURI(imageUrl)
        }
    }

    override fun onCreateView(
        inflater:LayoutInflater, container:ViewGroup?,
        savedInstanceState:Bundle?,
    ):View? {
        // Inflate the layout for this fragment
        binding = FragmentSliderBinding.inflate(layoutInflater)

        dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.prgress_layout)
        dialog.setCancelable(false)

        binding.apply {
            imageView.setOnClickListener {
                val intent = Intent("android.intent.action.GET_CONTENT")
                intent.type = "image/*"
                launchGalleryActivity.launch(intent)
            }
            button.setOnClickListener {

                if (imageUrl == null) {
                    Toast.makeText(requireContext(), "please select image", Toast.LENGTH_SHORT)
                        .show()

                } else {
                    uploadImage(imageUrl!!)
                }

            }
        }
            return binding.root
        }




    private fun uploadImage(imageUrl:Uri) {
        dialog.show()

        val fileName = UUID.randomUUID().toString()+".jpg"

        val refStorage = FirebaseStorage.getInstance().reference.child("image/$fileName")
        refStorage.putFile(imageUrl)
            .addOnSuccessListener {
                it.storage.downloadUrl.addOnSuccessListener { image ->
                    storeData( image.toString())

                }
            }
            .addOnFailureListener {
                dialog.dismiss()
                Toast.makeText(requireContext(),"something went wrong",Toast.LENGTH_SHORT).show()


            }

    }

    private fun storeData(image:String) {
        val db = Firebase.firestore
        val data = hashMapOf<String,Any>(

            "img" to image
        )
        db.collection("slider").document("item").set(data)
            .addOnSuccessListener {
                Toast.makeText(requireContext(),"categry updated",Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                dialog.dismiss()
                Toast.makeText(requireContext(),"something went wrong",Toast.LENGTH_SHORT).show()


            }


    }

}
