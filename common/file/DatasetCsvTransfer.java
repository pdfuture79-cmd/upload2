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
import egovframework.common.util.SFTPSyncManager;

/**
 * 포탈에 공개되고 있는 데이터셋을 CSV 파일로 만들어 빅데이터 센터로 전송한다. 
 * @author hsJang
 *
 */
public class DatasetCsvTransfer {

	private static final Logger log = Logger.getLogger(NetConnFileSyncManager.class.getName());
	private final boolean logger = true;
	
	private final String ENCODING = "UTF-8";
	
	// 작업 년월일
	private final String SYSDATE = new java.text.SimpleDateFormat("yyyyMMdd").format(new java.util.Date());
	
	// 작업 디렉토리
	private String workingDir = "/usr/bin/apache-tomcat-7.0.64/ggopen/job_shell/csvTsf/working/";
	
	///// FTP 접속 정보 START
	private String host = "";
	private int port = 22;
	private String user = "";
	private String pw = "";
	///// FTP 접속 정보 END	
	private String toPath = "";
	// 작업 파일 삭제 여부
	private boolean deleteWorkFile = false;
	
	public DatasetCsvTransfer(String host, int port, String user, String pw, String toPath, boolean deleteWorkFile) {
		this.host = host;
		this.port = port;
		this.user = user;
		this.pw = pw;
		this.toPath = toPath;
		this.deleteWorkFile = deleteWorkFile;
	}
	
	public void run() {
		LOG(">>> 데이터셋 CSV 전송 작업 시작");
		List<Map<String, String>> list = getDatasetList();
		
		String filePath = workingDir + SYSDATE + File.separator;
		File file = new File(filePath);
		if(!file.exists()) {
			file.mkdirs();
		}
		
		int csvFileSuccessCnt = 0;
		BufferedWriter writer = null;
		OutputStreamWriter osw = null;
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(filePath + "information_" + SYSDATE + ".csv");
			osw = new OutputStreamWriter(fos, ENCODING) ;
			writer = new BufferedWriter(osw);
			// information_YYYYMMDD.csv 파일 생성 START
			writer.write("DS_ID,CATE_NM,INF_TAG,INF_NM,INF_EXP,CCL_NM,LAST_DTTM,LOAD_NM,DATA_COND_DTTM,FILE_NM\r\n");

			for(Map<String, String> data : list) {
				String dsId = data.get("DS_ID");
				String fileNm = data.get("FILE_NM");
				
				LOG(">>> DS_ID : " + dsId + " 작업시작");
				csvFileSuccessCnt = csvFileSuccessCnt + createCsvFileByDataset(dsId, fileNm);
				LOG("<<< DS_ID : " + dsId + " 작업종료");
				
				createListInfo(data, writer);
			}
			
			// information_YYYYMMDD.csv 파일 생성 END
			
			// table_information_YYYYMMDD.csv 파일 생성 START
			createTableInformation();
			// table_information_YYYYMMDD.csv 파일 생성 END
			
			// csv 파일 전송 시작
			csvTransfer();
		} catch (Exception e) {//시큐어코딩 조치 - Exception to String
	        	log.info(ExceptionUtils.getStackTrace(e));
		}finally{
			try{
				if(writer!=null){writer.close();}
				if(osw!=null){osw.close();}
				if(fos!=null){fos.close();}
			}catch(IOException e){//시큐어코딩 조치 - Exception to String
	        	log.info(ExceptionUtils.getStackTrace(e));
			}
		}
		insertJobLog(csvFileSuccessCnt);
		LOG("<<< 데이터셋 CSV 전송 작업 종료");
	}

	/**
	 * 데이터셋 목록
	 * @return
	 */
	private List<Map<String, String>> getDatasetList() {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT");
		sql.append(" 	B.DS_ID");
		sql.append(" 	, D.CATE_NM || ' > ' || C.CATE_NM AS CATE_NM");
		sql.append(" 	, B.INF_TAG");
		sql.append(" 	, B.INF_NM");
		sql.append(" 	, B.INF_EXP");
		sql.append(" 	, E.DITC_NM AS CCL_NM");
		sql.append(" 	, NVL(TO_CHAR(B.LOAD_DTTM, 'YYYY-MM-DD'), '2015-09-23') AS LAST_DTTM");
		sql.append(" 	, (SELECT DITC_NM FROM TB_COMM_CODE WHERE GRP_CD = 'D1009' AND DITC_CD = B.LOAD_CD) LOAD_NM");
		sql.append(" 	, B.DATA_COND_DTTM");
		sql.append(" 	, DS_ID || '_"+SYSDATE+"' || '.csv' AS FILE_NM");
		sql.append(" FROM ("); 
		sql.append(" 	SELECT ");
		sql.append(" 		A.INF_ID AS INF_ID,");
	    sql.append("         SUM(A.VIEW_CNT) AS VIEW_CNT");
		sql.append(" 	FROM TB_OPEN_INF_SRV A"); 
		sql.append(" 		JOIN TB_OPEN_INF B ON B.INF_ID = A.INF_ID"); 
		sql.append(" 		JOIN TB_OPEN_CATE C ON C.CATE_ID = B.CATE_ID ");
		sql.append(" 		JOIN TB_OPEN_CATE D ON D.CATE_ID = C.CATE_ID_TOP ");
		sql.append(" 		LEFT JOIN TB_OPEN_DS E ON E.DS_ID = B.DS_ID");
		sql.append(" 	WHERE A.SRV_YN = 'Y'");
		sql.append(" 		AND A.SRV_CD IN ('S', 'C', 'M', 'F', 'A', 'L')");
		sql.append(" 		AND B.INF_STATE = 'Y'");
		sql.append(" 		AND B.OPEN_DTTM <= SYSDATE");
		sql.append(" 		AND C.USE_YN = 'Y'");
		sql.append(" 		AND D.USE_YN = 'Y'");
		sql.append(" 		AND CASE A.SRV_CD WHEN 'F' THEN 'Y' WHEN 'L' THEN 'Y' ELSE E.USE_YN END = 'Y'");
		sql.append(" 		AND CASE A.SRV_CD WHEN 'F' THEN 'RAW' WHEN 'L' THEN 'RAW' ELSE E.DS_CD END = 'RAW'");
		sql.append(" 	GROUP BY A.INF_ID");
		sql.append(" 	) A"); 
		sql.append(" 	JOIN TB_OPEN_INF B ON B.INF_ID = A.INF_ID"); 
		sql.append(" 	JOIN TB_OPEN_CATE C ON C.CATE_ID = B.CATE_ID ");
		sql.append(" 	JOIN TB_OPEN_CATE D ON D.CATE_ID = C.CATE_ID_TOP");
		sql.append(" 	LEFT JOIN TB_COMM_CODE E ON E.GRP_CD = 'D1008' AND E.DITC_CD = B.CCL_CD");
		sql.append(" WHERE");
		sql.append(" 	B.DS_ID IS NOT NULL");		
		sql.append(" ORDER BY A.VIEW_CNT DESC		");
		
		LOG(sql);
		
		DBConn db = new DBConn();
		List<Map<String, String>> list = db.selectList(sql.toString(), null);
		db.close();
		
		return list;
	}

	/**
	 * 데이터셋 데이터를 CSV 파일로 추출
	 * @param dsId
	 */
	private int createCsvFileByDataset(String dsId, String fileNm) {
		// 컬럼명
		List<Map<String, String>> columnNames = getDatasetColumnNames(dsId);
		
		StringBuffer csvHeader = new StringBuffer();
		for(int i = 0 ; i < columnNames.size() ; i ++) {
			csvHeader.append(columnNames.get(i).get("COLUMN_NAME") + ",");
		}       
		csvHeader.delete(csvHeader.length()-1, csvHeader.length());
		csvHeader.append("\r\n");
		
		String filePath = workingDir + SYSDATE + File.separator;
		File file = new File(filePath);
		if(!file.exists()) {
			file.mkdirs();
		}
		
		BufferedWriter writer = null;
		OutputStreamWriter osw = null;
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(filePath + fileNm);
			osw = new OutputStreamWriter(fos, ENCODING) ;
			writer = new BufferedWriter(osw);
			writer.write(csvHeader.toString());
			getDatasetData(dsId, columnNames, writer);
			return 1;
		} catch (Exception e) {//시큐어코딩 조치 - Exception to String
	        	log.info(ExceptionUtils.getStackTrace(e));
			return 0;
		} finally{
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
	private List<Map<String, String>> getDatasetColumnNames(String dsId) {
		String sql = "SELECT COLUMN_NAME FROM ALL_TAB_COLUMNS WHERE TABLE_NAME = '"+dsId+"' AND OWNER IN ('GGOPENGOV', 'GGOPENPTL')";
		
		DBConn db = new DBConn();
		List<Map<String, String>> list = db.selectList(sql, null);
		db.close();
		
		return list;
	}
	
	/**
	 * 데이터셋 데이터 조회
	 * @param dsId
	 * @return
	 * @throws IOException 
	 */
	private void getDatasetData(String dsId, List<Map<String, String>> columnNames, BufferedWriter writer) throws Exception {
		
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT ");
		for(int i = 0 ; i < columnNames.size() ; i ++) {
			sql.append(columnNames.get(i).get("COLUMN_NAME") + ", ");
		}
		sql.delete(sql.length()-2, sql.length());
		sql.append(" FROM GGOPENGOV."+dsId);
		
		DBConn dbConn = new DBConn();
		
		Connection conn = dbConn.getConnection();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
				
			pstmt = conn.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				StringBuffer sb = new StringBuffer();
				for(Map<String, String> entry: columnNames){
					if(entry.get("COLUMN_NAME") == null){
						sb.append("" + ",");
					}else{
						String value = rs.getString(entry.get("COLUMN_NAME"));
		                sb.append(csvParser(value) + ",");
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
	
	/**
	 * csv 파일 및 데이터셋 정보
	 * @param data
	 * @param writer
	 * @throws IOException
	 */
	private void createListInfo(Map<String, String> data, BufferedWriter writer) throws IOException {
		String dsId = data.get("DS_ID");
		String cateNm = data.get("CATE_NM");
		String infTag = data.get("INF_TAG");
		String infNm = data.get("INF_NM");
		String infExp = data.get("INF_EXP");
		String cclNm = data.get("CCL_NM");
		String lastDttm = data.get("LAST_DTTM");
		String fileNm = data.get("FILE_NM");
		
		writer.write(csvParser(dsId) + ","
				+ csvParser(cateNm) + ","
				+ csvParser(infTag) + ","
				+ csvParser(infNm) + ","
				+ csvParser(infExp) + ","
				+ csvParser(cclNm) + ","
				+ csvParser(lastDttm) + ","
				+ csvParser(fileNm) + "\r\n"
				);
	}
	
	/**
	 * 테이블 정보 csv 파일 생성
	 */
	private void createTableInformation() {
		LOG(">>> table_information_"+SYSDATE+".csv 만들기 시작");
		String csvHeader = "TABLE_NAME,TABLE_COMMENTS,COLUMN_NAME,COMMENTS,TYPE,NULLABLE,PK_YN\r\n";
		
		String filePath = workingDir + SYSDATE + File.separator;
		File file = new File(filePath);
		if(!file.exists()) {
			file.mkdirs();
		}
		BufferedWriter writer = null;
		OutputStreamWriter osw = null;
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(filePath + "table_information_"+SYSDATE+".csv");
			osw = new OutputStreamWriter(fos, ENCODING) ;
			writer = new BufferedWriter(osw);
			writer.write(csvHeader);
			getTableInfoList(writer);
		} catch (Exception e) {//시큐어코딩 조치 - Exception to String
	        	log.info(ExceptionUtils.getStackTrace(e));
		}finally{
			try{
				if(writer!=null){writer.close();}
				if(osw!=null){osw.close();}
				if(fos!=null){fos.close();}
			}catch(IOException e){//시큐어코딩 조치 - Exception to String
	        	log.info(ExceptionUtils.getStackTrace(e));
			}
		}
		LOG("<<< table_information_"+SYSDATE+".csv 만들기 종료");
	}
	
	private void getTableInfoList(BufferedWriter writer) throws IOException {

		StringBuffer sql = new StringBuffer();
		sql.append(" 		SELECT    ");
		sql.append("         A.TABLE_NAME");
		sql.append("      ,  D.COMMENTS AS TABLE_COMMENTS");
		sql.append("      ,  A.COLUMN_NAME");
		sql.append("      , B.COMMENTS");
		sql.append("      ,  CASE WHEN A.DATA_TYPE = 'NUMBER' THEN A.DATA_TYPE||'('||A.DATA_PRECISION||','||A.DATA_SCALE||')'");
		sql.append("                  WHEN A.DATA_TYPE = 'DATE' THEN A.DATA_TYPE");
		sql.append("                  ELSE  A.DATA_TYPE||'('||A.DATA_LENGTH||')'");
		sql.append("         END TYPE");
		sql.append("      ,  CASE WHEN A.NULLABLE = 'N' THEN 'NOT NULL' ELSE 'NULL' END NULLABLE");
		sql.append("      ,  CASE WHEN C.TABLE_NAME IS NULL THEN 'No' ELSE 'Yes' END PK_YN");
		sql.append(" FROM  ALL_TAB_COLUMNS A");
		sql.append("         INNER JOIN ALL_COL_COMMENTS B");
		sql.append("                     ON A.TABLE_NAME = B.TABLE_NAME AND");
		sql.append("                          A.COLUMN_NAME = B.COLUMN_NAME");
		sql.append("         INNER JOIN ALL_TAB_COMMENTS D");
		sql.append("                     ON A.TABLE_NAME = D.TABLE_NAME");
		sql.append("         LEFT OUTER JOIN (SELECT * FROM ALL_CONS_COLUMNS WHERE CONSTRAINT_NAME LIKE '%PK%' AND OWNER = 'GGOPENGOV') C");
		sql.append("                     ON A.TABLE_NAME = C.TABLE_NAME AND");
		sql.append("                          A.COLUMN_NAME = C.COLUMN_NAME");
		sql.append(" WHERE A.OWNER = 'GGOPENGOV'");
		sql.append(" AND B.OWNER = 'GGOPENGOV'");
		sql.append(" AND D.OWNER = 'GGOPENGOV'");
		sql.append(" AND A.TABLE_NAME IN (");
		sql.append(" 							   SELECT");
		sql.append(" 									B.DS_ID");
		sql.append(" 								FROM ("); 
		sql.append(" 									SELECT"); 
		sql.append(" 										A.INF_ID AS INF_ID,");
		sql.append(" 								    	SUM(A.VIEW_CNT) AS VIEW_CNT");
		sql.append(" 									FROM TB_OPEN_INF_SRV A"); 
		sql.append(" 										JOIN TB_OPEN_INF B ON B.INF_ID = A.INF_ID"); 
		sql.append(" 										JOIN TB_OPEN_CATE C ON C.CATE_ID = B.CATE_ID ");
		sql.append(" 										JOIN TB_OPEN_CATE D ON D.CATE_ID = C.CATE_ID_TOP ");
		sql.append(" 										LEFT JOIN TB_OPEN_DS E ON E.DS_ID = B.DS_ID");
		sql.append(" 									WHERE A.SRV_YN = 'Y'");
		sql.append(" 										AND A.SRV_CD IN ('S', 'C', 'M', 'F', 'A', 'L')");
		sql.append(" 										AND B.INF_STATE = 'Y'");
		sql.append(" 										AND B.OPEN_DTTM <= SYSDATE");
		sql.append(" 										AND C.USE_YN = 'Y'");
		sql.append(" 										AND D.USE_YN = 'Y'");
		sql.append(" 										AND CASE A.SRV_CD WHEN 'F' THEN 'Y' WHEN 'L' THEN 'Y' ELSE E.USE_YN END = 'Y'");
		sql.append(" 										AND CASE A.SRV_CD WHEN 'F' THEN 'RAW' WHEN 'L' THEN 'RAW' ELSE E.DS_CD END = 'RAW'");
		sql.append(" 									GROUP BY A.INF_ID");
		sql.append(" 									) A"); 
		sql.append(" 									JOIN TB_OPEN_INF B ON B.INF_ID = A.INF_ID"); 
		sql.append(" 									JOIN TB_OPEN_CATE C ON C.CATE_ID = B.CATE_ID ");
		sql.append(" 									JOIN TB_OPEN_CATE D ON D.CATE_ID = C.CATE_ID_TOP");
		sql.append(" 									LEFT JOIN TB_COMM_CODE E ON E.GRP_CD = 'D1008' AND E.DITC_CD = B.CCL_CD");
		sql.append(" 								WHERE");
		sql.append(" 									B.DS_ID IS NOT NULL	)");
		sql.append(" ORDER  BY A.TABLE_NAME, A.COLUMN_ID");		

		DBConn dbConn = new DBConn();
		
		Connection conn = dbConn.getConnection();
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
				
			pstmt = conn.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				StringBuffer data = new StringBuffer();
				data.append(csvParser(rs.getString("TABLE_NAME")) + ",");
				data.append(csvParser(rs.getString("TABLE_COMMENTS")) + ",");
				data.append(csvParser(rs.getString("COLUMN_NAME")) + ",");
				data.append(csvParser(rs.getString("COMMENTS")) + ",");
				data.append(csvParser(rs.getString("TYPE")) + ",");
				data.append(csvParser(rs.getString("NULLABLE")) + ",");
				data.append(csvParser(rs.getString("PK_YN")) + ",");
				data.append("\r\n");
				
				writer.write(data.toString());
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
	private void csvTransfer() {
		LOG(">>> FTP 전송 시작");
		try {
			SFTPSyncManager sm = new SFTPSyncManager();
			sm.init(this.host, this.port, this.user, this.pw);
			sm.uploadSync(this.workingDir + SYSDATE + File.separator, this.toPath);
			sm.disconnect();
		} catch (SftpException e) {//시큐어코딩 조치 - Exception to String
	        	log.info(ExceptionUtils.getStackTrace(e));
		}
		LOG("<<< FTP 전송 종료");
		deleteWorkFiles();
	}
	
	/**
	 * 작업 후 파일 삭제
	 */
	private void deleteWorkFiles() {
		if(deleteWorkFile) {
			File[] workFiles = new File(this.workingDir + SYSDATE + File.separator).listFiles(new FilenameFilter() {
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
		insert.append("		  ,'DatasetCsvTransfer.java' "); 
		insert.append("		  , 'Crontab' "); 
		insert.append("		  , '' "); 
		insert.append("		  , '' "); 
		insert.append("		  , ? "); 
		insert.append("		  , 'CSV 파일 성공 갯수' "); 
		insert.append("		  , SYSDATE from dual"); 

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
		
		public int dsCount(String dsId) {
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			int count = 0;
			try {
				pstmt = conn.prepareStatement("SELECT COUNT(*) TOTAL_CNT FROM GGOPENGOV."+dsId);
				rs = pstmt.executeQuery();
				while(rs.next()) {
					count = rs.getInt("TOTAL_CNT");
				}
				
			} catch (Exception e) {//시큐어코딩 조치 - Exception to String
	        	log.info(ExceptionUtils.getStackTrace(e));
			} finally {
				try {
					if(pstmt != null) { pstmt.close(); }
					if(rs != null) { rs.close(); 	}
				} catch (SQLException e) {//시큐어코딩 조치 - Exception to String
	        	log.info(ExceptionUtils.getStackTrace(e));
				}
			}
			return count;
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
			} finally{
				try{
					if(pstmt!=null){pstmt.close();}
				}catch(SQLException e){//시큐어코딩 조치 - Exception to String
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
	
	public static void main(String[] args) throws Exception {
		if(args.length != 6) {
			throw new Exception("arguments 갯수가 맞지 않습니다.");
		}
		
		String host = args[0];
		int port = Integer.parseInt(args[1]);
		String user = args[2];
		String pw = args[3];
		String toPath = args[4];
		boolean deleteWorkFile = Boolean.parseBoolean(args[5]);
		
		new DatasetCsvTransfer(host, port, user, pw, toPath, deleteWorkFile).run();
	}
}
