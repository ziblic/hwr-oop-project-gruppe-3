package hwr.oop.tnp

import kotlin.math.max

class Monster(
    private val name: String,
    private val stats: BattleStats,
    private val type: Type,
    private val attacks: List<Attack>,
    private var hp: Int = stats.hp
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

    fun getAttack(): List<Attack> {
        return this.attacks
    }

    fun attack(other: Monster) {
        TODO()
    }

    fun isKO(): Boolean {
        if (hp == 0) {
            return true
        } else {
            return false
        }
    }

    fun takeDamage(amountOfDamage: Int) {

        val newHp = max(this.hp - amountOfDamage, 0)
        this.hp = newHp
    }
}
