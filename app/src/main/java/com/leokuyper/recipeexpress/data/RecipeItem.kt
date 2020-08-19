package com.leokuyper.recipeexpress.data

import com.google.firebase.Timestamp

class RecipeItem (
    val userId: String = "",
    val timestamp: Timestamp = Timestamp.now(),
    val name: String = "",
    val ingredients: String = ""
)


