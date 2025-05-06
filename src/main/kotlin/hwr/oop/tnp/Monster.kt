package hwr.oop.tnp

import kotlin.math.max

class Monster(
    private val name: String,
    private val stats: BattleStats,
    private val type: Type,
    private val attacks: List<Attack> = getAttacks(),
) {
    fun getName(): String {
        return this.name
    }

    fun getHp(): Int {
        return this.stats.hp
    }

    fun getSpeed(): Int {
        return this.stats.speed
    }
    fun getAttack() = stats.attack

    fun getDefense(): Int {
        return this.stats.defense
    }

    fun getSpecialAttack(): Int {
        return this.stats.specialAttack
    }

    fun getSpecialDefense(): Int {
        return this.stats.specialDefense
    }

    fun getType(): Type {
        return this.type
    }

    fun getAttacks(): List<Attack> {
        return this.attacks
    }

    fun attack(a: Attack, m: Monster) {
        if (a.type == m.getType().veryEffectiveAgainst) {
            m.getHp() - ((a.damage * 2.0) + (a.damage * calcMultiplierHitQuote(a.hitQuote)))
        }
        else if (a.type == m.getType().lessEffectiveAgainst) {
            m.getHp() - ((a.damage * 0.5) + (a.damage * calcMultiplierHitQuote(a.hitQuote)))
        }
        else if (a.type == m.getType().effectiveAgainst)
            m.getHp() - (a.damage + (a.damage * calcMultiplierHitQuote(a.hitQuote)))
    }

    fun isKO(): Boolean {
        return this.stats.hp == 0
    }

    fun takeDamage(amountOfDamage: Int) {
        this.stats.hp = max(this.stats.hp - amountOfDamage, 0)
    }
}
