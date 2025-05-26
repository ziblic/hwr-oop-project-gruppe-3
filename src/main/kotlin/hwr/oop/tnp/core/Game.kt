package hwr.oop.tnp.core

class Game : GameUsage {

    override fun createTrainer(trainerName: String, battle: Battle) {
        val trainer = Trainer(trainerName)
        try {
            battle.addTrainerToBattle(trainer)
        } catch (e: IllegalStateException) {
            println(e.message)
        }
    }

    override fun addMonster(
        monsterName: String,
        hp: Int,
        speed: Int,
        primitiveType: PrimitiveType,
        attacks: List<Attack>,
        trainerName: String,
        battle: Battle,
    ) {
        val monster = Monster(monsterName, BattleStats(hp, speed), primitiveType, attacks)
        try {
            val trainer = battle.getTrainerByName(trainerName)
            trainer.addMonster(monster)
        } catch (e: IllegalArgumentException) {
            println(e.message)
        }
    }

    override fun initiateBattle(): Battle {
        val battle = Battle()
        println(battle.toString())
        return battle
    }

    override fun viewStatus(battle: Battle) {
        // TODO: print the battle history instead
        println(battle.toString())
    }

    override fun showAllBattles(battles: List<Battle>) {
        if (battles.isEmpty()) {
            println("No battles created yet")
        }
        for (battle in battles) {
            println(battle.toString())
        }
    }

    override fun performAttack(battle: Battle, selectedAttack: Attack) {
        try {
            battle.takeTurn(selectedAttack)
        } catch (e: IllegalStateException) {
            println(e.message)
        }
    }
}
