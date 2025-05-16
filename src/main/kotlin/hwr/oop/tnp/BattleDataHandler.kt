package hwr.oop.tnp

import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileNotFoundException

class BattleDataHandler(
    private val battleDir: File = File("data/battles"),
    private val counterFile: File = File("data/battle_counter.txt")
     ) {

    init {
        // Ensure counter file exists and initialize if needed
        if (!counterFile.exists()) {
            counterFile.parentFile.mkdirs()
            counterFile.writeText("0")
        }
    }

    /**
     * Generates the next battle ID and updates the counter file.
     */
     fun getNextBattleId(): Int {
        if (!counterFile.exists()) {
            counterFile.parentFile.mkdirs()
            counterFile.writeText("0")
        }
        val current = counterFile.readText().toIntOrNull() ?: 0
        val next = current + 1
        counterFile.writeText(next.toString())
        return next
    }

    fun createBattle(trainer1: Trainer, trainer2: Trainer): Battle? {
        return try {
            val battle = Battle(trainer1, trainer2, getNextBattleId())
            saveBattle(battle)
            return battle
        }
        catch (e: IllegalStateException){
            println("Battle that contains trainers with no monsters could not be created: ${e.message}")
            null
        }

    }


    fun saveBattle(battle: Battle) {
        // the battle json file must contain the id of the battle, two trainers and their monsters with all their stats
        // the trainer file and the monsters' file must stay the same
        // but the data in the battle file will be changed later during the battle

        // Convert each monster of trainerOne to JSON
        val trainerOneMonstersJson = JSONArray()
        for (monster in battle.trainerOne.getMonsters()) {
            trainerOneMonstersJson.put(MonsterJsonConverter.toJson(monster))
        }

        // Convert each monster of trainerTwo to JSON
        val trainerTwoMonstersJson = JSONArray()
        for (monster in battle.trainerTwo.getMonsters()) {
            trainerTwoMonstersJson.put(MonsterJsonConverter.toJson(monster))
        }

        // Final battle JSON object
        val battleJson = JSONObject()
            .put("battleId", battle.getBattleId())
            .put("rounds", JSONArray())
            .put(
                "trainerOne",
                JSONObject()
                    .put("name", battle.trainerOne.name)
                    .put("monsters", trainerOneMonstersJson)
            )
            .put(
                "trainerTwo",
                JSONObject()
                    .put("name", battle.trainerTwo.name)
                    .put("monsters", trainerTwoMonstersJson)
            )

        val battleFile = File(battleDir, "${battle.getBattleId()}.json")

        // Ensure the parent directory exists
        battleFile.parentFile.mkdirs()

        // Create the file if it does not exist
        if (!battleFile.exists()) {
            battleFile.createNewFile()
        }

        battleFile.writeText(battleJson.toString(4)) // pretty print

        println("✅ Battle '${battle.getBattleId()}' successfully saved between '${battle.trainerOne.name}' and '${battle.trainerTwo.name}'")
    }

    fun loadBattle(battleId: Int): Battle {
        val battleFile = File(battleDir, "$battleId.json")
        if (!battleFile.exists()) {
            throw FileNotFoundException("❌ Battle file with ID $battleId not found.")
        }

        val battleJson = JSONObject(battleFile.readText())

        val trainerOneJson = battleJson.getJSONObject("trainerOne")
        val trainerTwoJson = battleJson.getJSONObject("trainerTwo")

        val trainerOneMonsters = mutableListOf<Monster>()
        for (i in 0 until trainerOneJson.getJSONArray("monsters").length()) {
            val monsterJson = trainerOneJson.getJSONArray("monsters").getJSONObject(i)
            trainerOneMonsters.add(MonsterJsonConverter.fromJson(monsterJson))
        }

        val trainerTwoMonsters = mutableListOf<Monster>()
        for (i in 0 until trainerTwoJson.getJSONArray("monsters").length()) {
            val monsterJson = trainerTwoJson.getJSONArray("monsters").getJSONObject(i)
            trainerTwoMonsters.add(MonsterJsonConverter.fromJson(monsterJson))
        }

        val trainerOne = Trainer(trainerOneJson.getString("name"), trainerOneMonsters)
        val trainerTwo = Trainer(trainerTwoJson.getString("name"), trainerTwoMonsters)

        return Battle(
            battleId = battleId,
            trainerOne = trainerOne,
            trainerTwo = trainerTwo
        )
    }


}
