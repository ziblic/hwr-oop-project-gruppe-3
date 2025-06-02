package hwr.oop.tnp.persistency

import hwr.oop.tnp.core.Battle
import java.io.File
import kotlinx.serialization.json.Json

class FileSystemBasedJSONPersistence(
        private val dataFolder: File = File(System.getProperty("user.dir"), "data"),
) : SaveBattlePort, LoadBattlePort {
        private val json = Json

        init {
                if (!dataFolder.exists()) {
                        dataFolder.mkdirs()
                }
        }

        override fun saveBattle(battle: Battle) {
                val battleFile = File(dataFolder, "${battle.battleId}.json")
                battleFile.createNewFile()

                battleFile.writeText(json.encodeToString(battle))
        }

        override fun loadBattle(battleId: String): Battle {
                val battleFile = File(dataFolder, "$battleId.json")
                if (!battleFile.exists()) {
                        throw IllegalArgumentException("Could not find battle with id: $battleId.")
                }

                return json.decodeFromString<Battle>(battleFile.readText())
        }

        override fun loadAllBattles(): List<Battle> {
                val battles = mutableListOf<Battle>()

                val files = dataFolder.listFiles { file -> file.isFile && file.extension == "json" }

                for (file in files) {
                        try {
                                val battle = json.decodeFromString<Battle>(file.readText())
                                battles.add(battle)
                        } catch (e: Exception) {
                                println(
                                        "Failed to load battle from file ${file.name}: ${e.message}"
                                )
                        }
                }

                return battles
        }
}
