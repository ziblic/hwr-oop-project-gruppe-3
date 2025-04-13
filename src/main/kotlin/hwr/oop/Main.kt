package hwr.oop

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
                try {
                    printHelp(args[1])
                } catch (e: ArrayIndexOutOfBoundsException) {
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

        val gameManager = GameManager()
        gameManager.createTrainer(args[0])
    }

    private fun addMonsterProcedure(args: Array<String>) {
        if (args.isEmpty() || args.size != 6) {
            println(addMonsterHelp)
            return
        }

        val monsterName = args[0]

        try {
            val hp = parseToInt(args[1])
            val attack = parseToInt(args[2])
            val defense = parseToInt(args[3])
            val specAttack = parseToInt(args[4])
            val specDefense = parseToInt(args[5])

            val gameManager = GameManager()
            gameManager.addMonster(monsterName, hp, attack, defense, specAttack, specDefense)
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

        val gameManager = GameManager()
        gameManager.initiateBattle(args[0], args[1])
    }

    // TODO: Rework when the view logic is implemented
    private fun viewBattleProcedure(args: Array<String>) {
        if (args.isEmpty()) {
            println(viewBattleHelp)
            return
        }

        val gameManager = GameManager()
        gameManager.viewStatus()
    }

    private fun performAttackProcedure(args: Array<String>) {
        if (args.isEmpty() || args.size != 3) {
            println(attackHelp)
            return
        }

        try {
            val gameManager = GameManager()
            gameManager.performAttack(parseToInt(args[0]), args[1], args[2])
        } catch (e: Exception) {
            println("Some of the provided arguments could not be parsed to an Int")
            return
        }
    }

    private fun printHelp(command: String = "") {
        when (command) {
            commands[0] -> println(newTrainerHelp)
            commands[1] -> println(addMonsterHelp)
            commands[2] -> println(newBattleHelp)
            commands[3] -> println(viewBattleHelp)
            commands[4] -> println(attackHelp)
            else -> println(defaultHelp)
        }
    }
}

fun main(args: Array<String>) {
    GameParser(args)
}
