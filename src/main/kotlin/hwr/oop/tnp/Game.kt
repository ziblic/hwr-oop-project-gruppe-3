package hwr.oop.tnp

class Game : ParserInterface {

    private val dataHandler: DataHandlerInterface = DataHandler()

    override fun createTrainer(trainerName: String) = dataHandler.saveTrainer(trainerName)

    override fun addMonster(
        monsterName: String,
        hp: Int,
        speed: Int,
        attacks: List<String>, // For now, we assume there is on only one attack added into a JSONArray
        trainerName: String,
    ) {
        dataHandler.saveMonster(
            monsterName,
            hp,
            speed,
            attacks,
            trainerName,
        )
    }

    override fun initiateBattle(trainer1: String, trainer2: String) = dataHandler.createBattle(trainer1, trainer2)

    override fun viewStatus(battleId: Int) {
//        try {
//            val battle = dataHandler.loadBattle(battleId)
//
//            println("📊 Status of Battle '$battleId':")
//            println("Status: ${battle.battleStatus.name}")
//
//            val trainers = listOf(battle.trainer1, battle.trainer2)
//
//            for (trainer in trainers) {
//                println("\nTrainer: ${trainer.name}")
//                for (monster in trainer.getMonsters()) {
//                    val monsterName = monster.getName()
//                    val hp = monster.getHp()
//                    val firstAttackName = monster.getAttacks().firstOrNull()?.name ?: "Unknown"
//
//                    println("🧟 Monster: $monsterName | HP: $hp | Attack: $firstAttackName")
//                }
//            }
//
//            // TODO: who still needs to attack, current attackers, etc.
//        } catch (e: FileNotFoundException) {
//            println("❌ Battle with ID '$battleId' not found.")
//        } catch (e: Exception) {
//            println("❌ Failed to load battle: ${e.message}")
//        }
    }

    // TODO: Change Type to `selectedAttack: Attack`
    override fun performAttack(battleID: Int, trainerName: String, selectedAttack: String) {
        println("Executing attack...")
    }

    override fun showAllBattles() {
        TODO("Not yet implemented")
    }

//    private fun manageLoading() {
//        gameLoader.loadGame()
//    }
//
//    private fun manageSaving() {
//        gameLoader.saveGame()
//    }
}
