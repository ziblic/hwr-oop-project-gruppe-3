package hwr.oop.tnp.cli

import hwr.oop.tnp.core.Attack
import hwr.oop.tnp.core.Game
import hwr.oop.tnp.core.PrimitiveType
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.extensions.system.captureStandardOut
import org.assertj.core.api.Assertions.assertThat

class TotallyNotPokemonTest : AnnotationSpec() {

    @Test
    fun `test parseToXXXXX with invalid input`() {
        val output =
            captureStandardOut {
                main(
                    arrayOf(
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
                main(
                    arrayOf(
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
                main(
                    arrayOf(
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
        val output = captureStandardOut { main(arrayOf("some_unknown_command")) }.trim()
        assertThat(output)
            .isEqualTo(
                "'some_unknown_command' is not a valid command. Use 'help' for usage."
            )
    }

    @Test
    fun `Run without any command`() {
        val output = captureStandardOut { main(arrayOf()) }.trim()
        assertThat(output).isEqualTo(defaultHelp)
    }

    @Test
    fun `parseForPerformAttack throw Exception`() {
        val output = captureStandardOut { main(arrayOf("on", "a", "PUNCH")) }.trim()
        assertThat(output).isEqualTo(COULD_NOT_PARSE_TO_INT_ERROR)
    }

    @Test
    fun `Get help message from cli`() {
        val output = captureStandardOut { main(arrayOf("help")) }.trim()
        assertThat(output).isEqualTo(defaultHelp)
    }

    @Test
    fun `Get help message for new_trainer command`() {
        val output = captureStandardOut { main(arrayOf("help", "new_trainer")) }.trim()
        assertThat(output).isEqualTo(newTrainerHelp)
    }

    @Test
    fun `Get help message for add_monster command`() {
        val output = captureStandardOut { main(arrayOf("help", "add_monster")) }.trim()
        assertThat(output).isEqualTo(addMonsterHelp)
    }

    @Test
    fun `Get help message for new_battle command`() {
        val output = captureStandardOut { main(arrayOf("help", "new_battle")) }.trim()
        assertThat(output).isEqualTo(newBattleHelp)
    }

    @Test
    fun `Get help message for view_battle command`() {
        val output = captureStandardOut { main(arrayOf("help", "view_battle")) }.trim()
        assertThat(output).isEqualTo(viewBattleHelp)
    }

    @Test
    fun `Get help message for on command`() {
        val output = captureStandardOut { main(arrayOf("help", "on")) }.trim()
        assertThat(output).isEqualTo(attackHelp)
    }

    @Test
    fun `Create new trainer`() {
        val output = captureStandardOut { main(arrayOf("new_trainer", "Bob")) }.trim()
        assertThat(output)
            .isEqualTo(captureStandardOut { Game().createTrainer("Bob") }.trim())
    }

    @Test
    fun `Create new trainer with no arguments`() {
        val output = captureStandardOut { main(arrayOf("new_trainer")) }.trim()
        assertThat(output).isEqualTo(newTrainerHelp)
    }

    @Test
    fun `Add new monster`() {
        val output =
            captureStandardOut {
                main(
                    arrayOf(
                        "add_monster",
                        "Bob",
                        "100",
                        "20",
                        "FIRE",
                        "PUNCH",
                        "Trainer_Kevin"
                    )
                )
            }
                .trim()
        val output_2 =
            captureStandardOut {
                main(
                    arrayOf(
                        "add_monster",
                        "Bob",
                        "100",
                        "20",
                        "Fire",
                        "PUNCH",
                        "FIRE_Vow",
                        "Splash",
                        "Foliage_storm",
                        "Trainer_Kevin"
                    )
                )
            }
                .trim()
        // assertThat(output)
        //     .isEqualTo(
        //         captureStandardOut {
        //             Game().addMonster(
        //                 "Bob",
        //                 100,
        //                 20,
        //                 Type.FIRE,
        //                 listOf(Attack.PUNCH),
        //                 "Trainer_Kevin",
        //                 "1"
        //             )
        //         }
        //             .trim()
        //     )
        // assertThat(output_2)
        //     .isEqualTo(
        //         captureStandardOut {
        //             Game().addMonster(
        //                 "Bob",
        //                 100,
        //                 20,
        //                 Type.FIRE,
        //                 listOf(
        //                     Attack.PUNCH,
        //                     Attack.FIRE_VOW,
        //                     Attack.SPLASH,
        //                     Attack.FOLIAGE_STORM
        //                 ),
        //                 "Trainer_Kevin",
        //                 "1"
        //             )
        //         }
        //             .trim()
        //  )
    }

    @Test
    fun `Add new monster with no arguments`() {
        val output = captureStandardOut { main(arrayOf("add_monster")) }.trim()
        assertThat(output).isEqualTo(addMonsterHelp)
    }

    @Test
    fun `Add new monster with not enough or to many arguments`() {
        val output1 =
            captureStandardOut {
                main(
                    arrayOf(
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
                main(
                    arrayOf(
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
        val output =
            captureStandardOut { main(arrayOf("new_battle", "Bob", "Lisa")) }.trim()
        assertThat(output)
            .isEqualTo(
                captureStandardOut { Game().initiateBattle("Bob", "Lisa") }.trim()
            )
    }

    @Test
    fun `Start new battle with no arguments`() {
        val output = captureStandardOut { main(arrayOf("new_battle")) }.trim()
        assertThat(output).isEqualTo(newBattleHelp)
    }

    @Test
    fun `Start new battle with not enough or to many arguments`() {
        val output1 =
            captureStandardOut {
                main(arrayOf("new_battle", "Bob", "Lisa", "to many args"))
            }
                .trim()
        val output2 = captureStandardOut { main(arrayOf("new_battle", "Bob")) }.trim()
        assertThat(output1).isEqualTo(newBattleHelp)
        assertThat(output2).isEqualTo(newBattleHelp)
    }

    @Test
    fun `View battle status`() {
        val output = captureStandardOut { main(arrayOf("view_battle", "0")) }.trim()
        assertThat(output)
    }

    @Test
    fun `Throw exception on invalid battleId`() {
        val output = captureStandardOut { main(arrayOf("view_battle", "nr1")) }.trim()
        assertThat(output)
            .isEqualTo("Some of the provided arguments could not be parsed to an Int")
    }

    @Test
    fun `View all battles`() {
        val output = captureStandardOut { main(arrayOf("view_battle", "  ALL  ")) }.trim()
        assertThat(output).isEqualTo(captureStandardOut { Game().showAllBattles() }.trim())
    }

    @Test
    fun `View battle with no arguments`() {
        val output = captureStandardOut { main(arrayOf("view_battle")) }.trim()
        assertThat(output).isEqualTo(viewBattleHelp)
    }

    @Test
    fun `Attack enemy`() {
        val output = captureStandardOut { main(arrayOf("on", "1", "PUNCH")) }.trim()
        assertThat(output)
            .isEqualTo(
                captureStandardOut { Game().performAttack("1", Attack.PUNCH) }
                    .trim()
            )
    }

    @Test
    fun `Attack enemy with no arguments`() {
        val output = captureStandardOut { main(arrayOf("on")) }.trim()
        assertThat(output).isEqualTo(attackHelp)
    }

    @Test
    fun `Attack enemy with not enough or to many arguments`() {
        val output1 =
            captureStandardOut { main(arrayOf("on", "0", "PUNCH", "too many args")) }
                .trim()
        val output2 = captureStandardOut { main(arrayOf("on", "0")) }.trim()
        assertThat(output1).isEqualTo(attackHelp)
        assertThat(output2).isEqualTo(attackHelp)
    }
}
