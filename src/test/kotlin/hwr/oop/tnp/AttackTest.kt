package hwr.oop.tnp

import io.kotest.core.spec.style.AnnotationSpec
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.assertThrows

class AttackTest : AnnotationSpec() {
    @Test
    fun `calcMultiplierHitQuote valid input`() {
        val attack = Attack.PUNCH
        val critChance = 0.5
        val result = attack.calcMultiplierHitQuote(critChance)
        assert(result in 1.0..1.5)
    }

    @Test
    fun `calcMultiplierHitQuote when critChance is zero`() {
        val attack = Attack.PUNCH
        val critChance = 0.0
        val result = attack.calcMultiplierHitQuote(critChance)
        assertEquals(1.0, result, "Multiplier for critChance of 0 should be 1.0")
    }

    @Test
    fun `calcMultiplierHitQuote when critChance is one`() {
        val attack = Attack.PUNCH
        val critChance = 1.0
        val result = attack.calcMultiplierHitQuote(critChance)
        assertEquals(1.5, result, "Multiplier for critChance of 1 should be 1.5")
    }

    @Test
    fun `calcMultiplierHitQuote invalid critChance below range`() {
        val attack = Attack.PUNCH
        val critChance = -0.1
        val exception =
            assertThrows<IllegalArgumentException> { attack.calcMultiplierHitQuote(critChance) }
        assertEquals("critChance must be between 0.0 and 1.0", exception.message)
    }

    @Test
    fun `calcMultiplierHitQuote invalid critChance above range`() {
        val attack = Attack.PUNCH
        val critChance = 1.1
        val exception =
            assertThrows<IllegalArgumentException> { attack.calcMultiplierHitQuote(critChance) }
        assertEquals("critChance must be between 0.0 and 1.0", exception.message)
    }
    @Test
    fun `check getters for PUNCH attack`() {
        val attack = Attack.PUNCH

        assertEquals(Type.Normal, attack.type, "Type getter should return Normal for PUNCH")
        assertEquals(15, attack.damage, "Damage getter should return 15 for PUNCH")
        assertEquals(0.1, attack.hitQuote, "HitQuote getter should return 0.1 for PUNCH")
    }

    @Test
    fun `check getters for FLAME_WREATH attack`() {
        val attack = Attack.FLAME_WREATH

        assertEquals(Type.Fire, attack.type, "Type getter should return Fire for FLAME_WREATH")
        assertEquals(20, attack.damage, "Damage getter should return 20 for FLAME_WREATH")
        assertEquals(0.4, attack.hitQuote, "HitQuote getter should return 0.4 for FLAME_WREATH")
    }

    @Test
    fun `check getters for SPLASH attack`() {
        val attack = Attack.SPLASH

        assertEquals(Type.Water, attack.type, "Type getter should return Water for SPLASH")
        assertEquals(20, attack.damage, "Damage getter should return 20 for SPLASH")
        assertEquals(0.7, attack.hitQuote, "HitQuote getter should return 0.7 for SPLASH")
    }

    @Test
    fun `check getters for LEAF_GUN attack`() {
        val attack = Attack.LEAF_GUN

        assertEquals(Type.Plant, attack.type, "Type getter should return Plant for LEAF_GUN")
        assertEquals(20, attack.damage, "Damage getter should return 20 for LEAF_GUN")
        assertEquals(0.2, attack.hitQuote, "HitQuote getter should return 0.2 for LEAF_GUN")
    }

    @Test
    fun `check getters for SPOOKY_BALL attack`() {
        val attack = Attack.SPOOKY_BALL

        assertEquals(Type.Spirit, attack.type, "Type getter should return Spirit for SPOOKY_BALL")
        assertEquals(20, attack.damage, "Damage getter should return 20 for SPOOKY_BALL")
        assertEquals(0.1, attack.hitQuote, "HitQuote getter should return 0.1 for SPOOKY_BALL")
    }

    @Test
    fun `calcMultiplierHitQuote returns 1_5 when critical hit occurs`() {
        val attack = Attack.PUNCH
        val critChance = 0.5
        val random = 0.3 // Simulating scenario where critical hit should happen
        val result = attack.calcMultiplierHitQuote(critChance, random)
        assertEquals(1.5, result, "Expected multiplier should be 1.5 on critical hit")
    }

    @Test
    fun `calcMultiplierHitQuote returns 1_0 when no critical hit occurs`() {
        val attack = Attack.PUNCH
        val critChance = 0.5
        val random = 0.6 // Simulating scenario where critical hit does not happen
        val result = attack.calcMultiplierHitQuote(critChance, random)
        assertEquals(1.0, result, "Expected multiplier should be 1.0 when no critical hit")
    }

    @Test
    fun `calcMultiplierHitQuote boundary edge case`() {
        val attack = Attack.PUNCH
        val critChance = 0.5
        val random = 0.5 // Simulating scenario where critical hit does not happen
        val result = attack.calcMultiplierHitQuote(critChance, random)
        assertEquals(1.0, result, "Expected multiplier should be 1.0 when no critical hit")
    }
}
