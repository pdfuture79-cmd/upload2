package egovframework.admin.bbs.service;

import java.io.Serializable;

import org.springframework.web.multipart.MultipartFile;

import egovframework.common.grid.CommVo;

public class ImprvReq extends CommVo implements Serializable{
	
	private String reqTit;
	private String reqCd;
	private String reqCont;
	private String deptCd;
	private String deptNm;
	private String usrCd;
	private String usrId;
	private String usrNm;
	private String prssState;
	private String prssExp;
	private String delYn;
	private String regId;
	private String regDttm;
	private String updId;
	private String updDttm;
	private String srId;
	private String launchDttm;
	private String completeDttm;
	

	public String getSrId() {
		return srId;
	}
	public void setSrId(String srId) {
		this.srId = srId;
	}
	private String reception;
	
	private String searchWord;
	private String searchWd;
	
	
	private String startDttm;
	private String endDttm;
	private String orgNm;
	
	private int tempSeq;
	private int no;
	private String prssCont;
	private String prePrssState;
	private int detlSeq;
	
	
	public int getDetlSeq() {
		return detlSeq;
	}
	public void setDetlSeq(int detlSeq) {
		this.detlSeq = detlSeq;
	}
	//파일처리(새로만듬 여기서만씀 다른데서쓰지말것)
	private MultipartFile file;
	private MultipartFile fileP;
	private MultipartFile fileR;
	private String saveFileNm; // 저장될 파일명(fileSeq + 확장자
	private String srcFileNm; //원본파일명+확장자
	private String viewFileNm; //다운받을때 보여줄 파일명(확장자 없음)
	private String fileExt; //확장자
	private String fileTag; //확장자
	private int fileSeq; //파일시퀀스
	private long fileSize;
	
	
	public String getReception() {
		return reception;
	}
	public void setReception(String reception) {
		this.reception = reception;
	}
	 
	public MultipartFile getFileP() {
		return fileP;
	}
	public void setFileP(MultipartFile fileP) {
		this.fileP = fileP;
	}
	public MultipartFile getFileR() {
		return fileR;
	}
	public void setFileR(MultipartFile fileR) {
		this.fileR = fileR;
	}
	public long getFileSize() {
		return fileSize;
	}
	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}
	public MultipartFile getFile() {
		return file;
	}
	public void setFile(MultipartFile file) {
		this.file = file;
	}
	public String getSaveFileNm() {
		return saveFileNm;
	}
	public void setSaveFileNm(String saveFileNm) {
		this.saveFileNm = saveFileNm;
	}
	public String getSrcFileNm() {
		return srcFileNm;
	}
	public void setSrcFileNm(String srcFileNm) {
		this.srcFileNm = srcFileNm;
	}
	public String getViewFileNm() {
		return viewFileNm;
	}
	public void setViewFileNm(String viewFileNm) {
		this.viewFileNm = viewFileNm;
	}
	public String getFileExt() {
		return fileExt;
	}
	public void setFileExt(String fileExt) {
		this.fileExt = fileExt;
	}
	public int getFileSeq() {
		return fileSeq;
	}
	public void setFileSeq(int fileSeq) {
		this.fileSeq = fileSeq;
	}
	public String getOrgNm() {
		return orgNm;
	}
	public void setOrgNm(String orgNm) {
		this.orgNm = orgNm;
	}
	public String getSearchWord() {
		return searchWord;
	}
	public void setSearchWord(String searchWord) {
		this.searchWord = searchWord;
	}
	public String getSearchWd() {
		return searchWd;
	}
	public void setSearchWd(String searchWd) {
		this.searchWd = searchWd;
	}
	public String getStartDttm() {
		return startDttm;
	}
	public void setStartDttm(String startDttm) {
		this.startDttm = startDttm;
	}
	public String getEndDttm() {
		return endDttm;
	}
	public void setEndDttm(String endDttm) {
		this.endDttm = endDttm;
	}
	public String getReqTit() {
		return reqTit;
	}
	public void setReqTit(String reqTit) {
		this.reqTit = reqTit;
	}
	public String getReqCd() {
		return reqCd;
	}
	public void setReqCd(String reqCd) {
		this.reqCd = reqCd;
	}
	public String getReqCont() {
		return reqCont;
	}
	public void setReqCont(String reqCont) {
		this.reqCont = reqCont;
	}
	public String getDeptCd() {
		return deptCd;
	}
	public void setDeptCd(String deptCd) {
		this.deptCd = deptCd;
	}
	public String getDeptNm() {
		return deptNm;
	}
	public void setDeptNm(String deptNm) {
		this.deptNm = deptNm;
	}
	public String getUsrCd() {
		return usrCd;
	}
	public void setUsrCd(String usrCd) {
		this.usrCd = usrCd;
	}
	public String getUsrId() {
		return usrId;
	}
	public void setUsrId(String usrId) {
		this.usrId = usrId;
	}
	public String getUsrNm() {
		return usrNm;
	}
	public void setUsrNm(String usrNm) {
		this.usrNm = usrNm;
	}
	public String getPrssState() {
		return prssState;
	}
	public void setPrssState(String prssState) {
		this.prssState = prssState;
	}
	public String getPrssExp() {
		return prssExp;
	}
	public void setPrssExp(String prssExp) {
		this.prssExp = prssExp;
	}
	public String getDelYn() {
		return delYn;
	}
	public void setDelYn(String delYn) {
		this.delYn = delYn;
	}
	public String getRegId() {
		return regId;
	}
	public void setRegId(String regId) {
		this.regId = regId;
	}
	public String getRegDttm() {
		return regDttm;
	}
	public void setRegDttm(String regDttm) {
		this.regDttm = regDttm;
	}
	public String getUpdId() {
		return updId;
	}
	public void setUpdId(String updId) {
		this.updId = updId;
	}
	public String getUpdDttm() {
		return updDttm;
	}
	public void setUpdDttm(String updDttm) {
		this.updDttm = updDttm;
	}
	@Override
	public String toString() {
		return "ImprvReq [reqTit=" + reqTit + ", reqCd=" + reqCd + ", reqCont="
				+ reqCont + ", deptCd=" + deptCd + ", deptNm=" + deptNm
				+ ", userCd=" + usrCd + ", userId=" + usrId + ", userNm="
				+ usrNm + ", prssState=" + prssState + ", prssExp=" + prssExp
				+ ", delYn=" + delYn + ", regId=" + regId + ", regDttm="
				+ regDttm + ", updId=" + updId + ", updDttm=" + updDttm
				+ ", searchWord=" + searchWord + ", searchWd=" + searchWd
				+ ", startDttm=" + startDttm + ", endDttm=" + endDttm + "]";
	}
	public int getTempSeq() {
		return tempSeq;
	}
	public void setTempSeq(int tempSeq) {
		this.tempSeq = tempSeq;
	}
	public int getNo() {
		return no;
	}
	public void setNo(int no) {
		this.no = no;
	}
	public String getPrssCont() {
		return prssCont;
	}
	public void setPrssCont(String prssCont) {
		this.prssCont = prssCont;
	}
	public String getPrePrssState() {
		return prePrssState;
	}
	public void setPrePrssState(String prePrssState) {
		this.prePrssState = prePrssState;
	}
	public String getFileTag() {
		return fileTag;
	}
	public void setFileTag(String fileTag) {
		this.fileTag = fileTag;
	}
	
	public String getLaunchDttm() {
		return launchDttm;
	}
	public void setLaunchDttm(String launchDttm) {
		this.launchDttm = launchDttm;
	}
	public String getCompleteDttm() {
		return completeDttm;
	}
	public void setCompleteDttm(String completeDttm) {
		this.completeDttm = completeDttm;
	}
	
	

}
