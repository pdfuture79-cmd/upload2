package egovframework.common.file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.commons.lang.exception.ExceptionUtils;

import com.jcraft.jsch.SftpException;

import egovframework.com.cmm.service.EgovProperties;
import egovframework.common.util.SFTPSyncManager2;
import egovframework.common.util.UtilZip;

/**
 * 포탈에 공개되고 있는 데이터셋 중 일정 갯수 이상의 데이터셋을 CSV 파일로 만들어 웹서버 #1, #2로 전송한다. 
 * @author Wiseopen
 *
 */
public class DatasetCsvCreZip {

	private static final Logger log = Logger.getLogger(NetConnFileSyncManager.class.getName());
	private final boolean logger = true;
	
	//private final String ENCODING = "UTF-8";  // LINUX
	//private final String ENCODING = "EUC-KR";   // WINDOWS
	
	// 작업 년월일
	//private final String SYSDATE = new java.text.SimpleDateFormat("yyyyMMdd").format(new java.util.Date());
	
	// 작업 디렉토리 및 인코딩 정보
	//private String workingDir = "/usr/bin/apache-tomcat-7.0.64/ggopen/job_shell/csvTsf/working/";
	private String  workingDir = EgovProperties.getProperty("Globals.CsvCreateFilePath") + File.separator;
	String tmpEncoding = EgovProperties.getProperty("Globals.CsvCreateEncoding");
	private final String ENCODING = (tmpEncoding.equals(""))? "UTF-8" : tmpEncoding;
	
	///// FTP 접속 정보 -----------------------
	/*private String toPath = EgovProperties.getProperty("Globals.CsvFtpFilePath") ;	
	private String host1 = EgovProperties.getProperty("Globals.CsvFtpHost1");
	private String host2 = EgovProperties.getProperty("Globals.CsvFtpHost2");
	private int port = Integer.parseInt(EgovProperties.getProperty("Globals.CsvFtpPort"));
	private String user = EgovProperties.getProperty("Globals.CsvFtpUser");
	private String pw1 = EgovProperties.getProperty("Globals.CsvFtpPass1");	
    private String pw2 = EgovProperties.getProperty("Globals.CsvFtpPass2");	*/
	///// FTP 접속 정보 END -------------------

	// 작업 파일 삭제 여부
	//private boolean deleteWorkFile = false;
	
	// 대용량 파일 생성 기준
	private int defRecordCount = 10000;
	private boolean isFtp = false;
	
	
	// 생성자
	public DatasetCsvCreZip(int cnt, boolean isFtp) {
		if (cnt > 0) this.defRecordCount = cnt;
		if (isFtp) 	 this.isFtp = isFtp;
	}
	

	//---------------------------------------------------------	
	// 실행 
	//---------------------------------------------------------
	public void run() {
		if(this.workingDir.length() < 5 ) {
			LOG("Globals.CsvCreateFilePath 값을 정의하세요.");
			return;
		}
		LOG("=========================================================");
		LOG("데이터셋 CSV 생성 작업 시작 >> 인코딩 : (" + ENCODING + ") ");

		// 대상 데이터셋 가져오기
		List<Map<String, String>> list = getDatasetList();
		
		String filePath = this.workingDir;
		File file = new File(filePath);
		if(!file.exists()) {
			file.mkdirs();
		}
		
		int csvFileSuccessCnt = 0;
		try {
			// 파일 생성
			for(Map<String, String> data : list) {
				String infId = data.get("INF_ID");
				String infSeq = data.get("INF_SEQ");
				String dsId = data.get("DS_ID");  // owner 포함
				String fileNm = data.get("FILE_NM") ;
				// 특수문자 제거
				fileNm =  StringReplace(fileNm)+ ".csv";
				
				LOG(">>> DS_ID : " + dsId + " :" + fileNm + " 작업시작");
				csvFileSuccessCnt = csvFileSuccessCnt + createCsvFileByDataset(infId, infSeq, dsId, fileNm);
				LOG("<<< DS_ID : " + dsId + " 작업종료");
				
				LOG("<<< ZIP : " + fileNm + " 압축시작");
				zipFileWork (fileNm);
				LOG("<<< ZIP : " + fileNm + " 압축종료");
			}

			// csv 파일 웹서버에 전송
		/*	if (isFtp) {
				LOG(">>> local DIR " + this.workingDir);
				LOG(">>> remote DIR " + this.toPath);
				LOG(">>> remote host1 " + this.host1);
				LOG(">>> remote host2 " + this.host2);
				LOG(">>> remote port " + this.port);
				LOG(">>> remote user " + this.user);				
				// csv 파일 전송 시작
				csvFtp1Transfer(this.workingDir, this.toPath, this.host1, this.port, this.user, this.pw1);
				csvFtp1Transfer(this.workingDir, this.toPath, this.host2, this.port, this.user, this.pw2);				
			}*/
		} catch (Exception e) {//시큐어코딩 조치 - Exception to String
	        	log.info(ExceptionUtils.getStackTrace(e));
		}
		insertJobLog(csvFileSuccessCnt);
		LOG("<<< 데이터셋 CSV 생성 및 압축 작업 종료");
	}

	/**
	 * 특수문자 제거
	 * @return
	 */
	public static String StringReplace(String str){       
      String match = "[^\uAC00-\uD7A3xfe0-9a-zA-Z\\s]";
      str =str.replaceAll(match, "_");
      return str;
    }
	 
	/**
	 * zip 파일 압축
	 * @param zipFile
	 */
	private void zipFileWork(String fileNm) {
		
		//String fileName = zipFile.getName().substring(0, zipFile.getName().lastIndexOf("."));
		//String localPath = fileName.replaceAll("_", "/").replaceAll(EgovProperties.getProperty("FileNetConn.rootPath0"), EgovProperties.getProperty("FileNetConn.rootPath1"));
		//log("0 : localpath : " + localPath);
		String filePath = workingDir + fileNm;		
		LOG("**************** 2. zipFileWork : " + filePath );
		File file = new File(filePath);
		if(!file.exists()) {
			LOG("<<< 압축대상 FILE ERROR : " + filePath);
			return;
		}
		UtilZip uz = new UtilZip();
		try {
			uz.zip(file, false);
		} catch (IOException e) {//시큐어코딩 조치 - Exception to String
	        	log.info(ExceptionUtils.getStackTrace(e));
		}
		file.delete();
		
		// 파일 전송
//		transferFile(localPath);
		
	}
	
	
	/**
	 * 작업 대상 데이터셋 목록
	 * @return
	 */
	private List<Map<String, String>> getDatasetList() {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT");
		sql.append(" 	  B.INF_ID");
		sql.append(" 	, S.INF_SEQ");
		sql.append(" 	, C.OWNER_CD || '.' || C.DS_ID as DS_ID ");
		sql.append(" 	, A.INF_CNT");
		sql.append(" 	, replace(B.INF_NM, ' ', '') AS FILE_NM");
		sql.append(" FROM TB_STAT_OPEN A inner JOIN TB_OPEN_INF B on A.INF_ID = B.INF_ID "); 
		sql.append(" 	 INNER JOIN TB_OPEN_INF_SRV S on B.INF_ID = S.INF_ID and S.SRV_CD = 'S' and S.SRV_YN = 'Y' ");
		sql.append(" 	 INNER JOIN TB_OPEN_DS C on B.DS_ID = C.DS_ID ");
		sql.append(" 	WHERE YYYYMMDD = (select max(YYYYMMDD) from TB_STAT_OPEN) ");
		sql.append(" 		AND B.INF_STATE = 'Y' AND B.OPEN_DTTM <= SYSDATE ");
		sql.append(" 		AND INF_CNT >= " + this.defRecordCount  );
		//sql.append(" 		AND INF_CNT < 11700 " );  // 테스트를 위하여 제한    
		sql.append(" 	    AND EXISTS (select 1 from TB_OPEN_INF_SRV where  USE_YN = 'Y' and SRV_CD = 'S' and INF_ID = B.INF_ID) ");
		sql.append(" ORDER BY INF_CNT ASC "); // 작은 거 부터 생성
		
		//LOG("<<< 1. getDatasetList : " + sql.toString());
		
		DBConn db = new DBConn();
		List<Map<String, String>> list = db.selectList(sql.toString(), null);
		db.close();
		
		return list;
	}

	/**
	 * 데이터셋 데이터를 CSV 파일로 추출
	 * @param dsId
	 */
	private int createCsvFileByDataset(String infId, String infSeq, String dsId, String fileNm){
		// 컬럼정보 구하기
		List<Map<String, String>> columnNames = getDatasetColumnNames(infId, infSeq);
		  
		// 컬럼 리스트
		StringBuffer csvHeader = new StringBuffer();
		for(int i = 0 ; i < columnNames.size() ; i ++) {
			csvHeader.append(columnNames.get(i).get("COL_NM") + ",");
		}       
		csvHeader.delete(csvHeader.length()-1, csvHeader.length()); // 마지막 , 제거
		csvHeader.append("\r\n");
		
		String filePath = workingDir + fileNm;
		LOG("**************** 1. createCsvFile : " + filePath );
		// 파일 생성.
		File file = new File(filePath);
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {//시큐어코딩 조치 - Exception to String
	        	log.info(ExceptionUtils.getStackTrace(e));
			}
			//file..mkdirs();
		}
		
		BufferedWriter writer = null;
		OutputStreamWriter osw = null;
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(filePath);
			osw = new OutputStreamWriter(fos, ENCODING) ;
			writer = new BufferedWriter(osw);
			writer.write(csvHeader.toString());
			getDatasetData(writer, dsId, columnNames);
			return 1;
		} catch (IOException e) {//시큐어코딩 조치 - Exception to String
	        	log.info(ExceptionUtils.getStackTrace(e));
			return 0;
		}finally{
			try{
				if(writer!=null){writer.close();}
				if(osw!=null){osw.close();}
				if(fos!=null){fos.close();}
			}catch(IOException e){//시큐어코딩 조치 - Exception to String
	        	log.info(ExceptionUtils.getStackTrace(e));
			}
		}
	}

	/**
	 * 데이터셋 컬럼명 목록
	 * @param dsId
	 * @return
	 */
	private List<Map<String, String>> getDatasetColumnNames(String infId, String infSeq) {
		//String sql = "SELECT COLUMN_NAME FROM ALL_TAB_COLUMNS WHERE TABLE_NAME = '"+dsId+"'";
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT A.COL_NM AS COL_NM ");
		sql.append(" 	 , A.SRC_COL_ID AS SRC_COL_ID ");
		sql.append(" 	 , A.SORT_TAG AS SORT_TAG ");
		sql.append(" FROM ( ");
		/* sql.append(" 	 SELECT '번호' AS COL_NM ");
		sql.append(" 	 	, 'number' AS SRC_COL_ID ");
		sql.append(" 	 	, 'X' AS SORT_TAG ");		
		sql.append(" 	 	, 0 AS SORT ");
		sql.append(" 	 	, 0 AS V_ORDER ");		
		sql.append(" 	 FROM DUAL ");
		sql.append(" 	 UNION ALL ");
		*/
		sql.append(" 	 SELECT E.COL_NM || ( SELECT F.DITC_NM    FROM TB_COMM_CODE F WHERE F.GRP_CD = 'D1013' AND F.DITC_CD = E.UNIT_CD) AS COL_NM ");		
		sql.append(" 	 	, E.SRC_COL_ID AS SRC_COL_ID ");	
		sql.append(" 	 	, NVL(A.SORT_TAG, 'X') AS SORT_TAG ");
		sql.append(" 	 	, 1 AS SORT ");		
		sql.append(" 	 	, A.V_ORDER AS V_ORDER ");		
		sql.append(" 	 FROM TB_OPEN_INF_SCOL A JOIN TB_OPEN_INF_SRV B ON B.INF_ID = A.INF_ID   AND B.INF_SEQ = A.INF_SEQ ");		
		sql.append(" 	 JOIN TB_OPEN_INF C ON C.INF_ID = B.INF_ID  ");		
		sql.append(" 	 JOIN TB_OPEN_DS D ON D.DS_ID = C.DS_ID  ");		
		sql.append(" 	 JOIN TB_OPEN_DSCOL E ON E.DS_ID = D.DS_ID AND E.COL_SEQ = A.COL_SEQ ");		
		sql.append(" 	 WHERE A.INF_ID = '" + infId + "'");		
        sql.append(" 	   AND A.INF_SEQ = '" + infSeq + "'");         
        sql.append(" 	   AND A.USE_YN = 'Y' ");         
        sql.append(" 	   AND A.VIEW_YN = 'Y' ");
        sql.append(" 	   AND B.SRV_YN = 'Y' ");
        sql.append(" 	   AND B.SRV_CD = 'S' ");
        sql.append(" 	   AND C.INF_STATE = 'Y' ");
        sql.append(" 	   AND C.OPEN_DTTM <= SYSDATE ");
        sql.append(" 	   AND D.USE_YN = 'Y' ");
        sql.append(" 	   AND D.DS_CD = 'RAW' ");
        sql.append(" 	   AND E.USE_YN = 'Y' ");
        sql.append(" 	) A ");
        sql.append(" 	ORDER BY A.SORT ASC, A.V_ORDER ASC  ");			             
  
      //  LOG("<<< 2. getDatasetColumnNames : " + sql.toString());
        
		DBConn db = new DBConn();
		List<Map<String, String>> list = db.selectList(sql.toString(), null);
		db.close();
		
		return list;
	}
	
	/**
	 * 데이터셋 데이터 조회
	 * @param dsId
	 * @return
	 * @throws IOException 
	 */
	private void getDatasetData(BufferedWriter writer, String dsId, List<Map<String, String>> columnNames ){
		
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT ");
		for(int i = 0 ; i < columnNames.size() ; i ++) {
			sql.append(columnNames.get(i).get("SRC_COL_ID") + ", ");
		}
		sql.delete(sql.length()-2, sql.length());
		sql.append(" FROM " + dsId);
		
		// 구급활동 데이터 오류가 있어서 강제로 추가 ... 데이터 정제하면 필요없음
		if (dsId.equals("GGOPENGOV.TB_RELIF_ACT_L")) {
			sql.append(" WHERE SUM_YY IS NOT NULL" );
		}
		// SORT 정보
		StringBuffer csvSort = new StringBuffer();
		for(int i = 0 ; i < columnNames.size() ; i ++) {
			if (columnNames.get(i).get("SORT_TAG").equals("A") ) {				
				csvSort.append(columnNames.get(i).get("SRC_COL_ID") + " ASC,");
			} 
			else if (columnNames.get(i).get("SORT_TAG").equals("D")) {
					csvSort.append(columnNames.get(i).get("SRC_COL_ID") + " DESC,");
			}
		}    
		if (csvSort.length() > 0) { // 마지막 , 제거
			csvSort.delete(csvSort.length()-1, csvSort.length());
			sql.append(" ORDER BY " + csvSort.toString() );
		}
		
		//LOG("<<< 3. getDatasetData : " + sql.toString());
		
		DBConn dbConn = new DBConn();		
		Connection conn = dbConn.getConnection();		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
				
			pstmt = conn.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			
			int rowCnt = 0;		
			while(rs.next()) {
				rowCnt =  rowCnt + 1;
				StringBuffer sb = new StringBuffer();
				for(Map<String, String> entry: columnNames){
					if(entry.get("SRC_COL_ID") == null){
						sb.append("" + ",");
					}else{
						String value = rs.getString(entry.get("SRC_COL_ID"));
		                sb.append(csvParser(value) + ",");
		                //만건당 로그 출력
						if ((rowCnt % 1000000) == 0.0 ) {
								LOG(" " + rowCnt + " rows...");
						}
					}
				}                   
				sb.delete(sb.length()-1, sb.length());
				sb.append("\r\n");
				
				writer.write(sb.toString());
			}
			pstmt.close();
			rs.close();
				
		} catch (SQLException e) {//시큐어코딩 조치 - Exception to String
	        	log.info(ExceptionUtils.getStackTrace(e));
		} catch (IOException e) {//시큐어코딩 조치 - Exception to String
	        	log.info(ExceptionUtils.getStackTrace(e));
		} finally {
			try {
				if(pstmt != null) { pstmt.close(); }
				if(rs != null) { rs.close(); 	}
			} catch (SQLException e) {//시큐어코딩 조치 - Exception to String
	        	log.info(ExceptionUtils.getStackTrace(e));
			}
		}
		dbConn.close();
	}
	
	private String csvParser(String value) {
		value = value == null?"":value;
		
		if (value.indexOf("\r") >= 0 || value.indexOf("\n") >= 0) {
		    value = value.replaceAll("\\r", "").replaceAll("\\n", " ");
		}
		
        if (value.indexOf("\"") >= 0 || value.indexOf(",") >= 0) {
        	value = "\"" + value.replaceAll("\\\"", "\"\"") + "\"";
        }
		return value;
	}
	
	/**
	 * FTP 전송
	 */
	private void csvFtp1Transfer(String srcDir, String targetDir, String host, int port, String user, String pw) {
		LOG(">>> FTP 전송 시작");
		try {
			SFTPSyncManager2  sm = new SFTPSyncManager2();
			sm.init(host, port, user, pw);
			sm.uploadSync(srcDir , targetDir);
			sm.disconnect();
		} catch (SftpException e) {//시큐어코딩 조치 - Exception to String
	        	log.info(ExceptionUtils.getStackTrace(e));
		}
		LOG("<<< FTP 전송 종료");
		// deleteWorkFiles();
	}
	
	/**
	 * 작업 후 파일 삭제
	 */
	private void deleteWorkFiles(boolean delfile) {
		if(delfile) {
			File[] workFiles = new File(this.workingDir).listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String name) {
					if(name.endsWith(".csv")) {
						return true;
					}
					return false;
				}
			});
			
			for(File workFile : workFiles) {
				workFile.delete();
			}
		}
	}
	
	/**
	 * 배치 작업 로그
	 */
	private void insertJobLog(int fileCnt) {
		StringBuffer insert = new StringBuffer();
		insert.append(" INSERT INTO TB_LOG_JOB	(JOB_NO,JOB_NM, JOB_TAG,SOURCE,TARGET,ROWCOUNT,MESSAGE, REG_DTTM)");
		insert.append(" SELECT"); 
		insert.append("		SQ_LOG_JOB.NEXTVAL as JOB_NO"); 
		insert.append("		  ,'DatasetCsvCreZip.java' "); 
		insert.append("		  , 'Crontab' "); 
		insert.append("		  , '' "); 
		insert.append("		  , '' "); 
		insert.append("		  , ? "); 
		insert.append("		  , 'CSV 파일 성공 갯수' "); 
		insert.append("		  , SYSDATE from dual;"); 

		DBConn db = new DBConn();
		db.insert(insert.toString(), new Object[]{fileCnt});
	}
	
	/**
	 * DB 연결 클래스
	 * @author User
	 *
	 */
	public static class DBConn {
		
		Connection conn = null;
		
		public DBConn() {
			String driverClass = EgovProperties.getProperty("Globals.DriverClassName");
			String url = EgovProperties.getProperty("Globals.Url");
			String userName = EgovProperties.getProperty("Globals.UserName");
			String password = EgovProperties.getProperty("Globals.Password");
			
			try {
	            Class.forName(driverClass);
	            conn = DriverManager.getConnection(url, userName, password);
			} catch(Exception e) {//시큐어코딩 조치 - Exception to String
	        	log.info(ExceptionUtils.getStackTrace(e));
			}
		}
		
		public List<Map<String, String>> selectList(String sql, Object[] params) {
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			List<Map<String, String>> result = new ArrayList<Map<String, String>>();
			try {
				pstmt = conn.prepareStatement(sql);
				if(params != null) {
					for(int i = 0 ; i < params.length ; i ++) {
						pstmt.setObject(i+1, params[i]);
					}
				}
				rs = pstmt.executeQuery();
				
				ResultSetMetaData rsmd = rs.getMetaData();
				int colCnt = rsmd.getColumnCount();
				while(rs.next()) {
				    Map<String, String> data = new HashMap<String, String>();
				    for(int i = 1; i <= colCnt; i++){
				        String columnName = rsmd.getColumnLabel(i).toUpperCase();
				        data.put(columnName, rs.getString(columnName));
				    }
				    result.add(data);
				}
				pstmt.close();
				rs.close();
			} catch (SQLException e) {//시큐어코딩 조치 - Exception to String
	        	log.info(ExceptionUtils.getStackTrace(e));
			} finally {
				try {
					if(pstmt != null) { pstmt.close(); }
					if(rs != null) { rs.close(); 	}
				} catch (SQLException e) {//시큐어코딩 조치 - Exception to String
	        	log.info(ExceptionUtils.getStackTrace(e));
				}
			}
			return result;
		}
		
		public void insert(String insert, Object[] params) {
			PreparedStatement pstmt = null;
			try {
				pstmt = conn.prepareStatement(insert);
				if(params != null) {
					for(int i = 0 ; i < params.length ; i ++) {
						pstmt.setObject(i+1, params[i]);
					}
				}
				pstmt.executeUpdate();
			} catch (SQLException e) {//시큐어코딩 조치 - Exception to String
	        	log.info(ExceptionUtils.getStackTrace(e));
			} finally {
				try {
					if(pstmt!=null){pstmt.close();}
				} catch (SQLException e) {//시큐어코딩 조치 - Exception to String
	        	log.info(ExceptionUtils.getStackTrace(e));
				}
			}
		}
		
		public Connection getConnection() {
			return this.conn;
		}
		
		public void close() {
			if(conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {//시큐어코딩 조치 - Exception to String
	        	log.info(ExceptionUtils.getStackTrace(e));
				}
			}
		}
	}
	
	public void LOG(Object obj) {
		if(logger) {
			log.info(obj.toString());
		}
	}
	
	
	// app run
	public static void main(String[] args) throws Exception {
		if(args.length < 1) {
			System.out.print("arguments 갯수가 맞지 않습니다. \nUSAGE : DatasetCsvCreZip minRecordCount iscompress  ");
			return;
		}
		
		int count = Integer.parseInt(args[0]);
		boolean isFtp = false;
		if(args.length > 1) {			
			if (args[1].toString().equals("Y")) {
				isFtp =  true;
			}
			// isFtp = Boolean.parseBoolean(args[1]);
		}	
		new DatasetCsvCreZip(count, isFtp).run();
	}
}
