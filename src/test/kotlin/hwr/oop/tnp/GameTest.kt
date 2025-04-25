package hwr.oop.tnp

import io.kotest.core.spec.style.AnnotationSpec
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.assertThrows
import java.io.File

class GameTest : AnnotationSpec(){
    @Test
    fun `createTrainer creates correct trainer JSON`() {
        val trainerName = "Ash"
        val trainerFile = File("data/trainers/$trainerName.json")

        // clean up before test
        trainerFile.delete()

        Game().createTrainer(trainerName)

        assertThat(trainerFile.exists()).isTrue()

        val expectedJson = """
        {
            "name": "$trainerName",
            "monsters": []
        }
    """.trimIndent()

        assertThat(trainerFile.readText().trim()).isEqualTo(expectedJson)
    }


    @Test
    fun `addMonster creates correct monster JSON and updates trainer`() {
        val trainerName = "Misty"
        val monsterName = "Staryu"
        val monsterFile = File("data/monsters/$monsterName.json")
        val trainerFile = File("data/trainers/$trainerName.json")

        // Cleanup
        monsterFile.delete()
        trainerFile.delete()
        Game().createTrainer(trainerName)

        Game().addMonster(monsterName, 100, 40, 30, 60, 50, "WaterGun", trainerName)

        // Assert monster file
        assertThat(monsterFile.exists()).isTrue()
        val expectedMonsterJson = """
        {
            "name": "$monsterName",
            "hp": 100,
            "attack": 40,
            "defense": 30,
            "specialAttack": 60,
            "specialDefense": 50,
            "attackName": "WaterGun",
            "trainer": "$trainerName"
        }
    """.trimIndent()
        assertThat(monsterFile.readText().trim()).isEqualTo(expectedMonsterJson)

        // Assert trainer file updated
        val expectedTrainerJson = """
        {
            "name": "$trainerName",
            "monsters": ["$monsterName"]
        }
    """.trimIndent()
        assertThat(trainerFile.readText().trim()).isEqualTo(expectedTrainerJson)
    }

    @Test
    fun `addMonster fails when trainer does not exist`() {
        val trainerName = "Brock"
        val monsterName = "Onix"
        val monsterFile = File("data/monsters/$monsterName.json")
        val trainerFile = File("data/trainers/$trainerName.json")

        // Cleanup
        monsterFile.delete()
        trainerFile.delete()  // Ensure trainer does NOT exist

        // Attempt to add monster
        val exception = assertThrows<IllegalArgumentException> {
            Game().addMonster(monsterName, 120, 55, 45, 20, 35, "RockThrow", trainerName)
        }

        assertThat(exception.message).contains("Trainer $trainerName does not exist")
        assertThat(monsterFile.exists()).isFalse()
    }


}
