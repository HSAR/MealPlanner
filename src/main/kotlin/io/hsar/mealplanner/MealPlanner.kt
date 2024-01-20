package io.hsar.mealplanner

import io.hsar.mealplanner.storage.JsonStorage
import io.hsar.mealplanner.storage.MealOptionStorage
import io.hsar.mealplanner.storage.PreviousMealStorage
import java.time.LocalDate
import java.util.Date

/**
 * Main class responsible for the overall generation of meal plans.
 */
class MealPlanner(val storage: PreviousMealStorage, val mealOptions: MealOptionStorage) {
    fun generate(days: Int, fromDate: LocalDate = LocalDate.now()) {
        val possibleMeals = mealOptions.read()
        TODO("Not yet implemented")
    }
}