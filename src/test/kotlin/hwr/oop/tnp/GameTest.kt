package hwr.oop.tnp

import io.kotest.core.spec.style.AnnotationSpec
import org.assertj.core.api.Assertions.assertThat
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import java.lang.reflect.Field
import java.nio.file.Files

class GameTest : AnnotationSpec() {

    private lateinit var game: Game
    private lateinit var stdOutContent: ByteArrayOutputStream

    private fun getDataHandler(game: Game): DataHandler {
        val field: Field = game.javaClass.getDeclaredField("dataHandler")
        field.isAccessible = true
        return field.get(game) as DataHandler
    }

    @BeforeEach
    fun setup() {
        game = Game()
        stdOutContent = ByteArrayOutputStream()
        System.setOut(PrintStream(stdOutContent))

        val dh = getDataHandler(game)
        if (Files.exists(dh.rootPath)) {
            Files.walk(dh.rootPath)
                .sorted(Comparator.reverseOrder())
                .forEach { it.toFile().delete() }
        }
    }

    @Test
    fun `createTrainer saves trainer successfully`() {
        val trainerName = "Ash"
        game.createTrainer(trainerName)

        val trainer = getDataHandler(game).loadTrainer(trainerName)
        requireNotNull(trainer)
        assertThat(trainer.name).isEqualTo(trainerName)
    }

    @Test
    fun `addMonster saves monster and assigns to trainer`() {
        val trainerName = "Brock"
        game.createTrainer(trainerName)

        val monsterName = "Onix"
        val hp = 100
        val speed = 20
        val attacks = listOf("PUNCH", "DRUM")

        game.addMonster(monsterName, hp, speed, attacks, trainerName)

        val dataHandler = getDataHandler(game)

        val monster = dataHandler.loadMonster(monsterName)
        requireNotNull(monster)
        assertThat(monster.name).isEqualTo(monsterName)
        assertThat(monster.stats.hp).isEqualTo(hp)
        assertThat(monster.stats.speed).isEqualTo(speed)
        assertThat(monster.attacks.map { it.name }).isEqualTo(attacks)

        val trainer = dataHandler.loadTrainer(trainerName)
        requireNotNull(trainer)
        assertThat(trainer.getMonsters().map { it.name }).contains(monsterName)
    }

    @Test
    fun `initiateBattle returns null when trainer not found`() {
        val nonExisting = "Ghost"
        val existing = "Ash"
        game.createTrainer(existing)

        val battleId1 = game.initiateBattle(nonExisting, existing)
        assertThat(battleId1).isNull()

        val battleId2 = game.initiateBattle(existing, nonExisting)
        assertThat(battleId2).isNull()
    }

    @Test
    fun `initiateBattle catches IllegalArgumentException and prints message`() {
        // Cause IllegalArgumentException by passing empty strings (depends on your dataHandler)
        game.initiateBattle("", "")
        val output = stdOutContent.toString()
        assertThat(output).contains("Caught exception")
    }

    @Test
    fun `initiateBattle catches NullPointerException and prints message`() {
        // Another invalid call to trigger NPE or similar
        game.initiateBattle("NonExistent1", "NonExistent2")
        val output = stdOutContent.toString()
        assertThat(output).contains("Caught exception")
    }

    @Test
    fun `deleteMonster removes monster and updates trainer`() {
        val trainerName = "Misty"
        game.createTrainer(trainerName)

        val monsterName = "Staryu"
        game.addMonster(monsterName, 60, 45, listOf("SPLASH"), trainerName)

        val dataHandler = getDataHandler(game)
        val loadedMonster = dataHandler.loadMonster(monsterName)
        requireNotNull(loadedMonster)

        game.deleteMonster(monsterName)

        assertThat(dataHandler.loadMonster(monsterName)).isNull()
        val trainer = dataHandler.loadTrainer(trainerName)
        requireNotNull(trainer)
        assertThat(trainer.getMonsters().map { it.name }).doesNotContain(monsterName)
    }

    @Test
    fun `deleteMonster catches IllegalArgumentException and prints message`() {
        game.deleteMonster("") // Pass invalid monster name
        val output = stdOutContent.toString()
        assertThat(output).contains("Caught exception")
    }

    @Test
    fun `deleteTrainer removes trainer and all their monsters`() {
        val trainerName = "Erika"
        game.createTrainer(trainerName)

        val monsterName = "Tangela"
        game.addMonster(monsterName, 90, 20, listOf("LEAF_GUN"), trainerName)

        val dataHandler = getDataHandler(game)
        requireNotNull(dataHandler.loadTrainer(trainerName))
        requireNotNull(dataHandler.loadMonster(monsterName))

        game.deleteTrainer(trainerName)

        assertThat(dataHandler.loadTrainer(trainerName)).isNull()
        assertThat(dataHandler.loadMonster(monsterName)).isNull()
    }

    @Test
    fun `deleteTrainer catches IllegalArgumentException and prints message`() {
        game.deleteTrainer("") // invalid name
        val output = stdOutContent.toString()
        assertThat(output).contains("Caught exception")
    }

    @Test
    fun `performAttack prints executing attack message`() {
        game.performAttack(1, "Ash", "ThunderShock")
        val output = stdOutContent.toString()
        assertThat(output).contains("Executing attack...")
    }

    @Test
    fun `showAllBattles prints showing all battles`() {
        game.showAllBattles()
        val output = stdOutContent.toString()
        assertThat(output).contains("Showing all battles")
    }
}
