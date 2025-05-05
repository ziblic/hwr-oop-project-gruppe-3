package hwr.oop.tnp
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

class Game {

    private val gameLoader = GameLoader()

    fun createTrainer(trainerName: String) {
        val folder = File("data/trainers")
        if (!folder.exists()) folder.mkdirs()

        val trainerFile = File(folder, "$trainerName.json")

        if (trainerFile.exists()){
            println("You have already created a trainer with this name\n")
            return
        }

        val trainerJson = JSONObject()
            .put("name", trainerName)
            .put("monsters", JSONArray()) // empty monster list

        trainerFile.writeText(trainerJson.toString(4)) // pretty-printed
    }

    fun addMonster(
        monsterName: String,
        hp: Int,
        attack: Int,
        defense: Int,
        specAttack: Int,
        specDefense: Int,
        attackName: String, // For now, we assume there is on only one attack added into a JSONArray
        trainerName: String,
    ) {


        val file = File("data/monsters/$monsterName.json")
        // make sure that the already existing file with the monster class is not overwritten
        // there is only the monster with their name in the whole game
        if (file.exists())
            return

        val monsterJson = JSONObject()
            .put("name", monsterName)
            .put("hp", hp)
            .put("attack", attack)
            .put("defense", defense)
            .put("specialAttack", specAttack)
            .put("specialDefense", specDefense)
            .put("attacks", JSONArray().put(attackName)) // Attack array
            .put("trainer", trainerName)
        file.parentFile.mkdirs()
        file.writeText(monsterJson.toString(4)) // pretty print

        addMonsterToTrainer(trainerName, monsterName)
    }

    private fun addMonsterToTrainer(trainerName: String, monsterName: String) {
        val trainerFile = File("data/trainers/$trainerName.json")
        if (!trainerFile.exists()) {
            println("Trainer '$trainerName' not found.")
            return
        }

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

    fun initiateBattle(trainer1: String, trainer2: String) {

        // call two jsons with the trainers, and json files with their monsters
        // the battle json file must contain the id of the battle, two trainers and their monsters with all their stats
        // the trainer file and the monsters' file must stay the same
        // but the data in the battle file will be changed later during the battle

        val trainerDir = File("data/trainers")
        val monsterDir = File("data/monsters")
        val battleDir = File("data/battles").apply { mkdirs() }

        val trainer1File = File(trainerDir, "$trainer1.json")
        val trainer2File = File(trainerDir, "$trainer2.json")


        if (!trainer1File.exists() || !trainer2File.exists()) {
            println("❌ One or both trainer files do not exist.")
            return
        }

        val trainer1Json = JSONObject(trainer1File.readText())
        val trainer2Json = JSONObject(trainer2File.readText())

        val trainer1MonsterNames = trainer1Json.optJSONArray("monsters") ?: JSONArray()
        val trainer2MonsterNames = trainer2Json.optJSONArray("monsters") ?: JSONArray()

        fun loadMonsters(monsterNames: JSONArray): JSONArray {
            val monsters = JSONArray()
            for (i in 0 until monsterNames.length()) {
                val monsterName = monsterNames.getString(i)
                val file = File(monsterDir, "$monsterName.json")
                if (!file.exists()) {
                    println("❌ Monster file not found: $monsterName")
                    continue
                }
                val monsterJson = JSONObject(file.readText())
                monsters.put(monsterJson)
            }
            return monsters
        }
        // get JSON objects for monsters with their stats
        val trainer1Monsters = loadMonsters(trainer1MonsterNames)
        val trainer2Monsters = loadMonsters(trainer2MonsterNames)

        // form the final JSON for the battle

        val battleId = "battle-${System.currentTimeMillis()}"
        val battleJson = JSONObject()
            .put("battleId", battleId)
            .put("status", "initiated")
            .put("rounds", JSONArray())
            .put(
                "trainers",
                JSONArray().put(
                    JSONObject().put("name", trainer1).put("monsters", trainer1Monsters)
                ).put(
                    JSONObject().put("name", trainer2).put("monsters", trainer2Monsters)
                )
            )

        val battleFile = File(battleDir, "$battleId.json")
        battleFile.writeText(battleJson.toString(4)) // pretty print

        println("✅ Battle '$battleId' successfully initiated between '$trainer1' and '$trainer2'")
    }

    fun viewStatus(battleId: Int) {
        val battleFile = File("data/battles/$battleId.json")
        if (!battleFile.exists()) {
            println("❌ Battle with ID '$battleId' not found.")
            return
        }

        val battleJson = JSONObject(battleFile.readText())

        println("📊 Status of Battle '$battleId':")
        println("Status: ${battleJson.optString("status")}")

        val trainers = battleJson.optJSONArray("trainers") ?: JSONArray()
        for (i in 0 until trainers.length()) {
            val trainer = trainers.getJSONObject(i)
            val trainerName = trainer.getString("name")
            val monsters = trainer.optJSONArray("monsters") ?: JSONArray()

            println("\nTrainer: $trainerName")

            for (j in 0 until monsters.length()) {
                val monster = monsters.getJSONObject(j)
                val monsterName = monster.optString("name")
                val hp = monster.optInt("hp", -1)
                val attackName = monster.optString("attackName", "Unknown")

                println("🧟 Monster: $monsterName | HP: $hp | Attack: $attackName")
            }
        }

        // TODO: who still needs to attack, current attackers, etc.
    }

    // TODO: Change Type to `selectedAttack: Attack`
    fun performAttack(battleID: Int, trainerName: String, selectedAttack: String) {
        println("Executing attack...")
    }

    private fun manageLoading() {
        gameLoader.loadGame()
    }

    private fun manageSaving() {
        gameLoader.saveGame()
    }
}
