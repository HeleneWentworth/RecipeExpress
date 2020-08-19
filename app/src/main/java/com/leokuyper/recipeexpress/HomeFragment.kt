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

import com.google.firebase.ktx.Firebase
import com.leokuyper.recipeexpress.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentHomeBinding

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

        binding.createRecipe.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_createRecipeFragment)
        }


        return binding.root

    }

}