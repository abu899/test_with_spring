package com.example.sample;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {

    @Test
    void calculatePlus() {
        //given
        long num1 = 2;
        String operator = "+";
        long num2 = 3;
        Calculator calculator = new Calculator();

        //when
        long result = calculator.calculate(num1, operator, num2);

        //then
        assertThat(result).isEqualTo(5);
    }

    @Test
    void calculateMinus() {
        //given
        long num1 = 2;
        String operator = "-";
        long num2 = 3;
        Calculator calculator = new Calculator();

        //when
        long result = calculator.calculate(num1, operator, num2);

        //then
        assertThat(result).isEqualTo(-1);
    }

    @Test
    void calculateMultiply() {
        //given
        long num1 = 2;
        String operator = "*";
        long num2 = 3;
        Calculator calculator = new Calculator();

        //when
        long result = calculator.calculate(num1, operator, num2);

        //then
        assertThat(result).isEqualTo(6);
    }

    @Test
    void calculateDivide() {
        //given
        long num1 = 2;
        String operator = "/";
        long num2 = 3;
        Calculator calculator = new Calculator();

        //when
        long result = calculator.calculate(num1, operator, num2);

        //then
        assertThat(result).isEqualTo(0);
    }

    @Test
    void caclulateInvalidOperator() {
        //given
        long num1 = 2;
        String operator = "%";
        long num2 = 3;
        Calculator calculator = new Calculator();

        //when
        //then
        assertThrows(InvalidOperatorException.class, () -> {
            calculator.calculate(num1, operator, num2);
        });
    }
}