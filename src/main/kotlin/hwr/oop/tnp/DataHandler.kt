package hwr.oop.tnp

import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileNotFoundException

class DataHandler : DataHandlerInterface{

    override fun saveTrainer(trainerName: String) = saveTrainer(Trainer(trainerName))

    override fun saveTrainer(trainer: Trainer) {
        val trainersFile = File("data/trainers.json")
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
    override fun loadTrainer(trainerName: String): Trainer? {
        val trainersFile = File("data/trainers.json")
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
            loadMonster(monsterName)?.let { monsters.add(it) }
        }

        return Trainer(name = trainerName, monsters = monsters)
    }

    override fun saveMonster(monster: Monster, trainerName: String) {
        val monstersFile = File("data/monsters.json")
        val monstersJson = if (monstersFile.exists()) {
            JSONObject(monstersFile.readText())
        } else {
            JSONObject()
        }

        if (monstersJson.has(monster.name)) {
            println("Monster '${monster.name}' already exists.")
            return
        }

        monstersJson.put(monster.name, monsterToJson(monster))
        monstersFile.parentFile?.mkdirs()
        monstersFile.writeText(monstersJson.toString(4))

        val trainersFile = File("data/trainers.json")
        if (!trainersFile.exists()) {
            println("Trainer '$trainerName' not found.")
            return
        }

        fun saveMonsterToTrainer(trainersFile: File, trainerName: String, monsterName: String) {
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

        saveMonsterToTrainer(trainersFile, trainerName, monster.name)
    }
    override fun saveMonster(
        monsterName: String,
        hp: Int,
        attacks: List<String>, // For now, we assume there is on only one attack added into a JSONArray
        trainerName: String,
    ){
        // where do we take the monster's type from?

//        val monster = Monster(name=monsterName, stats=BattleStats(hp, 0, attack, defense, specAttack, specDefense), type = , attacks = )
//        saveMonster(monster)

        // TODO: make sure that an attack with the correct name is added
        TODO("pass the monster to this function itself")


    }
    override fun loadMonster(monsterName: String): Monster? {
        val monstersFile = File("data/monsters.json")
        if (!monstersFile.exists()) {
            println("Monsters file does not exist.")
            return null
        }

        val monstersJson = JSONObject(monstersFile.readText())
        if (!monstersJson.has(monsterName)) {
            println("No monster with name '$monsterName' found.")
            return null
        }

        return monsterFromJson(monstersJson.getJSONObject(monsterName))
    }

    override fun saveBattle(battle: Battle) {
        // the battle json file must contain the id of the battle, two trainers and their monsters with all their stats
        // the trainer file and the monsters' file must stay the same
        // but the data in the battle file will be changed later during the battle

        val battleDir = File("data/battles").apply { mkdirs() }

        // Convert each monster of trainer1 to JSON
        val trainer1MonstersJson = JSONArray()
        for (monster in battle.trainer1.getMonsters()) {
            trainer1MonstersJson.put(monsterToJson(monster))
        }

        // Convert each monster of trainer2 to JSON
        val trainer2MonstersJson = JSONArray()
        for (monster in battle.trainer2.getMonsters()) {
            trainer2MonstersJson.put(monsterToJson(monster))
        }

        // Final battle JSON object
        val battleJson = JSONObject()
            .put("battleId", battle.battleId)
            .put("status", battle.battleStatus.name)
            .put("rounds", JSONArray())
            .put("trainer1", JSONObject()
                .put("name", battle.trainer1.name)
                .put("monsters", trainer1MonstersJson)
            )
            .put("trainer2", JSONObject()
                .put("name", battle.trainer2.name)
                .put("monsters", trainer2MonstersJson)
            )

        val battleFile = File(battleDir, "${battle.battleId}.json")
        battleFile.writeText(battleJson.toString(4)) // pretty print

        println("✅ Battle '${battle.battleId}' successfully saved between '${battle.trainer1.name}' and '${battle.trainer2.name}'")
    }



    // you can only pass two trainers for saving battle data only when you first create a battle
    // then you need to pass the battle's ID to save the battle (check saveBattle and createBattle)
    override fun createBattle(trainer1: Trainer, trainer2: Trainer) = saveBattle(battle = Battle(trainer1, trainer2))
    override fun createBattle(trainerName1: String, trainerName2: String){
        val trainer1 = requireNotNull(loadTrainer(trainerName1)) {
            "Trainer '$trainerName1' not found."
        }
        val trainer2 = requireNotNull(loadTrainer(trainerName2)) {
            "Trainer '$trainerName2' not found."
        }

        createBattle(trainer1, trainer2)

    }
    override fun loadBattle(battleID: Int): Battle {
        val battleFile = File("data/battles/$battleID.json")
        if (!battleFile.exists()) {
            throw FileNotFoundException("❌ Battle file with ID $battleID not found.")
        }

        val battleJson = JSONObject(battleFile.readText())

        val trainer1Json = battleJson.getJSONObject("trainer1")
        val trainer2Json = battleJson.getJSONObject("trainer2")



        val trainer1Monsters = mutableListOf<Monster>()
        for (i in 0 until trainer1Json.getJSONArray("monsters").length()) {
            val monsterJson = trainer1Json.getJSONArray("monsters").getJSONObject(i)
            trainer1Monsters.add(monsterFromJson(monsterJson))
        }

        val trainer2Monsters = mutableListOf<Monster>()
        for (i in 0 until trainer2Json.getJSONArray("monsters").length()) {
            val monsterJson = trainer2Json.getJSONArray("monsters").getJSONObject(i)
            trainer2Monsters.add(monsterFromJson(monsterJson))
        }

        val trainer1 = Trainer(trainer1Json.getString("name"), trainer1Monsters)
        val trainer2 = Trainer(trainer2Json.getString("name"), trainer2Monsters)

        val battleStatus = BattleStatus.valueOf(battleJson.getString("status"))

        return Battle(
            battleId = battleID,
            trainer1 = trainer1,
            trainer2 = trainer2,
            battleStatus = battleStatus
        )
    }

    // these function are used twice
    // firstly when adding/loading a monster to the game as an entity
    // secondly when saving/loading a battle (and monsters in it)
    private fun monsterToJson(monster: Monster): JSONObject {
        val statsJson = JSONObject()
            .put("hp", monster.stats.hp)
            .put("speed", monster.stats.speed)

        val attackArray = JSONArray(monster.attacks.map { it.name })

        return JSONObject()
            .put("name", monster.name)
            .put("type", monster.type.name)
            .put("stats", statsJson)
            .put("attacks", attackArray)
    }



    private fun monsterFromJson(monsterJson: JSONObject): Monster {
        val statsJson = monsterJson.getJSONObject("stats")
        val stats = BattleStats(
            hp = statsJson.getInt("hp"),
            speed = statsJson.getInt("speed")
        )

        val attacksJsonArray = monsterJson.optJSONArray("attacks") ?: JSONArray()
        val attacks = mutableListOf<Attack>()
        for (i in 0 until attacksJsonArray.length()) {
            val attackName = attacksJsonArray.optString(i, null)
            try {
                if (attackName != null) {
                    val attack = enumValueOf<Attack>(attackName)
                    attacks.add(attack)
                }
            } catch (e: IllegalArgumentException) {
                println("⚠️ Invalid attack name in JSON: $attackName")
            }
        }

        return Monster(
            name = monsterJson.getString("name"),
            stats = stats,
            type = enumValueOf<Type>(monsterJson.getString("type")),
            attacks = attacks
        )
    }




}
