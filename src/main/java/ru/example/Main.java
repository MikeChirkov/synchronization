package ru.example;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Main {
    public final static Map<Integer, Integer> sizeToFreq = new HashMap<>();

    public static void main(String[] args) {
        for (int i = 0; i < 10000; i++) {
            new Thread(() -> {
                int countR = 0;
                String str = generateRoute("RLRFR", 100);

                for (int j = 0; j < str.length(); j++) {
                    if (str.charAt(j) == 'R')
                        countR++;
                }

                synchronized (sizeToFreq) {
                    if (sizeToFreq.containsKey(countR)) {
                        sizeToFreq.put(countR, sizeToFreq.get(countR) + 1);
                    } else {
                        sizeToFreq.put(countR, 1);
                    }
                }
            }).start();
        }

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