package hwr.oop

class Monster(private val name: String,
              private val stats:BattleStats,
              private val type:Type,
              private val attacks: List<Attack> ) {

    fun getName() : String {
        return this.name
    }

    fun getBattleStats(): BattleStats {
        return this.stats
    }

    fun getType() : Type {
        return this.type
    }

    fun getAttack() :List<Attack>{
        return this.attacks
    }

}