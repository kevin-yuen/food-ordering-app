package general;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GeneralTest {
    @Test
    void selectOption1ShouldEqualToOne() {
        assertEquals(1, General.verifyItemSelection("1"));
    }

    @Test
    void selectOptionNeg1ShouldEqualToZero() {
        assertEquals(0, General.verifyItemSelection("-1"));
    }

    @Test
    void selectOption9ShouldEqualToZero() {
        assertEquals(0, General.verifyItemSelection("9"));
    }

    @Test
    void inputUppercaseFShouldEqualToOption1() {
        assertEquals(1, General.verifyItemSelection("F"));
    }

    @Test
    void inputUppercaseBShouldEqualToOption2() {
        assertEquals(2, General.verifyItemSelection("B"));
    }

    @Test
    void inputUppercaseDShouldEqualToOption3() {
        assertEquals(3, General.verifyItemSelection("D"));
    }

    @Test
    void inputUppercaseDRShouldEqualToOption4() {
        assertEquals(4, General.verifyItemSelection("DR"));
    }

    @Test
    void inputUppercaseMShouldEqualToOption5() {
        assertEquals(5, General.verifyItemSelection("M"));
    }

    @Test
    void inputUppercaseSShouldEqualToOption6() {
        assertEquals(6, General.verifyItemSelection("S"));
    }

    @Test
    void inputUppercaseTShouldEqualToOption7() {
        assertEquals(7, General.verifyItemSelection("T"));
    }

    @Test
    void inputUppercaseBAShouldEqualToOption8() {
        assertEquals(8, General.verifyItemSelection("BA"));
    }

    @Test
    void inputLowercaseFShouldEqualToOption1() {
        assertEquals(1, General.verifyItemSelection("f"));
    }

    @Test
    void inputLowercaseBShouldEqualToOption2() {
        assertEquals(2, General.verifyItemSelection("b"));
    }

    @Test
    void inputLowercaseDShouldEqualToOption3() {
        assertEquals(3, General.verifyItemSelection("d"));
    }

    @Test
    void inputLowercaseDRShouldEqualToOption4() {
        assertEquals(4, General.verifyItemSelection("dr"));
    }

    @Test
    void inputLowercaseMShouldEqualToOption5() {
        assertEquals(5, General.verifyItemSelection("m"));
    }

    @Test
    void inputLowercaseSShouldEqualToOption6() {
        assertEquals(6, General.verifyItemSelection("s"));
    }

    @Test
    void inputLowercaseTShouldEqualToOption7() {
        assertEquals(7, General.verifyItemSelection("t"));
    }

    @Test
    void inputLowercaseBAShouldEqualToOption8() {
        assertEquals(8, General.verifyItemSelection("ba"));
    }

    @Test
    void inputUppercaseZShouldEqualToOption0() {
        assertEquals(0, General.verifyItemSelection("Z"));
    }

    @Test
    void inputLowercaseZShouldEqualToOption0() {
        assertEquals(0, General.verifyItemSelection("z"));
    }
}