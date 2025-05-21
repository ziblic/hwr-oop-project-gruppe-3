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
        trainerName: String
    ) {
        TODO()
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
