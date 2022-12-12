package com.example.util;

@SuppressWarnings("ALL")
public class PrintUtils {

    public static void title(Object title) {
        println("");
        println("-" + title);
    }
    static final String RESET = "\033[0m";

    
    public static <T> T green(T title) {
        final String GREEN = "\u001B[32m";
        println("");
        println(GREEN + title + RESET);
        println("-" + title);
        return title;
    }

    public static <T> T red(T title) {
        final String RED = "\u001B[31m";
        println("");
        println(RED + title + RESET);
        println("-" + title);
        return title;
    }

    public static <T> T cyan(T title) {
        final String CYAN = "\u001B[36m";
        println("");
        println(CYAN + title + RESET);
        println("-" + title);
        return title;
    }

    public static <T> T println(T t) {
        System.out.println(t);
        return t;
    }

    public static <T> T println(String description, T t) {
        System.out.print("description: "+ description + " ");
        System.out.print(t);
        return t;
    }
}