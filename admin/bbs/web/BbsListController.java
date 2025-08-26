package egovframework.admin.bbs.web;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import egovframework.admin.bbs.service.BbsList;
import egovframework.admin.bbs.service.BbsListService;
import egovframework.admin.bbs.service.ImprvReq;
import egovframework.admin.opendt.web.OpenInfTcolItemController;
import egovframework.com.cmm.EgovMessageSource;
import egovframework.com.cmm.EgovWebUtil;
import egovframework.com.cmm.service.EgovProperties;
import egovframework.common.WiseOpenConfig;
import egovframework.common.code.service.CodeListService;
import egovframework.common.file.service.FileService;
import egovframework.common.file.service.FileVo;
import egovframework.common.grid.CommVo;
import egovframework.common.grid.IBSResultVO;
import egovframework.common.grid.IBSheetListVO;
import egovframework.common.helper.Messagehelper;
import egovframework.common.helper.TABListVo;
import egovframework.common.util.UtilJson;
import egovframework.common.util.UtilString;

@Controller
public class BbsListController implements ApplicationContextAware ,InitializingBean {
	
protected static final Log logger = LogFactory.getLog(OpenInfTcolItemController.class);
	
	private ApplicationContext applicationContext;
	
	private Map<String, Object> codeMap;
	
	//공통코드 사용시 선언
	@Resource
	private CodeListService commCodeListService;
	
	@Resource(name = "FileService")
    private FileService FileService; 
	
	@Resource(name = "BbsListService")
    private BbsListService bbsListService;           
	
	static class bbsLists extends HashMap<String, ArrayList<BbsList>> { }
	
	//공통 메시지 사용시 선언
	@Resource
	Messagehelper messagehelper;
		
	@Resource
	EgovMessageSource message;
		
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
	 * 공통코드를 조회 한다.
	 * @return
	 * @throws Exception
	 */
	@ModelAttribute("codeMap")
	public Map<String, Object> getcodeMap() throws Exception{
		codeMap = new HashMap<String, Object>();
		codeMap.put("ditcCdIbs", UtilJson.convertJsonString(commCodeListService.getCodeListIBS("C1004")));// 게시글 분류 ibSheet
		codeMap.put("faqCdIbs", UtilJson.convertJsonString(commCodeListService.getCodeListIBS("B1001")));// FAQ 분류 ibSheet
		codeMap.put("qnaCdIbs", UtilJson.convertJsonString(commCodeListService.getCodeListIBS("B1002")));// QNA 분류 ibSheet
		codeMap.put("galleryCdIbs", UtilJson.convertJsonString(commCodeListService.getCodeListIBS("B1003")));// 갤러리 분류 ibSheet
		codeMap.put("fsl1001CdIbs", UtilJson.convertJsonString(commCodeListService.getCodeListIBS("B1004")));// 재정배움터 분류 ibSheet
		codeMap.put("fsl1004CdIbs", UtilJson.convertJsonString(commCodeListService.getCodeListIBS("B1005")));// 재정보고서 분류 ibSheet
		codeMap.put("fsl1007CdIbs", UtilJson.convertJsonString(commCodeListService.getCodeListIBS("B1006")));// 재정사이트 분류 ibSheet
		codeMap.put("ditcCd", commCodeListService.getCodeList("C1004")); // 게시글 분류 select box
		codeMap.put("faqCd", commCodeListService.getCodeList("B1001")); // FAQ 분류 select box
		codeMap.put("qnaCd", commCodeListService.getCodeList("B1002")); // QNA 분류 select box
		codeMap.put("galleryCd", commCodeListService.getCodeList("B1003")); // 갤러리 분류 select box
		codeMap.put("fsl1001Cd", commCodeListService.getCodeList("B1004")); // 재정배움터 분류 select box
		codeMap.put("fsl1004Cd", commCodeListService.getCodeList("B1005")); // 재정보고서 분류 select box
		codeMap.put("fsl1007Cd", commCodeListService.getCodeList("B1006")); // 재정사이트 분류 select box
		codeMap.put("telCd", commCodeListService.getEntityCodeList("LIST_CD","C1015")); // 연락처 select box
		codeMap.put("emailCd", commCodeListService.getEntityCodeList("LIST_CD","C1009")); // 이메일 select box
		
		codeMap.put("reqCd", commCodeListService.getCodeList("B1012")); //개선요청유형
		codeMap.put("prssState", commCodeListService.getCodeList("B1013")); //진행상태
		
		return codeMap;
	}
	
	/**
	 * 게시판 조회 화면, 게시판관리 정보 가져오기
	 * @param bbsAdmin
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/dream/bbs/bbsListPage.do") 
	public ModelAndView bbsListPage(BbsList bbsList, ModelMap model, String bbsCd) throws Exception{
		ModelAndView modelAndView = new ModelAndView();              
		Map<String, Object> map = new HashMap<String, Object>();
		map = bbsListService.selectBbsAdminInfo(bbsList);
		modelAndView.addObject("delYn", bbsListService.selectDelYn(bbsList));
		modelAndView.addObject("bbsTypeCd",bbsListService.selectBbsTypeCd(bbsList));
		modelAndView.addObject("bbsCd",bbsCd);
		modelAndView.addObject("listCd",bbsListService.selectBbsDitcCd(bbsList));
		modelAndView.addObject("result",map.get("result"));
		
		//SEQ
		modelAndView.addObject("seq", bbsList.getSeq());
		//타이틀
		modelAndView.addObject("title", bbsList.getBbsTit());
		//화면구분 
		modelAndView.addObject("scrnNm", bbsList.getScrnNm()); 
		
		modelAndView.setViewName("/admin/bbs/bbsList");
		return modelAndView;
	} 
	 
	
	/**
	 * 게시판 목록 조회
	 * @param bbsList
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/dream/bbs/selectBbsList.do")
	@ResponseBody
	public IBSheetListVO<BbsList> selectBbsList(@ModelAttribute("searchVO") BbsList bbsList, ModelMap model) throws Exception{
		//페이징 처리시 반드시 *IbPaging 메소드명을 사용해야함
		Map<String, Object> map = bbsListService.selectBbsListIbPaging(bbsList); 
		@SuppressWarnings("unchecked")
		List<BbsList> result = (List<BbsList>) map.get("resultList");
		int cnt = Integer.parseInt((String)map.get("resultCnt"));
		return new IBSheetListVO<BbsList>(result, cnt);
	}
	
	/**
	 * 게시판 단건 조회
	 * @param bbsList
	 * @param model
	 * @param bbsCd
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/dream/bbs/selectBbsDtlList.do")
	@ResponseBody
	public TABListVo<BbsList> openInfSheetInfo(@RequestBody BbsList bbsList, ModelMap model,String ansTag) throws Exception{
		bbsList.setAnsTag(ansTag);
		return new TABListVo<BbsList>(bbsListService.selectBbsDtlList(bbsList));
	}
	
	/**
	 * 게시판 링크(URL) 목록 조회
	 * @param bbsList
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/dream/bbs/selectBbsLinkList.do")
	@ResponseBody
	public IBSheetListVO<BbsList> selectBbsLinkList(@ModelAttribute("searchVO") BbsList bbsList, ModelMap model) throws Exception{
		//페이징 처리시 반드시 *IbPaging 메소드명을 사용해야함
		Map<String, Object> map = bbsListService.selectBbsLinkListIbPaging(bbsList); 
		@SuppressWarnings("unchecked")
		List<BbsList> result = (List<BbsList>) map.get("resultList");
		int cnt = Integer.parseInt((String)map.get("resultCnt"));
		return new IBSheetListVO<BbsList>(result, cnt);
	}
	
	/**
	 * 게시판 공공데이터 목록 조회
	 * @param bbsList
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/dream/bbs/selectBbsInfList.do")
	@ResponseBody
	public IBSheetListVO<BbsList> selectBbsInfList(@ModelAttribute("searchVO") BbsList bbsList, ModelMap model) throws Exception{
		//페이징 처리시 반드시 *IbPaging 메소드명을 사용해야함
		Map<String, Object> map = bbsListService.selectBbsInfListIbPaging(bbsList); 
		@SuppressWarnings("unchecked")
		List<BbsList> result = (List<BbsList>) map.get("resultList");
		int cnt = Integer.parseInt((String)map.get("resultCnt"));
		return new IBSheetListVO<BbsList>(result, cnt);
	}
	
	/**
	 * 공공데이터 검색 팝업 화면
	 * @return
	 */
	@RequestMapping("/dream/bbs/popup/bbsinf_pop.do")
	public String openinfPopPage(){
		return "/admin/bbs/popup/bbsinf_pop";
	}
	
	/**
	 * 공공데이터 검색 팝업 화면 목록 조회
	 * @param bbsList
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/dream/bbs/popup/selectBbsInfList.do")
	@ResponseBody
	public IBSheetListVO<BbsList> selectBbsinfPop(@ModelAttribute("searchVO") BbsList bbsList, ModelMap model) throws Exception{
		//페이징 처리시 반드시 *IbPaging 메소드명을 사용해야함
		Map<String, Object> map = bbsListService.selectBbsinfPopIbPaging(bbsList); 
		@SuppressWarnings("unchecked")
		List<BbsList> result = (List<BbsList>) map.get("resultList");
		int cnt = Integer.parseInt((String)map.get("resultCnt"));
		return new IBSheetListVO<BbsList>(result, cnt);
	}
	
	/**
	 * 게시판 내용 저장
	 * @param saveVO
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/dream/bbs/saveBbsList.do")
	@ResponseBody
	public IBSResultVO<BbsList> saveBbsList(@ModelAttribute BbsList saveVO, ModelMap model) throws Exception {
		int result = 0;
		result = bbsListService.saveBbsDtlListCUD(saveVO, WiseOpenConfig.STATUS_I); 
		return new IBSResultVO<BbsList>(result, messagehelper.getSavaMessage(result), saveVO.getSeq());
	}
	
	/**
	 * 게시판 내용 수정
	 * @param saveVO
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/dream/bbs/updateBbsList.do")
	@ResponseBody
	public IBSResultVO<BbsList> updateBbsList(@ModelAttribute BbsList saveVO, ModelMap model) throws Exception {
		int result = 0;
		result = bbsListService.saveBbsDtlListCUD(saveVO, WiseOpenConfig.STATUS_U); 
		return new IBSResultVO<BbsList>(result, messagehelper.getSavaMessage(result), saveVO.getSeq());
	}
	
	/**
	 * 게시판 완전 삭제
	 * @param saveVO
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/dream/bbs/deleteBbsList.do")
	@ResponseBody
	public IBSResultVO<BbsList> deleteBbsList(@ModelAttribute BbsList saveVO, ModelMap model) throws Exception {
		int result = 0;
		result = bbsListService.saveBbsDtlListCUD(saveVO, WiseOpenConfig.STATUS_D); 
		return new IBSResultVO<BbsList>(result, messagehelper.getSavaMessage(result), saveVO.getSeq());
	}
	
	/**
	 * 답변/승인 저장
	 * @param saveVO
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/dream/bbs/updateAnsState.do")
	@ResponseBody
	public IBSResultVO<BbsList> updateAnsState(@ModelAttribute BbsList saveVO, ModelMap model) throws Exception {
		int result = 0;
		result = bbsListService.updateAnsStateCUD(saveVO); 
		return new IBSResultVO<BbsList>(result, messagehelper.getSavaMessage(result));
	}
	
	/**
	 * 링크 정보 저장
	 * @param data
	 * @param locale
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/dream/bbs/saveBbsLinkList.do")
	@ResponseBody
	public IBSResultVO<BbsList> saveBbsLinkList(@RequestBody bbsLists data, Locale locale) throws Exception{
		ArrayList<BbsList> list = data.get("data");
		int result = bbsListService.saveBbsLinkListCUD(list);
		String resmsg;
		if(result > 0) {
			result = 0;
			resmsg = message.getMessage("MSG.SAVE");
		} else {
			result = -1;
			resmsg = message.getMessage("ERR.SAVE");
		}
		
		return new IBSResultVO<BbsList>(result, resmsg, "Url");
	}
	
	/**
	 * 링크 정보 완전 삭제
	 * @param data
	 * @param locale
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/dream/bbs/deleteBbsLinkList.do")
	@ResponseBody
	public IBSResultVO<BbsList> deleteBbsLinkList(@RequestBody bbsLists data, Locale locale) throws Exception{
		ArrayList<BbsList> list = data.get("data");
		int result = bbsListService.deleteBbsLinkListCUD(list);
		String resmsg;
		if(result > 0) {
			result = 0;
			resmsg = message.getMessage("MSG.SAVE");
		} else {
			result = -1;
			resmsg = message.getMessage("ERR.SAVE");
		}
		
		return new IBSResultVO<BbsList>(result, resmsg, "Url");
	}
	
	/**
	 * 공공데이터 정보 저장
	 * @param data
	 * @param locale
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/dream/bbs/saveBbsInfList.do")
	@ResponseBody
	public IBSResultVO<BbsList> saveBbsInfList(@RequestBody bbsLists data, Locale locale) throws Exception{
		ArrayList<BbsList> list = data.get("data");
		int result = bbsListService.saveBbsInfListCUD(list);
		String resmsg;
		if(result > 0) {
			result = 0;
			resmsg = message.getMessage("MSG.SAVE");
		} else {
			result = -1;
			resmsg = message.getMessage("ERR.SAVE");
		}
		return new IBSResultVO<BbsList>(result, resmsg, "Inf");
	}
	
	/**
	 * 공공데이터 정보 완전 삭제
	 * @param data
	 * @param locale
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/dream/bbs/deleteBbsInfList.do")
	@ResponseBody
	public IBSResultVO<BbsList> deleteBbsInfList(@RequestBody bbsLists data, Locale locale) throws Exception{
		ArrayList<BbsList> list = data.get("data");
		int result = bbsListService.deleteBbsInfListCUD(list);
		String resmsg;
		if(result > 0) {
			result = 0;
			resmsg = message.getMessage("MSG.SAVE");
		} else {
			result = -1;
			resmsg = message.getMessage("ERR.SAVE");
		}
		return new IBSResultVO<BbsList>(result, resmsg, "Inf");
	}
	
	/**
	 * 게시판 첨부파일 목록 조회
	 * @param bbsList
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/dream/bbs/selectBbsFileList.do")
	@ResponseBody
	public IBSheetListVO<BbsList> selectBbsFileList(@ModelAttribute("searchVO") BbsList bbsList, ModelMap model) throws Exception{
		//페이징 처리시 반드시 *IbPaging 메소드명을 사용해야함
		Map<String, Object> map = bbsListService.selectBbsFileListIbPaging(bbsList); 
		@SuppressWarnings("unchecked")
		List<BbsList> result = (List<BbsList>) map.get("resultList");
		int cnt = Integer.parseInt((String)map.get("resultCnt"));
		return new IBSheetListVO<BbsList>(result, cnt);
	}
	
	/**
	 * 첨부파일 조회
	 * @param bbsList
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/dream/bbs/selectBbsFileList2.do")
	@ResponseBody
	public List<BbsList> selectBbsFileList2(BbsList bbsList, ModelMap model) throws Exception{
		//페이징 처리시 반드시 *IbPaging 메소드명을 사용해야함
		Map<String, Object> map = bbsListService.selectBbsFileListIbPaging(bbsList); 
		@SuppressWarnings("unchecked")
		List<BbsList> result = (List<BbsList>) map.get("resultList");
		return result;
	}
	
	@RequestMapping("/dream/bbs/afterSaveFileList.do")
	@ResponseBody
	public Map<String, Object> afterSaveFileList(BbsList bbsList, ModelMap model) throws Exception{
		Map<String, Object> map = bbsListService.selectBbsFileListIbPaging(bbsList); 
		map.put("result", map.get("result"));
		return map;
	}
	
	@RequestMapping("/dream/bbs/bbsImgDetailView.do")
	@ResponseBody
	public Map<String, Object> bbsImgDetailView(BbsList bbsList, ModelMap model) throws Exception{
		Map<String, Object> map = bbsListService.bbsImgDetailView(bbsList);
    	map.put("resultImg", map.get("resultImg"));
    	map.put("resultTopYn",  map.get("resultTopYn"));
		return map;
	}

	/**
	 * 첨부파일 파일 저장
	 * @param fileVo
	 * @param request
	 * @param bbsList
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/dream/bbs/saveBbsFile.do")
	@ResponseBody
	public void saveBbsFile(FileVo fileVo, HttpServletRequest request, BbsList bbsList, ModelMap model, HttpServletResponse res) throws Exception{
		boolean successFileUpload = false;
		String rtnVal = "";
		/* 파일 타입 체크시... 아래코드 주석 풀고 사용..
		String[] type = {"hwp,ppt"}; //파일 허용타입
		if(FileService.fileTypeCkeck(fileVo, type)){ //타입이 정상인지 체크
			successFileUpload = FileService.fileUpload(fileVo, openInfSrv.getMstSeq(), EgovProperties.getProperty("Globals.ServiceFilePath"), WiseOpenConfig.FILE_SERVICE);
		}else{
			//리턴 에러메시지 뿌려야함
		}
		 */
		int mstSeq = bbsList.getMstSeq();
		int tmpSeq = bbsList.getTmpSeq();
		if(mstSeq == 0){		// 등록한 직후 파일업로드할 때 SEQ가 0으로 들어가는 거 방지
			bbsList.setMstSeq(tmpSeq);
		}
		String pathPlus = bbsListService.getBbsCd(bbsList.getMstSeq());
		FileService.setFileCuData(fileVo);//파일재정의(트랙잰션)
		if(pathPlus.equals("FSL1003")||pathPlus.equals("FSL1008")||pathPlus.equals("FSL1012")){	//국문,영문 보도자료일때는 Directory를 Seq별로 관리하지 않고 bbsCd에다가 몰아넣음
			successFileUpload =	FileService.fileUpload(fileVo, bbsList.getMstSeq(), EgovProperties.getProperty("Globals.BbsFilePath")+pathPlus, WiseOpenConfig.FILE_BBS2);
		}else{
			successFileUpload =	FileService.fileUpload(fileVo, bbsList.getMstSeq(), EgovProperties.getProperty("Globals.BbsFilePath")+pathPlus, WiseOpenConfig.FILE_BBS);	
		}
		
		//UtilString.setQueryStringData을 사용하여 data를 list 형태로 반환다.
		//반드시 new 연산자를 사용하여 vo를 넘겨야한다..
		res.setContentType("text/html;charset=UTF-8");
		PrintWriter writer = res.getWriter();
		if ( successFileUpload ) {
			@SuppressWarnings("unchecked")
			List<CommVo> list = (List<CommVo>)UtilString.setQueryStringData(request, new BbsList(), fileVo);
			rtnVal = objectMapper.writeValueAsString(saveBbsFileListCUD((ArrayList<?>) list, bbsList, model, fileVo));
		} else {
			rtnVal = objectMapper.writeValueAsString(new IBSResultVO<CommVo>(-2, messagehelper.getSavaMessage(-2)));	//저장실패
		}
		writer.println(rtnVal);
		writer.close();		//return type을 IBSResultVO 하면 IE에서 작업 완료시 다운로드 받는 문제가 생겨 변경..
	}
	
	@RequestMapping("/dream/bbs/saveRelSiteFile.do")
	public Map<String, Object> saveRelSiteFile(FileVo fileVo, HttpServletRequest request, BbsList bbsList, ModelMap model, HttpServletResponse res) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		boolean successFileUpload = false;
		String rtnVal = "";
		/* 파일 타입 체크시... 아래코드 주석 풀고 사용..
		String[] type = {"hwp,ppt"}; //파일 허용타입
		if(FileService.fileTypeCkeck(fileVo, type)){ //타입이 정상인지 체크
			successFileUpload = FileService.fileUpload(fileVo, openInfSrv.getMstSeq(), EgovProperties.getProperty("Globals.ServiceFilePath"), WiseOpenConfig.FILE_SERVICE);
		}else{
			//리턴 에러메시지 뿌려야함
		}
		 */
		int mstSeq = bbsList.getMstSeq();
		int tmpSeq = bbsList.getTmpSeq();
		if(mstSeq == 0){		// 등록한 직후 파일업로드할 때 SEQ가 0으로 들어가는 거 방지
			bbsList.setMstSeq(tmpSeq);
		}
		String pathPlus = bbsListService.getBbsCd(bbsList.getMstSeq());
		FileService.setFileCuData(fileVo);//파일재정의(트랙잰션)
		if(pathPlus.equals("FSL1003")||pathPlus.equals("FSL1008")||pathPlus.equals("FSL1012")){	//국문,영문 보도자료일때는 Directory를 Seq별로 관리하지 않고 bbsCd에다가 몰아넣음
			successFileUpload =	FileService.fileUpload(fileVo, bbsList.getMstSeq(), EgovProperties.getProperty("Globals.BbsFilePath")+pathPlus, WiseOpenConfig.FILE_BBS2);
		}else{
			successFileUpload =	FileService.fileUpload(fileVo, bbsList.getMstSeq(), EgovProperties.getProperty("Globals.BbsFilePath")+pathPlus, WiseOpenConfig.FILE_BBS);	
		}
		
		if ( successFileUpload ) {
			@SuppressWarnings("unchecked")
			List<CommVo> list = (List<CommVo>)UtilString.setQueryStringData(request, new BbsList(), fileVo);
			rtnVal = objectMapper.writeValueAsString(saveBbsFileListCUD((ArrayList<?>) list, bbsList, model, fileVo));
		} else {
			rtnVal = objectMapper.writeValueAsString(new IBSResultVO<CommVo>(-2, messagehelper.getSavaMessage(-2)));	//저장실패
		}
		map.put("result", "success");
		return map;
	}
	
	/**
	 * 첨부파일 데이터 저장
	 * @param list
	 * @param bbsList
	 * @param model
	 * @return
	 * @throws Exception
	 */
	private IBSResultVO<CommVo> saveBbsFileListCUD(ArrayList<?> list, BbsList bbsList, ModelMap model, FileVo fileVo) throws Exception{
		
		int result =  bbsListService.saveBbsFileListCUD(bbsList, list, fileVo);
		int fileSeq = 0;
		if(((BbsList)list.get(0)).getStatus().equals("I")){
			fileSeq = ((BbsList)list.get(0)).getFileSeq();
		}else{
			fileSeq = ((BbsList)list.get(0)).getArrFileSeq();
		}
		return new IBSResultVO<CommVo>(result, messagehelper.getSavaMessage(result), "File", fileSeq);
	}
	
	
	@RequestMapping("/dream/bbs/deleteBbsFileList.do")
	@ResponseBody
	public IBSResultVO<BbsList> deleteBbsFileList(@RequestBody bbsLists data, Locale locale) throws Exception{
		ArrayList<BbsList> list = data.get("data");
		int result = bbsListService.deleteBbsFileListCUD(list);
		String resmsg;
		if(result > 0) {
			result = 0;
			resmsg = message.getMessage("MSG.SAVE");
		} else {
			result = -1;
			resmsg = message.getMessage("ERR.SAVE");
		}
		return new IBSResultVO<BbsList>(result, resmsg, "File");
	}
	
	/**
	 * 첨부 이미지 저장(대표이미지 지정)
	 * @param saveVO
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/dream/bbs/updateTopYn.do")
	@ResponseBody
	public IBSResultVO<BbsList> updateTopYn(@ModelAttribute BbsList saveVO, ModelMap model) throws Exception {
		int result = 0;
		result = bbsListService.updateTopYn(saveVO, WiseOpenConfig.STATUS_I); 
		String resmsg;
		if(result > 0) {
			result = 0;
			resmsg = message.getMessage("MSG.SAVE");
		} else {
			result = -1;
			resmsg = message.getMessage("ERR.SAVE");
		}
		return new IBSResultVO<BbsList>(result, resmsg, "Img");
	}
	
	/**
	 * 첨부 이미지 삭제(입력모드에서)
	 * @param saveVO
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/dream/bbs/deleteImg.do")
	@ResponseBody
	public IBSResultVO<BbsList> deleteImg(@ModelAttribute BbsList saveVO, ModelMap model) throws Exception {
		int result = 0;
		result = bbsListService.deleteImg(saveVO, WiseOpenConfig.STATUS_D); 
		String resmsg;
		if(result > 0) {
			result = 0;
			resmsg = message.getMessage("MSG.SAVE");
		} else {
			result = -1;
			resmsg = message.getMessage("ERR.SAVE");
		}
		return new IBSResultVO<BbsList>(result, resmsg, "Img");
	}
	
	
	/**
	 * 첨부이미지 수정
	 * @param saveVO
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/dream/bbs/updateDeleteImg.do")
	@ResponseBody
	public IBSResultVO<BbsList> updateDeleteImg(@ModelAttribute BbsList saveVO) throws Exception{
		int result = bbsListService.updateDeleteImgCUD(saveVO);
		String resmsg;
		if(result > 0) {
			result = 0;
			resmsg = message.getMessage("MSG.SAVE");
		} else {
			result = -1;
			resmsg = message.getMessage("ERR.SAVE");
		}
		return new IBSResultVO<BbsList>(result, resmsg, "Img");
	}
	
	/**
	 * 첨부이미지 완전 삭제
	 * @param saveVO
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/dream/bbs/deleteImgDtl.do")
	@ResponseBody
	public IBSResultVO<BbsList> deleteImgDtl(@ModelAttribute BbsList saveVO) throws Exception{
		int result = bbsListService.deleteImgDtlCUD(saveVO);
		String resmsg;
		if(result > 0) {
			result = 0;
			resmsg = message.getMessage("MSG.SAVE");
		} else {
			result = -1;
			resmsg = message.getMessage("ERR.SAVE");
		}
		return new IBSResultVO<BbsList>(result, resmsg, "Img");
	}
	
	
	
	
	
	
	/**
	 * 관련사이트 관리
	 * @param bbsAdmin
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/dream/bbs/relationSiteMngPage.do")
	public ModelAndView relationSiteMngPage(BbsList bbsList, ModelMap model, String bbsCd) throws Exception{
		ModelAndView modelAndView = new ModelAndView();              
		Map<String, Object> map = new HashMap<String, Object>();
		map = bbsListService.selectBbsAdminInfo(bbsList);
		modelAndView.addObject("relSiteCd", commCodeListService.getCodeList("RELSITE"));
		modelAndView.addObject("bbsCd", "RELSITE");
		modelAndView.setViewName("/admin/bbs/relSiteMng");
		return modelAndView;
	}
	
	/**
	 * 관련사이트 관리
	 * @param bbsList
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/dream/bbs/selectRelSiteList.do")
	@ResponseBody
	public IBSheetListVO<BbsList> selectRelSiteList(@ModelAttribute("searchVO") BbsList bbsList, ModelMap model) throws Exception{
		Map<String, Object> map = bbsListService.selectRelSiteListIbPaging(bbsList); 
		@SuppressWarnings("unchecked")
		List<BbsList> result = (List<BbsList>) map.get("resultList");
		int cnt = (Integer) map.get("resultCnt");
		return new IBSheetListVO<BbsList>(result, cnt);
	}
	
	@RequestMapping("/dream/bbs/selectRelSiteDtlList.do")
	@ResponseBody
	public TABListVo<BbsList> selectRelSiteDetail(@RequestBody BbsList bbsList, ModelMap model,String ansTag) throws Exception{
		bbsList.setAnsTag(ansTag);
		return new TABListVo<BbsList>(bbsListService.selectRelSiteDtlList(bbsList));
	}
	
	@RequestMapping("/dream/bbs/saveRelSite.do")
	@ResponseBody
	public IBSResultVO<BbsList> saveRelSite(@ModelAttribute BbsList saveVO, ModelMap model) throws Exception {
		int result = 0;
		result = bbsListService.saveRelSiteCUD(saveVO, WiseOpenConfig.STATUS_I); 
		return new IBSResultVO<BbsList>(result, messagehelper.getSavaMessage(result), saveVO.getSeq());
	}
	
	@RequestMapping("/dream/bbs/updateRelSite.do")
	@ResponseBody
	public IBSResultVO<BbsList> updateRelSite(@ModelAttribute BbsList saveVO, ModelMap model) throws Exception {
		int result = 0;
		result = bbsListService.saveRelSiteCUD(saveVO, WiseOpenConfig.STATUS_U); 
		return new IBSResultVO<BbsList>(result, messagehelper.getSavaMessage(result), saveVO.getSeq());
	}
	
	@RequestMapping("/dream/bbs/deleteRelSite.do")
	@ResponseBody
	public IBSResultVO<BbsList> deleteRelSite(@ModelAttribute BbsList saveVO, ModelMap model) throws Exception {
		int result = 0;
		result = bbsListService.saveRelSiteCUD(saveVO, WiseOpenConfig.STATUS_D); 
		return new IBSResultVO<BbsList>(result, messagehelper.getSavaMessage(result), saveVO.getSeq());
	}
	
	/**
	 * 관련사이트 이미지만 삭제
	 */
	@RequestMapping("/dream/bbs/deleteRelSiteImgFile.do")
	@ResponseBody
	public int deleteRelSiteImgFile(@ModelAttribute BbsList bbsList, ModelMap model) throws Exception{
		int result = bbsListService.deleteRelSiteImgFile(bbsList);
		return result;
	}
	
	/**
	 * 관리자 공지사항
	 */
	@RequestMapping("/dream/bbs/admNoticeBbsPage.do")
	public ModelAndView admNoticeBbsPage(BbsList bbsList, ModelMap model) throws Exception{
		ModelAndView modelAndView = new ModelAndView();              
		Map<String, Object> map = new HashMap<String, Object>();
		bbsList.setBbsCd("ADMNOTICE");
		map = bbsListService.selectBbsAdminInfo(bbsList);
		modelAndView.addObject("delYn", bbsListService.selectDelYn(bbsList));
		modelAndView.addObject("bbsTypeCd",bbsListService.selectBbsTypeCd(bbsList));
		modelAndView.addObject("bbsCd", "ADMNOTICE");
		modelAndView.addObject("listCd",bbsListService.selectBbsDitcCd(bbsList));
		modelAndView.addObject("result",map.get("result"));
		
		//SEQ
		modelAndView.addObject("seq", bbsList.getSeq());
		//타이틀
		modelAndView.addObject("title", bbsList.getBbsTit());
		//화면구분 
		modelAndView.addObject("scrnNm", bbsList.getScrnNm());  
		
		modelAndView.setViewName("/admin/bbs/admBbsList");
		return modelAndView;
	}
	
	/**
	 * 관리자 공지사항
	 */
	@RequestMapping("/dream/bbs/admQnaBbsPage.do")
	public ModelAndView admQnaBbsPage(BbsList bbsList, ModelMap model) throws Exception{
		ModelAndView modelAndView = new ModelAndView();              
		Map<String, Object> map = new HashMap<String, Object>();
		bbsList.setBbsCd("ADMQNA");
		map = bbsListService.selectBbsAdminInfo(bbsList);
		modelAndView.addObject("delYn", bbsListService.selectDelYn(bbsList));
		modelAndView.addObject("bbsTypeCd",bbsListService.selectBbsTypeCd(bbsList));
		modelAndView.addObject("bbsCd", "ADMQNA");
		modelAndView.addObject("listCd",bbsListService.selectBbsDitcCd(bbsList));
		modelAndView.addObject("result",map.get("result"));
		
		//SEQ
		modelAndView.addObject("seq", bbsList.getSeq());
		//타이틀
		modelAndView.addObject("title", bbsList.getBbsTit());
		//화면구분 
		modelAndView.addObject("scrnNm", bbsList.getScrnNm()); 
		
		modelAndView.setViewName("/admin/bbs/admBbsList");
		return modelAndView;
	}
	
	@RequestMapping("/dream/bbs/selectAdmBbsDtlList.do")
	@ResponseBody
	public TABListVo<BbsList> selectAdmBbsDetail(@RequestBody BbsList bbsList, ModelMap model,String ansTag) throws Exception{
		bbsList.setAnsTag(ansTag);
		return new TABListVo<BbsList>(bbsListService.selectAdmBbsDtlList(bbsList));
	}
	
	@RequestMapping("/dream/bbs/bbsViewCntUp.do")
	@ResponseBody
	public void bbsViewCntUp(BbsList bbsList, ModelMap model) throws Exception{
		bbsListService.bbsViewCntUp(bbsList);
	}
	
	
	//개선요청 관리
	@RequestMapping("/dream/bbs/imprvReqMngPage.do")
	public String imprvReqMngPage(ModelMap modelMap){
		return "/admin/bbs/imprvReqMng";
	}
	
	
	//개선요청 관리 리스트
	@RequestMapping("/dream/bbs/imprvReqMngList.do")
	@ResponseBody
	public IBSheetListVO<ImprvReq> imprvReqMngList(HttpServletRequest request, HttpSession session,ImprvReq imprvReq) throws Exception{
		//개선요청 관리 리스트
		@SuppressWarnings("unchecked")
		List<ImprvReq> result = bbsListService.getImprvReqMngList(imprvReq);
		int cnt = 0;
		if(result!=null){
			cnt = result.size();
		}
		return new IBSheetListVO<ImprvReq>(result, cnt);
	}
	
	
	@RequestMapping("/dream/bbs/insertImprvReqMng.do")
	@ResponseBody
	public Map<String, Object> insertImprvReqMng(HttpServletRequest request, HttpSession session,ImprvReq imprvReq) throws Exception{
		
		int seq = bbsListService.insertImprvReqMng(imprvReq);
		String sr  = "SR-"+String.format("%06d", bbsListService.getImprvReqMngSr());

		Map<String, Object> map = new HashMap<String,Object>();
		map.put("seq", seq);
		map.put("sr", sr);
		return map;
	}

	
	@RequestMapping("/dream/bbs/updateImprvReqMng.do")
	@ResponseBody
	public Map<String, Object> updateImprvReqMng(HttpServletRequest request, HttpSession session,ImprvReq imprvReq) throws Exception{
		
		if(imprvReq.getSeq()==0){
			imprvReq.setSeq(imprvReq.getTempSeq());
		}


		int result = bbsListService.updateImprvReqMng(imprvReq);

		Map<String, Object> map = new HashMap<String,Object>();
		map.put("result",result);
		return map;
	}
	
	@RequestMapping("/dream/bbs/updateImprvReqReqCancel.do")
	@ResponseBody
	public Map<String, Object> updateImprvReqReqCancel(HttpServletRequest request, HttpSession session,ImprvReq imprvReq) throws Exception{
		
		if(imprvReq.getSeq()==0){
			imprvReq.setSeq(imprvReq.getTempSeq());
		}
		
		System.out.println(imprvReq);
		
		
		int result = bbsListService.insertImprvReqPrss(imprvReq);

		result = bbsListService.updateImprvReqCancel(imprvReq);
		
		
		
		Map<String, Object> map = new HashMap<String,Object>();
		map.put("result",result);
		return map;
	}
	
	//개선요청 단건 조회
	@RequestMapping("/dream/bbs/selectImprvReqDtl.do")
	@ResponseBody
	public TABListVo<ImprvReq> selectImprvReqDtl(@RequestBody ImprvReq imprvReq, ModelMap model) throws Exception{
		imprvReq = bbsListService.selectImprvReqDtl(imprvReq);
		model.addAttribute("seq", imprvReq.getSeq());
		model.addAttribute("srId", imprvReq.getSrId());
		return new TABListVo<ImprvReq>(imprvReq);
	}
	
	//개선요청 관리 처리정보 리스트
	@RequestMapping("/dream/bbs/imprvReqPrssList.do")
	@ResponseBody
	public IBSheetListVO<ImprvReq> imprvReqPrssList(HttpServletRequest request, HttpSession session,ImprvReq imprvReq) throws Exception{

		//System.out.println("\n\n개선요청 관리 리스트 \n" + imprvReq.toString());


		List<ImprvReq> result = bbsListService.getImprvReqPrssList(imprvReq);
		int cnt = 0;
		if(result!=null){
			cnt = result.size();
		}
		return new IBSheetListVO<ImprvReq>(result, cnt);
	}
	
	//개선요청 관리 처리정보
	@RequestMapping("/dream/bbs/insertImprvReqPrss.do")
	@ResponseBody
	public Map<String, Object> insertImprvReqPrss(HttpServletRequest request, HttpSession session,ImprvReq imprvReq) throws Exception{

		int result = bbsListService.insertImprvReqPrss(imprvReq);

		Map<String, Object> map = new HashMap<String,Object>();
		map.put("result",result);
		return map;
	}
	
	//개선요청 관리 처리정보 수정
	@RequestMapping("/dream/bbs/updateImprvReqPrss.do")
	@ResponseBody
	public Map<String, Object> updateImprvReqPrss(HttpServletRequest request, HttpSession session,ImprvReq imprvReq) throws Exception{

		int result = bbsListService.updateImprvReqPrss(imprvReq);

		Map<String, Object> map = new HashMap<String,Object>();
		map.put("result",result);
		return map;
	}
	
	//개선요청 관리 처리정보 삭제
	@RequestMapping("/dream/bbs/deleteImprvReqPrss.do")
	@ResponseBody
	public Map<String, Object> deleteImprvReqPrss(HttpServletRequest request, HttpSession session,ImprvReq imprvReq) throws Exception{

		int result = bbsListService.deleteImprvReqPrss(imprvReq);

		Map<String, Object> map = new HashMap<String,Object>();
		map.put("result",result);
		return map;
	}
	
	/**
	 * 첨부파일 파일 저장
	 */
	@RequestMapping("/dream/bbs/saveImprvReqFile.do")
	@ResponseBody
	public void saveBbsFile(ImprvReq imprvReq, HttpServletRequest request, BbsList bbsList, ModelMap model, HttpServletResponse res) throws Exception{

		ObjectMapper objectMapper = new ObjectMapper();

		if("R".equals(imprvReq.getFileTag())){
			imprvReq.setFile(imprvReq.getFileR());
		}else if("P".equals(imprvReq.getFileTag())){
			imprvReq.setFile(imprvReq.getFileP());
		}else{
			logger.error("비정상적인 접근입니다.");
		}

		logger.debug("\n\n첨부파일 저장 시작 \n\n");
		boolean successFileUpload = false;
		String[] type = {"bmp","gif","ief","jpe","jpeg","jpg","png","tif","tiff","doc","docx","hwp","pdf","ppt","pptx","txt","xls","xlsx","zip","alz","arj","rar","tar","war","gz"};
		String message="";

		if(imprvReq.getSeq()==null || imprvReq.getSeq()==0){
			imprvReq.setSeq(imprvReq.getTempSeq());
		}

		for(int i=0;i<type.length;i++){
			if(imprvReq.getFileExt().toLowerCase().equals(type[i])){
				successFileUpload = bbsListService.imprvReqFileUpload(imprvReq);
			}else{
				message="등록할 수 없는 파일 유형입니다.";
			}
		}

		res.setContentType("text/html;charset=UTF-8");
		PrintWriter writer = res.getWriter();

		if(successFileUpload){
			message="파일을 저장했습니다.";
		}else{
			if("".equals(message)){
				message="파일 저장에 실패했습니다.";
			}
		}
		writer.println(objectMapper.writeValueAsString(new IBSResultVO<CommVo>(-2, message)));
		writer.close();		//return type을 IBSResultVO 하면 IE에서 작업 완료시 다운로드 받는 문제가 생겨 변경..
	}
	
	@RequestMapping("/dream/bbs/getImprvReqFileList.do")
	@ResponseBody
	public Map<String,Object> getImprvReqFileList(ImprvReq imprvReq, HttpServletRequest request, BbsList bbsList, ModelMap model, HttpServletResponse res) throws Exception{

		Map<String,Object> map = new HashMap<String,Object>();

		if(imprvReq.getSeq()==null || imprvReq.getSeq()==0){
			imprvReq.setSeq(imprvReq.getTempSeq());
		}

		List<ImprvReq> result = bbsListService.getImprvReqFileList(imprvReq);

		map.put("result",result);

		return map;
	}
	
	@RequestMapping("/dream/bbs/imprvReqFileDownload.do")
	public ModelAndView imprvReqFileDownload(ImprvReq imprvReq) throws Exception {


		ImprvReq result = bbsListService.getImprvReqFile(imprvReq);

		String file = EgovProperties.getProperty("Globals.imprvReqFilePath") +  EgovWebUtil.filePathReplaceAll(result.getSaveFileNm());				//전체다운경로 및 파일
		ModelAndView mav = new ModelAndView();
		mav.addObject("fileName", result.getViewFileNm());
		mav.addObject("file", new File(file));
		mav.setViewName("downloadView");	//viewResolver...
		return mav;
	}
	
	@RequestMapping("/dream/bbs/imprvReqFileDelete.do")
	@ResponseBody
	public void imprvReqFileDelete(ImprvReq imprvReq) throws Exception {

		ImprvReq result = bbsListService.getImprvReqFile(imprvReq);
		bbsListService.deleteImprvReqFile(result);
	}
	
}
