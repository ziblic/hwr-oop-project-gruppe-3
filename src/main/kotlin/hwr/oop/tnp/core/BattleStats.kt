package hwr.oop.tnp.core

import kotlinx.serialization.Serializable

@Serializable
data class BattleStats(val maxHp: Int, var hp: Int, val speed: Int) {
    constructor(maxHp: Int, speed: Int) : this(maxHp, maxHp, speed)
}
