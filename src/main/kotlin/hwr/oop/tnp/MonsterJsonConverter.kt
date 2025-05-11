package hwr.oop.tnp

import org.json.JSONArray
import org.json.JSONObject

object MonsterJsonConverter {
    fun toJson(monster: Monster): JSONObject {
        val statsJson = JSONObject()
            .put("hp", monster.stats.hp)
            .put("speed", monster.stats.speed)

        val attackArray = JSONArray(monster.attacks.map { it.name })

        return JSONObject()
            .put("name", monster.name)
            .put("type", monster.type.name)
            .put("stats", statsJson)
            .put("attacks", attackArray)
    }
    fun fromJson(monsterJson: JSONObject): Monster {
        val statsJson = monsterJson.getJSONObject("stats")
        val stats = BattleStats(
            hp = statsJson.getInt("hp"),
            speed = statsJson.getInt("speed")
        )

        val attacksJsonArray = monsterJson.optJSONArray("attacks") ?: JSONArray()
        val attacks = mutableListOf<Attack>()
        for (i in 0 until attacksJsonArray.length()) {
            val attackName = attacksJsonArray.optString(i, null)
            try {
                if (attackName != null) {
                    val attack = enumValueOf<Attack>(attackName)
                    attacks.add(attack)
                }
            } catch (e: IllegalArgumentException) {
                println("⚠️ Invalid attack name in JSON: $attackName")
            }
        }

        return Monster(
            name = monsterJson.getString("name"),
            stats = stats,
            type = enumValueOf<Type>(monsterJson.getString("type")),
            attacks = attacks
        )
    }
}