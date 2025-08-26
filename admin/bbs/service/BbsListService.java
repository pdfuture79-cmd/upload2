package egovframework.admin.bbs.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import egovframework.common.file.service.FileVo;


public interface BbsListService {
	
	public Map<String, Object> selectBbsListIbPaging(BbsList bbsList) throws Exception;
	
	public Map<String, Object> selectBbsAdminInfo(BbsList bbsList) throws Exception;
	public String selectBbsTypeCd(BbsList bbsList) throws Exception;
	public String selectBbsDitcCd(BbsList bbsList) throws Exception;
	public String selectDelYn(BbsList bbsList) throws Exception;
	
	public BbsList selectBbsDtlList(BbsList bbsList) throws Exception;
	
	public Map<String, Object> selectBbsinfPopIbPaging(BbsList bbsList) throws Exception;
	
	public Map<String, Object> selectBbsLinkListIbPaging(BbsList bbsList) throws Exception;
	public Map<String, Object> selectBbsInfListIbPaging(BbsList bbsList) throws Exception;
	
	public Map<String, Object> selectBbsFileListIbPaging(BbsList bbsList) throws Exception;
	
	public Map<String, Object> bbsImgDetailView(BbsList bbsList) throws Exception;
	
	public int saveBbsDtlListCUD(BbsList saveVO, String status) throws Exception;
	public int updateTopYn(BbsList saveVO, String status) throws Exception;
	public int deleteImg(BbsList saveVO, String status) throws Exception;
	public int updateAnsStateCUD(BbsList saveVO) throws Exception;
	
	public int saveBbsLinkListCUD(ArrayList<BbsList> list) throws Exception;
	public int deleteBbsLinkListCUD(ArrayList<BbsList> list) throws Exception;
	
	public int saveBbsInfListCUD(ArrayList<BbsList> list) throws Exception;
	public int updateDeleteImgCUD(BbsList saveVO) throws Exception;
	public int deleteImgDtlCUD(BbsList saveVO) throws Exception;
	public int deleteBbsInfListCUD(ArrayList<BbsList> list) throws Exception;
	public int deleteBbsFileListCUD(ArrayList<BbsList> list) throws Exception;
	
	public int saveBbsFileListCUD(BbsList bbsList,ArrayList<?> list, FileVo fileVo) throws Exception;
	
	// 게시판에 첨부될 파일의 Directory경로 중간에 bbsCd를 넣어주기 위해 bbsCd를 가지고 오는 logic
	public String getBbsCd(int seq) throws Exception;
	
	public Map<String, Object> selectRelSiteListIbPaging(BbsList bbsList) throws Exception;
	public BbsList selectRelSiteDtlList(BbsList bbsList) throws Exception;
	public int saveRelSiteCUD(BbsList saveVO, String status) throws Exception;
	int deleteRelSiteImgFile(BbsList bbsList) throws Exception;
	
	//관리자용 게시판 상세조회
	public BbsList selectAdmBbsDtlList(BbsList bbsList) throws Exception;
	
	public void bbsViewCntUp(BbsList bbsList) throws Exception;

	public List<BbsList> getMainBbsList(BbsList srchVo);  
	
	List<ImprvReq> getImprvReqMngList(ImprvReq imprvReq) throws Exception;
	
	
	int insertImprvReqMng(ImprvReq imprvReq) throws Exception;

	int getImprvReqMngSr() throws Exception;
	
	int updateImprvReqMng(ImprvReq imprvReq)  throws Exception;
	
	int updateImprvReqCancel(ImprvReq imprvReq)  throws Exception;
	
	ImprvReq selectImprvReqDtl(ImprvReq imprvReq)  throws Exception;
	
	List<ImprvReq> getImprvReqPrssList(ImprvReq imprvReq) throws Exception;
	
	int insertImprvReqPrss(ImprvReq imprvReq) throws Exception;
	
	int updateImprvReqPrss(ImprvReq imprvReq) throws Exception;
	
	int deleteImprvReqPrss(ImprvReq imprvReq)  throws Exception;
	
	boolean imprvReqFileUpload(ImprvReq imprvReq) throws Exception;
	
	List<ImprvReq> getImprvReqFileList(ImprvReq imprvReq) throws Exception;

	ImprvReq getImprvReqFile(ImprvReq imprvReq) throws Exception;
	
	void deleteImprvReqFile(ImprvReq imprvReq) throws Exception;
	
}
