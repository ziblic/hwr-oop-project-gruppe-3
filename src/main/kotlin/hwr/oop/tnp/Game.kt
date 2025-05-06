package hwr.oop.tnp
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileNotFoundException

class Game {

    private val gameLoader = GameLoader()

    fun createTrainer(trainerName: String) = gameLoader.saveTrainer(trainerName)

    fun addMonster(
        monsterName: String,
        hp: Int,
        attack: Int,
        defense: Int,
        specAttack: Int,
        specDefense: Int,
        attacks: List<String>, // For now, we assume there is on only one attack added into a JSONArray
        trainerName: String,
    ) {
        gameLoader.saveMonster(
            monsterName,
            hp,
            attack,
            defense,
            specAttack,
            specDefense,
            attacks,
            trainerName,
        )
    }

    fun initiateBattle(trainer1: String, trainer2: String) = gameLoader.createBattle(trainer1, trainer2)


    fun viewStatus(battleId: Int) {
        try {
            val battle = gameLoader.loadBattle(battleId)

            println("📊 Status of Battle '$battleId':")
            println("Status: ${battle.battleStatus.name}")

            val trainers = listOf(battle.trainer1, battle.trainer2)

            for (trainer in trainers) {
                println("\nTrainer: ${trainer.name}")
                for (monster in trainer.getMonsters()) {
                    val monsterName = monster.getName()
                    val hp = monster.getHp()
                    val firstAttackName = monster.getAttacks().firstOrNull()?.name ?: "Unknown"

                    println("🧟 Monster: $monsterName | HP: $hp | Attack: $firstAttackName")
                }
            }

            // TODO: who still needs to attack, current attackers, etc.
        } catch (e: FileNotFoundException) {
            println("❌ Battle with ID '$battleId' not found.")
        } catch (e: Exception) {
            println("❌ Failed to load battle: ${e.message}")
        }
    }


    // TODO: Change Type to `selectedAttack: Attack`
    fun performAttack(battleID: Int, trainerName: String, selectedAttack: String) {
        println("Executing attack...")
    }

//    private fun manageLoading() {
//        gameLoader.loadGame()
//    }
//
//    private fun manageSaving() {
//        gameLoader.saveGame()
//    }
}
