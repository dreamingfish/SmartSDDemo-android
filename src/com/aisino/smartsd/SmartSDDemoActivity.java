package com.aisino.smartsd;

import java.util.Calendar;
import java.util.Random;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class SmartSDDemoActivity extends Activity {
	SmartSDSample SSDS = new SmartSDSample();

	Button initialization, RunApp, Reset, CASE1, CASE2, CASE3, CASE4per,
			CASE4En;

	TextView showInfo;

	int RevLength[] = new int[1];
	char RevBuffer[] = new char[SmartSDSample.CASE4_FIX_DATA_LEN];

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		initialization = (Button) findViewById(R.id.Initialization);
		RunApp = (Button) findViewById(R.id.RunAPP);
		CASE1 = (Button) findViewById(R.id.Case1);
		Reset = (Button) findViewById(R.id.reset);
		CASE2 = (Button) findViewById(R.id.Case2);
		CASE3 = (Button) findViewById(R.id.Case3);
		CASE4En = (Button) findViewById(R.id.Case4UnEn);
		CASE4per = (Button) findViewById(R.id.Case4En);


		showInfo = (TextView) findViewById(R.id.Text);

		// Do Initialization
		initialization.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				String path = Environment.getExternalStorageDirectory()
						.toString();
				int uRet = SSDS.init(path);
				if (uRet == SmartSDDev.OK) {
					showInfo.setText("init succeed..\n");
					showInfo.append(SSDS.getVersion());
				} else {
					showInfo.setText("init failed..\n");
				}
			}
		});

		// run APP
		RunApp.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				int uRet = SSDS.runapp();
				if (uRet == SmartSDDev.OK) // succeed
				{
					showInfo.setText("runapp succeed..\nThe SmartSD is in COS mode now.");
				} else {
					showInfo.setText("runapp failed..\nThe SmartSD is already in COS mode or Run APP failed, reinsert the TF card, and try again !!!");
				}
			}
		});

		// Reset
		Reset.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				int uRet = SSDS.reset();

				if (uRet == SmartSDDev.STATE_ERROR) {
					showInfo.setText("reset failed..\nSmartSD is not in the right condition, Reset failed!!!");
				} else if (uRet == SmartSDDev.STATE_BOOT) {
					showInfo.setText("reset succeed..\nSmartSD is now in Boot Mode, please run APP first.");
				} else if (uRet == SmartSDDev.STATE_APP) {
					showInfo.setText("reset succeed..\nSmartSD is now in COS Mode, you can continue.");
				} else {
					showInfo.setText("reset failed..\nstate code:" + uRet);
				}
			}
		});

		// case 1
		CASE1.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				int uRet = SSDS.case1_trans(SmartSDSample.SPICMD1,
						SmartSDSample.SPICMD1.length, RevBuffer,
						RevLength, 10000);

				if (uRet == SmartSDDev.OK) {
					showInfo.setText("...........case 1............:\n");
					showInfo.append("The Received Data:\n");
					showInfo.append("SW12:"
							+ Utils.To_Hex(RevBuffer, 0, RevLength[0]) + "\n");
				} else {
					showInfo.setText("case 1---> run trans error with code:"
							+ uRet);
				}
			}
		});

		// case 2
		CASE2.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				int uRet = SSDS.case2_trans(SmartSDSample.SPICMD2,
						SmartSDSample.SPICMD2.length, RevBuffer,
						RevLength, 10000);

				if (uRet == SmartSDDev.OK) {
					showInfo.setText("...........case 2............:\n");
					showInfo.append("The Received Data:");

					showInfo.append(""
							+ Utils.To_Hex(RevBuffer, 0, RevLength[0] - 2)
							+ "\n");
					showInfo.append("SW12:"
							+ Utils.To_Hex(RevBuffer, RevLength[0] - 2,
									RevLength[0]) + "\n");

					// showInfo.append(Utils.To_Hex(RevBuffer, 0,
					// RevLength[0]));
				} else {
					showInfo.setText("case 2---> run trans error with code:"
							+ uRet);
				}
			}
		});

		// case 3
		CASE3.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				int uRet = SSDS.case3_trans(SmartSDSample.SPICMD3,
						SmartSDSample.SPICMD3.length, RevBuffer,
						RevLength, 10000);

				if (uRet == SmartSDDev.OK) {
					showInfo.setText("...........case 3............:\n");
					showInfo.append("The Received Data:\n");
					showInfo.append("SW12:"
							+ Utils.To_Hex(RevBuffer, 0, RevLength[0]) + "\n");
				} else {
					showInfo.setText("case 3---> run trans error with code:"
							+ uRet);
				}
			}
		});

		// case 4 Encryption
		CASE4En.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				char resp1[] = new char[2];
				char resp2[] = new char[2];
				char pOut[] = new char[SmartSDSample.CASE4_FIX_DATA_LEN];
				int uRet = SSDS.case4_trans(SmartSDSample.SPI_EN_512,
						SmartSDSample.SPI_EN_512.length, resp1,
						SmartSDSample.SPI_512, pOut, 1, 1,
						SmartSDSample.SPI_UN_512,
						SmartSDSample.SPI_UN_512.length, resp2, 10000);
				if (uRet == SmartSDDev.OK) {
					showInfo.setText("Case 4  Encryption..\n");
					showInfo.append("succ!\n");
					showInfo.append("Resp2:" + Utils.To_Hex(resp2, 0, 2) + "\n");
					boolean flag = true;
					for (int i = 0; i < 512; i++) {
						if (pOut[i] != SmartSDSample.SPI_result_512[i]) {
							flag = false;
							break;
						}
					}
					if (!flag) {
						showInfo.append("but result is wrong..\n");
					} else {
						showInfo.append("and result is right..\n");
					}
				} else {
					showInfo.setText("Case 4 failed with code:" + uRet);
				}
			}

		});

		// case 4 performance
		CASE4per.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				int testRound = 32;
				char resp1[] = new char[2];
				char resp2[] = new char[2];
				char pIn[] = new char[SmartSDSample.CASE4_FIX_DATA_LEN
						];
				char pOut[] = new char[SmartSDSample.CASE4_FIX_DATA_LEN
						];
				Random ran = new Random(Calendar.getInstance()
						.getTimeInMillis());
				for (int i = 0; i < pIn.length; i++) {
					pIn[i] = (char) ((ran.nextInt() % 256) & 0xff);
				}
				long timeStart = Calendar.getInstance().getTimeInMillis();
				int uRet = SSDS.case4_trans(SmartSDSample.SPI_CMD_case4,
						SmartSDSample.SPI_CMD_case4.length, resp1, pIn,
						pOut, SmartSDSample.CASE4_FIX_DATA_LEN
								/ SmartSDSample.CASE4_FIX_DATA_LEN_BASE,
						testRound, SmartSDSample.SPI_END_case4,
						SmartSDSample.SPI_END_case4.length, resp2, 10000);
				long timeEnd = Calendar.getInstance().getTimeInMillis();
				if (uRet == SmartSDDev.OK) {
					showInfo.setText("Case 4  performance..\n");
					showInfo.append("succ!\n");
					showInfo.append("SW12:" + Utils.To_Hex(resp2, 0, 2) + "\n");
					boolean flag = true;
					for (int i = 0; i < SmartSDSample.CASE4_FIX_DATA_LEN; i++) {
						if (pOut[i] != pIn[i]) {
							flag = false;
							break;
						}
					}
					if (!flag) {
						showInfo.append("but result is wrong..\n");
					} else {
						showInfo.append("and result is right..\n");
						showInfo.append("Total bytes tested:" + pIn.length * testRound
								+ "\n");
						showInfo.append("Total time used:"
								+ (timeEnd - timeStart) + "ms\n");
						showInfo.append("Case 4 performance:"
								+ String.format(
										"%.3f",
										(pIn.length * testRound * 8.0  * 2 / (timeEnd - timeStart) / 1000.0))
								+ "Mbps\n");
					}
				} else {
					showInfo.setText("Case 4 failed with code:" + uRet);
				}
			}

		});
	}
	
	@Override
	protected void onPause() {
		SSDS.close();
		super.onPause();
	}
	
	@Override
	protected void onResume() {
		showInfo.setText(SSDS.getDllVersion());
		showInfo.append("\nSmartSD is not ready now.\nPlease start from 'Init' button.\n");
		this.getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		SSDS.close();
		super.onDestroy();
	}
}