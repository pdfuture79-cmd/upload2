package egovframework.common.file.service.impl;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

import egovframework.admin.service.service.OpenInfLcol;
import egovframework.admin.service.service.OpenInfVcol;
import egovframework.com.cmm.service.impl.EgovComAbstractDAO;

/**
 * 서비스설정관리
 * @author wiseopen
 * @since 2014.04.17
 * @version 1.0
 * @see
 *
 */
@Repository("FileDAO")
public class FileDAO extends EgovComAbstractDAO {

	
	 public int fileSeq() throws Exception {
		 return (Integer)getSqlMapClientTemplate().queryForObject("FileDAO.fileSeq");
	 }
	 
	 public int fileMultiSeq() throws Exception {
		 return (Integer)getSqlMapClientTemplate().queryForObject("FileDAO.fileMultiSeq");
	 }
	 
	 public int fileBbsSeq() throws Exception {
		 return (Integer)getSqlMapClientTemplate().queryForObject("FileDAO.fileBbsSeq");
	 }
	 public int filePublishSeq() throws Exception {
		 return (Integer)getSqlMapClientTemplate().queryForObject("FileDAO.filePublishSeq");
	 }
	 
	 @SuppressWarnings("unchecked")
	 public List<HashMap<String, Object>> getFileNameByFileSeq(int fileSeq) throws Exception {
		 return getSqlMapClientTemplate().queryForList("FileDAO.getFileNameByOpenInfFileSeq", fileSeq);
	 }
	 
	 @SuppressWarnings("unchecked")
	 public List<HashMap<String, Object>> getFileNameByBbsFileSeq(int fileSeq) throws Exception {
		 return getSqlMapClientTemplate().queryForList("FileDAO.getFileNameByBbsFileSeq", fileSeq);
	 }
	 @SuppressWarnings("unchecked")
	 public List<HashMap<String, Object>> getFileNameByPublishFileSeq(int fileSeq) throws Exception {
		 return getSqlMapClientTemplate().queryForList("FileDAO.getFileNameByPublishFileSeq", fileSeq);
	 }
	 @SuppressWarnings("unchecked")
	 public List<HashMap<String, Object>> getFileNameByCateId(String etc) throws Exception {
		 return getSqlMapClientTemplate().queryForList("FileDAO.getFileNameByCateId", etc);
	 }
	 @SuppressWarnings("unchecked")
	 public List<HashMap<String, Object>> getFileNameByLinkSeq(OpenInfLcol openInfLcol) throws Exception {
		 return getSqlMapClientTemplate().queryForList("FileDAO.getFileNameByLinkSeq", openInfLcol);
	 }
	 @SuppressWarnings("unchecked")
	 public List<HashMap<String, Object>> getFileNameByMediaNo(OpenInfVcol openInfVcol) throws Exception {
		 return getSqlMapClientTemplate().queryForList("FileDAO.getFileNameByMediaNo", openInfVcol);
	 }
}
