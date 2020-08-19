package com.leokuyper.recipeexpress

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.leokuyper.recipeexpress.databinding.FragmentHomeBinding
import com.leokuyper.recipeexpress.data.RecipeItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.fragment_all_recipe.*
import java.text.FieldPosition


class HomeFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentHomeBinding
    private lateinit var db: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        auth = Firebase.auth
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        auth.addAuthStateListener { firebaseAuth ->
            val firebaseUser = firebaseAuth.currentUser
            if (firebaseUser == null) {
                Log.d("Authentication", "User is not logged in")
                findNavController().navigate(R.id.loginFragment)
            }
        }

        var adapter = GroupAdapter<GroupieViewHolder>()
        db = Firebase.firestore
        binding.allRecipesView.adapter = adapter

        db.collection("recipes").get()
            .addOnSuccessListener {
                for (recipe in it){
                    val resultRecipeItem = recipe.toObject<RecipeItem>()
                    Log.d("RecipeItem", "${resultRecipeItem}")
                    adapter.add(RecipeItem(resultRecipeItem))
                }
            }

        binding.createRecipe.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_createRecipeFragment)
        }


        return binding.root

    }

}

class RecipeItem(private val recipeItem: RecipeItem) : Item(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int){
        viewHolder.recipeViewName.text = recipeItem.name
        viewHolder.recipeViewIngredients.text = recipeItem.ingredients
    }

    override fun getLayout(): Int = R.layout.fragment_all_recipe

}