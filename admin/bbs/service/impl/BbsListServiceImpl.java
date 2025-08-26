package egovframework.admin.bbs.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import egovframework.admin.bbs.service.BbsList;
import egovframework.admin.bbs.service.BbsListService;
import egovframework.admin.bbs.service.ImprvReq;
import egovframework.admin.openinf.service.impl.OpenDtServiceImpl;
import egovframework.com.cmm.EgovWebUtil;
import egovframework.com.cmm.service.EgovProperties;
import egovframework.common.WiseOpenConfig;
import egovframework.common.base.model.Params;
import egovframework.common.base.model.Record;
import egovframework.common.file.service.FileVo;
import egovframework.rte.fdl.cmmn.AbstractServiceImpl;

@Service("BbsListService")
public class BbsListServiceImpl extends AbstractServiceImpl implements BbsListService {

	@Resource(name = "BbsListDAO")
	private BbsListDAO bbsListDAO;

	private static final Logger logger = Logger	.getLogger(OpenDtServiceImpl.class.getClass());

	public Map<String, Object> selectBbsListIbPaging(BbsList bbsList)		throws Exception {

		List<BbsList> result = bbsListDAO.selectBbsList(bbsList);
		int cnt = bbsListDAO.selectBbsListCnt(bbsList);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("resultList", result);
		map.put("resultCnt", Integer.toString(cnt));

		return map;
	}

	@Override
	public Map<String, Object> selectBbsAdminInfo(BbsList bbsList)	throws Exception {
		List<BbsList> result = bbsListDAO.selectBbsAdminInfo(bbsList);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("result", result);
		return map;
	}

	public String selectBbsTypeCd(BbsList bbsList) throws Exception {
		return bbsListDAO.selectBbsTypeCd(bbsList);
	}
	
	public String selectBbsDitcCd(BbsList bbsList) throws Exception {
		return bbsListDAO.selectBbsDitcCd(bbsList);
	}

	public String selectDelYn(BbsList bbsList) throws Exception {
		return bbsListDAO.selectDelYn(bbsList);
	}

	public BbsList selectBbsDtlList(BbsList bbsList) throws Exception {
		return bbsListDAO.selectBbsDtlList(bbsList);
	}

	public Map<String, Object> selectBbsLinkListIbPaging(BbsList bbsList) throws Exception {

		List<BbsList> result = bbsListDAO.selectBbsLinkList(bbsList);
		int cnt = bbsListDAO.selectBbsLinkListCnt(bbsList);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("resultList", result);
		map.put("resultCnt", Integer.toString(cnt));

		return map;
	}

	public Map<String, Object> selectBbsInfListIbPaging(BbsList bbsList) throws Exception {

		List<BbsList> result = bbsListDAO.selectBbsInfList(bbsList);
		int cnt = bbsListDAO.selectBbsInfListCnt(bbsList);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("resultList", result);
		map.put("resultCnt", Integer.toString(cnt));

		return map;
	}
	
	public Map<String, Object> selectBbsFileListIbPaging(BbsList bbsList) throws Exception {
		List<BbsList> result = bbsListDAO.selectBbsFileList(bbsList);
		int cnt = bbsListDAO.selectBbsFileListCnt(bbsList);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("resultList", result);
		map.put("resultCnt", Integer.toString(cnt));
		return map;
	}
	
	public Map<String, Object> selectBbsinfPopIbPaging(BbsList bbsList) 	throws Exception {

		List<BbsList> result = bbsListDAO.selectBbsInfPopList(bbsList);
		int cnt = bbsListDAO.selectBbsInfPopListCnt(bbsList);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("resultList", result);
		map.put("resultCnt", Integer.toString(cnt));

		return map;
	}
	
	 @Override
    public Map<String, Object> bbsImgDetailView(BbsList bbsList) throws Exception {
    	List<BbsList> resultImg = bbsListDAO.bbsImgDetailView(bbsList);
    	List<BbsList> resultTopYn = bbsListDAO.getTopImg(bbsList);
    	Map<String, Object> map = new HashMap<String, Object>();
    	map.put("resultImg", resultImg);
    	map.put("resultTopYn", resultTopYn);
    	return map;   
    }

	// 단건저장, 수정, 삭제
	public int saveBbsDtlListCUD(BbsList saveVO, String status)	throws Exception {
		int result = 0;

		if (WiseOpenConfig.STATUS_I.equals(status)) {
			int seq = bbsListDAO.getSeq(saveVO);
			saveVO.setSeq(seq);
			saveVO.setpSeq(0);
//			saveVO.setBbsTit(saveVO.getBbsTit().replaceAll("<", "&lt;").replaceAll(">", "&gt;"));
			result = bbsListDAO.insertBbsList(saveVO);
		} else if ((WiseOpenConfig.STATUS_U.equals(status))) {
//			saveVO.setBbsTit(saveVO.getBbsTit().replaceAll("<", "&lt;").replaceAll(">", "&gt;"));
			result = bbsListDAO.updateBbsList(saveVO);
		} else if ((WiseOpenConfig.STATUS_D.equals(status))) {
			 result += bbsListDAO.deleteBbsList(saveVO);
			 if(result > 0){
				 result += bbsListDAO.deleteCPBbsLink(saveVO);
			 }
			 if(result > 0){
				 result += bbsListDAO.deleteCPBbsInf(saveVO);
			 }
			 if(result > 0){
				 result += bbsListDAO.deleteCPBbsFile(saveVO);
			 }
		   	if(result <= 0){
	    		return result;
	    	}
		} else {
			result = WiseOpenConfig.STATUS_ERR;
		}
		return result;
	}
	
	//대표이미지 저장
	public int updateTopYn(BbsList saveVO, String status) throws Exception {
		int result = 0;
		int topResult = 0;
		if (WiseOpenConfig.STATUS_I.equals(status)) {
			result = bbsListDAO.updateTopYn(saveVO);
			topResult = bbsListDAO.getTopYnCnt(saveVO);
			if(topResult > 0 ){
				result = bbsListDAO.updateTopYn2(saveVO);
			}
		}
		return result;
	}
	
	public int deleteImg(BbsList saveVO, String status) throws Exception {
		int result = 0;
		if (WiseOpenConfig.STATUS_D.equals(status)) {
			result = bbsListDAO.deleteImg(saveVO);
			
			//FILE_CNT 저장
			if(result > 0){
				int infCnt = bbsListDAO.getFileCnt(saveVO);
				saveVO.setFileCnt(infCnt);
				result = bbsListDAO.updateFileCnt(saveVO);
			}
		}
		return result;
	}
	
	public int updateAnsStateCUD(BbsList saveVO)	throws Exception {
		int result = 0;
		result = bbsListDAO.updateAnsState(saveVO);
		return result;
	}
	
	//URL 연결 저장, 수정
	public int saveBbsLinkListCUD(ArrayList<BbsList> list) throws Exception {
		int result = 0;
		for (BbsList saveVO : list) {
			if (saveVO.getStatus().equals("I")) {
				int linkSeq = bbsListDAO.getLinkSeq(saveVO);
				saveVO.setLinkSeq(linkSeq);
				result += bbsListDAO.insertBbsLink(saveVO);
			} else if (saveVO.getStatus().equals("U")) {
				 result += bbsListDAO.updateBbsLink(saveVO);
			} else if (saveVO.getStatus().equals("D")) {
				 result += bbsListDAO.deleteUpdateBbsLink(saveVO);
			} else{
				result = WiseOpenConfig.STATUS_ERR;
			}
			//LINK_CNT 저장
			if(result > 0){
				int linkCnt = bbsListDAO.getLinkCnt(saveVO);
				saveVO.setLinkCnt(linkCnt);
				result = bbsListDAO.updateLinkCnt(saveVO);
			}
			
		}

		return result;
	}
	
	// URL 연결 완전 삭제
	public int deleteBbsLinkListCUD(ArrayList<BbsList> list) throws Exception {
		int result = 0;
		for (BbsList saveVO : list) {
			if (saveVO.getStatus().equals("D")) {
				result += bbsListDAO.deleteBbsLink(saveVO);
			} else{
				result = WiseOpenConfig.STATUS_ERR;
			}
			//LINK_CNT 저장
			if(result > 0){
				int linkCnt = bbsListDAO.getLinkCnt(saveVO);
				saveVO.setLinkCnt(linkCnt);
				result = bbsListDAO.updateLinkCnt(saveVO);
			}
		}
		
		return result;
	}
	
	// 공공데이터 연결 저장, 수정
	public int saveBbsInfListCUD(ArrayList<BbsList> list) throws Exception {
		int result = 0;

		for (BbsList saveVO : list) {
			if (saveVO.getStatus().equals("I")) {
				result += bbsListDAO.insertBbsInf(saveVO);
			} else if (saveVO.getStatus().equals("U")) {
				 result += bbsListDAO.updateBbsInf(saveVO);
			} else if (saveVO.getStatus().equals("D")) {
				 result += bbsListDAO.updateDeleteBbsInf(saveVO);
			} else {
				result = WiseOpenConfig.STATUS_ERR;
			}
			//INF_CNT 저장
			if(result > 0){
				int infCnt = bbsListDAO.getInfCnt(saveVO);
				saveVO.setInfCnt(infCnt);
				result = bbsListDAO.updateInfCnt(saveVO);
			}
		}
		return result;
	}
	
	//공공데이터 연결 완전 삭제
	public int deleteBbsInfListCUD(ArrayList<BbsList> list) throws Exception {
		int result = 0;
		for (BbsList saveVO : list) {
			if (saveVO.getStatus().equals("D")) {
				result += bbsListDAO.deleteBbsInf(saveVO);
			} else{
				result = WiseOpenConfig.STATUS_ERR;
			}
			//INF_CNT 저장
			if(result > 0){
				int infCnt = bbsListDAO.getInfCnt(saveVO);
				saveVO.setInfCnt(infCnt);
				result = bbsListDAO.updateInfCnt(saveVO);
			}
		}
		
		return result;
	}
	
	
	//첨부파일 저장, 수정
	@Override
	public int saveBbsFileListCUD(BbsList bbsList, ArrayList<?> list, FileVo fileVo) throws Exception {
		int result = 0;
     	for(int i=0; i < list.size() ; i++){
    		if(((BbsList)list.get(i)).getStatus().equals("I")){
    			int fileSeq = bbsListDAO.getFileSeq((BbsList)list.get(i));
    			((BbsList)list.get(i)).setFileSeq(fileSeq);
    			((BbsList)list.get(i)).setSaveFileNm(fileVo.getSaveFileNm()[i]);	// saveFileNm이 srcFileNm와 똑같이 들어가서 수정..
    			result += bbsListDAO.insertBbsFile((BbsList)list.get(i));
    		} else if(((BbsList)list.get(i)).getStatus().equals("U")){
    			result += bbsListDAO.updateBbsFile((BbsList)list.get(i));
    		} else if(((BbsList)list.get(i)).getStatus().equals("D")){
    			result = bbsListDAO.deleteUpdateBbsFile((BbsList)list.get(i));
    		} else{
    			result = WiseOpenConfig.STATUS_ERR;
    		}
    		//FILE_CNT 저장
			if(result > 0){
				int fileCnt = bbsListDAO.getFileCnt((BbsList)list.get(i));
				((BbsList)list.get(i)).setFileCnt(fileCnt);
				result = bbsListDAO.updateFileCnt((BbsList)list.get(i));
			}
    	}
		return result;
	}
	
	// 첨부파일 완전 삭제
	public int deleteBbsFileListCUD(ArrayList<BbsList> list) throws Exception {
		
		int result = 0;
		for (BbsList saveVO : list) {
			if (saveVO.getStatus().equals("D")) {
				result += bbsListDAO.deleteBbsFile(saveVO);
			} else{
				result = WiseOpenConfig.STATUS_ERR;
			}
			//FILE_CNT 저장
			if(result > 0){
				int infCnt = bbsListDAO.getFileCnt(saveVO);
				saveVO.setFileCnt(infCnt);
				result = bbsListDAO.updateFileCnt(saveVO);
			}
		}
		
		return result;
	}
	
	public int updateDeleteImgCUD(BbsList saveVO) throws Exception {
		int result = 0;
		int topResult = 0;
		
		result = bbsListDAO.updateTopYn(saveVO);
		topResult = bbsListDAO.getTopYnCnt(saveVO);
		if(topResult > 0 ){
			result = bbsListDAO.updateTopYn2(saveVO);
		}
		
		if(saveVO.getDelYn0() != 0 && saveVO.getDelYn0() != null ){
			saveVO.setFileSeq(saveVO.getDelYn0());
			result += bbsListDAO.updateDeleteImg(saveVO);
		}
		if(saveVO.getDelYn1() != 0 && saveVO.getDelYn1() != null){
			saveVO.setFileSeq(saveVO.getDelYn1());
			result += bbsListDAO.updateDeleteImg(saveVO);
		}
		if(saveVO.getDelYn2() != 0 && saveVO.getDelYn2() != null){
			saveVO.setFileSeq(saveVO.getDelYn2());
			result += bbsListDAO.updateDeleteImg(saveVO);
		}
		if(saveVO.getDelYn3() != 0 && saveVO.getDelYn3() != null){
			saveVO.setFileSeq(saveVO.getDelYn3());
			result += bbsListDAO.updateDeleteImg(saveVO);
		}
		
		//INF_CNT 저장
		if(result > 0){
			int infCnt = bbsListDAO.getInfCnt(saveVO);
			saveVO.setInfCnt(infCnt);
			result += bbsListDAO.updateInfCnt(saveVO);
		}
		return result;
	}
	
	
	public int deleteImgDtlCUD(BbsList saveVO) throws Exception {			// 수정 필요함..
		int result = 0;
		
		if(saveVO.getDelYn0() != 0 && saveVO.getDelYn0() != null ){
			saveVO.setFileSeq(saveVO.getDelYn0());
			result += bbsListDAO.deleteImgDtl(saveVO);
		}
		if(saveVO.getDelYn1() != 0 && saveVO.getDelYn1() != null){
			saveVO.setFileSeq(saveVO.getDelYn1());
			result += bbsListDAO.deleteImgDtl(saveVO);
		}
		if(saveVO.getDelYn2() != 0 && saveVO.getDelYn2() != null){
			saveVO.setFileSeq(saveVO.getDelYn2());
			result += bbsListDAO.deleteImgDtl(saveVO);
		}
		if(saveVO.getDelYn3() != 0 && saveVO.getDelYn3() != null){
			saveVO.setFileSeq(saveVO.getDelYn3());
			result += bbsListDAO.deleteImgDtl(saveVO);
		}
		if(saveVO.getDelYn4() != 0 && saveVO.getDelYn4() != null){
			saveVO.setFileSeq(saveVO.getDelYn4());
			result += bbsListDAO.deleteImgDtl(saveVO);
		}
		if(saveVO.getDelYn5() != 0 && saveVO.getDelYn5() != null){
			saveVO.setFileSeq(saveVO.getDelYn5());
			result += bbsListDAO.deleteImgDtl(saveVO);
		}
		if(saveVO.getDelYn6() != 0 && saveVO.getDelYn6() != null){
			saveVO.setFileSeq(saveVO.getDelYn6());
			result += bbsListDAO.deleteImgDtl(saveVO);
		}
		if(saveVO.getDelYn7() != 0 && saveVO.getDelYn7() != null){
			saveVO.setFileSeq(saveVO.getDelYn7());
			result += bbsListDAO.deleteImgDtl(saveVO);
		}
		
		//INF_CNT 저장
		if(result > 0){
			int infCnt = bbsListDAO.getInfCnt(saveVO);
			saveVO.setInfCnt(infCnt);
			result += bbsListDAO.updateInfCnt(saveVO);
		}
		return result;
	}
	
	// 게시판에 첨부될 파일의 Directory경로 중간에 bbsCd를 넣어주기 위해 bbsCd를 가지고 오는 logic
		public String getBbsCd(int seq) throws Exception {
			return bbsListDAO.getBbsCd(seq);
		}
		


		
	public Map<String, Object> selectRelSiteListIbPaging(BbsList bbsList) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		List<BbsList> result = bbsListDAO.selectRelSiteList(bbsList);
		map.put("resultList", result);
		map.put("resultCnt", result.size());
		return map;
	}
	
	public BbsList selectRelSiteDtlList(BbsList bbsList) throws Exception {
		return bbsListDAO.selectRelSiteDtlList(bbsList);
	}
	
	public int deleteRelSiteImgFile(BbsList bbsList) throws Exception{
		return bbsListDAO.deleteRelSiteImgFile(bbsList);
	}
	
		// 단건저장, 수정, 삭제
	public int saveRelSiteCUD(BbsList saveVO, String status)	throws Exception {
		int result = 0;
		if (WiseOpenConfig.STATUS_I.equals(status)) {
			int seq = bbsListDAO.getSeq(saveVO);
			saveVO.setSeq(seq);
			saveVO.setpSeq(0);
			result = bbsListDAO.insertRelSite(saveVO);
		} else if ((WiseOpenConfig.STATUS_U.equals(status))) {
			result = bbsListDAO.updateRelSite(saveVO);
		} else if ((WiseOpenConfig.STATUS_D.equals(status))) {
			result= bbsListDAO.deleteRelSite(saveVO);
		} else {
			result = WiseOpenConfig.STATUS_ERR;
		}
		return result;
	}
	
	public BbsList selectAdmBbsDtlList(BbsList bbsList) throws Exception {
		return bbsListDAO.selectAdmBbsDtlList(bbsList);
	}
	
	public void bbsViewCntUp(BbsList bbsList) throws Exception{
		int result = bbsListDAO.updateViewCnt(bbsList);
	}

	@Override
	public List<BbsList> getMainBbsList(BbsList srchVo) {  
		
		List<BbsList> result = bbsListDAO.selectMainBbsList(srchVo); 
		
		return result;
	}
	
	
	//개선요청관리
	@Override
	public List<ImprvReq> getImprvReqMngList(egovframework.admin.bbs.service.ImprvReq imprvReq ) throws Exception {
		return bbsListDAO.getImprvReqMngList(imprvReq);
	}
	
	
	@Override
	public int insertImprvReqMng(ImprvReq imprvReq) throws Exception {
		bbsListDAO.insertImprvReqMng(imprvReq);
		return bbsListDAO.getImprvReqMngSeq(imprvReq);
	}

	@Override
	public int getImprvReqMngSr() throws Exception {
		return bbsListDAO.getImprvReqMngSr();
	}
	
	@Override
	public int updateImprvReqMng(ImprvReq imprvReq) throws Exception {
		return bbsListDAO.updateImprvReqMng(imprvReq);
	}
	
	@Override
	public int updateImprvReqCancel(ImprvReq imprvReq) throws Exception {
		return bbsListDAO.updateImprvReqCancel(imprvReq);
	}
	
	@Override
	public ImprvReq selectImprvReqDtl(ImprvReq imprvReq) throws Exception {
		return bbsListDAO.selectImprvReqDtl(imprvReq);
	}
	
	@Override
	public List<ImprvReq> getImprvReqPrssList(ImprvReq imprvReq)
			throws Exception {
		return bbsListDAO.getImprvReqPrssList(imprvReq);
	}
	
	@Override
	public int insertImprvReqPrss(ImprvReq imprvReq) throws Exception {
		
		System.out.println(imprvReq);
		
		bbsListDAO.updateImprvReqListPrss(imprvReq);
		
		return bbsListDAO.insertImprvReqPrss(imprvReq);
	}
	
	@Override
	public int updateImprvReqPrss(ImprvReq imprvReq) throws Exception {
		return bbsListDAO.updateImprvReqPrss(imprvReq);
	}
	
	@Override
	public int deleteImprvReqPrss(ImprvReq imprvReq) throws Exception {
		
		bbsListDAO.deleteImprvReqPrss(imprvReq);
		
		return bbsListDAO.updateImprvReqDeletePrss(imprvReq); 

	}
	
	@Override
	public boolean imprvReqFileUpload(ImprvReq imprvReq) throws Exception {
		
		imprvReq.setFileSize(imprvReq.getFile().getSize()); //파일 사이즈 
		int fileSeq = bbsListDAO.getImprvReqFileSeq()+1; //세이브 파일 네임을 만들기 위해 미리 파일시퀀스를 따놓음
		imprvReq.setSaveFileNm(fileSeq + "." + imprvReq.getFileExt()); //세이브 파일 네임 제작
		imprvReq.setFileSeq(fileSeq);
		imprvReq.setViewFileNm(imprvReq.getFile().getOriginalFilename());
		imprvReq.setSrcFileNm(imprvReq.getFile().getOriginalFilename());
		
		int result = bbsListDAO.saveImprvReqFileData(imprvReq); //디비인서트
		
		if(result==1){
			String saveFilePath = EgovProperties.getProperty("Globals.imprvReqFilePath") + imprvReq.getSaveFileNm();
			createFolder(saveFilePath);
			File file = new File(saveFilePath);
			imprvReq.getFile().transferTo(file);
			
			return true;
		}
		
		return false;
	}
	
	private boolean createFolder(String fileDir) {
    	File dir = new File(fileDir);
    	
    	if ( !dir.exists() ) {
    		if ( dir.mkdirs() ) {		//상위폴더 없는경우도 폴더생성
    			return true;
    		} else {
    			return false;
    		}
    	}
    	
    	return true;
    }
	
	@Override
	public List<ImprvReq> getImprvReqFileList(ImprvReq imprvReq)
			throws Exception {
		return bbsListDAO.getImprvReqFileList(imprvReq);
	}
	
	@Override
	public ImprvReq getImprvReqFile(ImprvReq imprvReq) throws Exception {
		return bbsListDAO.getImprvReqFile(imprvReq);
	}
	
	@Override
	public void deleteImprvReqFile(ImprvReq imprvReq) throws Exception {
		
		
		bbsListDAO.deleteImprvReqFile(imprvReq);
		
		String filePath = EgovProperties.getProperty("Globals.imprvReqFilePath") +  EgovWebUtil.filePathReplaceAll(imprvReq.getSaveFileNm());
		
		
	    File f1 = new File(filePath);
	    
	    if(f1.exists())
	    {
	     boolean deleteFlag = f1.delete();
	      
	     if(deleteFlag)
	      System.out.println("파일삭제 성공함");
	     else
	      System.out.println("파일 삭제 실패함");
	    }else{
	     System.out.println("파일이 존재하지 않습니다.");
	    }
		
	}
}
