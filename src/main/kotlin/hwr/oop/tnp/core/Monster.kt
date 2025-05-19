package hwr.oop.tnp.core

import kotlinx.serialization.Serializable
import kotlin.math.max

@Serializable
class Monster(
    val name: String,
    val stats: BattleStats,
    val type: Type,
    val attacks: List<Attack>,
) {
    fun attack(attackUsed: Attack, otherMonster: Monster) {
        require(this.attacks.contains(attackUsed)) {
            "The used attack is not part of the attacks of the monster"
        }

        val multiplier: Double =
            when (otherMonster.type) {
                attackUsed.type.effectiveAgainst -> 2.0
                attackUsed.type.lessEffectiveAgainst -> 0.5
                attackUsed.type.noEffectAgainst -> 0.0
                else -> 1.0
            }

        val damageAmount: Int =
            (
                attackUsed.damage.toDouble() *
                    multiplier *
                    attackUsed.calcMultiplierHitQuote(attackUsed.hitQuote)
                )
                .toInt()
        otherMonster.takeDamage(damageAmount)
    }

    fun isKO(): Boolean {
        return this.stats.hp == 0
    }

    private fun takeDamage(amountOfDamage: Int) {
        this.stats.hp = max(this.stats.hp - amountOfDamage, 0)
    }
}
