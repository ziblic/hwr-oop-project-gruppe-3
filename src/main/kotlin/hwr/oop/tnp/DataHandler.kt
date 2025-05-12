package hwr.oop.tnp

import java.io.File

class DataHandler(private val basePath: String = "data") : DataHandlerInterface {
    private val trainersFile: File = File("$basePath/trainers.json")
    private val monstersFile: File = File("$basePath/monsters.json")
    private val battleDir = File("$basePath/battles").apply { mkdirs() }


    private val trainerDataHandler = TrainerDataHandler(trainersFile)
    private val monsterDataHandler = MonsterDataHandler(monstersFile)
    private val battleDataHandler = BattleDataHandler(battleDir)

    override fun saveTrainer(trainerName: String) = trainerDataHandler.saveTrainer(Trainer(trainerName))
    override fun saveTrainer(trainer: Trainer) = trainerDataHandler.saveTrainer(trainer)

    override fun loadTrainer(trainerName: String): Trainer? = trainerDataHandler.loadTrainer(trainerName)

    override fun saveMonster(
        monsterName: String,
        hp: Int,
        speed: Int,
        attacks: List<String>, // For now, we assume there is on only one attack added into a JSONArray
        trainerName: String,
    ) {
        // where should we check if all attacks have the same type?

        val type = Attack.valueOf(attacks.first()).type
        val monster = Monster(name = monsterName, stats = BattleStats(hp, speed), type = type, attacks = attacks.mapNotNull { runCatching { Attack.valueOf(it) }.getOrNull() })

        saveMonster(monster, trainerName)
    }

    override fun saveMonster(monster: Monster, trainerName: String) {
        monsterDataHandler.saveMonster(monster)
        trainerDataHandler.addMonsterToTrainer(trainerName, monster.name)
    }

    override fun loadMonster(monsterName: String): Monster? = monsterDataHandler.loadMonster(monsterName)

    // you can only pass two trainers for saving battle data only when you first create a battle
    // then you need to pass the battle's ID to save the battle (check saveBattle and createBattle)
    override fun createBattle(trainerName1: String, trainerName2: String): Int {
        val trainer1 = requireNotNull(loadTrainer(trainerName1)) {
            "Trainer '$trainerName1' not found."
        }
        val trainer2 = requireNotNull(loadTrainer(trainerName2)) {
            "Trainer '$trainerName2' not found."
        }

        return createBattle(trainer1, trainer2)
    }

    override fun createBattle(trainer1: Trainer, trainer2: Trainer): Int {
        val battle = Battle(trainer1, trainer2)
        saveBattle(battle)
        return battle.battleId
    }

    override fun saveBattle(battle: Battle) = battleDataHandler.saveBattle(battle)

    override fun loadBattle(battleId: Int): Battle = battleDataHandler.loadBattle(battleId)

    override fun deleteTrainer(trainer: Trainer?) {
        if (trainer == null) {
            println("No trainer provided.")
            return
        }

        for (monster in trainer.getMonsters()) {
            monsterDataHandler.deleteMonster(monster)
        }
        trainerDataHandler.deleteTrainer(trainer)
    }
    override fun deleteTrainer(trainerName: String) = deleteTrainer(trainerDataHandler.loadTrainer(trainerName))

    override fun deleteMonster(monsterName: String) {
        trainerDataHandler.deleteMonsterInTrainers(monsterName)
        monsterDataHandler.deleteMonster(monsterName)
    }
}
