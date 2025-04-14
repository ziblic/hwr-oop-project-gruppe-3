package hwr.oop.tnp

import java.io.File

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
