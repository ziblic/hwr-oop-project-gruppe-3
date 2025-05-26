package hwr.oop.tnp.core

interface GameUsage {
    fun createTrainer(trainerName: String)
    fun addMonster(
        monsterName: String,
        hp: Int,
        speed: Int,
        primitiveType: PrimitiveType,
        attacks: List<Attack>,
        trainerName: String,
        battle: Battle,
    )
    fun initiateBattle(trainer1: String, trainer2: String)
    fun viewStatus(battleId: String)
    fun showAllBattles()
    fun performAttack(battleID: String, selectedAttack: Attack)
}
