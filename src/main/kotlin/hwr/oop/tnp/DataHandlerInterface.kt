package hwr.oop.tnp

interface DataHandlerInterface {
    fun saveTrainer(trainer: Trainer)
    fun loadTrainer(trainerName: String): Trainer
    fun saveMonster(monster: Monster)
    fun loadMonster(monsterName: String): Monster
    fun saveBattle(battle: Battle)
    fun loadBattle(battleId: Int): Battle
}
