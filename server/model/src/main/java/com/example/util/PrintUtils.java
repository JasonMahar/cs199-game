package com.example.util;

@SuppressWarnings("ALL")
public class PrintUtils {

    public static void title(Object title) {
        println("");
        println("-" + title);
    }
    static final String RESET = "\033[0m";

<<<<<<< HEAD:server/model/src/main/java/com/example/util/PrintUtils.java
=======
    public static void titleGreen(Object title) {
        final String GREEN = "\u001B[32m";
        println("");
        println(GREEN + title + RESET);
        println("-" + title);
    }

>>>>>>> develop:server/internal-module/src/main/java/com/example/util/PrintUtils.java
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
<<<<<<< HEAD:server/model/src/main/java/com/example/util/PrintUtils.java

=======
    public static void titleRed(Object title) {
        final String RED = "\u001B[31m";
        println("");
        println(RED + title + RESET);
        println("-"+ title);
    }

    public static void titleCyan(Object title) {
        final String CYAN = "\u001B[36m";
        println("");
        println(CYAN + title + RESET);
        println("-" + title);
    }
>>>>>>> develop:server/internal-module/src/main/java/com/example/util/PrintUtils.java
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