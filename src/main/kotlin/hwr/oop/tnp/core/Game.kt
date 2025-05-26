package hwr.oop.tnp.core

class Game : GameUsage {

    override fun createTrainer(trainerName: String) {
        TODO()
    }

    override fun addMonster(
        monsterName: String,
        hp: Int,
        speed: Int,
        type: Type,
        attacks: List<Attack>,
        trainerName: String,
        battle: Battle,
    ) {
        val monster = Monster(monsterName, BattleStats(hp, speed), type, attacks)
        try {
            val trainer = battle.getTrainerByName(trainerName)
            trainer.addMonster(monster)
        } catch (e: IllegalArgumentException) {
            println(e.message)
        }
    }

    override fun initiateBattle(trainer1: String, trainer2: String) {
        TODO()
    }

    override fun viewStatus(battleId: String) {
        TODO()
    }

    override fun showAllBattles() {
        TODO()
    }

    override fun performAttack(battleID: String, selectedAttack: Attack) {
        TODO()
    }
}
