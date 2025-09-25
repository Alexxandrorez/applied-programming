package org.example;

import java.util.ArrayList;
import java.util.Scanner;

public class Patient {
    private int id;
    private String name;
    private String surname;
    private String fatherName;
    private String Address;
    private int number;
    private int numbMedCard;
    private String diagnosis;

    public Patient(int id, String name, String surname, String fatherName, String address, int number, int numbMedCard, String diagnosis) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.fatherName = fatherName;
        this.Address = address;
        this.number = number;
        this.numbMedCard = numbMedCard;
        this.diagnosis = diagnosis;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getNumber() {
        return number;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public String getSurname() {
        return surname;
    }

    public String getFatherName() {
        return fatherName;
    }

    public String getAddress() {
        return Address;
    }

    public int getNumbMedCard() {
        return numbMedCard;
    }

    public static Patient createPatient(Scanner sc) {
        System.out.println("Id:");
        int id=sc.nextInt();
        sc.nextLine();

        System.out.println("Name:");
        String name=sc.nextLine();
        System.out.println("Surname:");
        String surname=sc.nextLine();
        System.out.println("Fathername:");
        String fathername=sc.nextLine();

        System.out.println("Adress:");
        String adress=sc.nextLine();

        System.out.println("Number:");
        int number=sc.nextInt();
        sc.nextLine();

        System.out.println("numbMedCard:");
        int numbMedCard=sc.nextInt();
        sc.nextLine();

        System.out.println("Diagnosis:");
        String diagnosis=sc.next();

        return new Patient(id,name,surname,fathername,adress,number,numbMedCard,diagnosis);
    }

    public static void findByDiagnosis(ArrayList<Patient> patiend, Scanner sc) {
        System.out.println("Введіть діагноз:");
        String diag=sc.nextLine();

        for(Patient p : patiend) {
            if(diag.equals(p.getDiagnosis())){
                System.out.println(p);
            }
        }
    }

    public static void findByIdMedCard(ArrayList<Patient> patiend, Scanner sc) {
        System.out.println("Введіть початок інтервалу:");
        int fisrtofinterval=sc.nextInt();
        sc.nextLine();

        System.out.println("Введіть кінець інтервалу:");
        int lastofinterval=sc.nextInt();
        sc.nextLine();

        for(Patient p:patiend){
            if(p.getNumbMedCard()>=fisrtofinterval&&p.getNumbMedCard()<=lastofinterval){
                System.out.println(p);
            }
        }
    }

    public static void findByFirstPhoneDigit(ArrayList<Patient> patients, Scanner sc) {
        System.out.println("Введіть першу цифру номера:");
        int digit = sc.nextInt();
        sc.nextLine();

        int calc=0;

        for (Patient p : patients) {
            int number = p.getNumber();
            while (number >= 10) {
                number /= 10;
            }

            if (number == digit) {
                calc++;
                System.out.println(p);
            }
        }
        System.out.println("Кількість людей=" + calc);
    }

    @Override
    public String toString() {
        return "Patient{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", surname='" + getSurname() + '\'' +
                ", fatherName='" + getFatherName() + '\'' +
                ", address='" + getAddress() + '\'' +
                ", number='" + getNumber() + '\'' +
                ", numbMedCard=" + getNumbMedCard() +
                ", diagnosis='" + getDiagnosis() + '\'' +
                '}';
    }
}
