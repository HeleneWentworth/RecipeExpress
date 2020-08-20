package com.leokuyper.recipeexpress

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.leokuyper.recipeexpress.databinding.FragmentCreateRecipeBinding
import com.leokuyper.recipeexpress.databinding.FragmentRegisterBinding
import java.util.*

class CreateRecipeFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentCreateRecipeBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private lateinit var uri: Uri

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        auth = Firebase.auth
        db = Firebase.firestore
        storage = Firebase.storage
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_create_recipe, container, false)

        binding.uploadRecipeImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }

        binding.recipeCreate.setOnClickListener {
            val blog = hashMapOf(
                "userId" to auth.currentUser?.uid,
                "timestamp" to Timestamp.now(),

                "name" to binding.recipeName.text.toString(),
                "category" to binding.recipeCategory.text.toString(),
                "ingredients" to binding.recipeIngredients.text.toString(),
                "steps" to binding.recipeSteps.text.toString()
            )

            db.collection("recipes")
                .add(blog)
                .addOnFailureListener {
                    Log.d("recipes", "Failed to create recipe")
                    Toast.makeText(context, "failed to create recipe", Toast.LENGTH_SHORT).show()
                }
                .addOnSuccessListener {
                    Log.d("recipes", "Recipe created")
                    UploadImageToStorage(it.id)
                    Toast.makeText(context, "Recipe created", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_createRecipeFragment_to_homeFragment)
                }
        }

        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            uri = data.data!!
            Log.d("Photo", "Photo URI: $uri")
            binding.uploadRecipeImage.setImageURI(uri)
        }
    }

    private fun UploadImageToStorage(recipeId: String) {
        val fileName = UUID.randomUUID().toString()
        Log.d("Photo", "UUID: $fileName")
        val ref = storage.getReference("/images/$fileName")
        ref.putFile(uri)
            .addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener {
                    Log.d("Photo", "Image URL: $it")
                    // upload image  firestore
                    saveImageToRecipe(it.toString(), recipeId)
                }
            }
            .addOnFailureListener {
                Log.d("Photo", "Failed Upload: $it")
            }
    }

    private fun saveImageToRecipe(imageUrl: String, blogId: String){
        db.collection("recipes").document(blogId).update("headerImageUrl", imageUrl)
            .addOnSuccessListener {
                Log.d("Photo", "Document: $it")
            }
            .addOnFailureListener {
                Log.d("Photo", "failed to save image to blog: $it")
            }
    }


}