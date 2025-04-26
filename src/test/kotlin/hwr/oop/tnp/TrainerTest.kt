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
        val m1 = Monster("Peter", bs, Type.Water, emptyList<Attack>())
        val m2 = Monster("Hans", bs, Type.Fire, emptyList<Attack>())

        val name = "Alex"
        val trainer = Trainer(name, mutableListOf(m1, m2))
        val monsters = trainer.getMonsters()

        assertThat(monsters[0].getName()).isEqualTo(m1.getName())
        assertThat(monsters[1].getName()).isEqualTo(m2.getName())
        assertThat(monsters.size).isEqualTo(2)
    }

    @Test
    fun `creating a trainer has no monsters`() {
        val trainer = Trainer("Alex")
        val monsters = trainer.getMonsters()

        assertThat(monsters.size).isEqualTo(0)
    }

    @Test
    fun `test add monster to trainer`() {
        val bs = BattleStats(100, 100, 100, 100, 100, 100)
        val m = Monster("Peter", bs, Type(), emptyList<Attack>())

        val trainer = Trainer("Alex")
        trainer.addMonster(m)
        val monsters = trainer.getMonsters()

        assertThat(monsters.size).isEqualTo(1)
        assertThat(monsters[0].getName()).isEqualTo(m.getName())
    }
}
