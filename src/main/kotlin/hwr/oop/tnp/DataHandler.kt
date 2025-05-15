package hwr.oop.tnp

import java.io.File
import java.io.FileNotFoundException

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
    override fun createBattle(trainer1Name: String, trainer2Name: String): Battle? {
        val trainer1 = requireNotNull(loadTrainer(trainer1Name)) {
            "Trainer '$trainer1Name' not found."
        }
        val trainer2 = requireNotNull(loadTrainer(trainer2Name)) {
            "Trainer '$trainer2Name' not found."
        }

        return battleDataHandler.createBattle(trainer1, trainer2)
    }

    override fun createBattle(trainer1: Trainer, trainer2: Trainer): Battle? {
        return battleDataHandler.createBattle(trainer1, trainer2)
    }

    override fun saveBattle(battle: Battle) = battleDataHandler.saveBattle(battle)

    override fun loadBattle(battleId: Int): Battle{

        return battleDataHandler.loadBattle(battleId)
    }


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

    override fun deleteTrainer(trainerName: String){
        val trainer = requireNotNull(trainerDataHandler.loadTrainer(trainerName)){
            "Trainer '$trainerName' to be deleted not found."
        }
        deleteTrainer(trainer)
    }

    override fun deleteMonster(monsterName: String) {
        monsterDataHandler.deleteMonster(monsterName)
        trainerDataHandler.deleteMonsterInTrainers(monsterName)
    }

}
