package egovframework.admin.basicinf.web;

/**
 * 담당자관리 Controller
 * @author KJH
 * @since 2014.07.23
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import egovframework.admin.basicinf.service.CommOrgMngUsrService;
import egovframework.admin.basicinf.service.CommUsrAdmin;
import egovframework.com.cmm.EgovMessageSource;
import egovframework.common.code.service.CodeListService;
import egovframework.common.grid.IBSResultVO;
import egovframework.common.grid.IBSheetListVO;
import egovframework.common.helper.Messagehelper;
import egovframework.common.util.UtilJson;


@Controller
public class CommOrgMngUsrController implements ApplicationContextAware ,InitializingBean {

	protected static final Log logger = LogFactory.getLog(CommUsrAdminController.class);
	
	private ApplicationContext applicationContext;
	
	private Map<String, Object> codeMap;
	
	//공통코드 사용시 선언
	@Resource
	private CodeListService commCodeListService;
	
	@Resource(name = "CommOrgMngUsrService")
    private CommOrgMngUsrService commOrgMngUsrService; 
	
	//공통 메시지 사용시 선언
	@Resource
	EgovMessageSource message;  
	
	static class commUsrAdmins extends HashMap<String, ArrayList<CommUsrAdmin>> { }
	
	
	public void afterPropertiesSet() throws Exception { 
		// TODO Auto-generated method stub

	}

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		// TODO Auto-generated method stub
		this.applicationContext = applicationContext;
	}
	
	/**            
	 * 공통코드를 조회 한다.
	 * @return
	 * @throws Exception
	 */
	@ModelAttribute("codeMap")
	public Map<String, Object> getcodeMap() throws Exception{
		codeMap = new HashMap<String, Object>();
		
		//사용자권한코드
		codeMap.put("accCdIbs", UtilJson.convertJsonString(commCodeListService.getCodeListIBS("C1002",true)));
		
		return codeMap;
	}
	
	
	/**
	 * 시군관리자관리 화면으로 이동한다.
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/dream/basicinf/commOrgMngUsrPage.do") 
	public String commUsrPage(ModelMap model) throws Exception{

		return "/admin/basicinf/commOrgMngUsr";  
	}
	
	/**
	 * 시군관리자관리 조회
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/dream/basicinf/commOrgMngUsrList.do")
	@ResponseBody 
	public IBSheetListVO<CommUsrAdmin> commOrgMngUsrList(@ModelAttribute CommUsrAdmin srchVo, ModelMap model) throws Exception{

		List<CommUsrAdmin> result = commOrgMngUsrService.selectOrgMngUsrList(srchVo);
		
		return new IBSheetListVO<CommUsrAdmin>(result, result.size());  
	}
	
	/**
	 * 시군관리자관리 저장 
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/dream/basicinf/saveOrgMngUsr.do")
	@ResponseBody 
	public IBSResultVO<CommUsrAdmin> saveOrgMngUsr(@RequestBody commUsrAdmins  data, ModelMap model) throws Exception{

		ArrayList<CommUsrAdmin> list = data.get("data");  
		
		int result = commOrgMngUsrService.saveOpenDtfileOrgRelCUD(list);    
		
		String resmsg;
		
		if(result > 0) {
			result = 0;
			resmsg = message.getMessage("MSG.SAVE");
		} else {
			result = -1;
			resmsg = message.getMessage("ERR.SAVE"); 
		}
		
		return new IBSResultVO<CommUsrAdmin>(result, resmsg);  
	}
	
}
