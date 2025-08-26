package egovframework.common.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Stack;

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants; 



/**
 * Zip 파일 압축 & 해제 유틸
 * @author hsJang
 *
 */
public class UtilZip {

	private static boolean debug = false;
	protected static final Log logger = LogFactory.getLog(UtilZip.class);
	public void unzip(File zippedFile) throws IOException {
		unzip(zippedFile, Charset.defaultCharset().name());
	}
	
	public void unzip(File zippedFile, String charsetName ) throws IOException {
		unzip(zippedFile, zippedFile.getParentFile(), charsetName);
	}
	public void unzip(File zippedFile, File destDir) throws IOException {
		FileInputStream fis = null;
		try{
			fis = new FileInputStream(zippedFile);
			unzip( fis , destDir, Charset.defaultCharset().name());
		}catch(FileNotFoundException e){
			debug (e.getMessage());
		}catch(IOException e){
			debug (e.getMessage());
		}finally{
			if(fis!=null){fis.close();}
		}
	}
	
	public void unzip(File zippedFile, File destDir, String charsetName) throws IOException {
		FileInputStream fis = null;
		try{
			fis = new FileInputStream(zippedFile);
			unzip( fis , destDir, charsetName);
		}catch(FileNotFoundException e){
			debug (e.getMessage());
		}catch(IOException e){
			debug (e.getMessage());
		}finally{
			if(fis!=null){fis.close();}
		}
	}
	public void unzip(InputStream is, File destDir) throws IOException{
		unzip(is, destDir, Charset.defaultCharset().name());
	}
	public void unzip( InputStream is, File destDir, String charsetName) throws IOException {
		ZipArchiveInputStream zis = null;
		ZipArchiveEntry entry ;
		String name ;
		File target ;
		int nWritten = 0;
		BufferedOutputStream bos =null;
		FileOutputStream fos = null;
		byte [] buf = new byte[1024 * 8];

		if(!destDir.exists()) {
			destDir.mkdirs();
		}
		
		try{
			zis = new ZipArchiveInputStream(is, charsetName, false);
			while ( (entry = zis.getNextZipEntry()) != null ){
				name = entry.getName();
				target = new File (destDir, name);
				if ( entry.isDirectory() ){
					target.mkdirs(); /*  does it always work? */
					debug ("dir  : " + name);
				} else {
					target.createNewFile();
					try{
						fos = new FileOutputStream(target);
						bos = new BufferedOutputStream(fos);
						while ((nWritten = zis.read(buf)) >= 0 ){
							bos.write(buf, 0, nWritten);
						}
					}catch(FileNotFoundException e){
						debug ("file : " + name + " : " + e.getMessage());
					}catch(IOException e){
						debug ("file : " + name + " : " + e.getMessage());
					}finally{
						if(bos!=null){bos.close();}
						if(fos!=null){fos.close();}
					}
				}
			}
		}catch(FileNotFoundException e){
			debug ( e.getMessage());
		}catch(IOException e){
			debug ( e.getMessage());
		}finally{
			if(zis!=null){zis.close();}
		}
	}
	
	/**
	 * compresses the given file(or dir) and creates new file under the same directory.
	 * @param src file or directory
	 * @throws IOException
	 */
	public void zip(File src) throws IOException{
		zip(src, Charset.defaultCharset().name(), true);
	}
	/**
	 * zips the given file(or dir) and create 
	 * @param src file or directory to compress
	 * @param includeSrc if true and src is directory, then src is not included in the compression. if false, src is included.
	 * @throws IOException
	 */
	public void zip(File src, boolean includeSrc) throws IOException{
		zip(src, Charset.defaultCharset().name(), includeSrc);
	}
	/**
	 * compresses the given src file (or directory) with the given encoding
	 * @param src
	 * @param charSetName
	 * @param includeSrc
	 * @throws IOException
	 */
	public void zip(File src, String charSetName, boolean includeSrc) throws IOException {
		zip( src, src.getParentFile(), charSetName, includeSrc);
	}
	/**
	 * compresses the given src file(or directory) and writes to the given output stream.
	 * @param src
	 * @param os
	 * @throws IOException
	 */
	public void zip(File src, OutputStream os) throws IOException {
		zip(src, os, Charset.defaultCharset().name(), true);
	}
	/**
	 * compresses the given src file(or directory) and create the compressed file under the given destDir. 
	 * @param src
	 * @param destDir
	 * @param charSetName
	 * @param includeSrc
	 * @throws IOException
	 */
	public void zip(File src, File destDir, String charSetName, boolean includeSrc) throws IOException {
		String fileName = src.getName();
		if ( !src.isDirectory() ){
			int pos = fileName.lastIndexOf(".");
			if ( pos >  0){
				fileName = fileName.substring(0, pos);
			}
		}
		fileName += ".zip";

		File zippedFile = new File ( destDir, fileName);
		if ( !zippedFile.exists() ) zippedFile.createNewFile();
		FileOutputStream fos = null; //시큐어코딩 조치
		try{
			fos = new FileOutputStream(zippedFile);
			zip(src,fos , charSetName, includeSrc);
		}catch(IOException e){
			logger.debug("오류메시지 :  " + e.getMessage()); 
		}finally{
			if(fos!=null){fos.close();}
		}
	}
	public String zipByEtl(String srcDir, String destDir) throws IOException {
		File _srcDir = new File(srcDir);
		String path = _srcDir.getPath();
		StringBuffer zipFilePath = new StringBuffer(new File(destDir).getPath());
		if(path.indexOf("/") != -1) {
			zipFilePath.append("/"+path.substring(path.indexOf("/")).replaceAll("/", "_"));
		} else if(path.indexOf("\\") != -1) {
			zipFilePath.append("\\" + path.substring(path.indexOf("\\")).replaceAll("\\\\", "_"));
		}
		zipFilePath.append(".zip");
		
		FileOutputStream fos = null;
		try{
			fos = new FileOutputStream(zipFilePath.toString());
			zip(_srcDir,fos , Charset.defaultCharset().name(), false);
		}catch(IOException e){
			logger.debug("오류메시지 :  " + e.getMessage()); 
		}finally{
			if(fos!=null){fos.close();}
		}
		return zipFilePath.toString();
	}
	
	public void zip(File src, OutputStream os, String charsetName, boolean includeSrc) throws IOException {
		ZipArchiveOutputStream zos = new ZipArchiveOutputStream(os);
		zos.setEncoding(charsetName);
		FileInputStream fis ;
		
		int length ;
		ZipArchiveEntry ze ;
		byte [] buf = new byte[8 * 1024];
		String name ;
		
		Stack<File> stack = new Stack<File>();
		File root ;
		if ( src.isDirectory() ) {
			if( includeSrc ){
				stack.push(src);
				root = src.getParentFile();
			}
			else {
				File [] fs = src.listFiles();
				for (int i = 0; i < fs.length; i++) {
					stack.push(fs[i]);
				}
				root = src;
			}
		} else {
			stack.push(src);
			root = src.getParentFile();
		}
		
		try{
			while ( !stack.isEmpty() ){
				File f = stack.pop();
				name = toPath(root, f);
				if ( f.isDirectory()){
					debug ("dir  : " + name);
					File [] fs = f.listFiles();
					if(fs != null) {
						for (int i = 0; i < fs.length; i++) {
							if ( fs[i].isDirectory() ) stack.push(fs[i]);
							else stack.add(0, fs[i]);
						}
					}
					ze = new ZipArchiveEntry(name);
					zos.putArchiveEntry(ze);
					zos.closeArchiveEntry();
				} else {
					debug("file : " + name);
					ze = new ZipArchiveEntry(name);
					zos.putArchiveEntry(ze);
					fis = new FileInputStream(f);
					try{
						while ( (length = fis.read(buf, 0, buf.length)) >= 0 ){
							zos.write(buf, 0, length);
						}
					}catch(IOException e){
						logger.debug("오류메시지 :  " + e.getMessage()); 
					}finally{
						if(fis!=null){fis.close();}
					}
					zos.closeArchiveEntry();
				}
			}
		}catch(IOException e){
			logger.debug("오류메시지 :  " + e.getMessage()); 
		}finally{
			if(zos!=null){zos.close();}
		}
	}
	private String toPath(File root, File dir){
		String path = dir.getAbsolutePath();
		path = path.substring(root.getAbsolutePath().length()).replace(File.separatorChar, '/');
		if ( path.startsWith("/")) path = path.substring(1);
		if ( dir.isDirectory() && !path.endsWith("/")) path += "/" ;
		return path ;
	}
	private static void debug(String msg){
		if( debug ) System.out.println(msg);
	}
    /*
	public static void main(String[] args) throws IOException {
		UtilZip uz = new UtilZip();
//		uz.zipByEtl("C:\\DATA\\upload\\mainmng\\aaa", "C:\\DATA\\receive");
		uz.unzip(new File("C:\\ETL_data\\send\\_DATA_upload_mainmng.zip"), new File("/DATA/upload/mainmng"));
	}*/
}
