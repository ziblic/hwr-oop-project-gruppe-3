package hwr.oop.tnp

import io.kotest.core.spec.style.AnnotationSpec
import kotlin.collections.listOf
import org.assertj.core.api.Assertions.assertThat

class MonsterTest : AnnotationSpec() {
    private val stats = BattleStats(200, 20, 20, 20, 40, 40)
    private val monstertype = Type.Water
    private val attacks = listOf(Attack.Tackle, Attack.Fireball)

    private val monster =
            Monster(
                    "Kevin",
                    stats,
                    monstertype,
                    attacks = attacks,
            )

    @Test
    fun `check if Monster is not KO`() {
        val monster =
                Monster(
                        "Bob",
                        BattleStats(200, 20, 20, 20, 40, 40),
                        Type.Fire,
                        attacks = listOf(Attack.Tackle, Attack.Fireball),
                )
        assertThat(monster.isKO()).isFalse()
    }

    @Test
    fun `Monster Kevin has name Kevin`() {
        assertThat(monster.getName()).isEqualTo("Kevin")
    }

    @Test
    fun `Monster has correct Stats `() {
        assertThat(monster.getHp()).isEqualTo(stats.hp)
        assertThat(monster.getSpeed()).isEqualTo(stats.speed)
        assertThat(monster.getAttack()).isEqualTo(stats.attack)
        assertThat(monster.getDefense()).isEqualTo(stats.defense)
        assertThat(monster.getSpecialAttack()).isEqualTo(stats.specialAttack)
        assertThat(monster.getSpecialDefense()).isEqualTo(stats.specialDefense)
    }

    @Test
    fun `Monster with Type Wasser has Type Wasser`() {
        assertThat(monster.getType()).isEqualTo(monstertype)
    }

    @Test
    fun `Monster with attacks X has attacks X`() {
        assertThat(monster.getAttacks()).isEqualTo(attacks)
    }

    @Test
    fun `Monster takes 20 damage`() {
        val monster =
                Monster(
                        "Bob",
                        BattleStats(200, 20, 20, 20, 40, 40),
                        Type.Fire,
                        attacks = listOf(Attack.Tackle, Attack.Fireball),
                )
        val hp = monster.getHp()
        val damage_amount = 20
        monster.takeDamage(damage_amount)
        assertThat(monster.getHp()).isEqualTo(hp - damage_amount)
    }

    @Test
    fun `Monster cant get negative Hp by taking damage`() {
        val hp = monster.getHp()
        monster.takeDamage(hp + 20)
        assertThat(monster.getHp()).isEqualTo(0)
    }

    @Test
    fun `check if Monster is KO`() {
        val hp = monster.getHp()
        monster.takeDamage(hp)
        assertThat(monster.isKO()).isTrue()
    }
}
