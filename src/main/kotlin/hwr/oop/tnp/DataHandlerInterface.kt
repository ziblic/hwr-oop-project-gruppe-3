package hwr.oop.tnp

import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileNotFoundException

interface DataHandlerInterface {
    fun saveTrainer(trainerName: String)
    fun saveTrainer(trainer: Trainer)
    fun loadTrainer(trainerName: String) : Trainer?


    fun saveMonster(monster: Monster, trainerName: String)
    fun saveMonster(
        monsterName: String,
        hp: Int,
        attacks: List<String>, // For now, we assume there is on only one attack added into a JSONArray
        trainerName: String,
    )
    fun loadMonster(monsterName: String) : Monster?


    fun saveBattle(battle: Battle)
    fun createBattle(trainer1: Trainer, trainer2: Trainer)
    fun createBattle(trainerName1: String, trainerName2: String)
    fun loadBattle(battleID: Int): Battle



}