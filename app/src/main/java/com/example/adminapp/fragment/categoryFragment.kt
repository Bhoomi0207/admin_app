package com.example.adminapp.fragment

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.adminapp.CategoryAdapter

import com.example.adminapp.R
import com.example.adminapp.categoryMdel
import com.example.adminapp.databinding.FragmentCategoryBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.mlkit.vision.text.Text

import java.util.UUID




class categoryFragment : Fragment() {
    private lateinit var binding:FragmentCategoryBinding
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
        binding = FragmentCategoryBinding.inflate(layoutInflater)
        dialog = Dialog(requireContext())
        dialog.setCancelable(false)


        getData()

        binding.apply {
            imageView.setOnClickListener {
                val intent = Intent("android.intent.action.GET_CONTENT")
                intent.type = "image/*"
                launchGalleryActivity.launch(intent)
            }
            button.setOnClickListener {

                validateData(binding.categry.text.toString())

            }
        }

        return binding.root
    }


    private fun getData() {
        val list = ArrayList<categoryMdel>()
        Firebase.firestore.collection("cate")
            .get().addOnSuccessListener {
            list.clear()
                for (doc in it.documents){
                    val data = doc.toObject(categoryMdel::class.java)
                    list.add(data!!)
                }
                binding.categryRecycler.adapter=CategoryAdapter(requireContext(),list)
            }
    }

    private fun validateData(categry:String) {
        if (categry.isEmpty()) {
            Toast.makeText(requireContext(), "please provide category name", Toast.LENGTH_SHORT)
                .show()
        } else if (imageUrl == null) {
            Toast.makeText(requireContext(), "please select image", Toast.LENGTH_SHORT).show()

        } else {
            uploadImage(categry)
        }

    }

    private fun uploadImage(categry:String) {
        dialog.show()

        val fileName = UUID.randomUUID().toString() + ".jpg"

        val refStorage = FirebaseStorage.getInstance().reference.child("categry/$fileName")
        refStorage.putFile(imageUrl!!)
            .addOnSuccessListener {
                it.storage.downloadUrl.addOnSuccessListener { image ->
                    storeData(categry, image.toString())

                }
            }
            .addOnFailureListener {
                dialog.dismiss()
                Toast.makeText(requireContext(), "something went wrong", Toast.LENGTH_SHORT).show()


            }

    }

    private fun storeData(categry:String, Url:String) {
        val db = Firebase.firestore
        val data = hashMapOf<String, Any>(
            "cate" to categry,
            "image" to Url
        )
        db.collection("cate").add(data)
            .addOnSuccessListener {
                dialog.dismiss()
                binding.imageView.setImageDrawable(resources.getDrawable(R.drawable.vector))
                binding.categry.text = null
                getData()
                Toast.makeText(requireContext(), "categry updated", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                dialog.dismiss()
                Toast.makeText(requireContext(), "something went wrong", Toast.LENGTH_SHORT).show()


            }

    }


}

