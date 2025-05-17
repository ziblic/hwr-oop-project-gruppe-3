package hwr.oop.tnp

import java.io.File
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class DataHandler(
        private val dataDirName: String = System.getenv("DATADIR")?.toString() ?: "data",
        private val isDryRun: Boolean = System.getenv("TESTING")?.toBoolean() ?: false
) : DataHandlerInterface {

        private val dataDir = File(System.getProperty("user.dir"), dataDirName)
        private val trainerFile = File(dataDir, "trainer.json")
        private val monsterFile = File(dataDir, "monster.json")
        private val battleFolder = File(dataDir, "battles")

        // SETUP SECTION ---
        init {
                if (!isDryRun) {
                        ensureDirectoryExists(dataDir)
                        ensureDirectoryExists(battleFolder)
                        ensureFileExists(trainerFile)
                        ensureFileExists(monsterFile)
                }
        }

        private fun ensureDirectoryExists(directory: File) {
                if (!directory.exists()) {
                        directory.mkdirs()
                }
        }

        private fun ensureFileExists(file: File) {
                if (!file.exists()) {
                        file.createNewFile()
                }
        }
        // END SETUP SECTION ---

        // TRAINER SECTION ---
        override fun saveTrainer(trainer: Trainer) {
                if (isDryRun) {
                        return
                }

                val trainers = loadAllTrainers().toMutableList()
                trainers.removeIf { it.name == trainer.name }
                trainers.add(trainer)
                trainerFile.writeText(Json.encodeToString(trainers))
        }

        override fun loadTrainer(trainerName: String): Trainer {
                if (isDryRun) {
                        throw Exception("This is a dry run")
                }
                return loadAllTrainers().find { it.name == trainerName }
                        ?: throw Exception("Trainer with the name $trainerName does not exist")
        }

        private fun loadAllTrainers(): List<Trainer> {
                if (trainerFile.readText().isBlank()) return emptyList<Trainer>()
                return Json.decodeFromString(trainerFile.readText())
        }
        // END TRAINER SECTION ---

        // MONSTER SECTION ---
        override fun saveMonster(monster: Monster) {
                if (isDryRun) {
                        return
                }

                val monsterList = loadAllMonster().toMutableList()
                monsterList.removeIf { it.name == monster.name }
                monsterList.add(monster)
                monsterFile.writeText(Json.encodeToString(monsterList))
        }

        override fun loadMonster(monsterName: String): Monster {
                if (isDryRun) {
                        throw Exception("This is a dry run")
                }
                return loadAllMonster().find { it.name == monsterName }
                        ?: throw Exception("Monster with the name $monsterName does not exist")
        }

        private fun loadAllMonster(): List<Monster> {
                if (monsterFile.readText().isBlank()) return emptyList<Monster>()
                return Json.decodeFromString(monsterFile.readText())
        }
        // END MONSTER SECTION ---

        // BATTLE SECTION ---
        override fun saveBattle(battle: Battle) {
                if (isDryRun) {
                        return
                }
                val battleFile = File(battleFolder, "${battle.battleId}.json")
                battleFile.writeText(Json.encodeToString(battle))
        }

        override fun loadBattle(battleId: Int): Battle {
                if (isDryRun) {
                        throw Exception("This is a dry run")
                }

                val battleFile = File(battleFolder, "$battleId.json")
                if (!battleFile.exists()) {
                        throw Exception("Could not find battle with id: $battleId.")
                }
                return Json.decodeFromString<Battle>(battleFile.readText())
        }

        companion object {
                fun getNextBattleId(battleFolder: File): Int {
                        val battleFiles =
                                battleFolder.listFiles { file -> file.extension == "json" }
                        val maxId =
                                battleFiles
                                        ?.mapNotNull { file ->
                                                file.nameWithoutExtension.toIntOrNull()
                                        }
                                        ?.maxOrNull()
                                        ?: 0

                        return maxId + 1
                }
        }
        // END BATTLE SECTION ---
}
