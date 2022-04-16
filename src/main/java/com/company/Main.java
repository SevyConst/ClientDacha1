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


        Db db = new Db(forProperties.getUrlForDb(), logger);
        httpClient httpClient = new httpClient(
                forProperties.getIp(), forProperties.getPort(), logger);

        Controller controller = new Controller(db, httpClient,
                forProperties.getDeviceId(), forProperties.getPeriod(),
                logger);
        controller.launch();

        System.out.println("Done!");
    }

    private static boolean isCorrectArgs(String[] args) {
        return 1 == args.length && args[0].startsWith(ForProperties.ARG_START);
    }
}
