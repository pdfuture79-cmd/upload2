package egovframework.common.file.service;

import java.io.File;

import org.springframework.web.multipart.MultipartFile;


/**
 * 파일로 사용하는 데이터 처리 모델
 * @author wiseopen
 * @since 2014.04.17
 * @version 1.0
 * @see
 *
 */
public class FileVo {
	
	
	MultipartFile file[];
	MultipartFile file1[];
	public int getStartExc() {
		return startExc;
	}
	public void setStartExc(int startExc) {
		this.startExc = startExc;
	}


	MultipartFile file2[];
	
	MultipartFile file3[];
	MultipartFile file4[];
	MultipartFile saveFile[];
	MultipartFile excelFileNm[];
	
	String[] saveFileNm;
	String[] fileStatus;
	String[] arrFileSeq;
	
	int weight;
	int height;
	int startExc;
	
	MultipartFile singleFile;
	
	
	public MultipartFile getSingleFile() {
		return singleFile;
	}
	public void setSingleFile(MultipartFile singleFile) {
		this.singleFile = singleFile;
	}
	public int getWeight() {
		return weight;
	}
	public void setWeight(int weight) {
		this.weight = weight;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}


	File exFile;
	
	public File getExFile() {
		return exFile;
	}
	public void setExFile(File exFile) {
		this.exFile = exFile;
	}
	
	
	String gubun;
	
	public String[] getArrFileSeq() {
		return arrFileSeq;
	}
	public void setArrFileSeq(String[] arrFileSeq) {
		this.arrFileSeq = arrFileSeq;
	}
	public MultipartFile[] getFile() {
		return file;
	}
	public void setFile(MultipartFile[] file) {
		this.file = file;
	}
	public String[] getSaveFileNm() {
		return saveFileNm;
	}
	public void setSaveFileNm(String[] saveFileNm) {
		this.saveFileNm = saveFileNm;
	}
	public String[] getFileStatus() {
		return fileStatus;
	}
	public void setFileStatus(String[] fileStatus) {
		this.fileStatus = fileStatus;
	}
	public MultipartFile[] getSaveFile() {
		return saveFile;
	}
	public void setSaveFile(MultipartFile[] saveFile) {
		this.saveFile = saveFile;
	}
	public MultipartFile[] getFile1() {
		return file1;
	}
	public void setFile1(MultipartFile[] file1) {
		this.file1 = file1;
	}
	public MultipartFile[] getFile2() {
		return file2;
	}
	public void setFile2(MultipartFile[] file2) {
		this.file2 = file2;
	}
	public MultipartFile[] getFile3() {
		return file3;
	}
	public void setFile3(MultipartFile[] file3) {
		this.file3 = file3;
	}
	public MultipartFile[] getFile4() {
		return file4;
	}
	public void setFile4(MultipartFile[] file4) {
		this.file4 = file4;
	}
	
	public MultipartFile[] getExcelFileNm() {
		return excelFileNm;
	}
	public void setExcelFileNm(MultipartFile[] excelFileNm) {
		this.excelFileNm = excelFileNm;
	}
	public String getGubun() {
		return gubun;
	}
	public void setGubun(String gubun) {
		this.gubun = gubun;
	}
}
