package by.khodokevich.port.reader;

import by.khodokevich.port.exception.ProjectPortException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class DataShipReader {
    private static final Logger LOGGER = LogManager.getLogger();
    public List<String> readShipData (String filename) throws ProjectPortException {
        LOGGER.info("Start readShipData (String filename). File = " + filename);
        List<String> dataLines;
        try {
            dataLines = Files.readAllLines(Paths.get(filename));
        } catch (IOException e) {
            throw new ProjectPortException("File can't be read. File = " + filename);
        }
        LOGGER.info("End readShipData (String filename). Has read next data = " + dataLines);
        return dataLines;

    }
}
