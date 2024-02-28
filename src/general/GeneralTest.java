package general;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GeneralTest {
    @Test
    void givenSelectedOption_whenOne_thenReturnItemCode1() {
        String actualItemOptionSelected = "1";
        int expectedItemOptionReturned = 1;
        assertEquals(expectedItemOptionReturned, General.verifyItemSelection(actualItemOptionSelected));
    }

    @Test
    void givenSelectedOption_whenNegativeOne_thenReturnItemCode0() {
        String actualItemOptionSelected = "-1";
        int expectedItemOptionReturned = 0;
        assertEquals(expectedItemOptionReturned, General.verifyItemSelection(actualItemOptionSelected));
    }

    @Test
    void givenSelectedOption_whenNine_thenReturnItemCode0() {
        String actualItemOptionSelected = "9";
        int expectedItemOptionReturned = 0;
        assertEquals(expectedItemOptionReturned, General.verifyItemSelection(actualItemOptionSelected));
    }

    @Test
    void givenSelectedOption_whenUppercaseF_thenReturnOption1() {
        String actualItemOptionSelected = "F";
        int expectedItemOptionReturned = 1;
        assertEquals(expectedItemOptionReturned, General.verifyItemSelection(actualItemOptionSelected));
    }

    @Test
    void givenSelectedOption_whenUppercaseB_thenReturnOption2() {
        String actualItemOptionSelected = "B";
        int expectedItemOptionReturned = 2;
        assertEquals(expectedItemOptionReturned, General.verifyItemSelection(actualItemOptionSelected));
    }

    @Test
    void givenSelectedOption_whenUppercaseD_thenReturnOption3() {
        String actualItemOptionSelected = "D";
        int expectedItemOptionReturned = 3;
        assertEquals(expectedItemOptionReturned, General.verifyItemSelection(actualItemOptionSelected));
    }

    @Test
    void givenSelectedOption_whenUppercaseDR_thenReturnOption4() {
        String actualItemOptionSelected = "DR";
        int expectedItemOptionReturned = 4;
        assertEquals(expectedItemOptionReturned, General.verifyItemSelection(actualItemOptionSelected));
    }

    @Test
    void givenSelectedOption_whenUppercaseM_thenReturnOption5() {
        String actualItemOptionSelected = "M";
        int expectedItemOptionReturned = 5;
        assertEquals(expectedItemOptionReturned, General.verifyItemSelection(actualItemOptionSelected));
    }

    @Test
    void givenSelectedOption_whenUppercaseS_thenReturnOption6() {
        String actualItemOptionSelected = "S";
        int expectedItemOptionReturned = 6;
        assertEquals(expectedItemOptionReturned, General.verifyItemSelection(actualItemOptionSelected));
    }

    @Test
    void givenSelectedOption_whenUppercaseT_thenReturnOption7() {
        String actualItemOptionSelected = "T";
        int expectedItemOptionReturned = 7;
        assertEquals(expectedItemOptionReturned, General.verifyItemSelection(actualItemOptionSelected));
    }

    @Test
    void givenSelectedOption_whenUppercaseBA_thenReturnOption8() {
        String actualItemOptionSelected = "BA";
        int expectedItemOptionReturned = 8;
        assertEquals(expectedItemOptionReturned, General.verifyItemSelection(actualItemOptionSelected));
    }

    @Test
    void givenSelectedOption_whenUppercaseX_thenReturnOption0() {
        String actualItemOptionSelected = "X";
        int expectedItemOptionReturned = 0;
        assertEquals(expectedItemOptionReturned, General.verifyItemSelection(actualItemOptionSelected));
    }

    @Test
    void givenSelectedOption_whenLowercaseF_thenReturnOption1() {
        String actualItemOptionSelected = "f";
        int expectedItemOptionReturned = 1;
        assertEquals(expectedItemOptionReturned, General.verifyItemSelection(actualItemOptionSelected));
    }

    @Test
    void givenSelectedOption_whenLowercaseB_thenReturnOption2() {
        String actualItemOptionSelected = "b";
        int expectedItemOptionReturned = 2;
        assertEquals(expectedItemOptionReturned, General.verifyItemSelection(actualItemOptionSelected));
    }

    @Test
    void givenSelectedOption_whenLowercaseD_thenReturnOption3() {
        String actualItemOptionSelected = "d";
        int expectedItemOptionReturned = 3;
        assertEquals(expectedItemOptionReturned, General.verifyItemSelection(actualItemOptionSelected));
    }

    @Test
    void givenSelectedOption_whenLowercaseDR_thenReturnOption4() {
        String actualItemOptionSelected = "dr";
        int expectedItemOptionReturned = 4;
        assertEquals(expectedItemOptionReturned, General.verifyItemSelection(actualItemOptionSelected));
    }

    @Test
    void givenSelectedOption_whenLowercaseM_thenReturnOption5() {
        String actualItemOptionSelected = "m";
        int expectedItemOptionReturned = 5;
        assertEquals(expectedItemOptionReturned, General.verifyItemSelection(actualItemOptionSelected));
    }

    @Test
    void givenSelectedOption_whenLowercaseS_thenReturnOption6() {
        String actualItemOptionSelected = "s";
        int expectedItemOptionReturned = 6;
        assertEquals(expectedItemOptionReturned, General.verifyItemSelection(actualItemOptionSelected));
    }

    @Test
    void givenSelectedOption_whenLowercaseT_thenReturnOption7() {
        String actualItemOptionSelected = "t";
        int expectedItemOptionReturned = 7;
        assertEquals(expectedItemOptionReturned, General.verifyItemSelection(actualItemOptionSelected));
    }

    @Test
    void givenSelectedOption_whenLowercaseBA_thenReturnOption8() {
        String actualItemOptionSelected = "ba";
        int expectedItemOptionReturned = 8;
        assertEquals(expectedItemOptionReturned, General.verifyItemSelection(actualItemOptionSelected));
    }

    @Test
    void givenSelectedOption_whenLowercaseX_thenReturnOption0() {
        String actualItemOptionSelected = "x";
        int expectedItemOptionReturned = 0;
        assertEquals(expectedItemOptionReturned, General.verifyItemSelection(actualItemOptionSelected));
    }
}