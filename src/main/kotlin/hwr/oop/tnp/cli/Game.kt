package hwr.oop.tnp.cli

import hwr.oop.tnp.core.Attack
import hwr.oop.tnp.core.Battle
import hwr.oop.tnp.core.BattleStats
import hwr.oop.tnp.core.BattleStatus
import hwr.oop.tnp.core.GameUsage
import hwr.oop.tnp.core.Monster
import hwr.oop.tnp.core.PrimitiveType
import hwr.oop.tnp.core.Trainer

class Game : GameUsage {
  override fun createTrainer(trainerName: String, battle: Battle) {
    val trainer = Trainer(trainerName)
    try {
      battle.addTrainerToBattle(trainer)
    } catch (e: IllegalStateException) {
      println(e.message)
    }
  }

  override fun addMonster(
    monsterName: String,
    hp: Int,
    speed: Int,
    primitiveType: PrimitiveType,
    attacks: List<Attack>,
    trainerName: String,
    battle: Battle,
  ) {
    val monster =
      Monster(monsterName, BattleStats(hp, speed), primitiveType, attacks)
    try {
      val trainer = battle.getTrainerByName(trainerName)
      trainer.addMonster(monster)
    } catch (e: Battle.EmptyTrainerException) {
      println(e.message)
    }
  }

  override fun initiateBattle(): Battle {
    val battle = Battle()
    println(battle.toString())
    return battle
  }

  override fun viewStatus(battle: Battle) {
    println(battle.toString())
  }

  override fun showAllBattles(battles: List<Battle>) {
    if (battles.isEmpty()) {
      println("No battles created yet")
    }
    for (battle in battles) {
      println(battle.toString())
    }
  }

  override fun performAttack(battle: Battle, selectedAttack: Attack) {
    try {
      if (battle.status == BattleStatus.PREGAME) {
        battle.startBattle()
      }
    } catch (e: IllegalArgumentException) {
      println(e.message)
      return
    }
    try {
      battle.takeTurn(selectedAttack)
    } catch (e: IllegalStateException) {
      println(e.message)
    }
  }
}