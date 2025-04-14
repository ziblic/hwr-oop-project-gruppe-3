package hwr.oop.tnp

import io.kotest.core.spec.style.AnnotationSpec
import org.assertj.core.api.Assertions.assertThat

class TrainerTest : AnnotationSpec() {
    @Test
    fun `test trainer has name`() {
        val name = "Alex"
        val trainer = Trainer(name)
        assertThat(trainer.getName()).isEqualTo(name)
    }

    @Test
    fun `test trainer has monsters`() {
        val bs = BattleStats(100, 100, 100, 100, 100, 100)
        val m1 = Monster("Peter", bs, Type(), emptyList<Attack>())
        val m2 = Monster("Hans", bs, Type(), emptyList<Attack>())

        val name = "Alex"
        val trainer = Trainer(name, listOf(m1, m2))
        val monsters = trainer.getMonsters()

        assertThat(monsters[0].getName()).isEqualTo(m1.getName())
        assertThat(monsters[1].getName()).isEqualTo(m2.getName())
        assertThat(monsters.size).isEqualTo(2)
    }
}