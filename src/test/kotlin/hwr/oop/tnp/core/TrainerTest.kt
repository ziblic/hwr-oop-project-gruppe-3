package hwr.oop.tnp.core

import io.kotest.core.spec.style.AnnotationSpec
import kotlinx.serialization.json.Json
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy

class TrainerTest : AnnotationSpec() {
  private val maxAllowedMonstersPerTrainer = 6
  private val bs = BattleStats(100, 100, 20, 20, 20, 20)
  private val monster = Monster("Peter", bs, PrimitiveType.WATER, emptyList())

  @Test
  fun `check serializable`() {
    val trainer = Trainer("Alex", mutableListOf(monster))
    val trainerJson = Json.encodeToString(trainer)
    assertThat(trainerJson)
      .isEqualTo(
        "{\"name\":\"Alex\",\"monsters\":[{\"name\":\"Peter\",\"stats\":{\"maxHp\":100,\"hp\":100,\"speed\":100,\"attack\":20,\"specialAttack\":20,\"defense\":20,\"specialDefense\":20},\"primitiveType\":\"WATER\",\"attacks\":[]}]}",
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
    val trainer = Trainer("Alex", mutableListOf(monster))
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
    var trainer = Trainer("Alex", mutableListOf(monster))
    val bs = BattleStats(130, 80, 20, 20, 20, 20)
    val m = Monster("Peter", bs, PrimitiveType.WATER, emptyList())

    trainer.addMonster(m)
    val monsters = trainer.monsters

    assertThat(monsters.size).isEqualTo(2)
    assertThat(monsters[1].name).isEqualTo(m.name)
  }

  @Test
  fun `init with max monsters works, throws no expection`() {
    Trainer("Alex", MutableList(maxAllowedMonstersPerTrainer) { monster })
  }

  @Test
  fun `init with too many monsters throws exception`() {
    assertThatThrownBy {
      Trainer("Alex", MutableList(maxAllowedMonstersPerTrainer + 1) { monster })
    }.hasMessageContaining("Too many monsters:")
  }

  @Test
  fun `add max monsters works`() {
    var trainer = Trainer("Peter")
    for (i in 1..maxAllowedMonstersPerTrainer) {
      trainer.addMonster(monster)
    }
    assertThat(trainer.monsters.size).isEqualTo(maxAllowedMonstersPerTrainer)
  }

  @Test
  fun `adding too many monsters throws IllegalArgumentException`() {
    var trainer = Trainer("Alex")

    repeat(maxAllowedMonstersPerTrainer) { trainer.addMonster(monster) }

    assertThatThrownBy { trainer.addMonster(monster) }
      .hasMessage("Too many monsters: maximum allowed is $maxAllowedMonstersPerTrainer")
    assertThat(trainer.monsters.size).isEqualTo(maxAllowedMonstersPerTrainer)
  }

  @Test
  fun `trainer has next monster`() {
    val trainer = Trainer("Alex", mutableListOf(monster))
    assertThat(trainer.nextMonster()).isEqualTo(monster)
  }

  @Test
  fun `next monster throws IllegalStateException if no monsters left`() {
    val trainer = Trainer("Alex")
    assertThatThrownBy { trainer.nextMonster() }.hasMessage("No monsters available")
  }

  @Test
  fun `trainer has next battle ready monster`() {
    val trainer = Trainer("Alex", mutableListOf(monster))
    assertThat(trainer.nextBattleReadyMonster()).isEqualTo(monster)
  }

  @Test
  fun `next battle ready monster returns null if no monsters left`() {
    val trainer = Trainer("Alex")
    assertThatThrownBy { trainer.nextBattleReadyMonster() }
      .hasMessage("No monster is alive anymore")
  }

  @Test
  fun `next battle ready monster returns null if monster is KO`() {
    val monster =
      Monster("Peter", BattleStats(0, 20, 20, 20, 20, 20), PrimitiveType.WATER, emptyList())
    val trainer = Trainer("Alex", mutableListOf(monster))
    assertThatThrownBy { trainer.nextBattleReadyMonster() }
      .hasMessage("No monster is alive anymore")
  }

  @Test
  fun `trainer is not defeated if at least one monster is alive`() {
    val trainer = Trainer("Alex", mutableListOf(monster))
    assertThat(trainer.isDefeated()).isFalse
  }

  @Test
  fun `trainer is defeated if all monsters are KO`() {
    val bs = BattleStats(0, 20, 20, 20, 20, 20)
    val monster = Monster("Peter", bs, PrimitiveType.WATER, emptyList())
    val trainer = Trainer("Alex", mutableListOf(monster))
    assertThat(trainer.isDefeated()).isTrue
  }
}
