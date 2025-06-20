package hwr.oop.tnp.cli

import hwr.oop.tnp.core.Attack
import hwr.oop.tnp.core.Battle
import hwr.oop.tnp.core.BattleStatus
import hwr.oop.tnp.core.DamageStrategy
import hwr.oop.tnp.core.PrimitiveType
import hwr.oop.tnp.persistency.FileSystemBasedJsonPersistence
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.extensions.system.captureStandardOut
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import java.io.File

class BattleCliAdapterTest : AnnotationSpec() {
  @AfterEach
  fun cleanUp() {
    File(System.getProperty("user.dir"), "data/1.json").delete()
  }

  @Test
  fun `Test adding three trainer prints exception message`() {
    val saveAdapter = FileSystemBasedJsonPersistence()
    val battle = Battle("1", DamageStrategy.DETERMINISTIC)
    saveAdapter.saveBattle(battle)
    val battleAdapter = BattleCliAdapter("1")
    battleAdapter.createTrainer("Kevin")
    battleAdapter.createTrainer("Bob")
    val output =
      captureStandardOut { battleAdapter.createTrainer("Ash") }.trim()
    assertThat(output).isEqualTo("Both trainers are already set.")
  }

  @Test
  fun `Trying to add a monster to a not existing trainer prints exception message`() {
    val saveAdapter = FileSystemBasedJsonPersistence()
    val battle = Battle("1", DamageStrategy.DETERMINISTIC)
    saveAdapter.saveBattle(battle)
    val output =
      captureStandardOut {
        BattleCliAdapter("1")
          .addMonster(
            "Pika",
            100,
            100,
            20,
            20,
            20,
            20,
            PrimitiveType.FIRE,
            listOf(Attack.PUNCH),
            "Bob"
          )
      }
        .trim()
    assertThat(output).isEqualTo("Trainer 'Bob' not found.")
  }

  @Test
  fun `View status prints the right output`() {
    val saveAdapter = FileSystemBasedJsonPersistence()
    val battle = Battle("1", DamageStrategy.DETERMINISTIC)
    saveAdapter.saveBattle(battle)
    val output =
      captureStandardOut { BattleCliAdapter("1").viewStatus() }.trim()
    assertThat(output).isEqualTo("Battle with ID: 1 is in a pregame state")
  }

  @Test
  fun `For showAllBattles right output is printed`() {
    val dataFolder = File(System.getProperty("user.dir"), "data/")
    dataFolder.mkdir()

    // 1. Load all battles into a list
    val loadAdapter = FileSystemBasedJsonPersistence()
    val saveAdapter = loadAdapter
    val battles = loadAdapter.loadAllBattles()

    // 2. Delete all battle JSON files
    val deletedFiles =
      dataFolder.listFiles { f -> f.name.endsWith(".json") }?.toList()
        ?: emptyList()
    deletedFiles.forEach { it.delete() }

    // 3. Run showAllBattles and capture output
    val output = captureStandardOut { BattleCliAdapter.showAllBattles() }.trim()

    // 4. Assert output is correct (no battles)
    assertThat(output).isEqualTo("No battles created yet")

    // 5. Create a battle and check the output
    saveAdapter.saveBattle(Battle("1", DamageStrategy.DETERMINISTIC))
    val output2 = captureStandardOut { BattleCliAdapter.showAllBattles() }.trim()
    assertThat(
      output2,
    ).isEqualTo("Battle with ID: 1 is in a pregame state")

    // 6. Recreate all battles (re-save them)
    battles.forEach { saveAdapter.saveBattle(it) }
  }

  @Test
  fun `Test performAttack fully`() {
    val saveAdapter = FileSystemBasedJsonPersistence()
    val loadAdapter = saveAdapter
    saveAdapter.saveBattle(Battle("1", DamageStrategy.DETERMINISTIC))
    val battleAdapter = BattleCliAdapter("1")
    val output =
      captureStandardOut { battleAdapter.performAttack(Attack.PUNCH) }.trim()
    assertThat(output).isEqualTo("Trainer need to have been set for this operation")
    battleAdapter.createTrainer("Bob")
    battleAdapter.createTrainer("Kevin")
    val output_1 =
      captureStandardOut { battleAdapter.performAttack(Attack.PUNCH) }.trim()
    assertThat(output_1).isEqualTo("No monsters available")
    battleAdapter.addMonster(
      "Pika",
      10,
      200,
      20,
      20,
      20,
      20,
      PrimitiveType.NORMAL,
      listOf(Attack.PUNCH),
      "Bob"
    )
    battleAdapter.addMonster(
      "Glurak",
      100,
      100,
      20,
      20,
      20,
      20,
      PrimitiveType.FIRE,
      listOf(Attack.PUNCH),
      "Kevin"
    )
    var battle = loadAdapter.loadBattle("1")
    assertThat(battle.status).isEqualTo(BattleStatus.PREGAME)
    battleAdapter.performAttack(Attack.PUNCH)
    battle = loadAdapter.loadBattle("1")
    assertThat(battle.status).isEqualTo(BattleStatus.STARTED)
    battleAdapter.performAttack(Attack.PUNCH)
    val output_2 =
      captureStandardOut { battleAdapter.performAttack(Attack.PUNCH) }.trim()
    assertThat(output_2).isEqualTo("Battle is already finished")
  }
}
