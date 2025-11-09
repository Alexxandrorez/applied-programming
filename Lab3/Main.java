package org.example;

import org.example.Droids.BlueDroid;
import org.example.Droids.Droid;
import org.example.Droids.GreenDroid;
import org.example.Droids.RedDroid;
import org.example.Droids.TypeOfDroids.TypeOfDroids;

import java.util.*;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ArrayList<Droid> droids = DroidStorage.loadDroids();

        boolean running = true;
        while (running) {
            System.out.println("""

                    ===== ⚙️ МЕНЮ =====
                    1 - Створити дроїда
                    2 - Показати список дроїдів
                    3 - Бій 1 на 1
                    4 - Бій 3 на 3
                    5 - Відтворити бій 1v1
                    6 - Відтворити бій 3v3
                    7 - Режим Виживання
                    8 - Турнір
                    0 - Вихід
                    ==================
                    """);
            System.out.print("Ваш вибір: ");
            int vybir = sc.nextInt();
            sc.nextLine();

            switch (vybir) {
                case 1 -> createDroid(droids, sc);
                case 2 -> printAllDroids(droids);
                case 3 -> start1v1battle(droids, sc);
                case 4 -> start3v3battle(droids, sc);
                case 5 -> BattleLogger.readFromFile("battle_1v1.txt");
                case 6 -> BattleLogger.readFromFile("battle_3v3.txt");
                case 7 -> startSurvivalMode(droids, sc);
                case 8 -> startTournamentMode(droids, sc); // ОНОВЛЕНО
                case 0 -> running = false;
                default -> System.out.println("❌ Невірна команда!");
            }
        }
    }


    public static void startTournamentMode(ArrayList<Droid> allDroids, Scanner sc) {
        if (allDroids.size() < 8) {
            System.out.println(ConsoleColor.RED + "❗ Потрібно мінімум 8 дроїдів для турніру!" + ConsoleColor.RESET);
            return;
        }

        BattleLogger log = new BattleLogger();
        log.add("\n" + ConsoleColor.CYAN + "=== 🏆 ПОЧАТОК ТУРНІРУ 🏆 ===" + ConsoleColor.RESET);
        log.add("Виберіть 8 дроїдів-учасників.");

        ArrayList<Droid> participants = selectTeam(allDroids, sc, 8, new ArrayList<>());

        ArrayList<Droid> semiFinalists = new ArrayList<>();
        ArrayList<Droid> finalists = new ArrayList<>();
        Droid champion = null;

        printBracket(participants, null, null, null, log);
        log.add("\n" + ConsoleColor.CYAN + "--- ⚔️ ЧВЕРТЬФІНАЛИ ⚔️ ---" + ConsoleColor.RESET);
        log.delay(1000);

        semiFinalists.add(runTournamentBattle(participants.get(0), participants.get(1), log, 1));
        semiFinalists.add(runTournamentBattle(participants.get(2), participants.get(3), log, 2));
        semiFinalists.add(runTournamentBattle(participants.get(4), participants.get(5), log, 3));
        semiFinalists.add(runTournamentBattle(participants.get(6), participants.get(7), log, 4));

        printBracket(participants, semiFinalists, null, null, log);
        log.add("\n" + ConsoleColor.CYAN + "--- ⚔️ ПІВФІНАЛИ ⚔️ ---" + ConsoleColor.RESET);
        log.delay(1000);

        finalists.add(runTournamentBattle(semiFinalists.get(0), semiFinalists.get(1), log, 5));
        finalists.add(runTournamentBattle(semiFinalists.get(2), semiFinalists.get(3), log, 6));

        printBracket(participants, semiFinalists, finalists, null, log);
        log.add("\n" + ConsoleColor.RED + "--- ⚔️ 🔥🔥🔥 ФІНАЛ 🔥🔥🔥 ⚔️ ---" + ConsoleColor.RESET);
        log.delay(1000);

        champion = runTournamentBattle(finalists.get(0), finalists.get(1), log, 7);

        printBracket(participants, semiFinalists, finalists, champion, log);

        log.add("\n" + "=".repeat(35));
        log.add(ConsoleColor.YELLOW + "🏆🏆🏆 ЧЕМПІОН ТУРНІРУ 🏆🏆🏆" + ConsoleColor.RESET);
        log.add(ConsoleColor.GREEN + "       " + champion.getName().toUpperCase() + "!" + ConsoleColor.RESET);
        log.add("=".repeat(35));

        allDroids.forEach(Droid::restore);
        System.out.println("💖 Усі ваші дроїди відновили здоров’я!");
    }

    private static void printBracket(ArrayList<Droid> participants,
                                     ArrayList<Droid> semiFinalists,
                                     ArrayList<Droid> finalists,
                                     Droid champion,
                                     BattleLogger log) {

        log.add("\n" + ConsoleColor.CYAN + "=== 🏆 ТУРНІРНА СІТКА 🏆 ===" + ConsoleColor.RESET);

        String[] qf = new String[8];
        String[] sf = new String[4];
        String[] f = new String[2];
        String champ = (champion != null)
                ? "🏆 " + champion.getName().toUpperCase() + " 🏆"
                : "---";

        for (int i = 0; i < 8; i++) qf[i] = participants.get(i).getName();

        for (int i = 0; i < 4; i++) {
            sf[i] = (semiFinalists != null && i < semiFinalists.size())
                    ? semiFinalists.get(i).getName()
                    : "......."; //
        }

        for (int i = 0; i < 2; i++) {
            f[i] = (finalists != null && i < finalists.size())
                    ? finalists.get(i).getName()
                    : ".......";
        }


        log.add(String.format("%-15s ┐", qf[0]));
        log.add(String.format("%-15s ┘ %-15s ┐", qf[1], sf[0]));
        log.add(String.format("%-15s ┐ │ %-15s ┐", qf[2], ""));
        log.add(String.format("%-15s ┘ %-15s ┘ │", qf[3], sf[1]));
        log.add(String.format("%-15s %-15s %-15s ┐", "", "", f[0]));
        log.add(String.format("%-15s %-15s %-15s ┴ %s%s%s", "", "", "",
                ConsoleColor.YELLOW, champ, ConsoleColor.RESET));
        log.add(String.format("%-15s %-15s %-15s ┌", "", "", f[1]));
        log.add(String.format("%-15s ┐ │ %-15s ┐", qf[4], ""));
        log.add(String.format("%-15s ┘ %-15s ┌ │", qf[5], sf[2]));
        log.add(String.format("%-15s ┐ %-15s ┘", qf[6], ""));
        log.add(String.format("%-15s ┘ %-15s ┘", qf[7], sf[3]));

        log.delay(2500);
    }

    private static Droid runTournamentBattle(Droid d1, Droid d2, BattleLogger log, int matchNumber) {
        log.add(String.format("\n--- Матч #%d: %s%s%s vs %s%s%s ---",
                matchNumber, ConsoleColor.GREEN, d1.getName(), ConsoleColor.RESET,
                ConsoleColor.RED, d2.getName(), ConsoleColor.RESET));

        d1.restore();
        d2.restore();

        Random rand = new Random();
        int round = 1;

        while (d1.isAlive() && d2.isAlive()) {
            log.add("~ Раунд " + round + " ~");
            Droid attacker = rand.nextBoolean() ? d1 : d2;
            Droid target = attacker == d1 ? d2 : d1;
            String attackerColor = (attacker == d1) ? ConsoleColor.GREEN : ConsoleColor.RED;
            String targetColor = (target == d1) ? ConsoleColor.GREEN : ConsoleColor.RED;

            logAttack(attacker, target, attacker.getDamage(), log, attackerColor, targetColor);

            printDroidStatus(d1);
            printDroidStatus(d2);
            log.delay(500);
            round++;
        }

        Droid winner = d1.isAlive() ? d1 : d2;
        log.add(ConsoleColor.GREEN + "Переміг: " + winner.getName() + ConsoleColor.RESET);
        log.delay(1000);
        return winner;
    }



    public static void startSurvivalMode(ArrayList<Droid> allDroids, Scanner sc) {
        if (allDroids.size() < 3) {
            System.out.println(ConsoleColor.RED + "❗ Потрібно мінімум 3 дроїди для цього режиму!" + ConsoleColor.RESET);
            return;
        }
        System.out.println("\n" + ConsoleColor.YELLOW + "=== ☠️ РЕЖИМ ВИЖИВАННЯ ☠️ ===" + ConsoleColor.RESET);
        System.out.println("Виберіть свою команду з 3-х дроїдів. Їх здоров'я НЕ буде відновлюватися між хвилями.");
        ArrayList<Droid> playerTeam = selectTeam(allDroids, sc, 3, new ArrayList<>());
        playerTeam.forEach(Droid::restore);
        int waveCount = 0;
        boolean battleOn = true;
        BattleLogger log = new BattleLogger();
        while (battleOn) {
            waveCount++;
            log.add("\n" + ConsoleColor.YELLOW + "🌊🌊🌊 ХВИЛЯ " + waveCount + " 🌊🌊🌊" + ConsoleColor.RESET);
            log.delay(1500);
            int enemyCount = 3 + (waveCount / 3);
            ArrayList<Droid> enemyTeam = generateEnemyTeam(enemyCount, waveCount);
            log.add("На вас насувається " + enemyCount + " ворогів!");
            log.delay(1000);
            boolean waveWon = runBattleWave(playerTeam, enemyTeam, log, sc, waveCount);
            if (waveWon) {
                log.add(ConsoleColor.GREEN + "🏆 Хвилю " + waveCount + " пройдено!" + ConsoleColor.RESET);
                log.delay(2000);
            } else {
                log.add(ConsoleColor.RED + "\n☠️☠️☠️ Вашу команду знищено. ☠️☠️☠️" + ConsoleColor.RESET);
                battleOn = false;
            }
        }
        System.out.println("\n" + "=".repeat(30));
        System.out.println(ConsoleColor.YELLOW + "РЕЖИМ ВИЖИВАННЯ ЗАВЕРШЕНО" + ConsoleColor.RESET);
        System.out.println("Ваш результат: " + (waveCount - 1) + " повних хвиль.");
        System.out.println("=".repeat(30));
        allDroids.forEach(Droid::restore);
        System.out.println("💖 Усі ваші дроїди відновили здоров’я!");
    }

    private static ArrayList<Droid> generateEnemyTeam(int count, int wave) {
        ArrayList<Droid> team = new ArrayList<>();
        Random rand = new Random();
        for (int i = 0; i < count; i++) {
            String name = "Ворог-Х" + wave + "-" + (i + 1);
            Droid enemyDroid = null;
            int type = rand.nextInt(3);
            switch (type) {
                case 0 -> enemyDroid = new RedDroid(name);
                case 1 -> enemyDroid = new BlueDroid(name);
                case 2 -> enemyDroid = new GreenDroid(name);
            }
            if (enemyDroid != null) {
                enemyDroid.setHealth(enemyDroid.getMaxHealth() / 2);
                team.add(enemyDroid);
            }
        }
        return team;
    }

    private static boolean runBattleWave(ArrayList<Droid> playerTeam, ArrayList<Droid> enemyTeam, BattleLogger log, Scanner sc, int waveNum) {
        boolean waveRunning = true;
        int round = 1;
        Random rand = new Random();
        while (waveRunning) {
            log.delay(1000);
            log.add("\n" + ConsoleColor.CYAN + "--- Хвиля " + waveNum + " | Раунд " + round + " ---" + ConsoleColor.RESET);
            printTeamStatus(playerTeam, enemyTeam);
            log.delay(1500);
            if (teamAlive(playerTeam)) {
                System.out.println(ConsoleColor.GREEN + "\n--- Ваш хід ---" + ConsoleColor.RESET);
                for (Droid attacker : playerTeam) {
                    if (attacker.isAlive()) {
                        if (!teamAlive(enemyTeam)) {
                            waveRunning = false;
                            break;
                        }
                        System.out.println("\nОберіть ціль для " + ConsoleColor.GREEN + attacker.getName() + ConsoleColor.RESET + ":");
                        printTeamStatus(playerTeam, enemyTeam);
                        int targetIndex;
                        do {
                            System.out.print("Номер цілі (індекс з команди супротивника): ");
                            targetIndex = sc.nextInt();
                            sc.nextLine();
                            if (targetIndex < 0 || targetIndex >= enemyTeam.size() || !enemyTeam.get(targetIndex).isAlive()) {
                                System.out.println(ConsoleColor.RED + "❌ Невірна ціль!" + ConsoleColor.RESET);
                            } else break;
                        } while (true);
                        Droid target = enemyTeam.get(targetIndex);
                        logAttack(attacker, target, attacker.getDamage(), log, ConsoleColor.GREEN, ConsoleColor.RED);
                        printTeamStatus(playerTeam, enemyTeam);
                        log.delay(700);
                    }
                }
            }
            if (!waveRunning || !teamAlive(enemyTeam)) break;
            if (teamAlive(enemyTeam)) {
                System.out.println(ConsoleColor.RED + "\n--- Хід ворогів ---" + ConsoleColor.RESET);
                log.delay(1000);
                for (Droid attacker : enemyTeam) {
                    if (attacker.isAlive()) {
                        if (!teamAlive(playerTeam)) {
                            waveRunning = false;
                            break;
                        }
                        Droid target;
                        do {
                            target = playerTeam.get(rand.nextInt(playerTeam.size()));
                        } while (!target.isAlive());
                        logAttack(attacker, target, attacker.getDamage(), log, ConsoleColor.RED, ConsoleColor.GREEN);
                        printTeamStatus(playerTeam, enemyTeam);
                        log.delay(700);
                    }
                }
            }
            round++;
            if (!teamAlive(playerTeam) || !teamAlive(enemyTeam)) {
                waveRunning = false;
            }
        }
        return teamAlive(playerTeam);
    }



    public static void createDroid(ArrayList<Droid> droids, Scanner sc) {
        System.out.println("Оберіть тип дроїда: REDDROID, BLUEDROID, GREENDROID");
        String vybir = sc.nextLine().toUpperCase();
        try {
            TypeOfDroids selectedType = TypeOfDroids.valueOf(vybir);
            System.out.print("Введіть ім'я дроїда: ");
            String name = sc.nextLine();
            switch (selectedType) {
                case REDDROID -> droids.add(new RedDroid(name));
                case BLUEDROID -> droids.add(new BlueDroid(name));
                case GREENDROID -> droids.add(new GreenDroid(name));
            }
            DroidStorage.saveDroids(droids);
        } catch (IllegalArgumentException e) {
            System.out.println("❌ Невірний тип дроїда!");
        }
    }

    public static void printAllDroids(ArrayList<Droid> droids) {
        if (droids.isEmpty()) {
            System.out.println("❗ Список дроїдів порожній!");
            return;
        }
        System.out.println("=== 🤖 Усі створені дроїди ===");
        for (int i = 0; i < droids.size(); i++) {
            printDroidStatus(droids.get(i), i);
        }
    }

    public static void printDroidStatus(Droid d, int index) {
        String color;
        double vidsotokzdorovja = (double) d.getHealth() / d.getMaxHealth();
        if (vidsotokzdorovja > 0.6) color = ConsoleColor.GREEN;
        else if (vidsotokzdorovja > 0.3) color = ConsoleColor.YELLOW;
        else color = ConsoleColor.RED;

        System.out.printf("%d → %s%s%s HP: [%s] %d/%d ⚔️ %d\n", index, color, d.getName(), ConsoleColor.RESET,
                getHpBar(d), d.getHealth(), d.getMaxHealth(), d.getDamage());
    }

    public static void printDroidStatus(Droid d) {
        String color;
        double vidsotokzdorovja = (double) d.getHealth() / d.getMaxHealth();
        if (vidsotokzdorovja > 0.6) color = ConsoleColor.GREEN;
        else if (vidsotokzdorovja > 0.3) color = ConsoleColor.YELLOW;
        else color = ConsoleColor.RED;

        System.out.printf("→ %s%s%s HP: [%s] %d/%d ⚔️ %d\n", color, d.getName(), ConsoleColor.RESET,
                getHpBar(d), d.getHealth(), d.getMaxHealth(), d.getDamage());
    }

    public static String getDroidStatusString(Droid d, int index) {
        return String.format("%d → %s HP: [%s] %d/%d ⚔️ %d", index, d.getName(),
                getHpBar(d), d.getHealth(), d.getMaxHealth(), d.getDamage());
    }

    public static String getDroidStatusString(Droid d) {
        return String.format("→ %s HP: [%s] %d/%d ⚔️ %d", d.getName(),
                getHpBar(d), d.getHealth(), d.getMaxHealth(), d.getDamage());
    }

    public static String getHpBar(Droid d) {
        int maxBarsZdorovja = 20;
        int povnyjBars = (int) ((double) d.getHealth() / d.getMaxHealth() * maxBarsZdorovja);
        StringBuilder bar = new StringBuilder();
        for (int i = 0; i < povnyjBars; i++) bar.append("█");
        for (int i = povnyjBars; i < maxBarsZdorovja; i++) bar.append("░");
        return bar.toString();
    }

    public static void logAttack(Droid attacker, Droid target, int damage, BattleLogger log, String attackerColor, String targetColor) {
        Random rand = new Random();
        String[] effects = {"💥", "⚡", "🗡️", "🔥", "💨"};
        String effect = effects[rand.nextInt(effects.length)];

        int chance = rand.nextInt(200);
        String podija = "";
        if (chance < 10) {
            damage *= 2;
            podija = " ⚡ КРИТИЧНИЙ удар!";
        } else if (chance < 20) {
            podija = " 😵 спіткнувся і пропустив хід!";
            damage = 0;
        } else if (chance < 30) {
            int heal = attacker.getMaxHealth() / 10;
            attacker.setHealth(attacker.getHealth() + heal);
            podija = " 💚 регенерація +" + heal + " HP!";
            damage = 0;
        } else if (chance < 40) {
            int reduce = target.getDamage() / 2;
            damage -= reduce;
            if (damage < 0) damage = 0;
            podija = " 🛡️ активував щит, отримав на " + reduce + " менше шкоди!";
        } else if (chance < 50) {
            int drain = damage / 2;
            attacker.setHealth(attacker.getHealth() + drain);
            podija = " 🧛 ВАМПІРИЗМ! Викрав " + drain + " HP!";
        }

        if (damage > 0) target.takeDamage(damage);

        String message = String.format("%s%s%s %s атакує %s%s%s на %d шкоди!%s",
                attackerColor, attacker.getName(), ConsoleColor.RESET, effect,
                targetColor, target.getName(), ConsoleColor.RESET, damage, podija);

        log.add(message);
    }

    public static void start1v1battle(ArrayList<Droid> droids, Scanner sc) {
        if (droids.size() < 2) {
            System.out.println("❗ Створіть хоча б двох дроїдів!");
            return;
        }
        BattleLogger log = new BattleLogger();
        printAllDroids(droids);
        System.out.print("Виберіть першого дроїда: ");
        int a = sc.nextInt();
        System.out.print("Виберіть другого дроїда: ");
        int b = sc.nextInt();
        sc.nextLine();
        if (a < 0 || b < 0 || a >= droids.size() || b >= droids.size() || a == b) {
            System.out.println("❌ Невірний вибір дроїдів!");
            return;
        }
        Droid d1 = droids.get(a);
        Droid d2 = droids.get(b);
        Random rand = new Random();
        int round = 1;
        log.add("\n⚔️ Починається ЕПІЧНИЙ БІЙ: " + d1.getName() + " vs " + d2.getName());
        log.delay(700);
        while (d1.isAlive() && d2.isAlive()) {
            log.add("\n🔥 Раунд " + round + " 🔥");
            Droid attacker = rand.nextBoolean() ? d1 : d2;
            Droid target = attacker == d1 ? d2 : d1;
            String attackerColor = (attacker == d1) ? ConsoleColor.GREEN : ConsoleColor.RED;
            String targetColor = (target == d1) ? ConsoleColor.GREEN : ConsoleColor.RED;
            logAttack(attacker, target, attacker.getDamage(), log, attackerColor, targetColor);
            log.add(getDroidStatusString(d1, a));
            log.add(getDroidStatusString(d2, b));
            printDroidStatus(d1, a);
            printDroidStatus(d2, b);
            log.delay(700);
            round++;
        }
        Droid winner = d1.isAlive() ? d1 : d2;
        log.add("\n🏆 Переможець: " + winner.getName() + " (" + winner.getTypeName() + ")");
        log.saveToFile("battle_1v1.txt");
        droids.forEach(Droid::restore);
        System.out.println("💖 Дроїди відновили свої сили!");
    }

    public static void start3v3battle(ArrayList<Droid> droids, Scanner sc) {
        if (droids.size() < 6) {
            System.out.println("❗ Потрібно мінімум 6 дроїдів для битви 3 на 3!");
            return;
        }
        System.out.println("\nОберіть режим бою 3v3:");
        System.out.println("1 - Гравець проти Комп'ютера (ручний режим)");
        System.out.println("2 - Автоматичний бій (комп'ютер проти комп'ютера)");
        System.out.print("Ваш вибір: ");
        int mode = sc.nextInt();
        sc.nextLine();
        BattleLogger log = new BattleLogger();
        log.add("=== ⚔️ Початок ЕПІЧНОЇ битви 3v3 ===");
        ArrayList<Droid> team1;
        ArrayList<Droid> team2;
        if (mode == 1) {
            System.out.println("\n" + ConsoleColor.GREEN + "=== Виберіть 3 дроїдів для ВАШОЇ команди ===" + ConsoleColor.RESET);
            team1 = selectTeam(droids, sc, 3, new ArrayList<>());
            System.out.println("\n" + ConsoleColor.RED + "=== Виберіть 3 дроїдів для команди СУПРОТИВНИКА ===" + ConsoleColor.RESET);
            team2 = selectTeam(droids, sc, 3, team1);
        } else {
            System.out.println("\n🤖 Команди обираються випадковим чином...");
            team1 = selectRandomTeam(droids, 3, new ArrayList<>());
            team2 = selectRandomTeam(droids, 3, team1);
            log.add("Команди були обрані випадковим чином для авто-бою.");
            log.delay(1500);
        }
        boolean battleOn = true;
        int round = 1;
        Random rand = new Random();
        while (battleOn) {
            log.delay(1000);
            log.add("\n🔥 Раунд " + round + " 🔥");
            printTeamStatus(team1, team2);
            log.add(getTeamStatusString(team1, team2));
            log.delay(1500);
            if (teamAlive(team1)) {
                System.out.println(ConsoleColor.GREEN + "\n--- Хід команди 1 ---" + ConsoleColor.RESET);
                for (Droid attacker : team1) {
                    if (attacker.isAlive()) {
                        if (!teamAlive(team2)) {
                            battleOn = false;
                            break;
                        }
                        Droid target;
                        if (mode == 1) {
                            System.out.println("\nОберіть ціль для " + ConsoleColor.GREEN + attacker.getName() + ConsoleColor.RESET + ":");
                            printTeamStatus(team1, team2);
                            int targetIndex;
                            do {
                                System.out.print("Номер цілі (індекс з команди супротивника): ");
                                targetIndex = sc.nextInt();
                                sc.nextLine();
                                if (targetIndex < 0 || targetIndex >= team2.size() || !team2.get(targetIndex).isAlive()) {
                                    System.out.println(ConsoleColor.RED + "❌ Невірна ціль!" + ConsoleColor.RESET);
                                } else break;
                            } while (true);
                            target = team2.get(targetIndex);
                        } else {
                            do {
                                target = team2.get(rand.nextInt(team2.size()));
                            } while (!target.isAlive());
                        }
                        logAttack(attacker, target, attacker.getDamage(), log, ConsoleColor.GREEN, ConsoleColor.RED);
                        log.add(getTeamStatusString(team1, team2));
                        printTeamStatus(team1, team2);
                        log.delay(700);
                    }
                }
            }
            if (!battleOn) break;
            if (teamAlive(team2)) {
                System.out.println(ConsoleColor.RED + "\n--- Хід команди 2 ---" + ConsoleColor.RESET);
                log.delay(1000);
                for (Droid attacker : team2) {
                    if (attacker.isAlive()) {
                        if (!teamAlive(team1)) {
                            battleOn = false;
                            break;
                        }
                        Droid target;
                        do {
                            target = team1.get(rand.nextInt(team1.size()));
                        } while (!target.isAlive());
                        logAttack(attacker, target, attacker.getDamage(), log, ConsoleColor.RED, ConsoleColor.GREEN);
                        log.add(getTeamStatusString(team1, team2));
                        printTeamStatus(team1, team2);
                        log.delay(700);
                    }
                }
            }
            round++;
            if (!teamAlive(team1) || !teamAlive(team2)) {
                battleOn = false;
            }
        }
        printTeamStatus(team1, team2);
        if (teamAlive(team1)) {
            log.add("\n" + ConsoleColor.GREEN + "🏆 Перемогла команда 1!" + ConsoleColor.RESET);
        } else {
            log.add("\n" + ConsoleColor.RED + "🏆 Перемогла команда 2!" + ConsoleColor.RESET);
        }
        log.saveToFile("battle_3v3.txt");
        droids.forEach(Droid::restore);
        System.out.println("💖 Усі дроїди відновили здоров’я!");
    }

    private static ArrayList<Droid> selectTeam(ArrayList<Droid> allDroids, Scanner sc, int count, List<Droid> excludedDroids) {
        ArrayList<Droid> team = new ArrayList<>();
        ArrayList<Droid> availableDroids = new ArrayList<>(allDroids);
        availableDroids.removeAll(excludedDroids);
        System.out.println("Оберіть дроїдів з доступного списку:");
        for (int i = 0; i < availableDroids.size(); i++) {
            printDroidStatus(availableDroids.get(i), allDroids.indexOf(availableDroids.get(i)));
        }
        while (team.size() < count) {
            System.out.print("Введіть номер дроїда #" + (team.size() + 1) + ": ");
            int id = sc.nextInt();
            sc.nextLine();
            if (id >= 0 && id < allDroids.size()) {
                Droid selectedDroid = allDroids.get(id);
                if (team.contains(selectedDroid) || excludedDroids.contains(selectedDroid)) {
                    System.out.println(ConsoleColor.RED + "❌ Цей дроїд вже обраний або недоступний!" + ConsoleColor.RESET);
                } else {
                    team.add(selectedDroid);
                    System.out.println(ConsoleColor.YELLOW + selectedDroid.getName() + " доданий до команди." + ConsoleColor.RESET);
                }
            } else {
                System.out.println(ConsoleColor.RED + "❌ Невірний номер дроїда!" + ConsoleColor.RESET);
            }
        }
        return team;
    }

    private static ArrayList<Droid> selectRandomTeam(ArrayList<Droid> allDroids, int count, List<Droid> excludedDroids) {
        ArrayList<Droid> team = new ArrayList<>();
        ArrayList<Droid> availableDroids = new ArrayList<>(allDroids);
        availableDroids.removeAll(excludedDroids);
        Collections.shuffle(availableDroids);
        for(int i = 0; i < count && i < availableDroids.size(); i++) {
            team.add(availableDroids.get(i));
        }
        return team;
    }

    private static boolean teamAlive(ArrayList<Droid> team) {
        for (Droid d : team) {
            if (d.isAlive()) {
                return true;
            }
        }
        return false;
    }

    private static void printTeamStatus(ArrayList<Droid> team1, ArrayList<Droid> team2) {
        System.out.println("\n" + "=".repeat(75));
        String team1Name = ConsoleColor.GREEN + "  Ваша команда" + ConsoleColor.RESET;
        String team2Name = ConsoleColor.RED + "  Команда ворогів" + ConsoleColor.RESET;

        System.out.printf("%-36s | %s\n", team1Name, team2Name);
        System.out.println("-".repeat(75));
        int maxSize = Math.max(team1.size(), team2.size());
        for (int i = 0; i < maxSize; i++) {
            String left, right;
            if (i < team1.size()) {
                Droid d = team1.get(i);
                String status = d.isAlive() ?
                        String.format("[%s] %d/%d", getHpBar(d), d.getHealth(), d.getMaxHealth()) :
                        "[✖️ ЗНИЩЕНИЙ ✖️]";
                left = String.format("%d: %s %s", i, d.getName(), status);
            } else left = "";
            if (i < team2.size()) {
                Droid d = team2.get(i);
                String status = d.isAlive() ?
                        String.format("[%s] %d/%d", getHpBar(d), d.getHealth(), d.getMaxHealth()) :
                        "[✖️ ЗНИЩЕНИЙ ✖️]";
                right = String.format("%d: %s %s", i, d.getName(), status);
            } else right = "";
            System.out.printf("%-42s | %s\n", ConsoleColor.GREEN + left + ConsoleColor.RESET, ConsoleColor.RED + right + ConsoleColor.RESET);
        }
        System.out.println("=".repeat(75));
    }

    private static String getTeamStatusString(ArrayList<Droid> team1, ArrayList<Droid> team2) {
        StringBuilder sb = new StringBuilder();
        sb.append("\n").append("=".repeat(75)).append("\n");
        sb.append(String.format("%-36s | %s\n", "  Команда 1", "  Команда 2"));
        sb.append("-".repeat(75)).append("\n");
        int maxSize = Math.max(team1.size(), team2.size());
        for (int i = 0; i < maxSize; i++) {
            String left, right;
            if (i < team1.size()) {
                Droid d = team1.get(i);
                String status = d.isAlive() ?
                        String.format("[%s] %d/%d", getHpBar(d), d.getHealth(), d.getMaxHealth()) :
                        "[✖️ ЗНИЩЕНИЙ ✖️]";
                left = String.format("%d: %s %s", i, d.getName(), status);
            } else left = "";
            if (i < team2.size()) {
                Droid d = team2.get(i);
                String status = d.isAlive() ?
                        String.format("[%s] %d/%d", getHpBar(d), d.getHealth(), d.getMaxHealth()) :
                        "[✖️ ЗНИЩЕНИЙ ✖️]";
                right = String.format("%d: %s %s", i, d.getName(), status);
            } else right = "";
            sb.append(String.format("%-42s | %s\n", left, right));
        }
        sb.append("=".repeat(75));
        return sb.toString();
    }
}