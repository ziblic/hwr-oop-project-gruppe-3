package hwr.oop.tnp

import kotlinx.serialization.Serializable
import kotlin.math.max

@Serializable
class Monster(
    val name: String,
    val stats: BattleStats,
    val primitiveType: PrimitiveType,
    val attacks: List<Attack>,
) {
    fun attack(attackUsed: Attack, otherMonster: Monster) {
        require(this.attacks.contains(attackUsed)) {
            "The used attack is not part of the attacks of the monster"
        }

        val multiplier = attackUsed.primitiveType.getEffectivenessAgainst(otherMonster.primitiveType)

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
