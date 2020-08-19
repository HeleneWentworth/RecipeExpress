package com.leokuyper.recipeexpress

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.leokuyper.recipeexpress.data.RecipePost
import com.leokuyper.recipeexpress.databinding.FragmentRecipeDetailBinding
import kotlinx.android.synthetic.main.fragment_recipe_detail.view.*


class RecipeDetailFragment : Fragment() {

    private lateinit var binding: FragmentRecipeDetailBinding
    private lateinit var db: FirebaseFirestore
    private val args: RecipeDetailFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_recipe_detail, container, false)
//        binding.recipeDetailName.setText(args.recipeId)
        db = Firebase.firestore


        db.collection("recipes").document(args.recipeId).get()
            .addOnSuccessListener {
                val item = it.toObject<RecipePost>()
                binding.recipeDetailName.setText(item?.name)
                binding.recipeDetailIngredients.setText(item?.ingredients)
                binding.recipeDetailSteps.setText(item?.ingredients)
            }
            .addOnFailureListener {
                Toast.makeText(context, "Sorry could not get blog", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_recipeDetailFragment_to_homeFragment)
            }




        return binding.root
    }

}