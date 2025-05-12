package hwr.oop.tnp

import org.json.JSONArray
import org.json.JSONObject
import java.io.File

class TrainerDataHandler(private val trainersFile: File = File("data/trainers.json")) {

    private val monsterDataHandler = MonsterDataHandler()

    fun saveTrainer(trainerName: String) = saveTrainer(Trainer(trainerName))
    fun saveTrainer(trainer: Trainer) {
        val trainersJson = if (trainersFile.exists()) {
            JSONObject(trainersFile.readText())
        } else {
            JSONObject()
        }

        if (trainersJson.has(trainer.name)) {
            println("Trainer '${trainer.name}' already exists.")
            return
        }

        val trainerJson = JSONObject()
            .put("name", trainer.name)
            .put("monsters", JSONArray(trainer.getMonsters().map { it.name }))

        trainersJson.put(trainer.name, trainerJson)
        trainersFile.parentFile?.mkdirs()
        trainersFile.writeText(trainersJson.toString(4))
    }
    fun loadTrainer(trainerName: String): Trainer? {
        if (!trainersFile.exists()) {
            println("Trainer file does not exist.")
            return null
        }

        val trainersJson = JSONObject(trainersFile.readText())
        if (!trainersJson.has(trainerName)) {
            println("No trainer with name '$trainerName' found.")
            return null
        }

        val trainerJson = trainersJson.getJSONObject(trainerName)
        val monsterNamesArray = trainerJson.optJSONArray("monsters") ?: JSONArray()
        val monsters = mutableListOf<Monster>()

        for (i in 0 until monsterNamesArray.length()) {
            val monsterName = monsterNamesArray.getString(i)
            monsterDataHandler.loadMonster(monsterName)?.let { monsters.add(it) }
        }

        return Trainer(name = trainerName, monsters = monsters)
    }
    fun addMonsterToTrainer(trainerName: String, monsterName: String) {

        val trainersJson = JSONObject(trainersFile.readText())
        if (!trainersJson.has(trainerName)) {
            println("Trainer '$trainerName' not found.")
            return
        }

        val trainerJson = trainersJson.getJSONObject(trainerName)
        val monstersArray = trainerJson.optJSONArray("monsters") ?: JSONArray()

        if ((0 until monstersArray.length()).any { monstersArray.getString(it) == monsterName }) {
            println("Trainer already has monster '$monsterName'.")
            return
        }

        monstersArray.put(monsterName)
        trainerJson.put("monsters", monstersArray)
        trainersJson.put(trainerName, trainerJson)
        trainersFile.writeText(trainersJson.toString(4))
    }
    fun deleteTrainer(trainer: Trainer?) {
        if (!trainersFile.exists()) {
            println("No trainers to delete.")
            return
        }

        val trainersJson = JSONObject(trainersFile.readText())
        if (!trainersJson.has(trainer?.name)) {
            println("Trainer '${trainer?.name}' not found.")
            return
        }
        trainersJson.remove(trainer?.name)
        trainersFile.writeText(trainersJson.toString(4))
        println("Trainer '${trainer?.name}' and their monsters deleted.")
    }

    fun deleteMonsterInTrainers(monsterName: String) {
        if (!trainersFile.exists()) return

        val trainersJson = JSONObject(trainersFile.readText())

        for (trainerName in trainersJson.keySet()) {
            val trainerJson = trainersJson.getJSONObject(trainerName)
            val monstersArray = trainerJson.optJSONArray("monsters") ?: continue
            val updatedArray = JSONArray()

            for (i in 0 until monstersArray.length()) {
                val currentMonster = monstersArray.getString(i)
                if (currentMonster != monsterName) {
                    updatedArray.put(currentMonster)
                }
            }

            trainerJson.put("monsters", updatedArray)
            trainersJson.put(trainerName, trainerJson)
        }

        trainersFile.writeText(trainersJson.toString(4))
        println("References to '$monsterName' removed from all trainers.")
    }
}
