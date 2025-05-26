package hwr.oop.tnp

import io.kotest.core.spec.style.AnnotationSpec
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatNoException
import org.junit.jupiter.api.assertThrows

class TrainerTest : AnnotationSpec() {
    @Test
    fun `test trainer has name`() {
        val name = "Alex"
        val trainer = Trainer(name)
        assertThat(trainer.name).isEqualTo(name)
    }

    @Test
    fun `test trainer has monsters`() {
        val bs = BattleStats(100, 100)
        val m1 = Monster("Peter", bs, PrimitiveType.WATER, emptyList())
        val m2 = Monster("Hans", bs, PrimitiveType.FIRE, emptyList())

        val name = "Alex"
        val trainer = Trainer(name, mutableListOf(m1, m2))
        val monsters = trainer.monsters

        assertThat(monsters[0].name).isEqualTo(m1.name)
        assertThat(monsters[1].name).isEqualTo(m2.name)
        assertThat(monsters.size).isEqualTo(2)
    }

    @Test
    fun `creating a trainer has no monsters`() {
        val trainer = Trainer("Alex")
        val monsters = trainer.monsters

        assertThat(monsters.size).isEqualTo(0)
    }

    @Test
    fun `test add monster to trainer`() {
        val bs = BattleStats(100, 100)
        val m = Monster("Peter", bs, PrimitiveType.WATER, emptyList())

        var trainer = Trainer("Alex")
        trainer = trainer.addMonster(m)
        val monsters = trainer.monsters

        assertThat(monsters.size).isEqualTo(1)
        assertThat(monsters[0].name).isEqualTo(m.name)
    }

    @Test
    fun `init with max monsters works, throws no expection`() {
        val bs = BattleStats(100, 100)
        val m = Monster("Peter", bs, PrimitiveType.WATER, emptyList())
        assertThatNoException().isThrownBy {
            Trainer("Alex", List(MAX_ALLOWED_MONSTERS_PER_TRAINER) { m })
        }
    }

    @Test
    fun `test init with too many monsters throws exception`() {
        val bs = BattleStats(100, 100)
        val m = Monster("Peter", bs, PrimitiveType.WATER, emptyList())

        assertThrows<IllegalArgumentException> {
            Trainer("Alex", List(MAX_ALLOWED_MONSTERS_PER_TRAINER + 1) { m })
        }
    }

    @Test
    fun `test add max monsters works`() {
        val bs = BattleStats(100, 100)
        val m = Monster("Peter", bs, PrimitiveType.WATER, emptyList())
        var t = Trainer("Alex")
        assertThatNoException().isThrownBy {
            for (i in 1..MAX_ALLOWED_MONSTERS_PER_TRAINER) {
                t = t.addMonster(m)
            }
        }
        assertThat(t.monsters.size).isEqualTo(MAX_ALLOWED_MONSTERS_PER_TRAINER)
    }

    @Test
    fun `cant add more than max monsters`() {
        val bs = BattleStats(100, 100)
        val m = Monster("Peter", bs, PrimitiveType.WATER, emptyList())
        var t = Trainer("Alex")
        assertThrows<IllegalArgumentException> {
            for (i in 0..MAX_ALLOWED_MONSTERS_PER_TRAINER) {
                t = t.addMonster(m)
            }
        }
        assertThat(t.monsters.size).isEqualTo(MAX_ALLOWED_MONSTERS_PER_TRAINER)
    }
}
