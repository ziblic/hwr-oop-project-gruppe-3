package hwr.oop.tnp.cli

import hwr.oop.tnp.InMemoryPersistence
import hwr.oop.tnp.core.Attack
import hwr.oop.tnp.core.Battle
import hwr.oop.tnp.core.Game
import hwr.oop.tnp.core.PrimitiveType
import hwr.oop.tnp.core.Trainer
import hwr.oop.tnp.persistency.PersistenceAdapter
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.extensions.system.captureStandardOut
import org.assertj.core.api.Assertions.assertThat

class TotallyNotPokemonTest : AnnotationSpec() {
    @Test
    fun `test parseToXXXXX with invalid input`() {
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
                    )
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
                    )
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
                    )
                )
            }
                .trim()
        assertThat(output3).isEqualTo(COULD_NOT_PARSE_ERROR)
    }

    @Test
    fun `Run invalid command`() {
        val output =
            captureStandardOut { TotallyNotPokemon(listOf("some_unknown_command")) }
                .trim()
        assertThat(output)
            .isEqualTo(
                "'some_unknown_command' is not a valid command. Use 'help' for usage."
            )
    }

    @Test
    fun `Run without any command`() {
        val output = captureStandardOut { TotallyNotPokemon(listOf()) }.trim()
        assertThat(output).isEqualTo(defaultHelp)
    }

    @Test
    fun `parseForPerformAttack throw Exception`() {
        val output =
            captureStandardOut { TotallyNotPokemon(listOf("on", "a", "PUNCH")) }.trim()
        assertThat(output).isEqualTo("Could not find battle with id: a.")
    }

    @Test
    fun `Get help message from cli`() {
        val output = captureStandardOut { main(arrayOf("help")) }.trim()
        assertThat(output).isEqualTo(defaultHelp)
    }

    @Test
    fun `Get help message for new_trainer command`() {
        val output =
            captureStandardOut { TotallyNotPokemon(listOf("help", "new_trainer")) }
                .trim()
        assertThat(output).isEqualTo(newTrainerHelp)
    }

    @Test
    fun `Get help message for add_monster command`() {
        val output =
            captureStandardOut { TotallyNotPokemon(listOf("help", "add_monster")) }
                .trim()
        assertThat(output).isEqualTo(addMonsterHelp)
    }

    @Test
    fun `Get help message for new_battle command`() {
        val output =
            captureStandardOut { TotallyNotPokemon(listOf("help", "new_battle")) }
                .trim()
        assertThat(output).isEqualTo(newBattleHelp)
    }

    @Test
    fun `Get help message for view_battle command`() {
        val output =
            captureStandardOut { TotallyNotPokemon(listOf("help", "view_battle")) }
                .trim()
        assertThat(output).isEqualTo(viewBattleHelp)
    }

    @Test
    fun `Get help message for on command`() {
        val output = captureStandardOut { TotallyNotPokemon(listOf("help", "on")) }.trim()
        assertThat(output).isEqualTo(attackHelp)
    }

    @Test
    fun `Create new trainer`() {
        val adapter = InMemoryPersistence()
        adapter.saveBattle(Battle("1"))
        val output =
            captureStandardOut {
                TotallyNotPokemon(
                    listOf("new_trainer", "Bob", "1"),
                    adapter
                )
            }
                .trim()
        assertThat(output)
            .isEqualTo(
                captureStandardOut {
                    Game().createTrainer("Bob", adapter.loadBattle("1"))
                }
                    .trim()
            )
    }

    @Test
    fun `Create new trainer with no arguments`() {
        val output = captureStandardOut { TotallyNotPokemon(listOf("new_trainer")) }.trim()
        assertThat(output).isEqualTo(newTrainerHelp)
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
        val output = captureStandardOut { TotallyNotPokemon(listOf("add_monster")) }.trim()
        assertThat(output).isEqualTo(addMonsterHelp)
    }

    @Test
    fun `Add new monster with not enough or to many arguments`() {
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
                    )
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
                    )
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
        val output1 =
            captureStandardOut {
                TotallyNotPokemon(listOf("new_battle", "to many args"))
            }
                .trim()
        assertThat(output1).isEqualTo(newBattleHelp)
    }

    @Test
    fun `View battle status`() {
        val output =
            captureStandardOut { TotallyNotPokemon(listOf("view_battle", "0")) }.trim()
        assertThat(output)
    }

    @Test
    fun `Throw exception on invalid battleId`() {
        val output =
            captureStandardOut { TotallyNotPokemon(listOf("view_battle", "nr1")) }
                .trim()
        assertThat(output).isEqualTo("Could not find battle with id: nr1.")
    }

    @Test
    fun `View all battles`() {
        val output =
            captureStandardOut { TotallyNotPokemon(listOf("view_battle", "  ALL  ")) }
                .trim()
        assertThat(output)
            .isEqualTo(
                captureStandardOut {
                    Game().showAllBattles(
                        PersistenceAdapter()
                            .loadAllBattles()
                    )
                }
                    .trim()
            )
    }

    @Test
    fun `View battle with no arguments`() {
        val output = captureStandardOut { TotallyNotPokemon(listOf("view_battle")) }.trim()
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
        adapter.saveBattle(battle)

        val output =
            captureStandardOut {
                TotallyNotPokemon(listOf("on", "1", "PUNCH"), adapter)
            }
                .trim()
        assertThat(output)
            .isEqualTo(
                captureStandardOut { Game().performAttack(battle, Attack.PUNCH) }
                    .trim()
            )
    }

    @Test
    fun `Attack enemy with no arguments`() {
        val output = captureStandardOut { TotallyNotPokemon(listOf("on")) }.trim()
        assertThat(output).isEqualTo(attackHelp)
    }

    @Test
    fun `Attack enemy with not enough or to many arguments`() {
        val output1 =
            captureStandardOut {
                TotallyNotPokemon(
                    listOf("on", "0", "PUNCH", "too many args"),
                    InMemoryPersistence()
                )
            }
                .trim()
        val output2 = captureStandardOut { TotallyNotPokemon(listOf("on", "0")) }.trim()
        assertThat(output1).isEqualTo(attackHelp)
        assertThat(output2).isEqualTo(attackHelp)
    }
}
