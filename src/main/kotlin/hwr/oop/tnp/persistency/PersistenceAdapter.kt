package hwr.oop.tnp.persistency

import hwr.oop.tnp.core.Battle
import kotlinx.serialization.json.Json
import java.io.File

class PersistenceAdapter : GamePersistencePort {
    private val dataFolder = File(System.getProperty("user.dir"), "data")
    private val json = Json {
        prettyPrint = true
        encodeDefaults = true
    }

    init {
        if (!dataFolder.exists()) {
            dataFolder.mkdirs()
        }
    }

    override fun saveBattle(battle: Battle) {
        val battleFile = File(dataFolder, battle.battleId)
        if (!battleFile.exists()) {
            battleFile.createNewFile()
        }

        battleFile.writeText(json.encodeToString(battle))
    }

    override fun loadBattle(battleId: String): Battle {
        val battleFile = File(dataFolder, "$battleId.json")
        if (!battleFile.exists()) {
            throw Exception("Could not find battle with id: $battleId.")
        }

        return json.decodeFromString<Battle>(battleFile.readText())
    }
}
