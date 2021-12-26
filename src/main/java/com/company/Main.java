package com.company;

public class Main {

    public static void main(String[] args) {

        // TODO: Get time

        if (!isCorrectArgs(args)) {
            return;
        }

        ForProperties forProperties = new ForProperties();
        if (!forProperties.load(args[0])) {
            return;
        }

        System.out.println("Done!");
    }

    private static boolean isCorrectArgs(String[] args) {
        return 1 == args.length && args[0].startsWith(ForProperties.ARG_START);
    }
}
