package hwr.oop.tnp

import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileNotFoundException

class BattleDataHandler(private val battleDir: File = File("data/battles")) {

    fun saveBattle(battle: Battle) {
        // the battle json file must contain the id of the battle, two trainers and their monsters with all their stats
        // the trainer file and the monsters' file must stay the same
        // but the data in the battle file will be changed later during the battle

        // Convert each monster of trainer1 to JSON
        val trainer1MonstersJson = JSONArray()
        for (monster in battle.trainer1.getMonsters()) {
            trainer1MonstersJson.put(MonsterJsonConverter.toJson(monster))
        }

        // Convert each monster of trainer2 to JSON
        val trainer2MonstersJson = JSONArray()
        for (monster in battle.trainer2.getMonsters()) {
            trainer2MonstersJson.put(MonsterJsonConverter.toJson(monster))
        }

        // Final battle JSON object
        val battleJson = JSONObject()
            .put("battleId", battle.battleId)
            .put("rounds", JSONArray())
            .put(
                "trainer1",
                JSONObject()
                    .put("name", battle.trainer1.name)
                    .put("monsters", trainer1MonstersJson)
            )
            .put(
                "trainer2",
                JSONObject()
                    .put("name", battle.trainer2.name)
                    .put("monsters", trainer2MonstersJson)
            )

        val battleFile = File(battleDir, "${battle.battleId}.json")
        battleFile.writeText(battleJson.toString(4)) // pretty print

        println("✅ Battle '${battle.battleId}' successfully saved between '${battle.trainer1.name}' and '${battle.trainer2.name}'")
    }

    fun loadBattle(battleId: Int): Battle {
        val battleFile = File(battleDir, "$battleId.json")
        if (!battleFile.exists()) {
            throw FileNotFoundException("❌ Battle file with ID $battleId not found.")
        }

        val battleJson = JSONObject(battleFile.readText())

        val trainer1Json = battleJson.getJSONObject("trainer1")
        val trainer2Json = battleJson.getJSONObject("trainer2")

        val trainer1Monsters = mutableListOf<Monster>()
        for (i in 0 until trainer1Json.getJSONArray("monsters").length()) {
            val monsterJson = trainer1Json.getJSONArray("monsters").getJSONObject(i)
            trainer1Monsters.add(MonsterJsonConverter.fromJson(monsterJson))
        }

        val trainer2Monsters = mutableListOf<Monster>()
        for (i in 0 until trainer2Json.getJSONArray("monsters").length()) {
            val monsterJson = trainer2Json.getJSONArray("monsters").getJSONObject(i)
            trainer2Monsters.add(MonsterJsonConverter.fromJson(monsterJson))
        }

        val trainer1 = Trainer(trainer1Json.getString("name"), trainer1Monsters)
        val trainer2 = Trainer(trainer2Json.getString("name"), trainer2Monsters)

        return Battle(
            battleId = battleId,
            trainer1 = trainer1,
            trainer2 = trainer2
        )
    }
}
