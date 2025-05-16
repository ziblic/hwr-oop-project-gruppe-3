package hwr.oop.tnp

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.extensions.system.captureStandardOut
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.assertThrows
import sun.java2d.cmm.ProfileDataVerifier.verify
import java.io.File
import kotlin.io.path.createTempDirectory

class DataHandlerTest : AnnotationSpec() {

    private lateinit var tempDir: File
    private lateinit var dataHandler: DataHandler

    @BeforeEach
    fun setup() {
        tempDir = createTempDirectory().toFile()
        dataHandler = DataHandler(tempDir.absolutePath)
    }

    @AfterEach
    fun cleanup() {
        tempDir.deleteRecursively()
    }

    @Test
    fun `save and load trainer`() {
        val trainerName = "Ash"
        dataHandler.saveTrainer(trainerName)

        val trainer = dataHandler.loadTrainer(trainerName)

        assertThat(trainer).isNotNull
        assertThat(trainer?.name).isEqualTo(trainerName)
    }

    @Test
    fun `save monster and load it correctly`() {
        val trainerName = "Brock"
        dataHandler.saveTrainer(trainerName)

        val monsterName = "Onix"
        val attacks = listOf("PUNCH", "DRUM")
        dataHandler.saveMonster(monsterName, 120, 30, attacks, trainerName)

        val monster = dataHandler.loadMonster(monsterName)

        assertThat(monster).isNotNull
        assertThat(monster!!.name).isEqualTo(monsterName)
        assertThat(monster.stats.hp).isEqualTo(120)
        assertThat(monster.stats.speed).isEqualTo(30)
        assertThat(monster.attacks.map { it.name }).containsExactlyElementsOf(attacks)
        assertThat(monster.type).isEqualTo(Attack.valueOf(attacks.first()).type)
    }

    @Test
    fun `save monster adds it to trainer`() {
        val trainerName = "Misty"
        dataHandler.saveTrainer(trainerName)

        val monsterName = "Staryu"
        dataHandler.saveMonster(monsterName, 60, 45, listOf("SPLASH"), trainerName)

        val trainer = dataHandler.loadTrainer(trainerName)

        assertThat(trainer).isNotNull
        assertThat(trainer!!.getMonsters().map { it.name }).contains(monsterName)
    }

    @Test
    fun `create and load battle with trainers having no monsters`() {
        val t1 = Trainer("Red")
        val t2 = Trainer("Blue")

        dataHandler.saveTrainer(t1)
        dataHandler.saveTrainer(t2)


        val battle = dataHandler.createBattle(t1, t2)
        val output = captureStandardOut {dataHandler.createBattle(t1, t2) }.trim()

        assertThat(battle).isNull()
        assertThat(output).contains("Battle that contains trainers with no monsters could not be created")



    }

    @Test
    fun `delete trainer deletes associated monsters`() {
        val trainerName = "Erika"
        dataHandler.saveTrainer(trainerName)

        val monsterName = "Tangela"
        dataHandler.saveMonster(monsterName, 90, 20, listOf("LEAF_GUN"), trainerName)

        dataHandler.deleteTrainer(trainerName)

        val trainer = dataHandler.loadTrainer(trainerName)
        val monster = dataHandler.loadMonster(monsterName)

        assertThat(trainer).isNull()
        assertThat(monster).isNull()
    }

    @Test
    fun `delete monster removes it from trainer`() {
        val trainerName = "LtSurge"
        dataHandler.saveTrainer(trainerName)

        val monsterName = "Voltorb"
        dataHandler.saveMonster(monsterName, 50, 90, listOf("SPARK_TAIL"), trainerName)

        dataHandler.deleteMonster(monsterName)

        val monster = dataHandler.loadMonster(monsterName)
        val trainer = dataHandler.loadTrainer(trainerName)

        assertThat(monster).isNull()
        assertThat(trainer!!.getMonsters().map { it.name }).doesNotContain(monsterName)
    }

    @Test
    fun `createBattle throws when trainer not found`() {
        val ex = assertThrows<IllegalArgumentException> {
            dataHandler.createBattle("Ghost", "Phantom")
        }
        assertThat(ex.message).contains("Trainer 'Ghost' not found")
    }

    @Test
    fun `saveTrainer(trainer) saves the trainer so it can be loaded`() {
        val trainer = Trainer("Gary")
        dataHandler.saveTrainer(trainer)

        val loadedTrainer = dataHandler.loadTrainer(trainer.name)

        assertThat(loadedTrainer).isNotNull
        assertThat(loadedTrainer!!.name).isEqualTo(trainer.name)
    }

    @Test
    fun `saveBattle persists the battle data correctly`() {
        var trainer1 = Trainer("TrainerOne")
        var trainer2 = Trainer("TrainerTwo")

        dataHandler.saveTrainer(trainer1)
        dataHandler.saveTrainer(trainer2)

        dataHandler.saveMonster("MonsterA", 100, 50, listOf("DEEP_SEA_GRIP"), trainer1.name)
        dataHandler.saveMonster("MonsterB", 90, 40, listOf("FLAME_WREATH"), trainer2.name)

        // Load updated trainers with their monsters
        trainer1  = dataHandler.loadTrainer(trainer1.name)!!
        trainer2  = dataHandler.loadTrainer(trainer2.name)!!

        val battle = dataHandler.createBattle(trainer1, trainer2)
        assertThat(battle).isNotNull

        dataHandler.saveBattle(battle!!)

        val loadedBattle = dataHandler.loadBattle(battle.getBattleId())
        assertThat(loadedBattle).isNotNull
        assertThat(loadedBattle.getBattleId()).isEqualTo(battle.getBattleId())
        assertThat(loadedBattle.trainerOne.name).isEqualTo(trainer1.name)
        assertThat(loadedBattle.trainerTwo.name).isEqualTo(trainer2.name)
    }


    @Test
    fun `deleteTrainer prints message when null trainer is passed`() {
        val output = captureStandardOut {
            dataHandler.deleteTrainer(null)
        }

        assertThat(output).contains("No trainer provided.")
    }

    @Test
    fun `deleteMonster removes monster from all trainers and prints message`() {
        val trainerName = "Brock"
        dataHandler.saveTrainer(trainerName)

        val monsterName = "Geodude"
        dataHandler.saveMonster(monsterName, 100, 20, listOf("SPARK_TAIL"), trainerName)

        // Confirm monster is linked to trainer before deletion
        var trainer = dataHandler.loadTrainer(trainerName)
        assertThat(trainer?.getMonsters()?.map { it.name }).contains(monsterName)

        val output = captureStandardOut {
            dataHandler.deleteMonster(monsterName)
        }

        // After deletion, monster should be removed from trainer's monsters list
        trainer = dataHandler.loadTrainer(trainerName)
        assertThat(trainer?.getMonsters()?.map { it.name }).doesNotContain(monsterName)

        assertThat(output).contains("References to '$monsterName' removed from all trainers.")
    }

    @Test
    fun `createBattle by trainer names returns non-null battle when trainers have monsters`() {
        val t1Name = "Red"
        val t2Name = "Blue"

        // Save trainers
        dataHandler.saveTrainer(t1Name)
        dataHandler.saveTrainer(t2Name)

        // Add monsters to trainers (required for battle creation)
        dataHandler.saveMonster("Pikachu", 100, 50, listOf("DRUM"), t1Name)
        dataHandler.saveMonster("Bulbasaur", 90, 40, listOf("GROUND_HAMMER"), t2Name)

        val battle = dataHandler.createBattle(t1Name, t2Name)

        assertThat(battle).isNotNull
    }


    @Test
    fun `createBattle by trainer objects returns non-null battle when trainers have monsters`() {
        val t1Name = "Red"
        val t2Name = "Blue"

        // Save trainers and monsters
        dataHandler.saveTrainer(t1Name)
        dataHandler.saveTrainer(t2Name)

        dataHandler.saveMonster("Pikachu", 100, 50, listOf("SPARK_TAIL"), t1Name)
        dataHandler.saveMonster("Bulbasaur", 90, 40, listOf("LAVA_FLOOD"), t2Name)

        // Load trainers (now guaranteed to have monsters)
        val t1 = requireNotNull(dataHandler.loadTrainer(t1Name))
        val t2 = requireNotNull(dataHandler.loadTrainer(t2Name))

        val battle = dataHandler.createBattle(t1, t2)

        assertThat(battle).isNotNull
    }

    @Test
    fun `createBattle throws if second trainer not found`() {
        val existingTrainerName = "Red"
        val missingTrainerName = "Ghost"

        // Save only the first trainer
        dataHandler.saveTrainer(existingTrainerName)

        val ex = assertThrows<IllegalArgumentException> {
            dataHandler.createBattle(existingTrainerName, missingTrainerName)
        }

        assertThat(ex.message).contains("Trainer '$missingTrainerName' not found.")
    }










}
