package org.example;

import org.example.Droids.BlueDroid;
import org.example.Droids.Droid;
import org.example.Droids.GreenDroid;
import org.example.Droids.RedDroid;

import java.io.*;
import java.util.ArrayList;

public class DroidStorage {
    private static final String FILE_NAME = "droids.txt";

    public static ArrayList<Droid> loadDroids() {
        ArrayList<Droid> droids = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                String type = parts[0];
                String name = parts[1];

                switch (type) {
                    case "RedDroid" -> droids.add(new RedDroid(name));
                    case "BlueDroid" -> droids.add(new BlueDroid(name));
                    case "GreenDroid" -> droids.add(new GreenDroid(name));
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Файл з дроїдами не знайдено, буде створено новий.");
        } catch (IOException e) {
            System.out.println("Помилка при читанні дроїдів: " + e.getMessage());
        }
        return droids;
    }

    public static void saveDroids(ArrayList<Droid> droids) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Droid d : droids) {
                pw.println(d.getTypeName().split(" ")[0] + ";" + d.getName() + ";" + d.getHealth() + ";" + d.getDamage());
            }
        } catch (IOException e) {
            System.out.println("Помилка при збереженні дроїдів: " + e.getMessage());
        }
    }
}
