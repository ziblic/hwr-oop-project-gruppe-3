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
    lateinit var trainerOne: Trainer
        private set
    lateinit var trainerTwo: Trainer
        private set
    private lateinit var currentTrainer: Trainer

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

    fun addTrainerToBattle(trainer: Trainer) {
        when {
            !::trainerOne.isInitialized -> trainerOne = trainer
            !::trainerTwo.isInitialized -> trainerTwo = trainer
            else ->
                throw IllegalStateException(
                    "Both trainerOne and trainerTwo are already initialized."
                )
        }
    }

    fun addMonsterToTrainer(monster: Monster, trainerName: String) {
        val trainer =
            when (trainerName) {
                trainerOne.name -> trainerOne
                trainerTwo.name -> trainerTwo
                else ->
                    throw IllegalArgumentException(
                        "Trainer with name $trainerName not found in battle."
                    )
            }

        trainer.addMonster(monster)
    }

    fun startBattle() {
        currentTrainer = determineBeginningTrainer(trainerOne, trainerTwo)
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
        return if (currentTrainer == trainerOne) trainerTwo else trainerOne
    }

    fun takeTurn(attack: Attack): Monster {
        if (status == BattleStatus.FINISHED) {
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
