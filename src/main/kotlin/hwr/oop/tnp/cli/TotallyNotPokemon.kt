package hwr.oop.tnp.cli

import hwr.oop.tnp.core.Attack
import hwr.oop.tnp.core.Game
import hwr.oop.tnp.core.GameUsage
import hwr.oop.tnp.core.Type
import kotlin.io.println

class TotallyNotPokemon(private val args: List<String>) {

    private val game: GameUsage = Game()

    init {
        parseArguments()
    }

    fun parseArguments() {
        if (args.isEmpty()) {
            printHelp()
            return
        }

        val command = args[0]
        val arguments = args.slice(1..args.size - 1)
        when (command) {
            commands[0] -> prepareForCreateTrainer(arguments)
            commands[1] -> parseForAddMonster(arguments)
            commands[2] -> parseForNewBattle(arguments)
            commands[3] -> parseForViewBattle(arguments)
            commands[4] -> parseForPerformAttack(arguments)
            commands[5] -> {
                if (args.size > 1) {
                    printHelp(args[1])
                } else {
                    printHelp()
                }
            }
            else -> println("'$command' is not a valid command. Use 'help' for usage.")
        }
    }

    private fun parseToInt(argument: String): Int {
        return try {
            argument.toInt()
        } catch (e: NumberFormatException) {
            throw Exception(
                "Error: Failed to convert '$argument' to Int. Reason: ${e.message}"
            )
        }
    }

    fun parseToAttack(input: String): Attack {
        return try {
            Attack.valueOf(input.uppercase())
        } catch (e: IllegalArgumentException) {
            throw Exception(
                "Error: Failed to convert '$input' to Attack. Reason: ${e.message}"
            )
        }
    }

    fun parseToType(input: String): Type {
        return try {
            Type.valueOf(input.uppercase())
        } catch (e: IllegalArgumentException) {
            throw Exception(
                "Error: Failed to convert '$input' to Type. Reason: ${e.message}"
            )
        }
    }

    private fun prepareForCreateTrainer(args: List<String>) {
        if (args.isEmpty()) {
            println(newTrainerHelp)
            return
        }

        game.createTrainer(args[0])
    }

    private fun parseForAddMonster(args: List<String>) {
        if (args.isEmpty() || !(args.size >= 6 && args.size <= 9)) {
            println(addMonsterHelp)
            return
        }

        val monsterName = args[0]
        val trainerName = args[args.size - 1]

        try {
            val hp = parseToInt(args[1])
            val speed = parseToInt(args[2])
            val type = parseToType(args[3])
            val attackList: MutableList<Attack> = mutableListOf()
            for (attack in args.slice(4..args.size - 2).toList()) {
                attackList.add(parseToAttack(attack))
            }

            game.addMonster(monsterName, hp, speed, type, attackList, trainerName)
        } catch (e: Exception) {
            println(COULD_NOT_PARSE_ERROR)
            return
        }
    }

    private fun parseForNewBattle(args: List<String>) {
        if (args.isEmpty() || args.size != 2) {
            println(newBattleHelp)
            return
        }

        game.initiateBattle(args[0], args[1])
    }

    private fun parseForViewBattle(args: List<String>) {
        if (args.isEmpty() || args.size != 1) {
            println(viewBattleHelp)
            return
        }

        if (args[0].trim().lowercase() == "all") {
            game.showAllBattles()
            return
        }

        try {
            game.viewStatus(parseToInt(args[0]))
        } catch (e: Exception) {
            println(COULD_NOT_PARSE_TO_INT_ERROR)
            return
        }
    }

    private fun parseForPerformAttack(args: List<String>) {
        if (args.isEmpty() || args.size != 3) {
            println(attackHelp)
            return
        }

        try {
            game.performAttack(parseToInt(args[0]), args[1], parseToAttack(args[2]))
        } catch (e: Exception) {
            println(COULD_NOT_PARSE_TO_INT_ERROR)
            return
        }
    }

    private fun printHelp(command: String = "") {
        val helpMsg = commandsHelpMap.getOrDefault(command, defaultHelp)
        println(helpMsg)
    }
}

fun main(args: Array<String>) {
    TotallyNotPokemon(args.toList())
}
