package org.example;

import java.util.ArrayList;
import java.util.Scanner;
import static org.example.Patient.*;

public class Main {

    public static void addPatient(ArrayList<Patient> patiend, Scanner sc) {
            patiend.add(createPatient(sc));
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ArrayList<Patient> patiend = new ArrayList<>();

        System.out.println("Введіть кількість пацієнтів");
        int kilkistpatientd=sc.nextInt();

        for (int i = 0; i < kilkistpatientd; i++) {
            System.out.println("Patient № " + (i+1));
            addPatient(patiend,sc);
        }

        boolean flg=true;
        while (flg) {
            System.out.println("1-Знайти людей за діагнозом");
            System.out.println("2-Знайти людей по номеру медичної карти з інтервалу");
            System.out.println("3-Знайти людей за першою цифрою phoneNumber");
            System.out.println("0-Стоп");

            int vybir=sc.nextInt();
            sc.nextLine();

            switch (vybir) {
                case 1->findByDiagnosis(patiend,sc);
                case 2->findByIdMedCard(patiend,sc);
                case 3->findByFirstPhoneDigit(patiend,sc);
                case 0->flg=false;
            }
        }
    }
}