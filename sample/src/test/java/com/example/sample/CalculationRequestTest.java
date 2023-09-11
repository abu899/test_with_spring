package com.example.sample;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CalculationRequestTest {

    @Test
    void success() {
        //given
        String[] parts = new String[]{"2", "+", "3"};

        //when
        CalculationRequest calculationRequest = new CalculationRequest(parts);

        //then
        assertEquals(2, calculationRequest.getNum1());
        assertEquals("+", calculationRequest.getOperator());
        assertEquals(3, calculationRequest.getNum2());
    }

    @Test
    void validatePartsLength() {
        //given
        String[] parts = new String[]{"2", "+", "3", "4"};

        //when
        //then
        assertThrows(BadRequestException.class, () -> new CalculationRequest(parts));
    }

    @Test
    void validateOperator() {
        //given
        String[] parts = new String[]{"2", "a", "3"};

        //when
        //then
        assertThrows(InvalidOperatorException.class, () -> new CalculationRequest(parts));
    }

    @Test
    void validOperatorLength() {
        //given
        String[] parts = new String[]{"2", "++", "3"};

        //when
        //then
        assertThrows(InvalidOperatorException.class, () -> new CalculationRequest(parts));
    }
}