package Hill.com.H0102;

import Hill.com.H0102.ShackDetector.OnShakeListener;
import android.app.Activity;
import android.app.Dialog;
import android.app.Service;
import android.content.Context;
import android.hardware.Sensor;


import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;




public class H0102 extends Activity {
	//<!--------丟骰子使用變數---------------------->
	private TextView h0102_T001;//顯示丟骰子的結果
	boolean[] check={false,false,false,false};//紀錄有幾棵骰子是相同的陣列 相同=true
	boolean[] checkLarge=new boolean[check.length];
    int[] Arr18la=new int[check.length];//記錄骰子隨機搖出的點數
	private static int cunt18la=0,num18la=0,larg18la=0; //cunt18la=紀錄有幾個骰子一樣   num18la=記錄不同點數的骰子的點數和
	private Dialog h0102a;//顯示骰子搖出點數的頁面
	private Vibrator myVibrator;//搖一次就震動一次
//<--------------------------------------------->

	// <!--------------取得系統服務變數-------------------->
	private SensorManager mSensorManager; // 變數為系統服務管理物件
	private Sensor mAccelerometer; // 宣告變數為加速度計物件
	private ShackDetector mShakeDetector=new ShackDetector(); // 搖動探知器
	// <------------------------------------------>

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.h0102);
		setupviewcomponent();
	}

	private void setupviewcomponent() {
		// TODO Auto-generated method stub
		h0102_T001=(TextView)findViewById(R.id.h0102_T001);
		myVibrator=(Vibrator)getSystemService(Service.VIBRATOR_SERVICE);
		mSensorManager=(SensorManager)getSystemService(Context.SENSOR_SERVICE);
		mAccelerometer=mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		h0102a=new Dialog(H0102.this);
		
		mShakeDetector.setOnShakeListener(new OnShakeListener() {
			
			@Override
			public void onShake(int count) {
				
				myVibrator.vibrate(500);
				Toast.makeText(getApplicationContext(),"搖了"+count+"次",Toast.LENGTH_SHORT).show();
				if(count%3==0){
					ShackDetector.mShakeCount=0;//搖動計數器歸0
					h0102a.cancel();
					domywork();
				}
			}
		});
	}
	@Override
	public void onResume() {
		super.onResume(); //在resume週期的時候開啟持續監聽
		// Add the following line to register the Session Manager Listener onResume
		mSensorManager.registerListener(mShakeDetector, mAccelerometer,	SensorManager.SENSOR_DELAY_UI);
	}
 
	@Override
	public void onPause() {
		// Add the following line to unregister the Sensor Manager onPause
		mSensorManager.unregisterListener(mShakeDetector);//在pause週期的時候停止監聽
		super.onPause();
	}

	protected void domywork() {
		// TODO Auto-generated method stub
        for(int l=0;l<check.length;l++){ //紀錄骰子相同陣列歸0
        	check[l]=false;
        	checkLarge[l]=false;
        }
		
		h0102_T001.setText("");//重設TextView
		cunt18la=0;//計數器歸0
		num18la=0;//計數器歸0
		larg18la=0;
		//<!-------------設定Dialog--------------------->
		
		h0102a.setTitle("十八啦~~~");
		h0102a.setCancelable(false);
		h0102a.setContentView(R.layout.h0102a);
		Button h0102a_B001=(Button)h0102a.findViewById(R.id.h0102a_B001);
		TextView h0102a_T001=(TextView)h0102a.findViewById(R.id.h0102a_T001);
		h0102a_B001.setOnClickListener(h0102a_B001on);
		//<!----------------------------------------------->
//<!------------------在Dialog裡面顯示搖出的點數-------------------------------------->
		String str=getString(R.string.h0102a_T001);
		for(int n=0;n<check.length;n++){
			 Arr18la[n]=(int) (Math.random()*6+1);//隨機產生1~6的數字定且放置進陣列裡面
			 str=str+Arr18la[n]+"  "; //將結果取出來印在h0102a的TextView
		 }
		//<!--------------------------------------------------->
		//<!-----------------依序取出骰子並且從頭開始掃描是否有相同的骰子-------------------->
		for(int i=0;i<check.length;i++){
			for(int j=0;j<check.length;j++){
				if(Arr18la[i]==Arr18la[j] && i!=j){ //先取出位置為i的骰子  並且開始比對位置為j的骰子 假如點數相同且骰子位置不相等
					check[i]=true;check[j]=true; //在記錄用的check[]裡面把相同的骰子設定=true					
				}
				if(Arr18la[i]>Arr18la[j]){
					checkLarge[i]=true;
				}

			}
		}
		//<!----------------------------------------------------------------->
		//<!------------依序取出check[]裡面的狀態-------------------------------------->
		for(int k=0;k<check.length;k++){
			if(check[k]==true){
				cunt18la++;//紀錄check[]有幾個骰子相同
			}
			if(check[k]==false){
				num18la+=Arr18la[k];//把不同的骰子點數作加總
			}
			if(checkLarge[k]==true){
				larg18la+=Arr18la[k];
			}
			
		}
//<!------------------------------------------------------------->
		h0102a_T001.setText(str);
		h0102a.show();//呼叫Dialog
	}
    private Button.OnClickListener h0102a_B001on=new Button.OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			//<!--------依照cunt18la判斷條件------------------->
			switch(cunt18la){
			case 0:
			case 3:
				
				  h0102_T001.setText("0點 請重新搖骰子");
				break;
			case 2:
                  if(num18la==3){					
				    h0102_T001.setText("G~~B~~啦~~!");
				    break;
				}else {
					h0102_T001.setText(getString(R.string.h0102_T001)+Integer.toString(num18la));
					break;
				}
				  
			case 4:
				if(Arr18la[0]==Arr18la[1]&&Arr18la[1]==Arr18la[2]&&Arr18la[2]==Arr18la[3]){
				    h0102_T001.setText(getString(R.string.h0102_T001)+"恭喜你骰到豹子");
				    break;
				}else if (larg18la==12) {
					h0102_T001.setText("1~~8~~啦~~!");
					break;
				}else {
					h0102_T001.setText(getString(R.string.h0102_T001)+Integer.toString(larg18la));
					break;
				}
			}
//<!-------------------------------------------------------------------------->
		
		
		
		h0102a.cancel();
		}
    	
    };
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.h0102, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}


}
