package com.eloan.business.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

/**
 * ftp涓婁紶涓嬭浇宸ュ叿绫�
 * <p>Title: FtpUtil</p>
 * <p>Description: </p>
 * <p>Company: www.itcast.com</p> 
 * @author	鍏ヤ簯榫�
 * @date	2015骞�7鏈�29鏃ヤ笅鍗�8:11:51
 * @version 1.0
 */
public class FtpUtil {

	/** 
	 * Description: 鍚慒TP鏈嶅姟鍣ㄤ笂浼犳枃浠� 
	 * @param host FTP鏈嶅姟鍣╤ostname 
	 * @param port FTP鏈嶅姟鍣ㄧ鍙� 
	 * @param username FTP鐧诲綍璐﹀彿 
	 * @param password FTP鐧诲綍瀵嗙爜 
	 * @param basePath FTP鏈嶅姟鍣ㄥ熀纭�鐩綍
	 * @param filePath FTP鏈嶅姟鍣ㄦ枃浠跺瓨鏀捐矾寰勩�備緥濡傚垎鏃ユ湡瀛樻斁锛�/2015/01/01銆傛枃浠剁殑璺緞涓篵asePath+filePath
	 * @param filename 涓婁紶鍒癋TP鏈嶅姟鍣ㄤ笂鐨勬枃浠跺悕 
	 * @param input 杈撳叆娴� 
	 * @return 鎴愬姛杩斿洖true锛屽惁鍒欒繑鍥瀎alse 
	 */  
	public static boolean uploadFile(String host, int port, String username, String password, String basePath,
			String filePath, String filename, InputStream input) {
		boolean result = false;
		FTPClient ftp = new FTPClient();
		try {
			int reply;
			ftp.connect(host, port);// 杩炴帴FTP鏈嶅姟鍣�
			// 濡傛灉閲囩敤榛樿绔彛锛屽彲浠ヤ娇鐢╢tp.connect(host)鐨勬柟寮忕洿鎺ヨ繛鎺TP鏈嶅姟鍣�
			ftp.login(username, password);// 鐧诲綍
			reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
				return result;
			}
			//鍒囨崲鍒颁笂浼犵洰褰�
			if (!ftp.changeWorkingDirectory(basePath+filePath)) {
				//濡傛灉鐩綍涓嶅瓨鍦ㄥ垱寤虹洰褰�
				String[] dirs = filePath.split("/");
				String tempPath = basePath;
				for (String dir : dirs) {
					if (null == dir || "".equals(dir)) continue;
					tempPath += "/" + dir;
					if (!ftp.changeWorkingDirectory(tempPath)) {
						if (!ftp.makeDirectory(tempPath)) {
							return result;
						} else {
							ftp.changeWorkingDirectory(tempPath);
						}
					}
				}
			}
			//璁剧疆涓婁紶鏂囦欢鐨勭被鍨嬩负浜岃繘鍒剁被鍨�
			ftp.setFileType(FTP.BINARY_FILE_TYPE);
			//涓婁紶鏂囦欢
			if (!ftp.storeFile(filename, input)) {
				return result;
			}
			input.close();
			ftp.logout();
			result = true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException ioe) {
				}
			}
		}
		return result;
	}
	
	/** 
	 * Description: 浠嶧TP鏈嶅姟鍣ㄤ笅杞芥枃浠� 
	 * @param host FTP鏈嶅姟鍣╤ostname 
	 * @param port FTP鏈嶅姟鍣ㄧ鍙� 
	 * @param username FTP鐧诲綍璐﹀彿 
	 * @param password FTP鐧诲綍瀵嗙爜 
	 * @param remotePath FTP鏈嶅姟鍣ㄤ笂鐨勭浉瀵硅矾寰� 
	 * @param fileName 瑕佷笅杞界殑鏂囦欢鍚� 
	 * @param localPath 涓嬭浇鍚庝繚瀛樺埌鏈湴鐨勮矾寰� 
	 * @return 
	 */  
	public static boolean downloadFile(String host, int port, String username, String password, String remotePath,
			String fileName, String localPath) {
		boolean result = false;
		FTPClient ftp = new FTPClient();
		try {
			int reply;
			ftp.connect(host, port);
			// 濡傛灉閲囩敤榛樿绔彛锛屽彲浠ヤ娇鐢╢tp.connect(host)鐨勬柟寮忕洿鎺ヨ繛鎺TP鏈嶅姟鍣�
			ftp.login(username, password);// 鐧诲綍
			reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
				return result;
			}
			ftp.changeWorkingDirectory(remotePath);// 杞Щ鍒癋TP鏈嶅姟鍣ㄧ洰褰�
			FTPFile[] fs = ftp.listFiles();
			for (FTPFile ff : fs) {
				if (ff.getName().equals(fileName)) {
					File localFile = new File(localPath + "/" + ff.getName());

					OutputStream is = new FileOutputStream(localFile);
					ftp.retrieveFile(ff.getName(), is);
					is.close();
				}
			}

			ftp.logout();
			result = true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException ioe) {
				}
			}
		}
		return result;
	}
	
	public static void main(String[] args) {
		try {  
	        FileInputStream in=new FileInputStream(new File("D:\\temp\\image\\gaigeming.jpg"));  
	        boolean flag = uploadFile("192.168.25.133", 21, "ftpuser", "ftpuser", "/home/ftpuser/www/images","/2015/01/21", "gaigeming.jpg", in);  
	        System.out.println(flag);  
	    } catch (FileNotFoundException e) {  
	        e.printStackTrace();  
	    }  
	}
}
