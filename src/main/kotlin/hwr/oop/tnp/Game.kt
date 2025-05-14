package hwr.oop.tnp

class Game : ParserInterface {

    private val dataHandler: DataHandlerInterface = DataHandler()

    override fun createTrainer(trainerName: String) = dataHandler.saveTrainer(trainerName)

    override fun addMonster(
        monsterName: String,
        hp: Int,
        speed: Int,
        attacks: List<String>, // For now, we assume there is on only one attack added into a JSONArray
        trainerName: String,
    ) {
        dataHandler.saveMonster(
            monsterName,
            hp,
            speed,
            attacks,
            trainerName,
        )
    }

    override fun initiateBattle(trainer1: String, trainer2: String) : Int {
        try {
            return dataHandler.createBattle(trainer1, trainer2)
        }
        catch (e: Exception ){
            println(e.message)
            return 0
        }
        

    }

    override fun viewStatus(battleId: Int) {

    }

    // TODO: Change Type to `selectedAttack: Attack`
    override fun performAttack(battleID: Int, trainerName: String, selectedAttack: String) {
        println("Executing attack...")
    }

    override fun showAllBattles() {
        // TO-DO: load all battles and print them
        println("Showing all battles")
    }

}