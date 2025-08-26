
package egovframework.common.util;

import java.io.IOException;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * json형태로 변환하는 class
 * @author wiseopen
 * @since 2014.04.17
 * @version 1.0
 * @see
 *
 */
public class UtilJson {
	
	protected static final Log logger = LogFactory.getLog(UtilJson.class);
	
	/**
	 * json을 형식으로 가공한다.
	 * @param obj
	 * @return
	 */
	public static String convertJsonString( Object obj) {
		
		String result = null;
		
		ObjectMapper om = new ObjectMapper();
		
			try {
				result = om.writeValueAsString(obj);
			} catch (JsonGenerationException e) {//시큐어코딩 조치 - Exception to String
	        	logger.debug(ExceptionUtils.getStackTrace(e)); 
				return "파싱에러";
			} catch (JsonMappingException e) {//시큐어코딩 조치 - Exception to String
	        	logger.debug(ExceptionUtils.getStackTrace(e)); 
				return "JSON매핑에러";
			} catch (IOException e) {//시큐어코딩 조치 - Exception to String
	        	logger.debug(ExceptionUtils.getStackTrace(e)); 
				return "파일에러";
			}
		return result;
	}

}
