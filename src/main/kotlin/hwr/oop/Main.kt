package hwr.oop

import java.io.File

class GameParser(private val gameManager: GameManager) {
    fun parseArguments(args: Array<String>) {
        if (args.isEmpty()) {
            printHelp()
            return
        }

        val command = args[0]
        val arguments = args.sliceArray(1..args.size - 1)
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

        gameManager.initiateBattle(args[0], args[1])
    }

    private fun viewBattleProcedure(args: Array<String>) {
        if (args.isEmpty()) {
            println(viewBattleHelp)
            return
        }

        gameManager.viewStatus()
    }

    private fun performAttackProcedure(args: Array<String>) {
        if (args.isEmpty() || args.size != 3) {
            println(attackHelp)
            return
        }

        gameManager.performAttack(parseToInt(args[0]), args[1], args[2])
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

class GameLoader {

    private val saveFile = File(System.getProperty("user.dir"), "save_file.json")
    // TODO: Use JSON later
    lateinit var saveData: Set<Int>

    fun loadGame() {
        println("Loading game from savefile...")
    }

    fun saveGame() {
        println("Saving game to savefile...")
    }
}

class GameManager(private val gameLoader: GameLoader) {

    fun createTrainer(trainerName: String) {
        println("Created Trainer with name $trainerName")
    }

    fun addMonster(
        monsterName: String,
        hp: Int,
        attack: Int,
        defense: Int,
        specAttack: Int,
        specDefense: Int
    ) {
        println(
            """Created new Monster:
Name:               $monsterName
HP:                 $hp
Attack:             $attack
Defense:            $defense
Special Attack:     $specAttack
Special Defense:    $specDefense
"""
        )
    }

    fun initiateBattle(trainer1: String, trainer2: String) {
        println("Executing battle...")
    }

    fun viewStatus() {
        println("Executing view...")
    }

    // TODO: Change Type to `selectedAttack: Attack`
    fun performAttack(battleID: Int, trainerName: String, selectedAttack: String) {
        println("Executing attack...")
    }

    private fun manageLoading() {
        gameLoader.loadGame()
    }

    private fun manageSaving() {
        gameLoader.saveGame()
    }
}

fun main(args: Array<String>) {
    val gameLoader = GameLoader()
    val gameManager = GameManager(gameLoader)
    val parser = GameParser(gameManager)
    parser.parseArguments(args)
}
