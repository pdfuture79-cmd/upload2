package egovframework.admin.bbs.service;

import java.io.Serializable;
import java.util.Date;

import egovframework.common.grid.CommVo;

@SuppressWarnings("serial")
public class BbsAdmin extends CommVo implements Serializable {
	
	private String bbsCd;
	private String bbsNm;
	private String listCd;
	private String langCd;
	private String bbsTypeCd;
	private String bbsExp;
	private String useYn;
	private String loginWtYn;
	private Integer listCnt;
	private Integer hlCnt;
	private String noticeYn;
	private String secretYn;
	private String atfileYn;
	private String extLimit;
	private Integer sizeLimit;
	private String linkYn;
	private String infYn;
	private String emailRegYn;
	private String emailNeedYn;
	private String telYn;
	private String telNeedYn;
	private String ansYn;
	private String ansTag;
	private String htmlYn;
	private String regId;
	private String regDttm;
	private String updId;
	private String updDttm;
	private String searchWd;
	private String langNm;
	private String typeNm;
	private String bbsOpenDttm;
	private String bbsCloseDttm;
	
	public String getBbsCd() {
		return bbsCd;
	}
	public void setBbsCd(String bbsCd) {
		this.bbsCd = bbsCd;
	}
	public String getBbsNm() {
		return bbsNm;
	}
	public void setBbsNm(String bbsNm) {
		this.bbsNm = bbsNm;
	}
	public String getLangCd() {
		return langCd;
	}
	public void setLangCd(String langCd) {
		this.langCd = langCd;
	}
	public String getBbsExp() {
		return bbsExp;
	}
	public void setBbsExp(String bbsExp) {
		this.bbsExp = bbsExp;
	}
	public String getUseYn() {
		return useYn;
	}
	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}
	public String getLoginWtYn() {
		return loginWtYn;
	}
	public void setLoginWtYn(String loginWtYn) {
		this.loginWtYn = loginWtYn;
	}
	public Integer getListCnt() {
		return listCnt;
	}
	public void setListCnt(Integer listCnt) {
		this.listCnt = listCnt;
	}
	public Integer getHlCnt() {
		return hlCnt;
	}
	public void setHlCnt(Integer hlCnt) {
		this.hlCnt = hlCnt;
	}
	public String getNoticeYn() {
		return noticeYn;
	}
	public void setNoticeYn(String noticeYn) {
		this.noticeYn = noticeYn;
	}
	public String getSecretYn() {
		return secretYn;
	}
	public void setSecretYn(String secretYn) {
		this.secretYn = secretYn;
	}
	public String getAtfileYn() {
		return atfileYn;
	}
	public void setAtfileYn(String atfileYn) {
		this.atfileYn = atfileYn;
	}
	public String getExtLimit() {
		return extLimit;
	}
	public void setExtLimit(String extLimit) {
		this.extLimit = extLimit;
	}
	public Integer getSizeLimit() {
		return sizeLimit;
	}
	public void setSizeLimit(Integer sizeLimit) {
		this.sizeLimit = sizeLimit;
	}
	public String getLinkYn() {
		return linkYn;
	}
	public void setLinkYn(String linkYn) {
		this.linkYn = linkYn;
	}
	public String getInfYn() {
		return infYn;
	}
	public void setInfYn(String infYn) {
		this.infYn = infYn;
	}
	public String getEmailNeedYn() {
		return emailNeedYn;
	}
	public void setEmailNeedYn(String emailNeedYn) {
		this.emailNeedYn = emailNeedYn;
	}
	public String getTelYn() {
		return telYn;
	}
	public void setTelYn(String telYn) {
		this.telYn = telYn;
	}
	public String getTelNeedYn() {
		return telNeedYn;
	}
	public void setTelNeedYn(String telNeedYn) {
		this.telNeedYn = telNeedYn;
	}
	public String getAnsYn() {
		return ansYn;
	}
	public void setAnsYn(String ansYn) {
		this.ansYn = ansYn;
	}
	public String getAnsTag() {
		return ansTag;
	}
	public void setAnsTag(String ansTag) {
		this.ansTag = ansTag;
	}
	public String getHtmlYn() {
		return htmlYn;
	}
	public void setHtmlYn(String htmlYn) {
		this.htmlYn = htmlYn;
	}
	public String getRegId() {
		return regId;
	}
	public void setRegId(String regId) {
		this.regId = regId;
	}
	public String getUpdId() {
		return updId;
	}
	public void setUpdId(String updId) {
		this.updId = updId;
	}
	public String getRegDttm() {
		return regDttm;
	}
	public void setRegDttm(String regDttm) {
		this.regDttm = regDttm;
	}
	public String getUpdDttm() {
		return updDttm;
	}
	public void setUpdDttm(String updDttm) {
		this.updDttm = updDttm;
	}
	public String getSearchWd() {
		return searchWd;
	}
	public void setSearchWd(String searchWd) {
		this.searchWd = searchWd;
	}
	public String getLangNm() {
		return langNm;
	}
	public void setLangNm(String langNm) {
		this.langNm = langNm;
	}
	public String getTypeNm() {
		return typeNm;
	}
	public void setTypeNm(String typeNm) {
		this.typeNm = typeNm;
	}
	public String getListCd() {
		return listCd;
	}
	public void setListCd(String listCd) {
		this.listCd = listCd;
	}
	public String getBbsTypeCd() {
		return bbsTypeCd;
	}
	public void setBbsTypeCd(String bbsTypeCd) {
		this.bbsTypeCd = bbsTypeCd;
	}
	public String getEmailRegYn() {
		return emailRegYn;
	}
	public void setEmailRegYn(String emailRegYn) {
		this.emailRegYn = emailRegYn;
	}
	public String getBbsOpenDttm() {
		return bbsOpenDttm;
	}
	public void setBbsOpenDttm(String bbsOpenDttm) {
		this.bbsOpenDttm = bbsOpenDttm;
	}
	public String getBbsCloseDttm() {
		return bbsCloseDttm;
	}
	public void setBbsCloseDttm(String bbsCloseDttm) {
		this.bbsCloseDttm = bbsCloseDttm;
	}
}
