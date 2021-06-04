package by.khodokevich.port.parser;

import by.khodokevich.port.entity.ArrivalPurpose;
import by.khodokevich.port.entity.Ship;
import by.khodokevich.port.entity.ShipFactory;
import by.khodokevich.port.validator.ShipDataValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShipDataParser {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String INTEGER_REG_EXP = "\\d+";
    private static final String WORD_REG_EXP = "\\p{Alpha}+";
    private static final String CAPACITY_REG_EXP = "<capacity>";
    private static final String ARRIVAL_PURPOSE_REG_EXP = "<ArrivalPurpose>";


    public List<Ship> parseShipData (List<String> dataShip) {
        LOGGER.info("Start parseShipData (List<String> dataShip). Data = " + dataShip);
        Pattern patternCapacity = Pattern.compile(CAPACITY_REG_EXP);
        Pattern patternPurpose = Pattern.compile(ARRIVAL_PURPOSE_REG_EXP);
        Pattern patternInteger = Pattern.compile(INTEGER_REG_EXP);
        Pattern patternWord = Pattern.compile(WORD_REG_EXP);
        Matcher matcher;

        int capacity;
        ArrivalPurpose arrivalPurpose;

        List<Ship> ships = new ArrayList<>();
        ShipFactory shipFactory = new ShipFactory();

        for (int i = 0; i < dataShip.size(); i++) {
            String shipString = dataShip.get(i);
            if (ShipDataValidator.validateShipData(shipString)) {
                matcher = patternCapacity.matcher(shipString);
                matcher.find();
                int endIndex = matcher.end();

                matcher = patternInteger.matcher(shipString);
                matcher.find(endIndex + 1);
                capacity = Integer.parseInt(matcher.group());

                matcher = patternPurpose.matcher(shipString);
                matcher.find();
                endIndex = matcher.end();

                matcher = patternWord.matcher(shipString);
                matcher.find(endIndex + 1);
                arrivalPurpose = ArrivalPurpose.valueOf(matcher.group().toUpperCase());

                Ship ship = shipFactory.createShip(capacity, arrivalPurpose);
                ships.add(ship);
            }
        }

        LOGGER.info("End parseShipData (List<String> dataShip). Ships = " + ships);
        return ships;
    }
}
