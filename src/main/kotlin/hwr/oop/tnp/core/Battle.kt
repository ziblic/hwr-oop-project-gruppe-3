package hwr.oop.tnp.core

import hwr.oop.tnp.persistency.FileSystemBasedJsonPersistence
import hwr.oop.tnp.persistency.SaveBattlePort
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import java.util.UUID

@Serializable
class Battle(
  val battleId: String = UUID.randomUUID().toString(),
  @Transient private val saveAdapter: SaveBattlePort =
    FileSystemBasedJsonPersistence(),
) : BattleUsage {
  var trainerOne: Trainer = Trainer.EMPTY
    private set
  var trainerTwo: Trainer = Trainer.EMPTY
    private set
  var currentTrainer: Trainer = Trainer.EMPTY
    private set

  override var status: BattleStatus = BattleStatus.PREGAME
    private set

  var currentRound: Int = 1
    private set

  companion object {
    fun fromBattleId(battleId: String): Battle {
      val loadAdapter = FileSystemBasedJsonPersistence()
      return loadAdapter.loadBattle(battleId)
    }

    private fun determineBeginningTrainer(
      trainerOne: Trainer,
      trainerTwo: Trainer,
    ): Trainer {
      val m1 = trainerOne.nextMonster()
      val m2 = trainerTwo.nextMonster()

      return if (m1.isFasterThan(m2)) trainerOne else trainerTwo
    }
  }

  private fun getTrainerByName(name: String): Trainer {
    val one = trainerOne
    val two = trainerTwo

    return when {
      one.name.equals(name, ignoreCase = true) -> one
      two.name.equals(name, ignoreCase = true) -> two
      else -> throw EmptyTrainerException("Trainer '$name' not found.")
    }
  }

  override fun addMonsterToTrainer(
    trainerName: String,
    monster: Monster,
  ) {
    val trainer = getTrainerByName(trainerName)
    trainer.addMonster(monster)
    saveAdapter.saveBattle(this)
  }

  override fun addTrainerToBattle(trainer: Trainer) {
    when {
      trainerOne == Trainer.EMPTY -> trainerOne = trainer
      trainerTwo == Trainer.EMPTY -> trainerTwo = trainer
      else -> throw IllegalStateException(
        "Both trainers are already set.",
      )
    }
    saveAdapter.saveBattle(this)
  }

  override fun startBattle() {
    requireNoTrainerIsEmpty()
    currentTrainer = determineBeginningTrainer(trainerOne, trainerTwo)
    status = BattleStatus.STARTED
    saveAdapter.saveBattle(this)
  }

  private fun endBattle() {
    status = BattleStatus.FINISHED
  }

  private fun advanceAndSaveRound() {
    if (determineWinner() != Trainer.EMPTY) {
      endBattle()
      saveAdapter.saveBattle(this)
      return
    }
    currentRound++
    saveAdapter.saveBattle(this)
  }

  private fun getOpponent(): Trainer =
    if (currentTrainer ==
      trainerOne
    ) {
      trainerTwo
    } else {
      trainerOne
    }

  override fun takeTurn(attack: Attack): Monster {
    if (status == BattleStatus.FINISHED) {
      throw IllegalStateException("Battle is already finished")
    }
    if (currentTrainer == Trainer.EMPTY) {
      throw IllegalStateException(
        "Trainer needs to have been set for this operation",
      )
    }
    val monster = currentTrainer.nextBattleReadyMonster()
    val opponent = getOpponent()
    val opponentMonster = opponent.nextBattleReadyMonster()
    monster.attack(attack, opponentMonster)
    currentTrainer = opponent

    advanceAndSaveRound()
    return opponentMonster
  }

  override fun determineWinner(): Trainer {
    requireNoTrainerIsEmpty()
    if (trainerOne.isDefeated()) return trainerTwo
    if (trainerTwo.isDefeated()) return trainerOne
    return Trainer.EMPTY
  }

  private fun requireNoTrainerIsEmpty() {
    if (trainerOne == Trainer.EMPTY || trainerTwo == Trainer.EMPTY) {
      throw EmptyTrainerException(
        "Trainer need to have been set for this operation",
      )
    }
  }

  class EmptyTrainerException(
    message: String,
  ) : Exception(message)

  override fun toString(): String =
    if (trainerOne != Trainer.EMPTY &&
      trainerTwo != Trainer.EMPTY &&
      currentTrainer != Trainer.EMPTY
    ) {
      """Battle ($battleId):
${trainerOne.name} vs. ${trainerTwo.name}
Round: $currentRound
Next Attacker: ${currentTrainer.name}"""
    } else {
      "Battle with ID: $battleId is in a pregame state"
    }
}
