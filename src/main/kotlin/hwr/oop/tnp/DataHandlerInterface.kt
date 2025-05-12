package hwr.oop.tnp

interface DataHandlerInterface {
    fun saveTrainer(trainerName: String)
    fun saveTrainer(trainer: Trainer)
    fun loadTrainer(trainerName: String): Trainer?
    fun deleteTrainer(trainerName: String)
    fun deleteTrainer(trainer: Trainer?)

    fun saveMonster(monster: Monster, trainerName: String)
    fun saveMonster(
        monsterName: String,
        hp: Int,
        speed: Int,
        attacks: List<String>, // For now, we assume there is on only one attack added into a JSONArray
        trainerName: String,
    )
    fun loadMonster(monsterName: String): Monster?

    fun saveBattle(battle: Battle)
    fun createBattle(trainer1: Trainer, trainer2: Trainer): Int
    fun createBattle(trainerName1: String, trainerName2: String): Int
    fun loadBattle(battleID: Int): Battle

    fun deleteMonster(monsterName: String)
}
