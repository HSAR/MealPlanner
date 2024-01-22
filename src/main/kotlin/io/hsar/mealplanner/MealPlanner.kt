package io.hsar.mealplanner

import io.hsar.mealplanner.data.Meal
import io.hsar.mealplanner.data.PastMeal
import io.hsar.mealplanner.data.PossibleMeal
import io.hsar.mealplanner.storage.Storage
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.*

/**
 * Main class responsible for the overall generation of meal plans.
 */
class MealPlanner(val previousMealStorage: Storage<SortedMap<LocalDate, PastMeal>>, val possibleMealStorage: Storage<Set<PossibleMeal>>) {
    fun generate(days: Int, numPeople: Int, fromDate: LocalDate = LocalDate.now()): List<PastMeal> {
        //val random = Random(Objects.hashCode(fromDate))

        val totalServingsRequired = days * numPeople
        var currServingsUnplanned = totalServingsRequired
        var currValidMeals = generatePossibleMealList(previousMealStorage.read(), possibleMealStorage.read(), fromDate)

        val results = mutableListOf<PastMeal>()
        while (currServingsUnplanned > 0) {
            currValidMeals = currValidMeals.filterByServings(currServingsUnplanned)
            if (currValidMeals.isEmpty()) {
                throw IllegalStateException("Cannot complete meal plan with current strategy. Current plan: $results")
            }

            val proposedMeal = currValidMeals.first()
                .also { currValidMeals = currValidMeals - it }
            val servings = Math.min(proposedMeal.possibleServings.max, currServingsUnplanned)
            results.add(proposedMeal.toPastMeal(servings))

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

    private fun List<PossibleMeal>.filterByServings(servingsRequired: Int) = this.filter { it.possibleServings.min < servingsRequired }

    private fun findMostRecentDateCooked(previousMeals: SortedMap<LocalDate, PastMeal>, mealName: String): LocalDate? =
        previousMeals
            .filter { (_, v) -> v.name == mealName }
            .keys
            .max()

    private fun generatePossibleMealList(previousMeals: SortedMap<LocalDate, PastMeal>, possibleMeals: Collection<PossibleMeal>, date: LocalDate): List<PossibleMeal> =
        possibleMeals
            .map {
                val previousDateCooked = findMostRecentDateCooked(previousMeals, it.name) ?: LocalDate.MIN
                it to ChronoUnit.DAYS.between(previousDateCooked, date)
            }
            .sortedWith(compareByDescending<Pair<PossibleMeal, Long>> { it.second }
                .thenBy { it.first.possibleServings.max } // NB: When multiple meals have the same duration, most importantly when they have never been cooked, secondary sort by largest meals first
                .thenBy { it.first.name })
            .map { it.first }
}