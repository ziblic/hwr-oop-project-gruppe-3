package hwr.oop.tnp.core

import io.kotest.core.spec.style.AnnotationSpec
import kotlinx.serialization.json.Json
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatNoException
import org.junit.jupiter.api.assertThrows

class TrainerTest : AnnotationSpec() {
    private val bs = BattleStats(100, 100)
    private val monster = Monster("Peter", bs, PrimitiveType.WATER, emptyList())

    @Test
    fun `check serializable`() {
        val trainer = Trainer("Alex", listOf(monster))
        val trainerJson = Json.encodeToString(trainer)
        assertThat(trainerJson)
            .isEqualTo(
                "{\"name\":\"Alex\",\"monsters\":[{\"name\":\"Peter\",\"stats\":{\"maxHp\":100,\"hp\":100,\"speed\":100},\"primitiveType\":\"WATER\",\"attacks\":[]}]}"
            )
        val decodedTrainer = Json.decodeFromString<Trainer>(trainerJson)
        assertThat(trainer.name).isEqualTo(decodedTrainer.name)
    }

    @Test
    fun `trainer has name`() {
        val name = "Bob"
        val trainer = Trainer(name)
        assertThat(trainer.name).isEqualTo(name)
    }

    @Test
    fun `trainer has monsters`() {
        val trainer = Trainer("Alex", listOf(monster))
        val monsters = trainer.monsters

        assertThat(monsters[0].name).isEqualTo(monster.name)
        assertThat(monsters.size).isEqualTo(1)
    }

    @Test
    fun `creating a trainer has no monsters`() {
        val trainer = Trainer("Alex")
        val monsters = trainer.monsters

        assertThat(monsters.size).isEqualTo(0)
    }

    @Test
    fun `add monster to trainer`() {
        var trainer = Trainer("Alex", listOf(monster))
        val bs = BattleStats(130, 80)
        val m = Monster("Peter", bs, PrimitiveType.WATER, emptyList())

        trainer.addMonster(m)
        val monsters = trainer.monsters

        assertThat(monsters.size).isEqualTo(2)
        assertThat(monsters[1].name).isEqualTo(m.name)
    }

    @Test
    fun `init with max monsters works, throws no expection`() {
        assertThatNoException().isThrownBy {
            Trainer("Alex", MutableList(MAX_ALLOWED_MONSTERS_PER_TRAINER) { monster })
        }
    }

    @Test
    fun `init with too many monsters throws exception`() {
        assertThrows<IllegalArgumentException> {
            Trainer(
                "Alex",
                MutableList(MAX_ALLOWED_MONSTERS_PER_TRAINER + 1) { monster }
            )
        }
    }

    @Test
    fun `add max monsters works`() {
        var trainer = Trainer("Peter")
        assertThatNoException().isThrownBy {
            for (i in 1..MAX_ALLOWED_MONSTERS_PER_TRAINER) {
                trainer.addMonster(monster)
            }
        }
        assertThat(trainer.monsters.size).isEqualTo(MAX_ALLOWED_MONSTERS_PER_TRAINER)
    }

    @Test
    fun `adding too many monsters throws IllegalArgumentException`() {
        var trainer = Trainer("Alex")

        repeat(MAX_ALLOWED_MONSTERS_PER_TRAINER) { trainer.addMonster(monster) }

        val exception =
            assertThrows<IllegalArgumentException> {
                trainer.addMonster(monster)
            }

        assertThat(exception.message)
            .isEqualTo(
                "Too many monsters: maximum allowed is $MAX_ALLOWED_MONSTERS_PER_TRAINER"
            )
        assertThat(trainer.monsters.size).isEqualTo(MAX_ALLOWED_MONSTERS_PER_TRAINER)
    }

    @Test
    fun `trainer has next monster`() {
        val trainer = Trainer("Alex", listOf(monster))
        assertThat(trainer.nextMonster()).isEqualTo(monster)
    }

    @Test
    fun `next monster throws IllegalStateException if no monsters left`() {
        val trainer = Trainer("Alex")
        assertThrows<IllegalStateException> { trainer.nextMonster() }
    }

    @Test
    fun `trainer has next battle ready monster`() {
        val trainer = Trainer("Alex", listOf(monster))
        assertThat(trainer.nextBattleReadyMonster()).isEqualTo(monster)
    }

    @Test
    fun `next battle ready monster returns null if no monsters left`() {
        val trainer = Trainer("Alex")
        assertThat(trainer.nextBattleReadyMonster()).isNull()
    }

    @Test
    fun `next battle ready monster returns null if monster is KO`() {
        val monster = Monster("Peter", BattleStats(0, 20), PrimitiveType.WATER, emptyList())
        val trainer = Trainer("Alex", mutableListOf(monster))
        assertThat(trainer.nextBattleReadyMonster()).isNull()
    }

    @Test
    fun `trainer is not defeated if at least one monster is alive`() {
        val trainer = Trainer("Alex", listOf(monster))
        assertThat(trainer.isDefeated()).isFalse
    }

    @Test
    fun `trainer is defeated if all monsters are KO`() {
        val bs = BattleStats(0, 20)
        val monster = Monster("Peter", bs, PrimitiveType.WATER, emptyList())
        val trainer = Trainer("Alex", mutableListOf(monster))
        assertThat(trainer.isDefeated()).isTrue
    }
}
