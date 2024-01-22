package io.hsar.mealplanner.data

import io.hsar.mealplanner.storage.PastMealStorage

interface Meal {
    val name: String
    val tags: Set<String>
    val description: String?
}

data class PossibleMeal(
    override val name: String,
    override val tags: Set<String>,
    val possibleServings: PossibleServings,
    override val description: String? = null
): Meal {
    fun toPastMeal(servings: Int): PastMeal {
        require(servings >= possibleServings.min && servings <= possibleServings.max)
        return PastMeal(name, tags, servings, description)
    }
}

/**
 * Both values inclusive.
 */
data class PossibleServings(val min: Int, val max: Int)

data class PastMeal(
    override val name: String,
    override val tags: Set<String>,
    val servings: Int,
    override val description: String? = null
): Meal
