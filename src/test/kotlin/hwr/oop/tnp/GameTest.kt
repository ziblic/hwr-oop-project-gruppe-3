package hwr.oop.tnp
//
//import io.kotest.core.spec.style.AnnotationSpec
//import org.assertj.core.api.Assertions.assertThat
//import org.json.JSONObject
//import org.junit.jupiter.api.assertThrows
//import java.io.ByteArrayOutputStream
//import java.io.File
//import java.io.PrintStream
//
//class GameTest : AnnotationSpec() {
//
//    private val trainer1 = "Ash"
//    private val trainer2 = "Misty"
//    private val monster1 = "Pikachu"
//    private val monster2 = "Staryu"
//    private val battleId = "battle-1714057289212"
//
//    private lateinit var trainer1File: File
//    private lateinit var trainer2File: File
//    private lateinit var monster1File: File
//    private lateinit var monster2File: File
//    private lateinit var battleFile: File
//
//    @BeforeEach
//    fun setup() {
//        // Set up file paths
//        trainer1File = File("data/trainers/$trainer1.json")
//        trainer2File = File("data/trainers/$trainer2.json")
//        monster1File = File("data/monsters/$monster1.json")
//        monster2File = File("data/monsters/$monster2.json")
//        battleFile = File("data/battles/$battleId.json")
//
//        // Cleanup old files before each test
//        trainer1File.delete()
//        trainer2File.delete()
//        monster1File.delete()
//        monster2File.delete()
//        battleFile.delete()
//
//        // Create fake trainer JSON data
//        val trainer1Json = """
//            {
//                "name": "$trainer1",
//                "monsters": ["$monster1"]
//            }
//        """.trimIndent()
//        val trainer2Json = """
//            {
//                "name": "$trainer2",
//                "monsters": ["$monster2"]
//            }
//        """.trimIndent()
//
//        // Create trainer files
//        trainer1File.writeText(trainer1Json)
//        trainer2File.writeText(trainer2Json)
//
//        // Create fake monster JSON data
//        val pikachuJson = """
//            {
//                "name": "$monster1",
//                "hp": 100,
//                "attack": 50,
//                "defense": 30,
//                "specialAttack": 40,
//                "specialDefense": 35,
//                "attackName": "Thunderbolt"
//            }
//        """.trimIndent()
//        val staryuJson = """
//            {
//                "name": "$monster2",
//                "hp": 80,
//                "attack": 45,
//                "defense": 50,
//                "specialAttack": 55,
//                "specialDefense": 40,
//                "attackName": "Water Gun"
//            }
//        """.trimIndent()
//
//        // Create monster files
//        monster1File.writeText(pikachuJson)
//        monster2File.writeText(staryuJson)
//    }
//
//    @Test
//    fun `createTrainer creates correct trainer JSON`() {
//        val trainerName = "Ash"
//        val trainerFile = File("data/trainers/$trainerName.json")
//
//        // clean up before test
//        trainerFile.delete()
//
//        Game().createTrainer(trainerName)
//
//        assertThat(trainerFile.exists()).isTrue()
//
//        val expectedJson = """
//            {
//                "name": "$trainerName",
//                "monsters": []
//            }
//        """.trimIndent()
//
//        assertThat(trainerFile.readText().trim()).isEqualTo(expectedJson)
//    }
//
//    @Test
//    fun `addMonster creates correct monster JSON and updates trainer`() {
//        val trainerName = "Misty"
//        val monsterName = "Staryu"
//        val monsterFile = File("data/monsters/$monsterName.json")
//        val trainerFile = File("data/trainers/$trainerName.json")
//
//        // Cleanup
//        monsterFile.delete()
//        trainerFile.delete()
//        Game().createTrainer(trainerName)
//
//        Game().addMonster(monsterName, 100, 40, listOf("WaterGun"), trainerName)
//
//        // Assert monster file
//        assertThat(monsterFile.exists()).isTrue()
//        val expectedMonsterJson = """
//            {
//                "name": "$monsterName",
//                "hp": 100,
//                "attack": 40,
//                "defense": 30,
//                "specialAttack": 60,
//                "specialDefense": 50,
//                "attackName": "WaterGun",
//                "trainer": "$trainerName"
//            }
//        """.trimIndent()
//        assertThat(monsterFile.readText().trim()).isEqualTo(expectedMonsterJson)
//
//        // Assert trainer file updated
//        val expectedTrainerJson = """
//            {
//                "name": "$trainerName",
//                "monsters": ["$monsterName"]
//            }
//        """.trimIndent()
//        assertThat(trainerFile.readText().trim()).isEqualTo(expectedTrainerJson)
//    }
//
//    @Test
//    fun `addMonster fails when trainer does not exist`() {
//        val trainerName = "Brock"
//        val monsterName = "Onix"
//        val monsterFile = File("data/monsters/$monsterName.json")
//        val trainerFile = File("data/trainers/$trainerName.json")
//
//        // Cleanup
//        monsterFile.delete()
//        trainerFile.delete() // Ensure trainer does NOT exist
//
//        // Attempt to add monster
//        val exception = assertThrows<IllegalArgumentException> {
//            Game().addMonster(monsterName, 120, 55, 45, 20, 35, "RockThrow", trainerName)
//        }
//
//        assertThat(exception.message).contains("Trainer $trainerName does not exist")
//        assertThat(monsterFile.exists()).isFalse()
//    }
//
//    @Test
//    fun `initiateBattle saves the battle file correctly`() {
//        // Initialize the battle
//        val battle = Game()
//        battle.initiateBattle(trainer1, trainer2)
//
//        // Verify the battle file exists
//        assertThat(battleFile.exists()).isTrue()
//
//        // Read the battle JSON
//        val battleJson = JSONObject(battleFile.readText())
//
//        // Check battle structure
//        assertThat(battleJson.optString("status")).isEqualTo("initiated")
//        assertThat(battleJson.optJSONArray("trainers")).isNotNull
//
//        val trainers = battleJson.optJSONArray("trainers")
//        assertThat(trainers?.length()).isEqualTo(2)
//
//        // Verify trainer data
//        val trainer1Data = trainers?.getJSONObject(0)
//        assertThat(trainer1Data?.getString("name")).isEqualTo(trainer1)
//
//        val trainer2Data = trainers?.getJSONObject(1)
//        assertThat(trainer2Data?.getString("name")).isEqualTo(trainer2)
//
//        // Verify that the trainers have monsters
//        val trainer1Monsters = trainer1Data?.optJSONArray("monsters")
//        assertThat(trainer1Monsters?.length()).isEqualTo(1)
//        assertThat(trainer1Monsters?.getJSONObject(0)?.getString("name")).isEqualTo("Pikachu")
//
//        val trainer2Monsters = trainer2Data?.optJSONArray("monsters")
//        assertThat(trainer2Monsters?.length()).isEqualTo(1)
//        assertThat(trainer2Monsters?.getJSONObject(0)?.getString("name")).isEqualTo("Staryu")
//    }
//
//    @Test
//    fun `viewStatus displays the correct battle status and trainer data`() {
//        // Test viewStatus output by capturing console output
//        val outputStream = ByteArrayOutputStream()
//        System.setOut(PrintStream(outputStream))
//
//        val game = Game()
//        game.viewStatus(battleId)
//
//        // Assertions to verify the printed output
//        val output = outputStream.toString()
//
//        assertThat(output).contains("📊 Status of Battle '$battleId':")
//        assertThat(output).contains("Status: initiated")
//        assertThat(output).contains("Trainer: Ash")
//        assertThat(output).contains("🧟 Monster: Pikachu | HP: 100 | Attack: Thunderbolt")
//        assertThat(output).contains("Trainer: Misty")
//        assertThat(output).contains("🧟 Monster: Staryu | HP: 80 | Attack: Water Gun")
//    }
//}
