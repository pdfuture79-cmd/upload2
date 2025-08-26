package egovframework.common.grid;

import java.text.SimpleDateFormat;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig.Feature;

/**
 * json 맴핑하는 class
 * @author wiseopen
 * @since 2014.04.17
 * @version 1.0
 * @see
 *
 */

public class CustomJacksonObjectMapper  extends ObjectMapper {
	public CustomJacksonObjectMapper() {
		
		super.configure(Feature.WRITE_DATES_AS_TIMESTAMPS, false);
		setDateFormat(new SimpleDateFormat("yyyyMMdd HHmmss"));
	}

}
