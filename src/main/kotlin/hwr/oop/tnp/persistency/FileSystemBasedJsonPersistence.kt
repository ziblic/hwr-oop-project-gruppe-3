package hwr.oop.tnp.persistency

import hwr.oop.tnp.core.Battle
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.File

class FileSystemBasedJsonPersistence(
  private val dataFolder: File = File(System.getProperty("user.dir"), "data"),
) : SaveBattlePort,
  LoadBattlePort {
  class LoadBattleException(
    message: String,
  ) : Exception(message)

  init {
    if (!dataFolder.exists()) {
      dataFolder.mkdirs()
    }
  }

  override fun saveBattle(battle: Battle) {
    val battleFile = File(dataFolder, "${battle.battleId}.json")
    battleFile.createNewFile()

    battleFile.writeText(Json.encodeToString(battle))
  }

  override fun loadBattle(battleId: String): Battle {
    val battleFile = File(dataFolder, "$battleId.json")
    if (!battleFile.exists()) {
      throw LoadBattleException(
        "Could not find battle with id: $battleId.",
      )
    }

    return Json.decodeFromString<Battle>(battleFile.readText())
  }

  override fun loadAllBattles(): List<Battle> {
    val battles = mutableListOf<Battle>()

    val files =
      dataFolder.listFiles()
        ?: throw LoadBattleException(
          "Failed to list files in directory $dataFolder",
        )

    for (file in files) {
      try {
        val battle = Json.decodeFromString<Battle>(file.readText())
        battles.add(battle)
      } catch (e: SerializationException) {
        continue
      }
    }

    return battles
  }
}
