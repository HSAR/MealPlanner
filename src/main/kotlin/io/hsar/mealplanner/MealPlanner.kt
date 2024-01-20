package io.hsar.mealplanner

import io.hsar.mealplanner.data.Meal
import io.hsar.mealplanner.storage.Storage
import java.time.LocalDate
import java.time.temporal.ChronoUnit

/**
 * Main class responsible for the overall generation of meal plans.
 */
class MealPlanner(val previousMealStorage: Storage<Map<String, LocalDate>>, val mealOptionStorage: Storage<Set<Meal>>) {
    fun generate(days: Int, numPeople: Int, fromDate: LocalDate = LocalDate.now()): MutableList<Pair<Meal, Int>> {
        //val random = Random(Objects.hashCode(fromDate))

        val totalServingsRequired = days * numPeople
        var currServingsUnplanned = totalServingsRequired
        var currValidMeals = generateMealList(previousMealStorage.read(), mealOptionStorage.read(), fromDate)

        val results = mutableListOf<Pair<Meal, Int>>()
        while (currServingsUnplanned > 0) {
            currValidMeals = currValidMeals.filterByServings(currServingsUnplanned)
            if (currValidMeals.isEmpty()) {
                throw IllegalStateException("Cannot complete meal plan with current strategy. Current plan: $results")
            }

            val proposedMeal = currValidMeals.first()
                .also { currValidMeals = currValidMeals - it }
            val servings = Math.min(proposedMeal.servings.max, currServingsUnplanned)
            results.add(proposedMeal to servings)

            currServingsUnplanned -= servings
        }

        return results
        /*
        find starting place
        iterate through checking how many days we've got and how many portions a meal provides
        prioritise higher portion numbers

        Things to consider when selecting options:
        - when the meal was last used
        - whether the tags are appropriate
         */

    }

    private fun List<Meal>.filterByServings(servingsRequired: Int) = this.filter { it.servings.min < servingsRequired }

    private fun generateMealList(previousMeals: Map<String, LocalDate>, possibleMeals: Collection<Meal>, date: LocalDate): List<Meal> =
        possibleMeals
            .map {
                val previousDateCooked = previousMeals[it.name] ?: LocalDate.MIN
                it to ChronoUnit.DAYS.between(previousDateCooked, date)
            }
            .sortedWith(compareByDescending<Pair<Meal, Long>> { it.second }
                .thenBy { it.first.servings.max } // NB: When multiple meals have the same duration, most importantly when they have never been cooked, secondary sort by largest meals first
                .thenBy { it.first.name })
            .map { it.first }
}