package hwr.oop.tnp

import kotlin.io.println

class GameParser(private val args: List<String>) {

    private val game: ParserInterface = Game()

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
        try {
            return argument.toInt()
        } catch (e: NumberFormatException) {
            throw Exception("Error: Failed to convert '$argument' to Int. Reason: ${e.message}")
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
        if (args.isEmpty() || !(args.size >= 8 && args.size <= 11)) {
            println(addMonsterHelp)
            return
        }

        val monsterName = args[0]
        val attacks = args.slice(6..args.size - 2).toList()
        val trainerName = args[args.size - 1]

        try {
            val hp = parseToInt(args[1])
            val attack = parseToInt(args[2])
            val defense = parseToInt(args[3])
            val specAttack = parseToInt(args[4])
            val specDefense = parseToInt(args[5])

            game.addMonster(
                monsterName,
                hp,
                attack,
                defense,
                specAttack,
                specDefense,
                attacks,
                trainerName
            )
        } catch (e: Exception) {
            println("Some of the provided arguments could not be parsed to an Int")
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

    // TODO: Rework when the view logic is implemented
    private fun parseForViewBattle(args: List<String>) {
        if (args.isEmpty()) {
            println(viewBattleHelp)
            return
        }

        game.viewStatus()
    }

    private fun parseForPerformAttack(args: List<String>) {
        if (args.isEmpty() || args.size != 3) {
            println(attackHelp)
            return
        }

        try {
            game.performAttack(parseToInt(args[0]), args[1], args[2])
        } catch (e: Exception) {
            println("Some of the provided arguments could not be parsed to an Int")
            return
        }
    }

    private fun printHelp(command: String = "") {
        val helpMsg = commandsHelpMap.getOrDefault(command, defaultHelp)
        println(helpMsg)
    }
}

fun main(args: List<String>) {
    GameParser(args)
}
