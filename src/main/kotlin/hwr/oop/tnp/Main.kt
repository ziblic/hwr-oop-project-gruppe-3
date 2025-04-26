package hwr.oop.tnp

class GameParser(private val args: Array<String>) {

    init {
        this.parseArguments()
    }

    fun parseArguments() {
        if (this.args.isEmpty()) {
            printHelp()
            return
        }

        val command = this.args[0]
        val arguments = this.args.sliceArray(1..this.args.size - 1)
        when (command) {
            commands[0] -> this.createTrainerProcedure(arguments)
            commands[1] -> this.addMonsterProcedure(arguments)
            commands[2] -> this.newBattleProcedure(arguments)
            commands[3] -> this.viewBattleProcedure(arguments)
            commands[4] -> this.performAttackProcedure(arguments)
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

    private fun createTrainerProcedure(args: Array<String>) {
        if (args.isEmpty()) {
            println(newTrainerHelp)
            return
        }

        val game = Game()
        game.createTrainer(args[0])
    }

    private fun addMonsterProcedure(args: Array<String>) {
        if (args.isEmpty() || (args.size >= 8 && args.size <= 11)) {
            println(addMonsterHelp)
            return
        }

        val monsterName = args[0]
        val attacks = args.sliceArray(6..args.size - 2).toList()
        val trainerName = args[args.size - 1]

        try {
            val hp = parseToInt(args[1])
            val attack = parseToInt(args[2])
            val defense = parseToInt(args[3])
            val specAttack = parseToInt(args[4])
            val specDefense = parseToInt(args[5])

            val game = Game()
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

    private fun newBattleProcedure(args: Array<String>) {
        if (args.isEmpty() || args.size != 2) {
            println(newBattleHelp)
            return
        }

        val game = Game()
        game.initiateBattle(args[0], args[1])
    }

    // TODO: Rework when the view logic is implemented
    private fun viewBattleProcedure(args: Array<String>) {
        if (args.isEmpty()) {
            println(viewBattleHelp)
            return
        }

        val game = Game()
        game.viewStatus()
    }

    private fun performAttackProcedure(args: Array<String>) {
        if (args.isEmpty() || args.size != 3) {
            println(attackHelp)
            return
        }

        try {
            val game = Game()
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

fun main(args: Array<String>) {
    GameParser(args)
}
