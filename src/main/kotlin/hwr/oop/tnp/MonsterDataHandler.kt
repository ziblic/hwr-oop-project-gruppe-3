package hwr.oop.tnp

import org.json.JSONObject
import java.io.File

class MonsterDataHandler(private val monstersFile: File = File("data/monsters.json")) {
    fun saveMonster(monster: Monster) {
        val monstersJson = if (monstersFile.exists()) {
            JSONObject(monstersFile.readText())
        } else {
            JSONObject()
        }

        if (monstersJson.has(monster.name)) {
            println("Monster '${monster.name}' already exists.")
            return
        }

        monstersJson.put(monster.name, MonsterJsonConverter.toJson(monster))
        monstersFile.parentFile?.mkdirs()
        monstersFile.writeText(monstersJson.toString(4))
    }
    fun loadMonster(monsterName: String): Monster? {
        if (!monstersFile.exists()) {
            println("Monsters file does not exist.")
            return null
        }

        val monstersJson = JSONObject(monstersFile.readText())
        if (!monstersJson.has(monsterName)) {
            println("No monster with name '$monsterName' found.")
            return null
        }

        return MonsterJsonConverter.fromJson(monstersJson.getJSONObject(monsterName))
    }

    fun deleteMonster(monsterName: String) {
        val monster  = requireNotNull(loadMonster(monsterName)){
            "Monster ${monsterName} to be deleted not found"
        }
        deleteMonster(loadMonster(monsterName))
    }

    fun deleteMonster(monster: Monster?) {

        if (!monstersFile.exists()) {
            println("No monsters file found.")
            return
        }

        val monstersJson = JSONObject(monstersFile.readText())
        if (!monstersJson.has(monster?.name)) {
            println("Monster '${monster?.name}' not found.")
            return
        }

        monstersJson.remove(monster?.name)
        monstersFile.writeText(monstersJson.toString(4))
        println("Monster '${monster?.name}' deleted from monsters.json.")
    }
}
