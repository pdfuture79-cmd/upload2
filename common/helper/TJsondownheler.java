package egovframework.common.helper;

/**
 * Json 형태로 다운로드하는 Class
 * @author wiseopen
 * @since 2014.04.17
 * @version 1.0
 * @see
 *
 */
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import egovframework.common.util.UtilString;

@Component
public class TJsondownheler  implements ApplicationContextAware ,InitializingBean {

	protected static final Log logger = LogFactory.getLog(TJsondownheler.class);
	
	private ApplicationContext applicationContext;
	

	private ObjectMapper objectMapper = new ObjectMapper();

	
	
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub

	}
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		// TODO Auto-generated method stub
		this.applicationContext = applicationContext;
	}
	
	/**
	 * Json 형태로 다운로드한다.
	 * @param result
	 * @param head
	 * @param infNm
	 * @param response
	 * @param requset
	 * @throws Exception
	 */
	public void download(List<LinkedHashMap<String,?>> result ,LinkedHashMap<String,?> head, String infNm, HttpServletResponse response,HttpServletRequest requset) throws Exception{
		if(head != null){
			result.add(0, head);
		}
		List<LinkedHashMap<String,String>> newResult = new ArrayList<LinkedHashMap<String,String>>();
		for(LinkedHashMap<String,?> map : result){
			LinkedHashMap<String,String> map2 = new LinkedHashMap<String,String>();
			for(Entry<String,?> entry: map.entrySet()){   
				if(((String)entry.getKey()).equals("ITEM_CD1") || 
						((String)entry.getKey()).equals("ITEM_CD2") ||
						((String)entry.getKey()).equals("V_ORDER") ||   
						((String)entry.getKey()).equals("YYYYMQ") ||
						((String)entry.getKey()).equals("RN") ||
						((String)entry.getKey()).equals("CONV_CD_1")||
						((String)entry.getKey()).equals("WISEOPEN_CNT"))
				{
						continue;
				}
				if(entry.getValue() ==  null){
					map2.put((String)entry.getKey(), "");
				}else{
					map2.put((String)entry.getKey(), (entry.getValue()+""));
				}
			}
			try{
				newResult.add(map2);
			}catch(Exception e){//시큐어코딩 조치 - Exception to String
	        	logger.debug(ExceptionUtils.getStackTrace(e));
			}
			
		}
		String value = objectMapper.writeValueAsString(newResult);          
		String fileNm = UtilString.replace(infNm, " ", "")+".json";
		if(UtilString.getBrowser(requset).equals("I")){
			fileNm = URLEncoder.encode(fileNm,"UTF-8").replaceAll("\\|", "%20").replaceAll("\\+", "%20");
		}else{
			fileNm = new String(fileNm.getBytes("UTF-8"),"ISO-8859-1");
		}
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setContentType("application/json;charset=UTF-8");
		response.setHeader("Content-Disposition", "attachment;filename="+"\""+fileNm+"\"");
		PrintWriter printwriter = response.getWriter();              
		printwriter.println(value);         
		printwriter.flush();
		printwriter.close();                     
	}
}
