package hwr.oop.tnp

interface ParserInterface {
    fun createTrainer(trainerName: String)
    fun addMonster(
        monsterName: String,
        hp: Int,
        speed: Int,
        attacks: List<String>,
        trainerName: String
    )
    fun initiateBattle(trainer1: String, trainer2: String): Int?
    fun viewStatus(battleId: Int)
    fun showAllBattles()
    fun performAttack(battleID: Int, trainerName: String, selectedAttack: String)

    fun deleteMonster(monsterName: String)
    fun deleteTrainer(trainerName: String)
}
