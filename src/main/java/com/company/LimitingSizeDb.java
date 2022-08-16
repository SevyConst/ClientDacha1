package com.company;

import org.slf4j.Logger;

import java.util.concurrent.TimeUnit;

public class LimitingSizeDb implements Runnable {

    private final long maxRows;
    private final Db db;
    private final long sleepSeconds;
    private final Logger logger;

    public LimitingSizeDb(long maxRows, Db db, long sleepSeconds, Logger logger) {
        this.maxRows = maxRows;
        this.db = db;
        this.sleepSeconds = sleepSeconds;
        this.logger = logger;

        Thread thread = new Thread(this, LimitingSizeDb.class.getName());
        thread.start();
    }

    @Override
    public void run() {
        while (true) {

            boolean isRemoved = false;  // Variable indicates are rows removed successfully

            long nRows = db.countRow();
            logger.info("rows: " + nRows);
            if (nRows > maxRows) {
                logger.info("Start clearing db");

                long nRowsToRemove = nRows - maxRows;
                isRemoved = db.removeRows(db.getMinId() + nRowsToRemove);

                if (isRemoved) {
                    logger.info("clearing db is finished");
                }
            }

            try {
                TimeUnit.SECONDS.sleep(sleepSeconds);
            } catch (InterruptedException e){
                logger.error("Interrupt sleeping", e);
            }
        }
    }
}
