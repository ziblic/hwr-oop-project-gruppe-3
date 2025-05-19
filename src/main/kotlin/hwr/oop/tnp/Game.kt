package hwr.oop.tnp

class Game : ParserInterface {

    private val dataHandler: DataHandlerInterface = DataHandler()

    override fun createTrainer(trainerName: String) {
        val trainer = Trainer(trainerName)
        println("Created Trainer with name $trainerName")
        dataHandler.saveTrainer(trainer)
    }

    override fun addMonster(
        monsterName: String,
        hp: Int,
        speed: Int,
        type: Type,
        attacks: List<Attack>,
        trainerName: String
    ) {
        try {
            val trainer = dataHandler.loadTrainer(trainerName)
            val monster = Monster(monsterName, BattleStats(hp, speed), type, attacks)
            dataHandler.saveTrainer(trainer.addMonster(monster))
            println(
                """Created new Monster:
Name:               $monsterName
HP:                 $hp
Speed:              $speed
Type:               $type
Attacks:            $attacks
Trainer:            $trainerName
"""
            )
            dataHandler.saveMonster(monster)
        } catch (e: Exception) {
            println(e.message)
        }
    }

    override fun initiateBattle(trainer1: String, trainer2: String) {
        try {
            val battle =
                Battle(
                    dataHandler.loadTrainer(trainer1),
                    dataHandler.loadTrainer(trainer2)
                )
            dataHandler.saveBattle(battle)
            println("Battle with ID ${battle.battleId} was created")
        } catch (e: Exception) {
            println(e.message)
        }
    }

    override fun viewStatus(battleId: Int) {
        try {
            val battle = dataHandler.loadBattle(battleId)
            battle.viewStatus()
        } catch (_: Exception) {
            println("The battle with ID $battleId does not exist")
        }
    }

    override fun showAllBattles() {
        Battle.showAll()
    }

    override fun performAttack(battleID: Int, selectedAttack: Attack) {
        try {
            val battle = dataHandler.loadBattle(battleID)
            dataHandler.saveMonster(battle.takeTurn(selectedAttack))
            dataHandler.saveBattle(battle)
        } catch (e: Exception) {
            println(e.message)
        }
    }
}
