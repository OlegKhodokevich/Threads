package by.khodokevich.port.validator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ShipDataValidator {
    private static final Logger LOGGER = LogManager.getLogger();

    private static final String SHIP_DATA_REG_EXP = "\\s*<capacity>\\s*\\d{1,3}\\s*<ArrivalPurpose>\\s*[a-zA-Z]{1,10}\\s*";

    public static boolean validateShipData (String line) {
        LOGGER.info("Start validateShipData (String line). Line = " + line);
        boolean result = line.matches(SHIP_DATA_REG_EXP);
        LOGGER.info("End validateShipData (String line). Result = " + result);
        return result;
    }
}
