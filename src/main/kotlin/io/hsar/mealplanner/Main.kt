package io.hsar.mealplanner

import com.beust.jcommander.JCommander
import io.hsar.mealplanner.cli.Command
import io.hsar.mealplanner.cli.CommandLineInterface
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    val instances: Map<String, Command> = listOf(
        CommandLineInterface()
    )
        .associateBy { it.name }
    val commander = JCommander()
    instances.forEach { (name, command) -> commander.addCommand(name, command) }

    if (args.isEmpty()) {
        commander.usage()
        System.err.println("Expected some arguments")
        exitProcess(1)
    }

    try {
        commander.parse(*args)
        val command = instances[commander.parsedCommand]
        command!!.run()
    } catch (e: Exception) {
        e.printStackTrace()
        exitProcess(1)
    }
}