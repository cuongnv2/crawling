/**
 * Created by cuongnguyen on 4/25/18.
 */
import org.apache.log4j.*;

public final class Helper {
    public static void setupLogging()  {
        Logger rootLogger = Logger.getRootLogger();
        rootLogger.setLevel(Level.ERROR);
    }
}
