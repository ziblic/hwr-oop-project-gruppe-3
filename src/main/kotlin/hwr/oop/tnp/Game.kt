package hwr.oop.tnp

class Game {

    private val gameLoader = GameLoader()

    fun createTrainer(trainerName: String) {
        println("Created Trainer with name $trainerName")
    }

    fun addMonster(
        monsterName: String,
        hp: Int,
        attack: Int,
        defense: Int,
        specAttack: Int,
        specDefense: Int
    ) {
        println(
            """Created new Monster:
Name:               $monsterName
HP:                 $hp
Attack:             $attack
Defense:            $defense
Special Attack:     $specAttack
Special Defense:    $specDefense
"""
        )
    }

    fun initiateBattle(trainer1: String, trainer2: String) {
        println("Executing battle...")
    }

    fun viewStatus() {
        println("Executing view...")
    }

    // TODO: Change Type to `selectedAttack: Attack`
    fun performAttack(battleID: Int, trainerName: String, selectedAttack: String) {
        println("Executing attack...")
    }

    private fun manageLoading() {
        gameLoader.loadGame()
    }

    private fun manageSaving() {
        gameLoader.saveGame()
    }
}
