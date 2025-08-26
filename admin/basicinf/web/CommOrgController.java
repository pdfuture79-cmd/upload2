package egovframework.admin.basicinf.web;

/**
 * 조직정보 관리를 위한 Controller
 * @author KJH
 * @since 2014.07.17
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
import egovframework.admin.basicinf.service.CommOrg;
import egovframework.admin.basicinf.service.CommOrgService;
import egovframework.admin.opendt.service.OpenInfTcolItem;
import egovframework.admin.openinf.service.OpenInf;
import egovframework.com.cmm.EgovMessageSource;
import egovframework.common.WiseOpenConfig;
import egovframework.common.code.service.CodeListService;
import egovframework.common.grid.IBSResultVO;
import egovframework.common.grid.IBSheetListVO;
import egovframework.common.helper.Messagehelper;
import egovframework.common.helper.TABListVo;


@Controller
public class CommOrgController implements ApplicationContextAware ,InitializingBean {

	private ApplicationContext applicationContext;

	protected static final Log logger = LogFactory.getLog(CommOrgController.class);
	
	private Map<String, Object> codeMap;
	
	@Resource
	private CodeListService commCodeListService;
	
	@Resource(name = "CommOrgService")
    private CommOrgService commOrgService;
	
	//공통 메시지 사용시 선언
	@Resource
	Messagehelper messagehelper;
	
	static class commOrgs extends HashMap<String, ArrayList<CommOrg>> { }
	
	
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub

	}

	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		// TODO Auto-generated method stub
		this.applicationContext = applicationContext;
		logger.info("EgovComIndexController setApplicationContext method has called!");
	}
	
	/**            
	 * 공통코드를 조회 한다.
	 * @return
	 * @throws Exception
	 */
	@ModelAttribute("codeMap")
	public Map<String, Object> getcodeMap() throws Exception{
		codeMap = new HashMap<String, Object>();
		codeMap.put("typeCd", commCodeListService.getCodeList("C1001"));   
		
		return codeMap;
	} 
	
	/**
	 * 공통코드 화면으로 이동한다.
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/dream/basicinf/popup/commOrg_pop.do")
	public String commorg_popPage(ModelMap model) throws Exception{
		return "/admin/basicinf/popup/commorg_pop";  
	}
	
	
	/**
	 * 사용자를 전체 조회한다.
	 * @param commUsr
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/dream/basicinf/popup/commOrgListAll.do")
	@ResponseBody
	public IBSheetListVO<CommOrg> commOrgListAll(@ModelAttribute("searchVO") CommOrg Commorg, ModelMap model) throws Exception{
		List<CommOrg> list = commOrgService.selectCommOrgAll(Commorg);
		return new IBSheetListVO<CommOrg>(list, list.size());
	}
	
	/**
	 * 페이지 이동
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/dream/basicinf/commOrgPage.do")
	public String commOrgPage(ModelMap model) throws Exception{
		//페이지 title을 가져오려면 반드시 *Page로 끝나야한다.
		//Interceptor(PageNavigationInterceptor)에서 조회함
		return "/admin/basicinf/commOrg";  
	}
	
	/**
	 * 조직정보 목록트리 조회.
	 * @param commOrg
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/dream/basicinf/commOrgListTree.do")
	@ResponseBody
	public IBSheetListVO<CommOrg> commOrgListTree(CommOrg commOrg, ModelMap model) throws Exception{
		//페이징 처리시 반드시 *IbPaging 메소드명을 사용해야함
		Map<String, Object> map = commOrgService.commOrgListTree(commOrg);
		@SuppressWarnings("unchecked")         
		List<CommOrg> result = (List<CommOrg>) map.get("resultList");
		return new IBSheetListVO<CommOrg>(result, result.size());
	}
	
	/**
	 * 그룹코드 중복체크
	 * @param commOrg
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/dream/basicinf/commOrgCdDup.do")
	@ResponseBody
	public IBSResultVO<CommOrg> commOrgCdDup(CommOrg commOrg, ModelMap model) throws Exception{
		int result = 0;
		result = commOrgService.commOrgCdDup(commOrg);
		return new IBSResultVO<CommOrg>(result, messagehelper.getSavaMessage(result));
	}
	
	/**
	 * 신규등록
	 * @param commOrg
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/dream/basicinf/commOrgReg.do")
	@ResponseBody
	public IBSResultVO<CommOrg> commOrgReg(CommOrg commOrg, ModelMap model) throws Exception{
		int result = 0;
		//등록, 변경, 삭제 처리시  등록자, 수정자 Id를 세션에서 가져올 경우 *CUD 메소드명을 사용해야함
		result = commOrgService.saveCommOrgCUD(commOrg,WiseOpenConfig.STATUS_I);
		return new IBSResultVO<CommOrg>(result, messagehelper.getSavaMessage(result));
	}
	
	
	/**
	 * 조직정보 단건 조회
	 * @param commOrg
	 * @param model
	 * @return
	 * @throws Exception
	 */
	//@RequestMapping("/dream/basicinf/commOrgRetr.do")
	//@ResponseBody
	//public TABListVo<CommOrg> commOrgRetv(@RequestBody CommOrg commOrg, ModelMap model) throws Exception{
	//	return new TABListVo<CommOrg>(commOrgService.commOrgRetr(commOrg));
	//}
	
	/**
	 * 조직정보 단건 조회
	 * @param commOrg
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/dream/basicinf/commOrgRetr.do")
	@ResponseBody
	public Map<String,Object> commOrgRetv(CommOrg commOrg, ModelMap model) throws Exception{
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("DATA", commOrgService.commOrgRetr(commOrg));
		return map;
	}
	
	/**
	 * 조직정보 삭제(하위구조 있을경우 동시처리)
	 * @param commOrg
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/dream/basicinf/commOrgDel.do")
	@ResponseBody
	public IBSResultVO<CommOrg> commOrgDel(CommOrg commOrg, ModelMap model) throws Exception{
		int result = 0;
		//등록, 변경, 삭제 처리시  등록자, 수정자 Id를 세션에서 가져올 경우 *CUD 메소드명을 사용해야함
		result = commOrgService.saveCommOrgCUD(commOrg, WiseOpenConfig.STATUS_D);
		return new IBSResultVO<CommOrg>(result, messagehelper.getDeleteMessage(result));
	}
	
	/**
	 * 조직정보 수정(미사용 체크시 하위조직도 같이 USE_YN = 'N' 으로 변경
	 * @param commOrg
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/dream/basicinf/commOrgUpd.do")
	@ResponseBody
	public IBSResultVO<CommOrg> commOrgUpd(CommOrg commOrg, ModelMap model) throws Exception{
		int result = 0;
		//등록, 변경, 삭제 처리시  등록자, 수정자 Id를 세션에서 가져올 경우 *CUD 메소드명을 사용해야함
		result = commOrgService.saveCommOrgCUD(commOrg, WiseOpenConfig.STATUS_U);
		return new IBSResultVO<CommOrg>(result, messagehelper.getUpdateMessage(result));
	}
	
	/**
	 * 조직정보의 항목 순서를 변경
	 * @param data
	 * @param locale
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/dream/basicinf/commOrgListUpdateTreeOrder.do")
	@ResponseBody
	public IBSResultVO<CommOrg> commOrgListUpdateTreeOrder(@RequestBody commOrgs data, Locale locale) throws Exception {
		ArrayList<CommOrg> list = data.get("data");
		int result = commOrgService.commOrgListUpdateTreeOrderCUD(list);  
		return new IBSResultVO<CommOrg>(result, messagehelper.getSavaMessage(result));
	}
	
	/**
	 * 공통 담당조직과 직원명 조회 화면으로 이동
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/dream/basicinf/popup/commOrgNUsr_pop.do")
	public String commorgnusr_popPage(ModelMap model) throws Exception{
		return "/admin/basicinf/popup/commorgnusr_pop";  
	}
	
}
