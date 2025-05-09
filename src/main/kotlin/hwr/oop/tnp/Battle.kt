package hwr.oop.tnp

class Battle(
    private val trainerOne: Trainer,
    private val trainerTwo: Trainer
) {
    private var currentTurn: Int = 0
    var finished: Boolean = false
        private set // Only this class can modify the value

    fun getCurrentTurn(): Int = currentTurn

    fun startBattle() {
        currentTurn = 1
        finished = false
    }

    private fun endBattle() {
        finished = true
    }

    fun executeTurn(attack1: Attack, attack2: Attack) {
        if (finished) {
            throw IllegalStateException("Battle is already finished")
        }

        val m1 = trainerOne.getMonsters().firstOrNull()
            ?: throw IllegalStateException("Trainer one has no monsters")
        val m2 = trainerTwo.getMonsters().firstOrNull()
            ?: throw IllegalStateException("Trainer two has no monsters")

        var first: Monster
        var second: Monster
        var firstAttack: Attack
        var secondAttack: Attack

        if (m1.getSpeed() >= m2.getSpeed()) {
            first = m1
            second = m2
            firstAttack = attack1
            secondAttack = attack2
        } else {
            first = m2
            second = m1
            firstAttack = attack2
            secondAttack = attack1
        }

        first.attack(firstAttack, second)
        if (second.isKO()) {
            endBattle()
            return
        }

        second.attack(secondAttack, first)
        if (first.isKO()) {
            endBattle()
            return
        }

        currentTurn++
    }

    fun determineWinner(): Trainer? {
        if (!finished) return null

        val m1 = trainerOne.getMonsters().firstOrNull() ?: return null
        val m2 = trainerTwo.getMonsters().firstOrNull() ?: return null

        return when {
            !m1.isKO() && m2.isKO() -> trainerOne
            m1.isKO() && !m2.isKO() -> trainerTwo
            else -> null
        }
    }
}
