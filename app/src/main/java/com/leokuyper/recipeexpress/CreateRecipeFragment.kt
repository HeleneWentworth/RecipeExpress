package com.leokuyper.recipeexpress

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
import com.leokuyper.recipeexpress.databinding.FragmentCreateRecipeBinding
import com.leokuyper.recipeexpress.databinding.FragmentRegisterBinding

class CreateRecipeFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentCreateRecipeBinding
    private lateinit var db: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        auth = Firebase.auth
        db = Firebase.firestore
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_create_recipe, container, false)

        binding.recipeBack.setOnClickListener {
            findNavController().navigate(R.id.action_createRecipeFragment_to_homeFragment)
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
                    Toast.makeText(context, "Recipe created", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_createRecipeFragment_to_homeFragment)
                }
        }

        return binding.root
    }


}