package tw.com.geminihsu.app01.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import android.os.Environment;
import android.util.Log;

public class FileUtil {
	private static final String TAG = FileUtil.class.getSimpleName();
	
	public static String mkDirs(String path) {
		String[] pathAry = path.split("[/]|\\\\");

		StringBuffer list = new StringBuffer();
		for (int i = 0; i < pathAry.length; i++) {
			if (!pathAry[i].equals("")) {
				list.append(pathAry[i] + "/");
				File dir = new File(list.toString());
				// System.out.println(dir.getName());
				if (!dir.exists())
				{
					dir.mkdir();

				}
			}
		}
		return list.toString();
	}

	public static void delFile(String filePathAndName) {
		try {
			String filePath = filePathAndName;
			filePath = filePath.toString();
			File myDelFile = new File(filePath);
			myDelFile.delete();

		} catch (Exception e) {
			Log.e(TAG,"刪除檔操作出錯");
			e.printStackTrace();

		}

	}


	public static void delFolder(String folderPath) {
		try {
			delAllFile(folderPath); // 刪除完裡面所有內容
			String filePath = folderPath;
			filePath = filePath.toString();
			File myFilePath = new File(filePath);
			//myFilePath.delete(); // 刪除空資料夾

		} catch (Exception e) {
			Log.e(TAG,"刪除資料夾操作出錯");
			e.printStackTrace();

		}

	}

	/**
	 * 刪除資料夾裡面的所有檔
	 * 
	 * @param path
	 *            String資料夾路徑 如 c:/fqf
	 */
	public static void delAllFile(String path) {
		File file = new File(path);
		if (!file.exists()) {
			return;
		}
		if (!file.isDirectory()) {
			return;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);// 先刪除資料夾裡面的檔
				delFolder(path + "/" + tempList[i]);// 再刪除空資料夾
			}
		}
	}

	/**
	 * 複製單個檔
	 * 
	 * @param oldPath
	 *            String原檔路徑 如：c:/fqf.txt
	 * @param newPath
	 *            String複製後路徑 如：f:/fqf.txt
	 * @return boolean
	 */
	public static void copyFile(String oldPath, String newPath) {
		try {
			int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(oldPath);
			if (oldfile.exists()) { // 檔存在時
				InputStream inStream = new FileInputStream(oldPath);// 讀入原檔
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1444];
				int length;
				while ((byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread; // 位元組數 檔案大小
					
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
			}
		} catch (Exception e) {
			Log.e(TAG,"複製單個檔操作出錯");
			e.printStackTrace();

		}

	}

	/**
	 * 複製整個資料夾內容
	 * 
	 * @param oldPath
	 *            String原檔路徑 如：c:/fqf
	 * @param newPath
	 *            String複製後路徑 如：f:/fqf/ff
	 * @return boolean
	 */
	public static void copyFolder(String oldPath, String newPath) {

		try {
			(new File(newPath)).mkdirs(); // 如果資料夾不存在則建立新資料夾
			File a = new File(oldPath);
			String[] file = a.list();
			File temp = null;
			for (int i = 0; i < file.length; i++) {
				if (oldPath.endsWith(File.separator)) {
					temp = new File(oldPath + file[i]);
				} else {
					temp = new File(oldPath + File.separator + file[i]);
				}

				if (temp.isFile()) {
					FileInputStream input = new FileInputStream(temp);
					FileOutputStream output = new FileOutputStream(newPath
							+ "/" + (temp.getName()).toString());
					byte[] b = new byte[1024 * 5];
					int len;
					while ((len = input.read(b)) != -1) {
						output.write(b, 0, len);
					}
					output.flush();
					output.close();
					input.close();
				}
				if (temp.isDirectory()) {// 如果是子資料夾
					copyFolder(oldPath + "/" + file[i], newPath + "/" + file[i]);
				}
			}
		} catch (Exception e) {
			Log.e(TAG,"複製整個資料夾內容操作出錯");
			e.printStackTrace();

		}

	}

	/**
	 * 移動檔到指定目錄
	 * 
	 * @param oldPath
	 *            String如：c:/fqf.txt
	 * @param newPath
	 *            String如：d:/fqf.txt
	 */
	public static void moveFile(String oldPath, String newPath) {
		copyFile(oldPath, newPath);
		delFile(oldPath);

	}

	/**
	 * 移動檔到指定目錄
	 * 
	 * @param oldPath
	 *            String如：c:/fqf.txt
	 * @param newPath
	 *            String如：d:/fqf.txt
	 */
	public static void moveFolder(String oldPath, String newPath) {
		delFolder(newPath);
		copyFolder(oldPath, newPath);
		delFolder(oldPath);

	}
	/* 檢查是否有SD 卡, 並且建立相關資料夾 */
	public static boolean checkSdCard(String filePath) {
		boolean result=false;
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED); // 判斷sd記憶卡是否存在
		if (sdCardExist) {
			//FileUtil.mkDirs(Environment.getExternalStorageDirectory()+Constants.SDACRD_DIR_APP_ROOT);
			FileUtil.mkDirs(filePath);
			result=true;
		} else {
			result=false;

		}
		return result;
	}
	
	
}
