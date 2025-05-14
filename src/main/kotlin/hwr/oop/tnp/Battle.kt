package hwr.oop.tnp

class Battle(
    private val trainerOne: Trainer,
    private val trainerTwo: Trainer,
    private val battleId: Int,
    private var currentTrainer: Trainer = setBeginningTrainer(trainerOne, trainerTwo)
) {
    companion object {
        private fun setBeginningTrainer(trainerOne: Trainer, trainerTwo: Trainer): Trainer {
            val m1 = trainerOne.nextMonster()
                ?: throw IllegalStateException("Trainer one has no alive monsters")
            val m2 = trainerTwo.nextMonster()
                ?: throw IllegalStateException("Trainer two has no alive monsters")

            if (m1.stats.speed >= m2.stats.speed) {
                return trainerOne
            } else {
                return trainerTwo
            }
        }
    }
    fun getBattleId(): Int = battleId
    var currentRound: Int = 0
        private set
    var finished: Boolean = false
        private set // Only this class can modify the value

    private fun endBattle() {
        finished = true
    }

    private fun startRound() {
        if (determineWinner() != null) {
            endBattle()
            return
        }
        currentRound++
    }

    private fun getOpponent(): Trainer {
        return if (currentTrainer == trainerOne) trainerTwo else trainerOne
    }

    fun takeTurn(attack: Attack) {
        startRound()
        if (finished) {
            throw IllegalStateException("Battle is already finished")
        }
        val monster = currentTrainer.nextMonster()
        val opponent = getOpponent()
        val oppMonster = opponent.nextMonster()

        monster!!.attack(attack, oppMonster!!)
        currentTrainer = opponent
    }

    fun determineWinner(): Trainer? {
        if (trainerOne.isDefeated()) return trainerTwo
        if (trainerTwo.isDefeated()) return trainerOne
        return null
    }
}
