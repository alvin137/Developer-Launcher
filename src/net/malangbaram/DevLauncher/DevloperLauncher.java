package net.malangbaram.DevLauncher;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import jdk.nashorn.internal.runtime.linker.JavaAdapterFactory;
import net.malangbaram.DevLauncher.Util.CompressionUtil;
import net.malangbaram.DevLauncher.Util.Util;
import net.malangbaram.DevLauncher.Util.VersionManagementUtil;
import net.malangbaram.DevLauncher.Util.javafx.AlterUtil;

public class DevloperLauncher extends Application {

	public static String myVersion;
	public static String lastVersion;
	public static String lastUrl;
	
	public static boolean plzUpModpack;
	public static boolean plzUpLauncher = VersionManagementUtil.booleanLauncherVersion("https://raw.githubusercontent.com/alvin137/Developer-Launcher/master/version/launcher.txt", Lang.VERSION);

	public static void main(String[] args) throws Exception {
		myVersion = VersionManagementUtil.checkMyVersion(System.getenv("APPDATA") + "\\.minecraft", "\\mVersion.txt");
		lastVersion = VersionManagementUtil.checkLastVersion("https://raw.githubusercontent.com/alvin137/Developer-Launcher/master/version/mod.txt");
		lastUrl = Util.getnewUrl("https://raw.githubusercontent.com/alvin137/Developer-Launcher/master/version/Url.txt");
		Application.launch(args);
	}

	@Override
	public void start(Stage frame) throws Exception {

		frame.setTitle(Lang.TITLE);
		Group gp = new Group();
		Scene sc = new Scene(gp, 149, 120);

		Label loVer = new Label("현재버전:");
		final Label viewLoVer = new Label(myVersion);
		loVer.setLayoutX(5);
		loVer.setLayoutY(10);
		viewLoVer.setLayoutX(72);
		viewLoVer.setLayoutY(10);

		Label laVer = new Label("최신버전:");
		Label viewLaVer = new Label(lastVersion);
		laVer.setLayoutX(5);
		laVer.setLayoutY(25);
		viewLaVer.setLayoutX(72);
		viewLaVer.setLayoutY(25);

		final Button btnDownModpack = new Button("모드팩 다운로드");
		final Button btnLauch = new Button("런처실행");
		btnDownModpack.setLayoutX(5);
		btnDownModpack.setLayoutY(45);
		btnLauch.setLayoutX(5);
		btnLauch.setLayoutY(75);

		btnDownModpack.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				AlterUtil.showInformationAlter(Lang.TITLE, "모드 설치를 시작합니다. 완료 창이 뜰 때까지 기다려 주세요.");
				try {
					btnDownModpack.setDisable(true);
					btnLauch.setDisable(true);

					File minecraftP = new File(System.getenv("APPDATA") + "\\.minecraft");

					Util.delDir(minecraftP);
					Util.webDown(minecraftP, lastUrl, "modpack.zip");
					File zip = new File(minecraftP + "modpack.zip");

					CompressionUtil cu = new CompressionUtil();
					cu.unzip(zip, new File(System.getenv("APPDATA") + "\\.minecraft"));

					FileWriter mVersionFW = new FileWriter(minecraftP + "\\mVersion.txt");
					mVersionFW.write(lastVersion);
					BufferedWriter bw = new BufferedWriter(mVersionFW);
					bw.close();
					mVersionFW.close();
					myVersion = lastVersion;
					viewLoVer.setText(myVersion);

					zip.delete();

					AlterUtil.showInformationAlter(Lang.TITLE, "다운로드 및 설치 완료");
					btnDownModpack.setDisable(false);
					btnLauch.setDisable(false);
				} catch (Exception e) {
					AlterUtil.showErrorAlter(Lang.TITLE, "다운로드 및 설치 실패, 잠시후 다시 시도해주세요");
					btnDownModpack.setDisable(false);
					btnLauch.setDisable(false);
				}
			}
		});

		btnLauch.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				File launcher = new File(System.getenv("APPDATA") + "\\.minecraft\\launcher.jar");

				if (myVersion.equals("미설치")) {
					AlterUtil.showErrorAlter(Lang.TITLE, "현재 로컬에 모드팩이 설치되어 있지 않습니다. 설치 후 다시 시도 해주세요.");
				} else if (myVersion.equals("확인실패")) {
					AlterUtil.showErrorAlter(Lang.TITLE, "모드팩 버전확인이 불가능합니다. 서버접속이 불가능할 수도 있습니다");
					try {
						Util.PGAction(launcher);
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					try {	
						Util.PGAction(launcher);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});

		if (plzUpLauncher) {
			AlterUtil.showErrorAlter(Lang.TITLE, "런처 버전이 낮습니다 최신버전으로 업데이트 해주세요");
			Platform.exit();
		} else {
			if (lastVersion.equals(myVersion)) {
				plzUpModpack = false;
			} else {
				plzUpModpack = true;
			}
		}

		if(lastVersion.equals("unknown")) {
			btnDownModpack.setDisable(true);
		}
		
		if (plzUpModpack) {
			AlterUtil.showInformationAlter(Lang.TITLE, "모드팩이 새로 업데이트되었습니다! 모드팩 다운로드 버튼을 눌러서 최신버전으로 설치해주세요");
		}

		gp.getChildren().addAll(loVer, viewLoVer, laVer, viewLaVer);
		gp.getChildren().addAll(btnDownModpack, btnLauch);
		frame.setScene(sc);
		frame.show();
	}
}
