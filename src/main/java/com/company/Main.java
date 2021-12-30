package com.company;

public class Main {

    public static void main(String[] args) {

        if (!isCorrectArgs(args)) {
            return;
        }

        ForProperties forProperties = new ForProperties();
        if (!forProperties.load(args[0])) {
            return;
        }

        Sqlite sqlite = new Sqlite(forProperties.getUrlForSql());
        Client client = new Client(forProperties.getIp(), forProperties.getPort());

        Controller controller = new Controller(sqlite, client);
        controller.work();

        System.out.println("Done!");
    }

    private static boolean isCorrectArgs(String[] args) {
        return 1 == args.length && args[0].startsWith(ForProperties.ARG_START);
    }
}
