package egovframework.admin.basicinf.web;

/**
 * 코드 페이지로 이동하는 클래스
 * @author wiseopen
 * @since 2014.04.17
 * @version 1.0
 * @see
 *
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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

import egovframework.admin.basicinf.service.CommCode;
import egovframework.admin.basicinf.service.CommCodeService;
import egovframework.com.cmm.EgovMessageSource;
import egovframework.common.WiseOpenConfig;
import egovframework.common.code.service.CodeListService;
import egovframework.common.grid.IBSResultVO;
import egovframework.common.grid.IBSheetListVO;
import egovframework.common.helper.Messagehelper;
import egovframework.common.helper.TABListVo;
import egovframework.common.util.UtilJson;


@Controller
public class CommCodeController implements ApplicationContextAware ,InitializingBean {

	private ApplicationContext applicationContext;

	protected static final Log logger = LogFactory.getLog(CommCodeController.class);
	
	private Map<String, Object> codeMap;
	
	@Resource
	private CodeListService commCodeListService;
	
	//공통 메시지 사용시 선언
	@Resource
	Messagehelper messagehelper;
	
	@Resource(name = "CommCodeService")
    private CommCodeService commCodeService;
	@Resource
	EgovMessageSource message;
	
	static class commCode extends HashMap<String, ArrayList<CommCode>> { }
	
	
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub

	}

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		// TODO Auto-generated method stub
		this.applicationContext = applicationContext;
	}
	
	
	/**
	 * 공통코드를 조회한다.
	 * @return HashMap<String,Object>
	 * @throws Exception
	 */
	@ModelAttribute("codeMap")
	public Map<String, Object> getcodeMap() throws Exception{
		codeMap = new HashMap<String, Object>();
		CommCode commCode = new CommCode();
		codeMap.put("grpCd", commCodeListService.getEntityCodeList("GRP_CD"));
		return codeMap;
	}

	/**
	 * 공통코드관리 화면으로 이동한다.
	 * @param model
	 * @return String
	 * @throws Exception
	 */
	@RequestMapping("/dream/basicinf/commcodePage.do")
	public String commcodePage(ModelMap model) throws Exception{
		return "admin/basicinf/commcode";  
	}
	
	/**
     * 조건에 맞는 공통코드 목록을 조회 한다.
     * @param CommCode
     * @param model
     * @return IBSheetListVO<CommCode>
     * @throws Exception
     */
	@RequestMapping("/dream/basicinf/commCodeListAll.do")
	@ResponseBody
	public IBSheetListVO<CommCode> commCodeListAllIbPaging(CommCode commCode, ModelMap model) throws Exception{
		//페이징 처리시 반드시 *IbPaging 메소드명을 사용해야함
		Map<String, Object> map = commCodeService.commCodeListAllIbPaging(commCode);
		@SuppressWarnings("unchecked")
		List<CommCode> result = (List<CommCode>) map.get("resultList");
		int cnt = Integer.parseInt((String)map.get("resultCnt"));
		return new IBSheetListVO<CommCode>(result, cnt);
	}
	
	/**
	 * 공통코드 순서를 변경 한다.
	 * 
	 * @param commCode
	 * @param locale
	 * @return IBSResultVO<CommCode>
	 * @throws Exception
	 */
	@RequestMapping("/dream/basicinf/commCodeOrderBySave.do")
	@ResponseBody
	public IBSResultVO<CommCode> commCodeOrderBySave(@RequestBody commCode data, Locale locale) throws Exception {
		ArrayList<CommCode> list = data.get("data");
		int result = commCodeService.commCodeOrderBySave(list);
		//코드등록시 코드 리로딩
		commCodeListService.init();
		return new IBSResultVO<CommCode>(result, messagehelper.getSavaMessage(result));
	}
	
	/**
	 * 그룹코드 선택 팝업화면으로 이동한다.
	 * @param model
	 * @return String
	 * @throws Exception
	 */
	@RequestMapping("/dream/basicinf/commCodeGrpCdListPopUp.do")
	public String commCodeGrpCdPopUp(ModelMap model) throws Exception{
		return "/admin/basicinf/popup/commgrpcdlistpopup";    
	}
	
	
	
	/**
	 * 그룹 코드 목록을 조회 한다.(팝업)
	 * @param CommCode
	 * @param model
	 * @return IBSheetListVO<CommCode>
	 * @throws Exception
	 */				
	@RequestMapping("/dream/basicinf/commCodeGrpCdList.do")
	@ResponseBody
	public IBSheetListVO<CommCode> commCodeGrpCdListIbPaging(CommCode commCode, ModelMap model) throws Exception{
		//페이징 처리시 반드시 *IbPaging 메소드명을 사용해야함
		Map<String, Object> map = commCodeService.commCodeGrpCdListIbPaging(commCode);
		@SuppressWarnings("unchecked")
		List<CommCode> result = (List<CommCode>) map.get("resultList");
		int cnt = Integer.parseInt((String)map.get("resultCnt"));
		return new IBSheetListVO<CommCode>(result, cnt);
	}
	
	/**
	 * 공통코드 중복을 체크한다.
	 * 
	 * @param CommCode
	 * @param model
	 * @return IBSResultVO<CommCode>
	 * @throws Exception
	 */
	@RequestMapping("/dream/basicinf/commCodeCheckDup.do")
	@ResponseBody
	public IBSResultVO<CommCode> commCodeCheckDup(CommCode commCode, ModelMap model) throws Exception{
		int result = 0;
		result = commCodeService.commCodeCheckDup(commCode);
		return new IBSResultVO<CommCode>(result, messagehelper.getSavaMessage(result));
	}
	
	/**
	 * 공통코드항목을 등록한다.
	 * 
	 * @param CommCode
	 * @param model
	 * @return IBSResultVO<CommCode>
	 * @throws Exception
	 */
	@RequestMapping("/dream/basicinf/commCodeReg.do")
	@ResponseBody
	public IBSResultVO<CommCode> commCodeReg(CommCode commCode, ModelMap model) throws Exception{
		int result = 0;
		//등록, 변경, 삭제 처리시  등록자, 수정자 Id를 세션에서 가져올 경우 *CUD 메소드명을 사용해야함
		result = commCodeService.saveCommCodeCUD(commCode,WiseOpenConfig.STATUS_I);
		//코드등록시 코드 리로딩
		commCodeListService.init();
		return new IBSResultVO<CommCode>(result, messagehelper.getSavaMessage(result));
	}
	

	
	/**
	 * 공통코드를 단건 조회한다.
	 * 
	 * @param CommCode
	 * @param model
	 * @return TABListVo<CommCode>
	 * @throws Exception
	 */
	@RequestMapping("/dream/opendt/commCodeOne.do")
	@ResponseBody
	public TABListVo<CommCode> commCodeOne(@RequestBody CommCode commCode, ModelMap model) throws Exception{
		return new TABListVo<CommCode>(commCodeService.selectCommCodeOne(commCode));
	}
	
	
	/**
	 * 공통코드항목을 변경한다.
	 * 
	 * @param CommCode
	 * @param model
	 * @return IBSResultVO<CommCode>
	 * @throws Exception
	 */
	@RequestMapping("/dream/basicinf/commCodeUpd.do")
	@ResponseBody
	public IBSResultVO<CommCode> commCodeUpd(CommCode commCode, ModelMap model) throws Exception{
		int result = 0;
		//등록, 변경, 삭제 처리시  등록자, 수정자 Id를 세션에서 가져올 경우 *CUD 메소드명을 사용해야함
		result = commCodeService.saveCommCodeCUD(commCode,WiseOpenConfig.STATUS_U); 
		//코드변경시 코드 리로딩
		commCodeListService.init();
		return new IBSResultVO<CommCode>(result, messagehelper.getSavaMessage(result));
	}
	
	/**
	 * 공통코드항목을 삭제한다.
	 * 
	 * @param CommCode
	 * @param model
	 * @return IBSResultVO<CommCode>
	 * @throws Exception
	 */
	@RequestMapping("/dream/basicinf/commCodeDel.do")
	@ResponseBody
	public IBSResultVO<CommCode> commCodeDel(CommCode commCode, ModelMap model) throws Exception{
		int result = 0;
		//등록, 변경, 삭제 처리시  등록자, 수정자 Id를 세션에서 가져올 경우 *CUD 메소드명을 사용해야함
		result = commCodeService.saveCommCodeCUD(commCode,WiseOpenConfig.STATUS_D); 
		//코드삭제시 코드 리로딩
		commCodeListService.init();
		return new IBSResultVO<CommCode>(result, messagehelper.getSavaMessage(result));
	}
	
	
	/**
	 * 조건에 맞는 코드 목록을 조회 한다.(분류관리 등록시 표준맵핑 선택에 사용)
	 * @param commCode
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/dream/basicinf/opencate/openCateDitcList.do")
	@ResponseBody
	public IBSheetListVO<CommCode> openCateDitcList(@ModelAttribute("searchVO") CommCode commCode, ModelMap model) throws Exception{
		 
		Map<String, Object> map = commCodeService.selectOpenCateDitcList(commCode);
		@SuppressWarnings("unchecked")
		List<CommCode> result = (List<CommCode>) map.get("resultList");
		int cnt = Integer.parseInt((String)map.get("resultCnt"));
		return new IBSheetListVO<CommCode>(result, cnt);
	}
	
}
