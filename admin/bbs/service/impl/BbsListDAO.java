package egovframework.admin.bbs.service.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import egovframework.admin.bbs.service.BbsList;
import egovframework.admin.bbs.service.ImprvReq;
import egovframework.com.cmm.service.impl.EgovComAbstractDAO;

@Repository("BbsListDAO")
public class BbsListDAO extends EgovComAbstractDAO {
	
	@SuppressWarnings("unchecked")
	public List<BbsList> selectBbsList(BbsList bbsList) throws Exception {
    	return list("BbsListDAO.selectBbsList", bbsList);
    }
	
	public int selectBbsListCnt(BbsList bbsList) throws Exception {
    	return (Integer)getSqlMapClientTemplate().queryForObject("BbsListDAO.selectBbsListCnt", bbsList);
    }
	
	@SuppressWarnings("unchecked")
	public List<BbsList> selectBbsAdminInfo(BbsList bbsList) throws Exception {
		return list("BbsListDAO.selectBbsAdminInfo", bbsList);
	}
	
	 public String selectBbsTypeCd(BbsList bbsList) throws Exception {
		 return (String)selectByPk("BbsListDAO.selectBbsTypeCd", bbsList);
	 }
	 public String selectBbsDitcCd(BbsList bbsList) throws Exception {
		 return (String)selectByPk("BbsListDAO.selectBbsDitcCd", bbsList);
	 }
	 
	 public String selectDelYn(BbsList bbsList) throws Exception {
		 return (String)selectByPk("BbsListDAO.selectDelYn", bbsList);
	 }
	
    public BbsList selectBbsDtlList(BbsList bbsList) throws Exception {
    	return (BbsList)selectByPk("BbsListDAO.selectBbsDtlList", bbsList);
    }
    
	@SuppressWarnings("unchecked")
	public List<BbsList> selectBbsLinkList(BbsList bbsList) throws Exception {
    	return list("BbsListDAO.selectBbsLinkList", bbsList);
    }
	
	public int selectBbsLinkListCnt(BbsList bbsList) throws Exception {
    	return (Integer)getSqlMapClientTemplate().queryForObject("BbsListDAO.selectBbsLinkListCnt", bbsList);
    }
	
	@SuppressWarnings("unchecked")
	public List<BbsList> selectBbsInfList(BbsList bbsList) throws Exception {
    	return list("BbsListDAO.selectBbsInfList", bbsList);
    }
	
	public int selectBbsInfListCnt(BbsList bbsList) throws Exception {
    	return (Integer)getSqlMapClientTemplate().queryForObject("BbsListDAO.selectBbsInfListCnt", bbsList);
    }
	
	@SuppressWarnings("unchecked")
	public List<BbsList> selectBbsFileList(BbsList bbsList) throws Exception {
		return list("BbsListDAO.selectBbsFileList", bbsList);
	}
	
	public int selectBbsFileListCnt(BbsList bbsList) throws Exception {
		return (Integer)getSqlMapClientTemplate().queryForObject("BbsListDAO.selectBbsFileListCnt", bbsList);
	}
	
	@SuppressWarnings("unchecked")
	public List<BbsList> selectBbsInfPopList(BbsList bbsList) throws Exception {
		return list("BbsListDAO.selectBbsInfPopList", bbsList);
	}
	
	public int selectBbsInfPopListCnt(BbsList bbsList) throws Exception {
		return (Integer)getSqlMapClientTemplate().queryForObject("BbsListDAO.selectBbsInfPopListCnt", bbsList);
	}
	
    @SuppressWarnings("unchecked")
    public List<BbsList> bbsImgDetailView(BbsList bbsList) throws Exception {
    	return list("BbsListDAO.bbsImgDetailView", bbsList);
    }
    @SuppressWarnings("unchecked")
    public List<BbsList> getTopImg(BbsList bbsList) throws Exception {
    	return list("BbsListDAO.getTopImg", bbsList);
    }
	
	public int getSeq(BbsList bbsList) throws Exception {
    	return (Integer)getSqlMapClientTemplate().queryForObject("BbsListDAO.getSeq", bbsList);
    }
	
	public int getLinkSeq(BbsList bbsList) throws Exception {
		return (Integer)getSqlMapClientTemplate().queryForObject("BbsListDAO.getLinkSeq", bbsList);
	}
	
	public int getFileSeq(BbsList bbsList) throws Exception {
		return (Integer)getSqlMapClientTemplate().queryForObject("BbsListDAO.getFileSeq", bbsList);
	}
	
	public int getLinkCnt(BbsList bbsList) throws Exception {
		return (Integer)getSqlMapClientTemplate().queryForObject("BbsListDAO.getLinkCnt", bbsList);
	}
	
	public int getInfCnt(BbsList bbsList) throws Exception {
		return (Integer)getSqlMapClientTemplate().queryForObject("BbsListDAO.getInfCnt", bbsList);
	}
	public int getFileCnt(BbsList bbsList) throws Exception {
		return (Integer)getSqlMapClientTemplate().queryForObject("BbsListDAO.getFileCnt", bbsList);
	}
	
	 public int insertBbsList(BbsList bbsList) throws Exception {
    	return (Integer)update("BbsListDAO.insertBbsList", bbsList);
    }
	 
	 public int deleteImg(BbsList bbsList) throws Exception {
		 return (Integer)update("BbsListDAO.deleteImg", bbsList);
	 }
	 public int updateTopYn(BbsList bbsList) throws Exception {
		 return (Integer)update("BbsListDAO.updateTopYn", bbsList);
	 }
	 public int updateTopYn2(BbsList bbsList) throws Exception {
		 return (Integer)update("BbsListDAO.updateTopYn2", bbsList);
	 }
	public int getTopYnCnt(BbsList bbsList) throws Exception {
		return (Integer)getSqlMapClientTemplate().queryForObject("BbsListDAO.getTopYnCnt", bbsList);
	}
	 
	 public int updateBbsList(BbsList bbsList) throws Exception {
		 return (Integer)update("BbsListDAO.updateBbsList", bbsList);
	 }
	 
	 public int deleteBbsList(BbsList bbsList) throws Exception {
		 return (Integer)update("BbsListDAO.deleteBbsList", bbsList);
	 }
	 public int updateAnsState(BbsList bbsList) throws Exception {
		 return (Integer)update("BbsListDAO.updateAnsState", bbsList);
	 }
	 
	 public int deleteCPBbsLink(BbsList bbsList) throws Exception {
		 return (Integer)update("BbsListDAO.deleteCPBbsLink", bbsList);
	 }
	 public int deleteCPBbsInf(BbsList bbsList) throws Exception {
		 return (Integer)update("BbsListDAO.deleteCPBbsInf", bbsList);
	 }
	 
    public int insertBbsLink(BbsList saveVO) throws Exception {
    	return (Integer)update("BbsListDAO.insertBbsLink", saveVO);
    } 
    
	 public int deleteCPBbsFile(BbsList saveVO) throws Exception {
		 return (Integer)update("BbsListDAO.deleteCPBbsFile", saveVO);
	 }
    
    public int updateLinkCnt(BbsList saveVO) throws Exception {
    	return (Integer)update("BbsListDAO.updateLinkCnt", saveVO);
    } 
    public int updateInfCnt(BbsList saveVO) throws Exception {
    	return (Integer)update("BbsListDAO.updateInfCnt", saveVO);
    } 
    public int updateFileCnt(BbsList saveVO) throws Exception {
    	return (Integer)update("BbsListDAO.updateFileCnt", saveVO);
    } 
    
    public int updateBbsLink(BbsList saveVO) throws Exception {
    	return (Integer)update("BbsListDAO.updateBbsLink", saveVO);
    } 
    
    public int deleteUpdateBbsLink(BbsList saveVO) throws Exception {
    	return (Integer)update("BbsListDAO.deleteUpdateBbsLink", saveVO);
    } 
    
    public int deleteBbsLink(BbsList saveVO) throws Exception {
    	return (Integer)update("BbsListDAO.deleteBbsLink", saveVO);
    } 
    
    public int insertBbsInf(BbsList saveVO) throws Exception {
    	return (Integer)update("BbsListDAO.insertBbsInf", saveVO);
    } 
    
    public int updateBbsInf(BbsList saveVO) throws Exception {
    	return (Integer)update("BbsListDAO.updateBbsInf", saveVO);
    } 
    public int updateDeleteBbsInf(BbsList saveVO) throws Exception {
    	return (Integer)update("BbsListDAO.updateDeleteBbsInf", saveVO);
    } 
    public int deleteBbsInf(BbsList saveVO) throws Exception {
    	return (Integer)update("BbsListDAO.deleteBbsInf", saveVO);
    } 
    
    
    public int insertBbsFile(BbsList saveVO) throws Exception {
    	return (Integer)update("BbsListDAO.insertBbsFile", saveVO);
    } 
    public int updateBbsFile(BbsList saveVO) throws Exception {
    	return (Integer)update("BbsListDAO.updateBbsFile", saveVO);
    } 
    
    public int deleteUpdateBbsFile(BbsList saveVO) throws Exception {
    	return (Integer)update("BbsListDAO.deleteUpdateBbsFile", saveVO);
    } 
    
    public int deleteBbsFile(BbsList saveVO) throws Exception {
    	return (Integer)update("BbsListDAO.deleteBbsFile", saveVO);
    } 
    
    public int updateDeleteImg(BbsList saveVO) throws Exception {
    	return (Integer)update("BbsListDAO.updateDeleteImg", saveVO);
    } 
    
    public int deleteImgDtl(BbsList saveVO) throws Exception {
    	return (Integer)update("BbsListDAO.deleteImgDtl", saveVO);
    } 
    
    public int updateTopImg(BbsList saveVO) throws Exception {
    	return (Integer)update("BbsListDAO.updateTopImg", saveVO);
    } 
    
	 public int mergeIntoFile(BbsList bbsList) throws Exception {
		 return (Integer)update("BbsListDAO.mergeIntoFile", bbsList);
	 }

	 public String getBbsCd(int seq) throws Exception {
		 return (String)getSqlMapClientTemplate().queryForObject("BbsListDAO.getBbsCd", seq);
	 }
	 
 	@SuppressWarnings("unchecked")
	public List<BbsList> selectRelSiteList(BbsList bbsList) throws Exception {
    	return list("BbsListDAO.selectRelSiteList", bbsList);
    }

	public BbsList selectRelSiteDtlList(BbsList bbsList) {
		return (BbsList)selectByPk("BbsListDAO.selectRelSiteDtlList", bbsList);
	}

	public int insertRelSite(BbsList bbsList) throws Exception {
    	return (Integer)update("BbsListDAO.insertRelSite", bbsList);
    }
	public int updateRelSite(BbsList bbsList) throws Exception {
		return (Integer)update("BbsListDAO.updateRelSite", bbsList);
	}
	public int deleteRelSite(BbsList bbsList) throws Exception {
		return (Integer)update("BbsListDAO.deleteRelSite", bbsList);
	}
	public int deleteRelSiteImgFile(BbsList bbsList) {
		return (Integer)delete("BbsListDAO.deleteRelSiteImgFile", bbsList);
	}
	
	public BbsList selectAdmBbsDtlList(BbsList bbsList) {
		return (BbsList)selectByPk("BbsListDAO.selectAdmBbsDtlList", bbsList);
	}
	
	public int updateViewCnt(BbsList bbsList) throws Exception {
		return (Integer)update("BbsListDAO.updateViewCnt", bbsList);
	}

	@SuppressWarnings("unchecked")
	public List<BbsList> selectMainBbsList(BbsList srchVo) {  

		return list("BbsListDAO.selectMainBbsList", srchVo);
	}
	
	public List<ImprvReq> getImprvReqMngList(ImprvReq imprvReq) {
		return (List<ImprvReq>) getSqlMapClientTemplate().queryForList("BbsListDAO.getImprvReqMngList", imprvReq);
	}
	
	
	public void insertImprvReqMng(ImprvReq imprvReq) {
		update("BbsListDAO.insertImprvReqMng", imprvReq);
	}

	public int getImprvReqMngSr() {
		return (Integer) getSqlMapClientTemplate().queryForObject("BbsListDAO.getImprvReqMngSr");
	}
	
	
	public int getImprvReqMngSeq(ImprvReq imprvReq) {
		return (Integer) getSqlMapClientTemplate().queryForObject("BbsListDAO.getImprvReqMngSeq", imprvReq);
	}
	
	public int updateImprvReqMng(ImprvReq imprvReq) {
		return update("BbsListDAO.updateImprvReqMng", imprvReq);
	}
	
	public int updateImprvReqCancel(ImprvReq imprvReq) {
		return update("BbsListDAO.updateImprvReqCancel", imprvReq);
	}
	
	public ImprvReq selectImprvReqDtl(ImprvReq imprvReq) {
		return (ImprvReq)getSqlMapClientTemplate().queryForObject("BbsListDAO.selectImprvReqDtl", imprvReq);
	}
	
	public List<ImprvReq> getImprvReqPrssList(ImprvReq imprvReq) {
		return  (List<ImprvReq>) getSqlMapClientTemplate().queryForList("BbsListDAO.getImprvReqPrssList", imprvReq);
	}
	
	public int insertImprvReqPrss(ImprvReq imprvReq) {
		return update("BbsListDAO.insertImprvReqPrss", imprvReq);
	}
	
	public int updateImprvReqListPrss(ImprvReq imprvReq) {
		return update("BbsListDAO.updateImprvReqListPrss", imprvReq);
	}
	
	public int updateImprvReqPrss(ImprvReq imprvReq) {
		return update("BbsListDAO.updateImprvReqPrss", imprvReq);
	}
	
	public int deleteImprvReqPrss(ImprvReq imprvReq) {
		return update("BbsListDAO.deleteImprvReqPrss", imprvReq);
	}
	
	public int updateImprvReqDeletePrss(ImprvReq imprvReq) {
		return update("BbsListDAO.updateImprvReqDeletePrss", imprvReq);
	}
	
	public int getImprvReqFileSeq() {
		return (Integer) getSqlMapClientTemplate().queryForObject("BbsListDAO.getImprvReqFileSeq");
	}
	
	public int saveImprvReqFileData(ImprvReq imprvReq) {
		return update("BbsListDAO.saveImprvReqFileData", imprvReq);
	}
	
	public List<ImprvReq> getImprvReqFileList(ImprvReq imprvReq) {
		return (List<ImprvReq>) getSqlMapClientTemplate().queryForList("BbsListDAO.getImprvReqFileList", imprvReq);
	}
	
	public ImprvReq getImprvReqFile(ImprvReq imprvReq) {
		return (ImprvReq) getSqlMapClientTemplate().queryForObject("BbsListDAO.getImprvReqFile", imprvReq);
	}
	
	public void deleteImprvReqFile(ImprvReq imprvReq) {
		update("BbsListDAO.deleteImprvReqFile", imprvReq);		
	}
	
}
