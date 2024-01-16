package io.hsar.mealplanner.data

import java.time.LocalDate

data class PreviousMeals(val data: Map<LocalDate, Meal>)