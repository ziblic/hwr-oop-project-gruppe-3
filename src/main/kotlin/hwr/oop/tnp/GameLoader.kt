package hwr.oop.tnp

import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileNotFoundException

class GameLoader {

    private val saveFile = File(System.getProperty("user.dir"), "save_file.json")
    // TODO: Use JSON later
    lateinit var saveData: Set<Int>

    fun saveTrainer(trainerName: String) = saveTrainer(Trainer(trainerName))
    fun saveTrainer(trainer: Trainer) {
        val folder = File("data/trainers")
        if (!folder.exists()) folder.mkdirs()

        val trainerFile = File(folder, "${trainer.name}.json")

        if (trainerFile.exists()){
            println("You have already created a trainer with this name\n")
            return
        }
        // Theoretically, you could get a trainer with their monsters
        // But in practice, you first just add the trainer and then monster, also by modifying the trainer's file
        // Otherwise, just add an empty list
        val trainerJson = JSONObject()
            .put("name", trainer.name)
            .put("monsters", trainer.getMonsters())

        trainerFile.writeText(trainerJson.toString(4)) // pretty-printed
    }
    fun loadTrainer(trainerName: String) : Trainer? {
        val folder = File("data/trainers")
        val trainerFile = File(folder, "${trainerName}.json")
        if (!trainerFile.exists()){
            println("Loading the trainer with name ${trainerName} failed, there is no such a trainer saved\n")
            return null
        }

        // extract the trainer's data from the file (name and their monsters)
        val trainerJson = JSONObject(trainerFile.readText())

        val trainerName = trainerJson.getString("name")

        // Get monster names (JSONArray → List<String>)
        val monstersJsonArray = trainerJson.optJSONArray("monsters") ?: JSONArray()  // Safe fallback
        val monsterNames: MutableList<String> = mutableListOf()
        for (i in 0 until monstersJsonArray.length()) {
            val name = monstersJsonArray.optString(i, null)
            if (name != null) {
                monsterNames.add(name)
            }
        }

        // load monsters after their names
        val monsters: MutableList<Monster> = mutableListOf()
        monsterNames.forEach { name ->
            val monster = loadMonster(name)
            if (monster != null) {
                monsters.add(monster)
            }
        }

        val trainer = Trainer(name = trainerName, monsters=monsters)

        return trainer
    }


    fun saveMonster(monster: Monster, trainerName: String){
        val monsterFile = File("data/monsters/${monster.getName()}.json")
        val trainerFile = File("data/trainers/$trainerName.json")

        if(monsterFile.exists()){
            println("A monster with such the name ${monster.getName()} already exists\n")
            return
        }
        // Make sure the monster file doesn't already exist and the trainer does
        if (!trainerFile.exists()){
            println("No trainer with such the name ${trainerName} found\n You first need to add the trainer and the monster\n")
            return
        }
        val monsterJson = monsterToJson(monster)

        // Write to file
        monsterFile.parentFile.mkdirs()
        monsterFile.writeText(monsterJson.toString(4)) // pretty print


        fun saveMonsterToTrainer(trainerName: String, monsterName: String) {
            val trainerJson = JSONObject(trainerFile.readText())
            val monstersArray = trainerJson.optJSONArray("monsters") ?: JSONArray()

            // Check for duplicates
            for (i in 0 until monstersArray.length()) {
                if (monstersArray.getString(i) == monsterName) {
                    println("Monster '$monsterName' is already assigned to trainer '$trainerName'.")
                    return
                }
            }

            // Add the monster and update the file
            monstersArray.put(monsterName)
            trainerJson.put("monsters", monstersArray)

            trainerFile.writeText(trainerJson.toString(4))
        }

        saveMonsterToTrainer(trainerName, monster.getName())
    }


    fun saveMonster(
        monsterName: String,
        hp: Int,
        attack: Int,
        defense: Int,
        specAttack: Int,
        specDefense: Int,
        attacks: List<String>, // For now, we assume there is on only one attack added into a JSONArray
        trainerName: String,
    ){
        // where do we take the monster's speed and type?

//        val monster = Monster(name=monsterName, stats=BattleStats(hp, 0, attack, defense, specAttack, specDefense), type = , attacks = )
//        saveMonster(monster)

        // TODO: make sure that an attack with the correct name is added
        TODO("pass the monster to this function itself")


    }

    fun loadMonster(monsterName: String) : Monster?{
        val monsterFile = File("data/monsters/${monsterName}.json")
        if (!monsterFile.exists()) {
            println("No monster with such a name found")
            return null
        }
        val monsterJson = JSONObject(monsterFile.readText())
        return monsterFromJson(monsterJson)

    }
    fun saveBattle(battle: Battle) {
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
    fun createBattle(trainer1: Trainer, trainer2: Trainer) = saveBattle(battle = Battle(trainer1, trainer2))
    fun createBattle(trainerName1: String, trainerName2: String){
        val trainer1 = requireNotNull(loadTrainer(trainerName1)) {
            "Trainer '$trainerName1' not found."
        }
        val trainer2 = requireNotNull(loadTrainer(trainerName2)) {
            "Trainer '$trainerName2' not found."
        }

        createBattle(trainer1, trainer2)

    }
    fun loadBattle(battleID: Int): Battle {
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
    fun monsterToJson(monster: Monster): JSONObject {
        val statsJson = JSONObject()
            .put("hp", monster.getHp())
            .put("speed", monster.getSpeed())
            .put("attack", monster.getAttack())
            .put("defense", monster.getDefense())
            .put("specialAttack", monster.getSpecialAttack())
            .put("specialDefense", monster.getSpecialDefense())

        val attackArray = JSONArray(monster.getAttacks().map { it.name })

        return JSONObject()
            .put("name", monster.getName())
            .put("type", monster.getType().name)
            .put("stats", statsJson)
            .put("attacks", attackArray)
    }


    fun monsterFromJson(monsterJson: JSONObject): Monster {
        val stats = BattleStats(
            hp = monsterJson.getInt("hp"),
            speed = monsterJson.getInt("speed"),
            attack = monsterJson.getInt("attack"),
            defense = monsterJson.getInt("defense"),
            specialAttack = monsterJson.getInt("specialAttack"),
            specialDefense = monsterJson.getInt("specialDefense")
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
