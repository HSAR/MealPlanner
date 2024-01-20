package io.hsar.mealplanner

import io.hsar.mealplanner.storage.MealOptionStorage
import io.hsar.mealplanner.storage.PreviousMealStorage
import java.time.LocalDate
import java.util.*
import kotlin.random.Random

/**
 * Main class responsible for the overall generation of meal plans.
 */
class MealPlanner(val storage: PreviousMealStorage, val mealOptions: MealOptionStorage) {
    fun generate(days: Int, fromDate: LocalDate = LocalDate.now()) {
        val possibleMeals = mealOptions.read()
        val seed = Objects.hashCode(fromDate)

        val firstIndex = Random(seed).nextInt()

        /*
        find starting place
        iterate through checking how many days we've got and how many portions a meal provides
        prioritise higher portion numbers

        Things to consider when selecting options:
        - when the meal was last used
        - whether the tags are appropriate
         */

    }
}