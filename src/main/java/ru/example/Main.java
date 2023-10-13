package ru.example;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Main {
    public final static Map<Integer, Integer> sizeToFreq = new HashMap<>();

    public static void main(String[] args) throws InterruptedException {

        Thread threadMax = new Thread(() -> {
            synchronized (sizeToFreq) {
                while (!Thread.interrupted()) {
                    try {
                        sizeToFreq.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    Map.Entry<Integer, Integer> entryMax = sizeToFreq.entrySet().stream()
                            .max(Map.Entry.comparingByValue())
                            .orElse(null);
                    System.out.printf("Текущий лидер: %d (%d раз)\n",
                            entryMax.getKey(), entryMax.getValue());
                }
            }
        });
        threadMax.start();

        for (int i = 0; i < 100; i++) {
            Thread thread = new Thread(() -> {
                String str = generateRoute("RLRFR", 100);
                int countR = (int) str.chars().filter(x -> x == 'R').count();

                synchronized (sizeToFreq) {
                    if (sizeToFreq.containsKey(countR)) {
                        sizeToFreq.put(countR, sizeToFreq.get(countR) + 1);
                    } else {
                        sizeToFreq.put(countR, 1);
                    }
                    sizeToFreq.notify();
                }
            });
            thread.start();
            thread.join();
        }
        threadMax.interrupt();

        Map.Entry<Integer, Integer> entryMax = sizeToFreq.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .orElse(null);
        System.out.printf("Самое частое количество повторений %d (%d раз)\n",
                entryMax.getKey(), entryMax.getValue());
        System.out.println("Другие размеры:");
        for (Map.Entry<Integer, Integer> entry : sizeToFreq.entrySet()) {
            System.out.printf("- %d (%d раз)\n", entry.getKey(), entry.getValue());
        }
    }

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            char letter = letters.charAt(random.nextInt(letters.length()));
            route.append(letter);
        }
        return route.toString();
    }

}