package Lab1;

import java.util.ArrayList;

/**
 * Клас LucasNumber представляє число Лукаса
 * разом із його позицією у послідовності.
 */
public class LucasNumber {
    private final long value;
    private final int position;

    /**
     * Створює об'єкт LucasNumber.
     * value значення числа Лукаса
     * position позиція у послідовності
     */
    public LucasNumber(long value, int position) {
        this.value = value;
        this.position = position;
    }


    public long getValue() {
        return value;
    }


    public int getPosition() {
        return position;
    }

    /**
     * Генерує список чисел Лукаса, що закінчуються на певну цифру.
     * count кількість чисел для генерації
     * lastDigit цифра від 0 до 9, на яку повинні закінчуватися числа
     * повертає список знайдених LucasNumber
     */
    public static ArrayList<LucasNumber> generate(int count, int ostanniacyfra) {
        if (ostanniacyfra < 0 || ostanniacyfra > 9) {
            throw new IllegalArgumentException("Остання цифра має бути між 0 і 9");
        }

        System.out.println("Всі числа лукаса:");

        ArrayList<LucasNumber> lucasNumbers = new ArrayList<>();
        long prev = 2;
        long curr = 1;

        int position = 0;

        while (position < count) {
            long lucasValue;
            if (position == 0) {
                lucasValue = prev;
                System.out.println(new LucasNumber(lucasValue, position));
            }
            else if (position == 1) {
                lucasValue = curr;
                System.out.println(new LucasNumber(lucasValue, position));
            }
            else {
                long next = prev + curr;
                lucasValue = next;
                prev = curr;
                curr = next;
                System.out.println(new LucasNumber(lucasValue, position));

            }

            if (lucasValue % 10 == ostanniacyfra) {
                lucasNumbers.add(new LucasNumber(lucasValue, position));
            }

            position++;
        }

        return lucasNumbers;
    }

    /**
     * повертає рядкове представлення об'єкта
     */
    @Override
    public String toString() {
        return String.format("Числа Лука{позиція=%d, значення=%d}", getPosition(), getValue());
    }
}
