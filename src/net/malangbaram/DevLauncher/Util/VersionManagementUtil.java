package net.malangbaram.DevLauncher.Util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import net.malangbaram.DevLauncher.Lang;

public class VersionManagementUtil {

	public static String checkMyVersion(String file, String version) throws Exception {

		File versionText = new File(file);

		try {
			FileReader mVersionFR = new FileReader(versionText + version);
			BufferedReader mVersionBR = new BufferedReader(mVersionFR);
			String buf;
			buf = mVersionBR.readLine();

			if (buf.equals("unknown")) {
				return "미설치";
			} else {
				return buf;
			}

		} catch (FileNotFoundException e) {
			versionText.mkdirs();
			FileWriter mVersionFW = new FileWriter(versionText + version);
			mVersionFW.write("unknown");
			BufferedWriter bw = new BufferedWriter(mVersionFW);
			bw.close();
			mVersionFW.close();
			return "미설치";
		}

	}

	public static String checkLastVersion(String url) {

		try {
			URL lUrl = new URL(url);
			HttpURLConnection connect;
			BufferedReader rd;
			String result;

			connect = (HttpURLConnection) lUrl.openConnection();
			connect.setRequestMethod("GET");
			rd = new BufferedReader(new InputStreamReader(connect.getInputStream()));
			result = rd.readLine();
			rd.close();

			return result;

		} catch (Exception e) {
		}
		return "unknown";
	}

	public static boolean booleanLauncherVersion(String url,int version) {

		try {
			URL lUrl = new URL(url);
			HttpURLConnection connect;
			BufferedReader rd;
			String result;

			connect = (HttpURLConnection) lUrl.openConnection();
			connect.setRequestMethod("GET");
			rd = new BufferedReader(new InputStreamReader(connect.getInputStream()));
			result = rd.readLine();
			rd.close();

			if (Integer.parseInt(result) > version) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {}
		return false;
	}

}
