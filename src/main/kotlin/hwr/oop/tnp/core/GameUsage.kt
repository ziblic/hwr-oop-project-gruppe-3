package hwr.oop.tnp.core

interface GameUsage {
  fun createTrainer(trainerName: String, battle: Battle)
  fun addMonster(
    monsterName: String,
    hp: Int,
    speed: Int,
    primitiveType: PrimitiveType,
    attacks: List<Attack>,
    trainerName: String,
    battle: Battle,
  )

  fun initiateBattle(): Battle
  fun viewStatus(battle: Battle)
  fun showAllBattles(battles: List<Battle>)
  fun performAttack(battle: Battle, selectedAttack: Attack)
}
