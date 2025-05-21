package hwr.oop.tnp.core

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
class Battle(
    val trainerOne: Trainer,
    val trainerTwo: Trainer,
    val battleId: String = UUID.randomUUID().toString(),
    private var currentTrainer: Trainer = determineBeginningTrainer(trainerOne, trainerTwo)
) {
    companion object {
        private fun determineBeginningTrainer(trainerOne: Trainer, trainerTwo: Trainer): Trainer {
            val m1 = trainerOne.nextMonster()
            val m2 = trainerTwo.nextMonster()

            return if (m1.isFasterThan(m2))
                trainerOne
            else
                trainerTwo
        }
    }

    fun currentTrainer() = currentTrainer

    var currentRound: Int = 1
        private set

    var finished: Boolean = false
        private set

    private fun endBattle() {
        finished = true
    }

    private fun advanceRound() {
        if (determineWinner() != null) {
            endBattle()
            return
        }
        currentRound++
    }

    private fun getOpponent(): Trainer {
        return if (currentTrainer == trainerOne) trainerTwo else trainerOne
    }

    fun takeTurn(attack: Attack): Monster {
        if (finished) {
            throw IllegalStateException("Battle is already finished")
        }
        val monster = currentTrainer.nextBattleReadyMonster()
        val opponent = getOpponent()
        val opponentMonster = opponent.nextBattleReadyMonster()
        if (monster == null || opponentMonster == null) {
            throw IllegalStateException("No battle ready monster available")
        }
        monster.attack(attack, opponentMonster)
        currentTrainer = opponent

        advanceRound()
        return opponentMonster
    }

    fun determineWinner(): Trainer? {
        if (trainerOne.isDefeated()) return trainerTwo
        if (trainerTwo.isDefeated()) return trainerOne
        return null
    }

    override fun toString(): String {
        return """Battle ($battleId):
${trainerOne.name} vs. ${trainerTwo.name}
Round: $currentRound
Next Attacker: ${currentTrainer.name}"""
    }
}
