package hwr.oop.tnp

import java.io.File
import java.net.IDN

class GameLoader {

    private val saveFile = File(System.getProperty("user.dir"), "save_file.json")
    // TODO: Use JSON later
    lateinit var saveData: Set<Int>


    fun saveMonster(monster: Monster){

    }

    fun loadMonster(monsterName: String) : Monster{

    }

    fun saveTrainer(trainer: Trainer) {

    }
    fun loadTrainer(trainerName: String) {

    }
    fun loadBattle(battleID: Int) : Battle{

    }
    fun saveBattle(trainerName1: String, trainerName2: String) {

    }
    fun saveBattle(battleID: Int) {

    }

}
