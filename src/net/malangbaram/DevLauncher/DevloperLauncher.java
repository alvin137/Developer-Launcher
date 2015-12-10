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

		Label loVer = new Label("�������:");
		final Label viewLoVer = new Label(myVersion);
		loVer.setLayoutX(5);
		loVer.setLayoutY(10);
		viewLoVer.setLayoutX(72);
		viewLoVer.setLayoutY(10);

		Label laVer = new Label("�ֽŹ���:");
		Label viewLaVer = new Label(lastVersion);
		laVer.setLayoutX(5);
		laVer.setLayoutY(25);
		viewLaVer.setLayoutX(72);
		viewLaVer.setLayoutY(25);

		final Button btnDownModpack = new Button("����� �ٿ�ε�");
		final Button btnLauch = new Button("��ó����");
		btnDownModpack.setLayoutX(5);
		btnDownModpack.setLayoutY(45);
		btnLauch.setLayoutX(5);
		btnLauch.setLayoutY(75);

		btnDownModpack.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				AlterUtil.showInformationAlter(Lang.TITLE, "��� ��ġ�� �����մϴ�. �Ϸ� â�� �� ������ ��ٷ� �ּ���.");
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

					AlterUtil.showInformationAlter(Lang.TITLE, "�ٿ�ε� �� ��ġ �Ϸ�");
					btnDownModpack.setDisable(false);
					btnLauch.setDisable(false);
				} catch (Exception e) {
					AlterUtil.showErrorAlter(Lang.TITLE, "�ٿ�ε� �� ��ġ ����, ����� �ٽ� �õ����ּ���");
					btnDownModpack.setDisable(false);
					btnLauch.setDisable(false);
				}
			}
		});

		btnLauch.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				File launcher = new File(System.getenv("APPDATA") + "\\.minecraft\\launcher.jar");

				if (myVersion.equals("�̼�ġ")) {
					AlterUtil.showErrorAlter(Lang.TITLE, "���� ���ÿ� ������� ��ġ�Ǿ� ���� �ʽ��ϴ�. ��ġ �� �ٽ� �õ� ���ּ���.");
				} else if (myVersion.equals("Ȯ�ν���")) {
					AlterUtil.showErrorAlter(Lang.TITLE, "����� ����Ȯ���� �Ұ����մϴ�. ���������� �Ұ����� ���� �ֽ��ϴ�");
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
			AlterUtil.showErrorAlter(Lang.TITLE, "��ó ������ �����ϴ� �ֽŹ������� ������Ʈ ���ּ���");
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
			AlterUtil.showInformationAlter(Lang.TITLE, "������� ���� ������Ʈ�Ǿ����ϴ�! ����� �ٿ�ε� ��ư�� ������ �ֽŹ������� ��ġ���ּ���");
		}

		gp.getChildren().addAll(loVer, viewLoVer, laVer, viewLaVer);
		gp.getChildren().addAll(btnDownModpack, btnLauch);
		frame.setScene(sc);
		frame.show();
	}
}
