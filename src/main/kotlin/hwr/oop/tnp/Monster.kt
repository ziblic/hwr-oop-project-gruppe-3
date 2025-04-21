package hwr.oop.tnp

import kotlin.math.max

class Monster(
    private val name: String,
    private val stats: BattleStats,
    private val type: Type,
    private val attacks: List<Attack>,

) {

    fun getHp(): Int {
        return this.stats.hp
    }

    fun getName(): String {
        return this.name
    }

    fun getBattleStats(): BattleStats {
        return this.stats
    }

    fun getType(): Type {
        return this.type
    }

    fun getAttacks(): List<Attack> {
        return this.attacks
    }

    fun attack(other: Monster) {
        TODO()
    }

    fun isKO(): Boolean {
        return this.stats.hp == 0
    }

    fun takeDamage(amountOfDamage: Int) {

        this.stats.hp = max(this.stats.hp - amountOfDamage, 0)
    }
}
