package hwr.oop.tnp

class Game : ParserInterface {

    private val gameLoader = GameLoader()

    override fun createTrainer(trainerName: String) {
        println("Created Trainer with name $trainerName")
    }

    override fun addMonster(
        monsterName: String,
        hp: Int,
        attack: Int,
        defense: Int,
        specAttack: Int,
        specDefense: Int,
        attacks: List<String>,
        trainerName: String
    ) {
        println(
            """Created new Monster:
Name:               $monsterName
HP:                 $hp
Attack:             $attack
Defense:            $defense
Special Attack:     $specAttack
Special Defense:    $specDefense
Attacks:            $attacks
Trainer:            $trainerName
"""
        )
    }

    override fun initiateBattle(trainer1: String, trainer2: String) {
        println("Executing battle...")
    }

    override fun viewStatus() {
        println("Executing view...")
    }

    // TODO: Change Type to `selectedAttack: Attack`
    override fun performAttack(battleID: Int, trainerName: String, selectedAttack: String) {
        println("Executing attack...")
    }

    private fun manageLoading() {
        gameLoader.loadGame()
    }

    private fun manageSaving() {
        gameLoader.saveGame()
    }
}
