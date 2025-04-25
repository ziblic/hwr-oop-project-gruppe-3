package hwr.oop.tnp

import io.kotest.core.spec.style.AnnotationSpec
import org.assertj.core.api.Assertions.assertThat

class MonsterTest : AnnotationSpec() {
    private val stats = BattleStats(200, 20, 20, 20, 40, 40)
    private val monstertype = Type.Water
    private val attacks = listOf(Attack(), Attack())

    private val monster = Monster(
        "Kevin", stats,
        monstertype,
        attacks = TODO(),
    )

    @Test
    fun `Monster Kevin has name Kevin`() {
        assertThat(monster.getName()).isEqualTo("Kevin")
    }

    @Test
    fun `Monster with 200Hp has Hp 200`() {
        assertThat(monster.getHp()).isEqualTo(200)
    }

    @Test
    fun `Monster has correct Stats `() {
        assertThat(monster.getSpeed()).isEqualTo(20)
        assertThat(monster.getAttack()).isEqualTo(20)
        assertThat(monster.getDefense()).isEqualTo(20)
        assertThat(monster.getSpecialAttack()).isEqualTo(40)
        assertThat(monster.getSpecialDefense()).isEqualTo(40)
    }
    @Test
    fun `Monster with Type Wasser has Type Wasser`() {
        assertThat(monster.getType()).isEqualTo(monstertype)
    }
    @Test
    fun `Monster with attacks X has attacks X`() {
        assertThat(monster.getAttack()).isEqualTo(attacks)
    }
    @Test
    fun `Monster takes 20 danmage`() {
        monster.takeDamage(20)
        assertThat(monster.getHp()).isEqualTo(200 - 20)
    }

    @Test
    fun `check if Monster is not KO`() {
        assertThat(monster.isKO()).isFalse()
    }

    @Test
    fun `check if Monster is KO`() {
        monster.takeDamage(200)
        assertThat(monster.isKO()).isTrue()
    }
}
