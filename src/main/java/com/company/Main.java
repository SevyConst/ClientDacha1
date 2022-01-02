package com.company;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class Main {
    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(Main.class);

        if (!isCorrectArgs(args)) {
            logger.error("main method: wrong input arguments");
            return;
        }

        ForProperties forProperties = new ForProperties();
        if (!forProperties.load(args[0], logger)) {
            return;
        }


        Sqlite sqlite = new Sqlite(forProperties.getUrlForSql(), logger);
        Client client = new Client(
                forProperties.getIp(), forProperties.getPort(), logger);

        Controller controller = new Controller(sqlite, client, logger);
        controller.launch();

        System.out.println("Done!");
    }

    private static boolean isCorrectArgs(String[] args) {
        return 1 == args.length && args[0].startsWith(ForProperties.ARG_START);
    }
}
