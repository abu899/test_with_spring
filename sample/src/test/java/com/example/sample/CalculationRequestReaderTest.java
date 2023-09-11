package com.example.sample;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CalculationRequestReaderTest {

    @Test
    void read() {
        //given
        CalculationRequestReader calculationRequestReader = new CalculationRequestReader();

        //when
        System.setIn(new java.io.ByteArrayInputStream("2 + 3".getBytes()));
        CalculationRequest calculationRequest = calculationRequestReader.read();

        //then
        assertEquals(2, calculationRequest.getNum1());
        assertEquals("+", calculationRequest.getOperator());
        assertEquals(3, calculationRequest.getNum2());
    }
}