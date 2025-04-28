package hwr.oop.tnp

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.extensions.system.captureStandardOut
import org.assertj.core.api.Assertions.assertThat

class GameParserTest : AnnotationSpec() {
    @Test
    fun `test parseToInt with invalid input`() {
        val output =
                captureStandardOut {
                            main(
                                    listOf(
                                            "add_monster",
                                            "Bob",
                                            "abc",
                                            "abc",
                                            "abc",
                                            "abc",
                                            "abc",
                                            "Tackle",
                                            "Trainer_Kevin"
                                    )
                            )
                        }
                        .trim()
        assertThat(output).isEqualTo("Some of the provided arguments could not be parsed to an Int")
    }

    @Test
    fun `Get help message from cli`() {
        val output = captureStandardOut { main(listOf("help")) }.trim()
        assertThat(output).isEqualTo(defaultHelp)
    }

    @Test
    fun `Get help message for new_trainer command`() {
        val output = captureStandardOut { main(listOf("help", "new_trainer")) }.trim()
        assertThat(output).isEqualTo(newTrainerHelp)
    }

    @Test
    fun `Get help message for add_monster command`() {
        val output = captureStandardOut { main(listOf("help", "add_monster")) }.trim()
        assertThat(output).isEqualTo(addMonsterHelp)
    }

    @Test
    fun `Get help message for new_battle command`() {
        val output = captureStandardOut { main(listOf("help", "new_battle")) }.trim()
        assertThat(output).isEqualTo(newBattleHelp)
    }

    @Test
    fun `Get help message for view_battle command`() {
        val output = captureStandardOut { main(listOf("help", "view_battle")) }.trim()
        assertThat(output).isEqualTo(viewBattleHelp)
    }

    @Test
    fun `Get help message for on command`() {
        val output = captureStandardOut { main(listOf("help", "on")) }.trim()
        assertThat(output).isEqualTo(attackHelp)
    }

    @Test
    fun `Create new trainer`() {
        val output = captureStandardOut { main(listOf("new_trainer", "Bob")) }.trim()
        assertThat(output).isEqualTo(captureStandardOut { Game().createTrainer("Bob") }.trim())
    }

    @Test
    fun `Create new trainer with no arguments`() {
        val output = captureStandardOut { main(listOf("new_trainer")) }.trim()
        assertThat(output).isEqualTo(newTrainerHelp)
    }

    @Test
    fun `Add new monster`() {
        val output =
                captureStandardOut {
                            main(
                                    listOf(
                                            "add_monster",
                                            "Bob",
                                            "100",
                                            "20",
                                            "10",
                                            "5",
                                            "20",
                                            "Tackle",
                                            "Trainer_Kevin"
                                    )
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
                                                    10,
                                                    5,
                                                    20,
                                                    listOf("Tackle"),
                                                    "Trainer_Kevin"
                                            )
                                }
                                .trim()
                )
    }

    @Test
    fun `Add new monster with no arguments`() {
        val output = captureStandardOut { main(listOf("add_monster")) }.trim()
        assertThat(output).isEqualTo(addMonsterHelp)
    }

    @Test
    fun `Add new monster with not enough or to many arguments`() {
        val output1 =
                captureStandardOut {
                            main(
                                    listOf(
                                            "add_monster",
                                            "Bob",
                                            "100",
                                            "20",
                                            "10",
                                            "5",
                                            "20",
                                            "Fireball",
                                            "Tackle",
                                            "Waterbomb",
                                            "Backfire",
                                            "Trainer_Kevin",
                                            "too many args"
                                    )
                            )
                        }
                        .trim()
        val output2 =
                captureStandardOut { main(listOf("add_monster", "Bob", "100", "20", "10")) }.trim()
        assertThat(output1).isEqualTo(addMonsterHelp)
        assertThat(output2).isEqualTo(addMonsterHelp)
    }

    @Test
    fun `Start new battle`() {
        val output = captureStandardOut { main(listOf("new_battle", "Bob", "Lisa")) }.trim()
        assertThat(output)
                .isEqualTo(captureStandardOut { Game().initiateBattle("Bob", "Lisa") }.trim())
    }

    @Test
    fun `Start new battle with no arguments`() {
        val output = captureStandardOut { main(listOf("new_battle")) }.trim()
        assertThat(output).isEqualTo(newBattleHelp)
    }

    @Test
    fun `Start new battle with not enough or to many arguments`() {
        val output1 =
                captureStandardOut { main(listOf("new_battle", "Bob", "Lisa", "to many args")) }
                        .trim()
        val output2 = captureStandardOut { main(listOf("new_battle", "Bob")) }.trim()
        assertThat(output1).isEqualTo(newBattleHelp)
        assertThat(output2).isEqualTo(newBattleHelp)
    }

    @Test
    fun `View battle status`() {
        val output = captureStandardOut { main(listOf("view_battle", "0")) }.trim()
        assertThat(output).isEqualTo(captureStandardOut { Game().viewStatus() }.trim())
    }

    @Test
    fun `View battle with no arguments`() {
        val output = captureStandardOut { main(listOf("view_battle")) }.trim()
        assertThat(output).isEqualTo(viewBattleHelp)
    }

    @Test
    fun `Attack enemy`() {
        val output = captureStandardOut { main(listOf("on", "0", "Bob", "Tackle")) }.trim()
        assertThat(output)
                .isEqualTo(captureStandardOut { Game().performAttack(0, "Bob", "Tackle") }.trim())
    }

    @Test
    fun `Attack enemy with no arguments`() {
        val output = captureStandardOut { main(listOf("on")) }.trim()
        assertThat(output).isEqualTo(attackHelp)
    }

    @Test
    fun `Attack enemy with not enough or to many arguments`() {
        val output1 =
                captureStandardOut { main(listOf("on", "0", "Lisa", "Tackle", "to many args")) }
                        .trim()
        val output2 = captureStandardOut { main(listOf("on", "0", "Lisa")) }.trim()
        assertThat(output1).isEqualTo(attackHelp)
        assertThat(output2).isEqualTo(attackHelp)
    }
}
