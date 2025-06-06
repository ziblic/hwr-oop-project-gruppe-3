package hwr.oop.tnp.persistency

import hwr.oop.tnp.core.Battle

interface SaveBattlePort {
  fun saveBattle(battle: Battle)
}
