package hwr.oop

import io.kotest.core.spec.style.AnnotationSpec
import org.assertj.core.api.Assertions.assertThat

class MonsterTest : AnnotationSpec() {
    private val stats = BattleStats(200, 20, 20, 20, 40, 40)
    private val
    private val monster = Monster(
        "Kevin", stats,
        type = TODO(),
        attacks = TODO(),
    )

    @Test
    fun `Monster Kevin has name Kevin`() {
        assertThat(monster.getName()).isEqualTo("Kevin")
    }
    @Test
    fun `Monster with Stats X has Stats X`() {
        assertThat(monster.getBattleStats()).isEqualTo(stats)
    }
    @Test
    fun `Monster with Type X has Type X`() {
        assertThat(monster.getType()).isEqualTo("X")
    }

}