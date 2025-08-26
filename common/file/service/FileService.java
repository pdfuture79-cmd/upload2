package egovframework.common.file.service;

import java.util.HashMap;
import java.util.List;


/**
 * 파일 서비스를 정의하기 위한 서비스 인터페이스
 * @author wiseopen
 * @since 2014.04.17
 * @version 1.0
 * @see
 *
 */

public interface FileService {
	/**
	 * @param fileVo
	 * @param Seq
	 * @param filePath
	 * @param fileCd
	 * @return
	 * @throws Exception
	 */
	public boolean fileUpload(FileVo fileVo, int Seq, String filePath, String fileCd)  throws Exception;
	
	/**
	 * @param fileVo
	 * @param Seq
	 * @param filePath
	 * @param fileCd
	 * @param etc
	 * @return
	 * @throws Exception
	 */
	public boolean fileUpload(FileVo fileVo, int Seq, String filePath, String fileCd, String etcString)  throws Exception;

	/**
	 * @param fileVo
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public boolean fileTypeCkeck(FileVo fileVo,String[] type) throws Exception;
	/**
	 * @param fileVo
	 * @throws Exception
	 */
	public void setFileCuData(FileVo fileVo)throws Exception;
	//public String getFileNameByFileSeq(String downCd, int fileSeq) throws Exception;
	
	/**
	 * @param downCd
	 * @param fileSeq
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String, Object>> getFileNameByFileSeq(String downCd, int fileSeq) throws Exception;
	
	/**
	 * @param downCd
	 * @param fileSeq
	 * @param etc
	 * @return
	 * @throws Exception
	 */
	public List<HashMap<String, Object>> getFileNameByFileSeq(String downCd, int fileSeq, String etc) throws Exception;
    
}
