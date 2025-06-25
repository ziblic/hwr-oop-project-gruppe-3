package hwr.oop.tnp.core

interface BattleUsage {
  val status: BattleStatus

  fun addMonsterToTrainer(
    trainerName: String,
    monster: Monster,
  )

  fun addTrainerToBattle(trainer: Trainer)

  fun startBattle()

  fun takeTurn(attack: Attack): Monster

  fun determineWinner(): Trainer
}
