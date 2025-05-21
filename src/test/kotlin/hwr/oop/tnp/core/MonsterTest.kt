package hwr.oop.tnp.core

import io.kotest.core.spec.style.AnnotationSpec
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.assertThrows
import kotlin.collections.listOf

class MonsterTest : AnnotationSpec() {
    private val stats = BattleStats(200, 20)
    private val monstertype = Type.WATER
    private val attacks = listOf(Attack.PUNCH, Attack.DRUM)

    private val monster =
        Monster(
            "Kevin",
            stats,
            monstertype,
            attacks = attacks,
        )

    @Test
    fun `check serializable`() {
        val jsonMonster = Json.encodeToString(monster)
        assertThat(jsonMonster)
            .isEqualTo(
                "{\"name\":\"Kevin\",\"stats\":${Json.encodeToString(stats)},\"type\":\"WATER\",\"attacks\":[\"PUNCH\",\"DRUM\"]}"
            )
        val decodedMonster = Json.decodeFromString<Monster>(jsonMonster)
        assertThat(monster.name).isEqualTo(decodedMonster.name)
        assertThat(monster.stats).isEqualTo(decodedMonster.stats)
        assertThat(monster.type).isEqualTo(decodedMonster.type)
        assertThat(monster.attacks).isEqualTo(decodedMonster.attacks)
    }

    @Test
    fun `wrong Json throws JsonDecodingException`() {
        assertThrows<SerializationException> {
            Json.decodeFromString<Monster>(
                "{\"name\":\"Kevin\",\"battlestats\":{\"maxHp\":0,\"hp\":0,\"speed\":20},\"type\":\"WATER\",\"attacks\":[\"PUNCH\",\"DRUM\"]}"
            )
        }
        assertThrows<SerializationException> {
            Json.decodeFromString<Monster>(
                "{\"name\":\"Kevin\",\"stats\":{\"hp\":\"abc\",\"speed\":20},\"type\":\"WATER\",\"attacks\":[\"PUNCH\",\"DRUM\"]}"
            )
        }
    }

    @Test
    fun `check if Monster is not KO`() {
        val monster =
            Monster(
                "Bob",
                BattleStats(200, 20),
                Type.FIRE,
                attacks = listOf(Attack.DEEP_SEA_GRIP, Attack.LAVA_FLOOD),
            )
        assertThat(monster.isKO()).isFalse()
    }

    @Test
    fun `Monster Kevin has name Kevin`() {
        assertThat(monster.name).isEqualTo("Kevin")
    }

    @Test
    fun `Monster has correct Stats `() {
        assertThat(monster.stats.hp).isEqualTo(stats.hp)
        assertThat(monster.stats.speed).isEqualTo(stats.speed)
    }

    @Test
    fun `Monster with Type Wasser has Type Wasser`() {
        assertThat(monster.type).isEqualTo(monstertype)
    }

    @Test
    fun `Monster with attacks X has attacks X`() {
        assertThat(monster.attacks).isEqualTo(attacks)
    }

    @Test
    fun `Monster does not have attack`() {
        val otherMonster =
            Monster(
                "Kevin",
                BattleStats(200, 20),
                Type.FIRE,
                attacks = listOf(Attack.DEEP_SEA_GRIP, Attack.LAVA_FLOOD),
            )
        assertThrows<IllegalArgumentException> {
            monster.attack(Attack.SPLASH, otherMonster)
        }
    }
    @Test
    fun `throws exception when attack is not part of the monster's attacks`() {
        val monster =
            Monster(
                "Kevin",
                BattleStats(100, 100),
                Type.FIRE,
                listOf(Attack.DEEP_SEA_GRIP)
            )
        val invalidAttack = Attack.LEAF_GUN
        val otherMonster = Monster("Bob", BattleStats(100, 100), Type.PLANT, listOf())

        assertThrows<IllegalArgumentException> {
            monster.attack(invalidAttack, otherMonster)
        }
    }

    @Test
    fun `calculates damage correctly with effective type`() {
        val fireAttack = Attack.LAVA_FLOOD
        val fireMonster =
            Monster("Kevin", BattleStats(100, 100), Type.FIRE, listOf(fireAttack))
        val plantMonster =
            Monster(
                "Bob",
                BattleStats(Attack.LAVA_FLOOD.damage * 2 + 1000, 200),
                Type.PLANT,
                listOf()
            )

        fireMonster.attack(fireAttack, plantMonster)
        assert(
            plantMonster.stats.hp == 1000 ||
                plantMonster.stats.hp == 1000 - Attack.LAVA_FLOOD.damage
        )
    }

    @Test
    fun `calculates damage correctly with less effective type`() {
        val fireAttack = Attack.LAVA_FLOOD
        val fireMonster =
            Monster("Kevin", BattleStats(100, 100), Type.FIRE, listOf(fireAttack))
        val waterMonster =
            Monster(
                "Bob",
                BattleStats((Attack.LAVA_FLOOD.damage * 0.5 + 1000).toInt(), 100),
                Type.WATER,
                listOf()
            )

        fireMonster.attack(fireAttack, waterMonster)
        assert(
            waterMonster.stats.hp == 1000 ||
                waterMonster.stats.hp ==
                (1000 - Attack.LAVA_FLOOD.damage * 0.25).toInt()
        )
    }

    @Test
    fun `calculates damage correctly with none effecting types`() {
        val normalAttack = Attack.PUNCH
        val normalMonster =
            Monster("Kevin", BattleStats(100, 100), Type.NORMAL, listOf(normalAttack))
        val spiritMonster = Monster("Bob", BattleStats(100, 100), Type.SPIRIT, listOf())

        normalMonster.attack(normalAttack, spiritMonster)
        assert(spiritMonster.stats.hp == 100)
    }

    @Test
    fun `calculates damage correctly with default effective type`() {
        val fireAttack = Attack.LAVA_FLOOD
        val fireMonster =
            Monster("Kevin", BattleStats(100, 100), Type.FIRE, listOf(fireAttack))
        val normalMonster =
            Monster(
                "Bob",
                BattleStats(Attack.LAVA_FLOOD.damage + 1000, 100),
                Type.NORMAL,
                listOf()
            )

        fireMonster.attack(fireAttack, normalMonster)
        assert(
            normalMonster.stats.hp == 1000 ||
                normalMonster.stats.hp ==
                (1000 - Attack.LAVA_FLOOD.damage * 0.5).toInt()
        )
    }
}
