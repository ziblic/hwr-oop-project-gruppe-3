package hwr.oop

class GameParser(private val gameManager: GameManager) {
    fun parseArguments(args: Array<String>) {
        if (args.isEmpty()) {
            printHelp()
            return
        }

        val command = args[0]
        when (command) {
            "new_trainer" -> gameManager.createTrainer()
            "add_monster" -> gameManager.addMonster()
            "battle" -> gameManager.initiateBattle()
            "view" -> gameManager.viewStatus()
            "on" -> gameManager.toggleFeature()
            "help" -> printHelp()
            else -> println("'$command' is not a valid command. Use 'help' for usage.")
        }
    }

    private fun printHelp() {
        println("Usage: command [options]")
        println("Commands:")
        println("  new_trainer     - Creates a new trainer")
        println("  add_monster     - Adds a new monster to your roster")
        println("  battle          - Starts a battle sequence")
        println("  view            - Views current game status")
        println("  on              - Select a attack to perform")
        println("  help            - Shows this help message")
    }
}

class GameLoader {
    fun loadGame() {
        println("Loading game from savefile...")
    }

    fun saveGame() {
        println("Saving game to savefile...")
    }
}

class GameManager(private val gameLoader: GameLoader) {

    fun createTrainer() {
        println("Executing new_trainer...")
    }

    fun addMonster() {
        println("Executing add_monster...")
    }

    fun initiateBattle() {
        println("Executing battle...")
    }

    fun viewStatus() {
        println("Executing view...")
    }

    fun toggleFeature() {
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
    // TODO: add the file to the GameLoader constructor
    val gameLoader = GameLoader()
    val gameManager = GameManager(gameLoader)
    val parser = GameParser(gameManager)
    parser.parseArguments(args)
}
