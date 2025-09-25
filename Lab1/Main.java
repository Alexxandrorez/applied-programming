package Lab1;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Програма запитує у користувача число n та тип фігури,
 * генерує послідовність чисел Лукаса
 * та виводить її на екран.
 */
public class Main {

    /**
     * Головний метод програми.
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Введіть кількість чисел Лукаса(N) ");
        int kilkistchysellucasa = sc.nextInt();
        sc.nextLine();

        if (kilkistchysellucasa < 0||kilkistchysellucasa>91) {
            throw new IllegalArgumentException("N має бути >= 0 і <=91");
        }

        System.out.println("Введіть цифру: ");
        int figure = sc.nextInt();

        ArrayList<LucasNumber> numbers;
        numbers = LucasNumber.generate(kilkistchysellucasa, figure);


        System.out.println("\nЧисла лукаса, які закінчуються на цифру "+figure );
        for (LucasNumber l : numbers) {
            System.out.println(l);
        }
    }
}
