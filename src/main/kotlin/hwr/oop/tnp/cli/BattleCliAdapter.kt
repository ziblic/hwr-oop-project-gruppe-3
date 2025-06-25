package hwr.oop.tnp.cli

import hwr.oop.tnp.core.Attack
import hwr.oop.tnp.core.Battle
import hwr.oop.tnp.core.BattleStats
import hwr.oop.tnp.core.BattleStatus
import hwr.oop.tnp.core.BattleUsage
import hwr.oop.tnp.core.DamageStrategy
import hwr.oop.tnp.core.Monster
import hwr.oop.tnp.core.PrimitiveType
import hwr.oop.tnp.core.Trainer
import hwr.oop.tnp.persistency.FileSystemBasedJsonPersistence
import hwr.oop.tnp.persistency.LoadBattlePort
import hwr.oop.tnp.persistency.SaveBattlePort

class BattleCliAdapter(
  private val battleId: String,
) {
  private var battle: BattleUsage

  init {
    battle = Battle.fromBattleId(battleId)
  }

  fun createTrainer(trainerName: String) {
    val trainer = Trainer(trainerName)
    try {
      battle.addTrainerToBattle(trainer)
    } catch (e: IllegalStateException) {
      println(e.message)
    }
  }

  fun addMonster(
    monsterName: String,
    hp: Int,
    speed: Int,
    attack: Int,
    specialAttack: Int,
    defense: Int,
    specialDefense: Int,
    primitiveType: PrimitiveType,
    attacks: List<Attack>,
    trainerName: String,
  ) {
    val monster =
      Monster(
        monsterName,
        BattleStats(hp, speed, attack, specialAttack, defense, specialDefense),
        primitiveType,
        attacks,
      )
    try {
      battle.addMonsterToTrainer(trainerName, monster)
    } catch (e: Battle.EmptyTrainerException) {
      println(e.message)
    }
  }

  fun viewStatus() {
    println(battle.toString())
  }

  companion object {
    fun initiateBattle(strategy: DamageStrategy) {
      val saveAdapter: SaveBattlePort = FileSystemBasedJsonPersistence()
      val battle = Battle(damageStrategy = strategy)
      println(battle.toString())
      saveAdapter.saveBattle(battle)
    }

    fun showAllBattles() {
      val loadAdapter: LoadBattlePort = FileSystemBasedJsonPersistence()
      val battles = loadAdapter.loadAllBattles()
      if (battles.isEmpty()) {
        println("No battles created yet")
      }
      for (battle in battles) {
        println(battle.toString())
      }
    }
  }

  fun performAttack(selectedAttack: Attack) {
    try {
      if (battle.status == BattleStatus.PREGAME) {
        battle.startBattle()
      }
    } catch (e: IllegalStateException) {
      println(e.message)
      return
    } catch (e: Battle.EmptyTrainerException) {
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
