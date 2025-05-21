package hwr.oop.tnp.persistency

import hwr.oop.tnp.core.Battle

interface GamePersistencePort {
    fun saveBattle(battle: Battle)
    fun loadBattle(battleId: String): Battle
}
