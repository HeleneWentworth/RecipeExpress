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
import com.leokuyper.recipeexpress.data.RecipePost
import com.squareup.picasso.Picasso
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

        adapter.setOnItemClickListener { item, view ->
            val recipeItem = item as RecipeItem
            Log.d("Click", "Item ID: ${recipeItem.recipeItem.id}")
            val action = HomeFragmentDirections.actionHomeFragmentToRecipeDetailFragment(recipeItem.recipeItem.id)
           findNavController().navigate(action)
        }

        db.collection("recipes").get()
            .addOnSuccessListener {
                for (recipe in it){
                    val resultRecipeItem = recipe.toObject<RecipePost>()
                    resultRecipeItem.id = recipe.id
                    Log.d("RecipeItem", "${resultRecipeItem.id}")
                    adapter.add(RecipeItem(resultRecipeItem))
                }
            }

        binding.createRecipe.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_createRecipeFragment)
        }


        return binding.root

    }

}

class RecipeItem(val recipeItem: RecipePost) : Item(){
    override fun bind(viewHolder: GroupieViewHolder, position: Int){
        viewHolder.recipeViewName.text = recipeItem.name
        viewHolder.recipeViewIngredients.text = recipeItem.ingredients
        if(recipeItem.headerImageUrl != "" && recipeItem.headerImageUrl.isNotEmpty()){
            Picasso.get().load(recipeItem.headerImageUrl).fit().centerCrop().into(viewHolder.recipeViewImage)
        }
    }

    override fun getLayout(): Int = R.layout.fragment_all_recipe

}