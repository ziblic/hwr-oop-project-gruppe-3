package hwr.oop.tnp.core

import kotlinx.serialization.Serializable
import java.io.File
import java.util.UUID

@Serializable
class Battle(
    val trainerOne: Trainer,
    val trainerTwo: Trainer,
    val battleId: String = UUID.randomUUID().toString(),
    private var currentTrainer: Trainer = setBeginningTrainer(trainerOne, trainerTwo)
) {
    companion object {
        private fun setBeginningTrainer(trainerOne: Trainer, trainerTwo: Trainer): Trainer {
            val m1 =
                trainerOne.nextMonster()
                    ?: throw IllegalStateException(
                        "Trainer one has no alive monsters"
                    )
            val m2 =
                trainerTwo.nextMonster()
                    ?: throw IllegalStateException(
                        "Trainer two has no alive monsters"
                    )

            if (m1.stats.speed >= m2.stats.speed) {
                return trainerOne
            } else {
                return trainerTwo
            }
        }

        fun showAll() {
            val dataDirName: String = System.getenv("DATADIR")?.toString() ?: "data"
            val dataDir = File(System.getProperty("user.dir"), dataDirName)
            val battleFolder = File(dataDir, "battles")
            val battleFiles =
                battleFolder.listFiles { file ->
                    file.extension == "json" &&
                        file.nameWithoutExtension.toIntOrNull() != null
                }

            if (battleFiles == null || battleFiles.size == 0) {
                println("No battles found")
            } else {
                println("List of all Battles")
                battleFiles.forEach { file ->
                    println("BattleID: ${file.nameWithoutExtension}")
                }
            }
        }
    }
    var currentRound: Int = 0
        private set
    var finished: Boolean = false
        private set

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

    fun takeTurn(attack: Attack, trainer: Trainer): Monster {
        require(currentTrainer.name == trainer.name) {
            "It is the turn of ${currentTrainer.name}"
        }
        startRound()
        if (finished) {
            throw IllegalStateException("Battle is already finished")
        }
        val monster = currentTrainer.nextMonster()
        val opponent = getOpponent()
        val oppMonster = opponent.nextMonster()

        monster!!.attack(attack, oppMonster!!)
        currentTrainer = opponent

        return oppMonster
    }

    fun determineWinner(): Trainer? {
        if (trainerOne.isDefeated()) return trainerTwo
        if (trainerTwo.isDefeated()) return trainerOne
        return null
    }

    fun viewStatus() {
        println(
            """Battle ($battleId):
${trainerOne.name} vs ${trainerTwo.name}
Next trainer to turn is ${currentTrainer.name}"""
        )
    }
}
