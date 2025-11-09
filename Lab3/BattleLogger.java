package org.example;

import java.io.*;
import java.util.*;

public class BattleLogger {
    private final List<String> log = new ArrayList<>();


    public void add(String message) {
        log.add(message);
        if (!message.contains(" HP: [") && !message.contains("  Команда 1")) {
            System.out.println(message);
        }
    }

    public void delay(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignored) {}
    }

    public void saveToFile(String filename) {
        try (PrintWriter out = new PrintWriter(new FileWriter(filename))) {
            for (String line : log) out.println(line);
            System.out.println("✅ Бій збережено у файл: " + filename);
        } catch (IOException e) {
            System.out.println("❌ Помилка при записі у файл: " + e.getMessage());
        }
    }

    public static void readFromFile(String filename) {
        System.out.println("=== Відтворення бою ===");
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
                Thread.sleep(300);
            }
        } catch (Exception e) {
            System.out.println("❌ Помилка читання файлу: " + e.getMessage());
        }
    }
}