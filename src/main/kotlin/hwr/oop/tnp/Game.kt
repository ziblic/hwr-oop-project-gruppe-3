package hwr.oop.tnp
import java.io.File
//import org.json.JSONArray
//import org.json.JSONObject


class Game {

    private val gameLoader = GameLoader()

    fun createTrainer(trainerName: String) {
        val folder = File("data/trainers")
        if (!folder.exists())
            folder.mkdirs()

        val trainerFile = File(folder, "$trainerName.json")

        if (trainerFile.exists())
            return

        // Manually create JSON string for the trainer
        val jsonData = """
            {
                "name": "$trainerName",
                "monsters": []
            }
        """.trimIndent()

        trainerFile.writeText(jsonData)
    }

    fun addMonster(
        monsterName: String,
        hp: Int,
        attack: Int,
        defense: Int,
        specAttack: Int,
        specDefense: Int,
        attackName: String,
        trainerName: String,
    ) {
        val monsterData = """
        {
            "name": "$monsterName",
            "hp": $hp,
            "attack": $attack,
            "defense": $defense,
            "specialAttack": $specAttack,
            "specialDefense": $specDefense,
            "attackName": "$attackName",
            "trainer": "$trainerName"
        }
        """.trimIndent()

        val file = File("/data/monsters/$monsterName.json")
        file.parentFile.mkdirs() // makes sure 'monsters' folder exists
        file.writeText(monsterData)

        addMonsterToTrainer(trainerName, monsterName)

    }
    private fun addMonsterToTrainer(trainerName: String, monsterName: String){
        val trainerFile = File("data/trainers/$trainerName.json")
        if (!trainerFile.exists()) {
            println("Trainer '$trainerName' not found.")
            return
        }

        // Read the existing trainer JSON content
        val jsonText = trainerFile.readText()

        // use regular expressions for parsing
        // [^"]+ matches any string except for double-quotes
        // [(.*?)\] captures all the monsters' names inside [] brackets
        val namePattern = """"name":\s*"([^"]+)"""".toRegex()
        val monstersPattern = """"monsters":\s*\[(.*?)\]""".toRegex()

        // matches of the regex
        val nameMatch = namePattern.find(jsonText)
        val monstersMatch = monstersPattern.find(jsonText)

        if (nameMatch != null && monstersMatch != null) {

            // captured groups (if any) based on parentheses () in the regex
            val name = nameMatch.groupValues[1]
            val monstersJson = monstersMatch.groupValues[1]

            if (monstersJson.contains(monsterName)) {
                println("Monster '$monsterName' is already assigned to trainer '$trainerName'.")
                return
            }

            val updatedMonstersJson = if (monstersJson.isEmpty()) {
                "\"$monsterName\""
            } else {
                "$monstersJson, \"$monsterName\""
            }

            // Manually build the new JSON string
            val updatedJson = """
                {
                    "name": "$name",
                    "monsters": [$updatedMonstersJson]
                }
                """.trimIndent()

            // Overwrite the old content with the new JSON
            trainerFile.writeText(updatedJson)
        } else {
            println("Invalid JSON format in the trainer's file.")
        }
    }

    fun initiateBattle(trainer1: String, trainer2: String) {
        println("Executing battle...")
    }

    fun viewStatus(id: Int) {
        // load a battle from a json
        println("Executing view...")
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
