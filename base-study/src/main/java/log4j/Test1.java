package log4j;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

public class Test1 {
	private static final Logger LOGGER = LogManager.getLogger(Test1.class);

	@Test
	public void Test() {
		LOGGER.trace("trace");
		LOGGER.debug("debug");
		LOGGER.info("info");
		LOGGER.warn("warn");
		LOGGER.error("error");
		LOGGER.fatal("fatal");
	}

	@Test
    public void Test2(){
	    LOGGER.info(LOGGER);
	    LOGGER.info(LOGGER.getClass().getName());
    }

    @Test
    public void Test3(){
	    LOGGER.info("{%d %-5p [%t] %C{2} (%F:%L) - %m%n}");
    }

}
