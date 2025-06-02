package hwr.oop.tnp.persistency

import hwr.oop.tnp.core.Battle

interface LoadBattlePort {
  fun loadBattle(battleId: String): Battle
  fun loadAllBattles(): List<Battle>
}