package hwr.oop.tnp.core

import kotlinx.serialization.Serializable
import java.util.UUID

enum class BattleStatus {
    PREGAME,
    STARTED,
    FINISHED,
}

@Serializable
class Battle(
    val battleId: String = UUID.randomUUID().toString(),
) {
    var trainerOne: Trainer? = null
        private set
    var trainerTwo: Trainer? = null
        private set
    var currentTrainer: Trainer? = null
        private set

    var status: BattleStatus = BattleStatus.PREGAME
        private set

    var currentRound: Int = 1
        private set

    companion object {
        private fun determineBeginningTrainer(
            trainerOne: Trainer,
            trainerTwo: Trainer
        ): Trainer {
            val m1 = trainerOne.nextMonster()
            val m2 = trainerTwo.nextMonster()

            return if (m1.isFasterThan(m2)) trainerOne else trainerTwo
        }
    }

    fun getTrainerByName(name: String): Trainer {
        val one = trainerOne
        val two = trainerTwo

        return when {
            one?.name.equals(name, ignoreCase = true) -> one!!
            two?.name.equals(name, ignoreCase = true) -> two!!
            else -> throw IllegalArgumentException("Trainer '$name' not found.")
        }
    }

    fun addTrainerToBattle(trainer: Trainer) {
        when {
            trainerOne == null -> trainerOne = trainer
            trainerTwo == null -> trainerTwo = trainer
            else -> throw IllegalStateException("Both trainers are already set.")
        }
    }

    fun startBattle() {
        require(trainerOne != null && trainerTwo != null) {
            "Trainer need to have been set for this operation"
        }
        currentTrainer = determineBeginningTrainer(trainerOne!!, trainerTwo!!)
        status = BattleStatus.STARTED
    }

    private fun endBattle() {
        status = BattleStatus.FINISHED
    }

    private fun advanceRound() {
        if (determineWinner() != null) {
            endBattle()
            return
        }
        currentRound++
    }

    private fun getOpponent(): Trainer {
        require(trainerOne != null && trainerTwo != null && currentTrainer != null) {
            "Trainer need to have been set for this operation"
        }
        return if (currentTrainer!! == trainerOne!!) trainerTwo!! else trainerOne!!
    }

    fun takeTurn(attack: Attack): Monster {
        if (status == BattleStatus.FINISHED) {
            throw IllegalStateException("Battle is already finished")
        }
        if (currentTrainer == null) {
            throw IllegalStateException(
                "Trainer need to have been set for this operation"
            )
        }
        val monster = currentTrainer!!.nextBattleReadyMonster()
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
        require(trainerOne != null && trainerTwo != null) {
            "Trainer need to have been set for this operation"
        }
        if (trainerOne!!.isDefeated()) return trainerTwo!!
        if (trainerTwo!!.isDefeated()) return trainerOne!!
        return null
    }

    override fun toString(): String {
        return if (trainerOne != null && trainerTwo != null && currentTrainer != null)
            """Battle ($battleId):
${trainerOne!!.name} vs. ${trainerTwo!!.name}
Round: $currentRound
Next Attacker: ${currentTrainer!!.name}"""
        else "Battle with ID: $battleId is in a pregame state"
    }
}
