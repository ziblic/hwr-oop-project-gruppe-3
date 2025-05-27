package hwr.oop.tnp.cli

import hwr.oop.tnp.InMemoryPersistence
import hwr.oop.tnp.core.Attack
import hwr.oop.tnp.core.Battle
import hwr.oop.tnp.core.BattleStats
import hwr.oop.tnp.core.Game
import hwr.oop.tnp.core.Monster
import hwr.oop.tnp.core.PrimitiveType
import hwr.oop.tnp.core.Trainer
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.extensions.system.captureStandardOut
import kotlinx.serialization.json.Json
import org.assertj.core.api.Assertions.assertThat

class TotallyNotPokemonTest : AnnotationSpec() {
    @Test
    fun `test parseToXXXXX with invalid input`() {
        val adapter = InMemoryPersistence()
        val output =
            captureStandardOut {
                TotallyNotPokemon(
                    listOf(
                        "add_monster",
                        "Bob",
                        "abc",
                        "abc",
                        "Fire",
                        "PUNCH",
                        "Trainer_Kevin",
                        "1"
                    ),
                    adapter
                )
            }
                .trim()
        assertThat(output).isEqualTo(COULD_NOT_PARSE_ERROR)
        val output2 =
            captureStandardOut {
                TotallyNotPokemon(
                    listOf(
                        "add_monster",
                        "Bob",
                        "100",
                        "20",
                        "Hello",
                        "PUNCH",
                        "Trainer_Kevin",
                        "1"
                    ),
                    adapter
                )
            }
                .trim()
        assertThat(output2).isEqualTo(COULD_NOT_PARSE_ERROR)
        val output3 =
            captureStandardOut {
                TotallyNotPokemon(
                    listOf(
                        "add_monster",
                        "Bob",
                        "100",
                        "20",
                        "Fire",
                        "Tackle_3",
                        "Trainer_Kevin",
                        "1"
                    ),
                    adapter
                )
            }
                .trim()
        assertThat(output3).isEqualTo(COULD_NOT_PARSE_ERROR)
    }

    @Test
    fun `Run invalid command`() {
        val adapter = InMemoryPersistence()
        val output =
            captureStandardOut {
                TotallyNotPokemon(listOf("some_unknown_command"), adapter)
            }
                .trim()
        assertThat(output)
            .isEqualTo(
                "'some_unknown_command' is not a valid command. Use 'help' for usage."
            )
    }

    @Test
    fun `Run without any command`() {
        val adapter = InMemoryPersistence()
        val output = captureStandardOut { TotallyNotPokemon(listOf(), adapter) }.trim()
        assertThat(output).isEqualTo(defaultHelp)
    }

    @Test
    fun `parseForPerformAttack throw Exception`() {
        val adapter = InMemoryPersistence()
        val output =
            captureStandardOut {
                TotallyNotPokemon(listOf("on", "a", "PUNCH"), adapter)
            }
                .trim()
        assertThat(output).isEqualTo("Battle not found: a")
    }

    @Test
    fun `Get help message from cli`() {
        val output = captureStandardOut { main(arrayOf("help")) }.trim()
        assertThat(output).isEqualTo(defaultHelp)
    }

    @Test
    fun `Get help message for new_trainer command`() {
        val adapter = InMemoryPersistence()
        val output =
            captureStandardOut {
                TotallyNotPokemon(listOf("help", "new_trainer"), adapter)
            }
                .trim()
        assertThat(output).isEqualTo(newTrainerHelp)
    }

    @Test
    fun `Get help message for add_monster command`() {
        val adapter = InMemoryPersistence()
        val output =
            captureStandardOut {
                TotallyNotPokemon(listOf("help", "add_monster"), adapter)
            }
                .trim()
        assertThat(output).isEqualTo(addMonsterHelp)
    }

    @Test
    fun `Get help message for new_battle command`() {
        val adapter = InMemoryPersistence()
        val output =
            captureStandardOut {
                TotallyNotPokemon(listOf("help", "new_battle"), adapter)
            }
                .trim()
        assertThat(output).isEqualTo(newBattleHelp)
    }

    @Test
    fun `Get help message for view_battle command`() {
        val adapter = InMemoryPersistence()
        val output =
            captureStandardOut {
                TotallyNotPokemon(listOf("help", "view_battle"), adapter)
            }
                .trim()
        assertThat(output).isEqualTo(viewBattleHelp)
    }

    @Test
    fun `Get help message for on command`() {
        val adapter = InMemoryPersistence()
        val output =
            captureStandardOut { TotallyNotPokemon(listOf("help", "on"), adapter) }
                .trim()
        assertThat(output).isEqualTo(attackHelp)
    }

    @Test
    fun `Create new trainer`() {
        val adapter = InMemoryPersistence()
        val battle = Battle("1")
        adapter.saveBattle(battle)
        val output =
            captureStandardOut {
                TotallyNotPokemon(
                    listOf("new_trainer", "Bob", "1"),
                    adapter
                )
            }
                .trim()
        assertThat(Json.encodeToString(adapter.loadBattle("1")))
            .isEqualTo("{\"battleId\":\"1\",\"trainerOne\":{\"name\":\"Bob\"}}")
        assertThat(output)
            .isEqualTo(
                captureStandardOut { Game().createTrainer("Bob", battle) }.trim()
            )
    }

    @Test
    fun `Create new trainer with no arguments`() {
        val adapter = InMemoryPersistence()
        val output =
            captureStandardOut { TotallyNotPokemon(listOf("new_trainer"), adapter) }
                .trim()
        assertThat(output).isEqualTo(newTrainerHelp)
    }

    @Test
    fun `Try adding trainer to non existing battle print exception message`() {
        val adapter = InMemoryPersistence()
        val output =
            captureStandardOut {
                TotallyNotPokemon(
                    listOf("new_trainer", "Bob", "1"),
                    adapter
                )
            }
                .trim()
        assertThat(output).isEqualTo("Battle not found: 1")
    }

    @Test
    fun `Add new monster`() {
        val adapter = InMemoryPersistence()
        val battle = Battle("1")
        battle.addTrainerToBattle(Trainer("Trainer_Kevin"))
        adapter.saveBattle(battle)

        val output =
            captureStandardOut {
                TotallyNotPokemon(
                    listOf(
                        "add_monster",
                        "Bob",
                        "100",
                        "20",
                        "FIRE",
                        "PUNCH",
                        "Trainer_Kevin",
                        "1"
                    ),
                    adapter
                )
            }
                .trim()
        assertThat(Json.encodeToString(adapter.loadBattle("1")))
            .isEqualTo(
                "{\"battleId\":\"1\",\"trainerOne\":{\"name\":\"Trainer_Kevin\",\"monsters\":[{\"name\":\"Bob\",\"stats\":{\"maxHp\":100,\"hp\":100,\"speed\":20},\"primitiveType\":\"FIRE\",\"attacks\":[\"PUNCH\"]}]}}"
            )
        val output_2 =
            captureStandardOut {
                TotallyNotPokemon(
                    listOf(
                        "add_monster",
                        "Bob",
                        "100",
                        "20",
                        "Fire",
                        "PUNCH",
                        "FIRE_Vow",
                        "Splash",
                        "Foliage_storm",
                        "Trainer_Kevin",
                        "1"
                    ),
                    adapter
                )
            }
                .trim()
        assertThat(output)
            .isEqualTo(
                captureStandardOut {
                    Game().addMonster(
                        "Bob",
                        100,
                        20,
                        PrimitiveType.FIRE,
                        listOf(Attack.PUNCH),
                        "Trainer_Kevin",
                        battle
                    )
                }
                    .trim()
            )
        assertThat(output_2)
            .isEqualTo(
                captureStandardOut {
                    Game().addMonster(
                        "Bob",
                        100,
                        20,
                        PrimitiveType.FIRE,
                        listOf(
                            Attack.PUNCH,
                            Attack.FIRE_VOW,
                            Attack.SPLASH,
                            Attack.FOLIAGE_STORM
                        ),
                        "Trainer_Kevin",
                        battle
                    )
                }
                    .trim()
            )
    }

    @Test
    fun `Add new monster with no arguments`() {
        val adapter = InMemoryPersistence()
        val output =
            captureStandardOut { TotallyNotPokemon(listOf("add_monster"), adapter) }
                .trim()
        assertThat(output).isEqualTo(addMonsterHelp)
    }

    @Test
    fun `Add new monster with not enough or to many arguments`() {
        val adapter = InMemoryPersistence()
        val output1 =
            captureStandardOut {
                TotallyNotPokemon(
                    listOf(
                        "add_monster",
                        "Bob",
                        "100",
                        "20",
                        "FIRE",
                        "Flame_Wreath",
                        "PUNCH",
                        "Splash",
                        "Leaf_gun",
                        "Trainer_Kevin",
                        "1",
                        "too many args"
                    ),
                    adapter
                )
            }
                .trim()
        val output2 =
            captureStandardOut {
                TotallyNotPokemon(
                    listOf(
                        "add_monster",
                        "Bob",
                        "100",
                        "20",
                        "FIRE",
                        "PUNCH"
                    ),
                    adapter
                )
            }
                .trim()
        assertThat(output1).isEqualTo(addMonsterHelp)
        assertThat(output2).isEqualTo(addMonsterHelp)
    }

    @Test
    fun `Start new battle`() {
        val adapter = InMemoryPersistence()
        TotallyNotPokemon(listOf("new_battle"), adapter)
        assert(1 == adapter.countBattles())
    }

    @Test
    fun `Start new battle with not enough or to many arguments`() {
        val adapter = InMemoryPersistence()
        val output1 =
            captureStandardOut {
                TotallyNotPokemon(
                    listOf("new_battle", "to many args"),
                    adapter
                )
            }
                .trim()
        assertThat(output1).isEqualTo(newBattleHelp)
    }

    @Test
    fun `View battle status`() {
        val adapter = InMemoryPersistence()
        val battle = Battle("0")
        adapter.saveBattle(battle)
        val output =
            captureStandardOut {
                TotallyNotPokemon(listOf("view_battle", "0"), adapter)
            }
                .trim()
        val expectedOutput = captureStandardOut { Game().viewStatus(battle) }.trim()
        assertThat(output).isEqualTo(expectedOutput)
    }

    @Test
    fun `Throw exception on invalid battleId`() {
        val adapter = InMemoryPersistence()
        val output =
            captureStandardOut {
                TotallyNotPokemon(listOf("view_battle", "nr1"), adapter)
            }
                .trim()
        assertThat(output).isEqualTo("Battle not found: nr1")
    }

    @Test
    fun `View all battles`() {
        val adapter = InMemoryPersistence()
        val output =
            captureStandardOut {
                TotallyNotPokemon(listOf("view_battle", "  ALL  "), adapter)
            }
                .trim()
        assertThat(output)
            .isEqualTo(
                captureStandardOut {
                    Game().showAllBattles(adapter.loadAllBattles())
                }
                    .trim()
            )
    }

    @Test
    fun `View battle with no arguments`() {
        val adapter = InMemoryPersistence()
        val output =
            captureStandardOut { TotallyNotPokemon(listOf("view_battle"), adapter) }
                .trim()
        assertThat(output).isEqualTo(viewBattleHelp)
    }

    @Test
    fun `Attack enemy`() {
        val adapter = InMemoryPersistence()
        val battle = Battle("1")
        val t1 = Trainer("Trainer_Kevin")
        val t2 = Trainer("Trainer_Bob")
        battle.addTrainerToBattle(t1)
        battle.addTrainerToBattle(t2)
        t1.addMonster(
            Monster(
                "Pika",
                BattleStats(100, 20),
                PrimitiveType.FIRE,
                listOf(Attack.PUNCH)
            )
        )
        t2.addMonster(
            Monster(
                "Glurak",
                BattleStats(100, 20),
                PrimitiveType.FIRE,
                listOf(Attack.PUNCH)
            )
        )
        val battleJson = Json.encodeToString(battle)
        adapter.saveBattle(battle)

        val output =
            captureStandardOut {
                TotallyNotPokemon(listOf("on", "1", "PUNCH"), adapter)
            }
                .trim()
        val newBattleJson = Json.encodeToString(adapter.loadBattle("1"))
        assertThat(battleJson).isNotEqualTo(newBattleJson)
        assertThat(output)
            .isEqualTo(
                captureStandardOut { Game().performAttack(battle, Attack.PUNCH) }
                    .trim()
            )
    }

    @Test
    fun `Attack enemy with no arguments`() {
        val adapter = InMemoryPersistence()
        val output = captureStandardOut { TotallyNotPokemon(listOf("on"), adapter) }.trim()
        assertThat(output).isEqualTo(attackHelp)
    }

    @Test
    fun `Attack enemy with not enough or to many arguments`() {
        val adapter = InMemoryPersistence()
        val output1 =
            captureStandardOut {
                TotallyNotPokemon(
                    listOf("on", "0", "PUNCH", "too many args"),
                    adapter
                )
            }
                .trim()
        val output2 =
            captureStandardOut { TotallyNotPokemon(listOf("on", "0"), adapter) }.trim()
        assertThat(output1).isEqualTo(attackHelp)
        assertThat(output2).isEqualTo(attackHelp)
    }
}
