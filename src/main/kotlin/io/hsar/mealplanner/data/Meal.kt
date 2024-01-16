package io.hsar.mealplanner.data

data class Meal(val name: String, val tags: Set<String>, val servings: Servings, val description: String? = null)

data class Servings(val min: Int, val max: Int)
