package hwr.oop.tnp

interface ParserInterface {
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
    fun viewStatus(battleId: String)
    fun showAllBattles()
    fun performAttack(battleID: String, selectedAttack: Attack)
}
