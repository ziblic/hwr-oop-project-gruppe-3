package hwr.oop.tnp.core

interface GameUsage {
    fun createTrainer(trainerName: String)
    fun addMonster(
        monsterName: String,
        hp: Int,
        speed: Int,
        type: Type,
        attacks: List<Attack>,
        trainerName: String
    )
    fun initiateBattle(trainer1: String, trainer2: String)
    fun viewStatus(battleId: Int)
    fun showAllBattles()
    fun performAttack(battleID: Int, trainerName: String, selectedAttack: Attack)
}
