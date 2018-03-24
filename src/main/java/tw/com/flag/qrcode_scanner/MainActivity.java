package tw.com.flag.qrcode_scanner;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity {
    public static SQLiteDatabase qrdb = null;
    public static Cursor cursor;
    static final  String db_name = "qr_db";
    static final  String col_name = "qr";
    static final String[] reg_5 = new String[5];
    private  int shift_index = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //這邊只需要存取url一個欄位就好
        String CREATE_TABLE = "CREATE TABLE if not exists " + col_name +
                "(url VARCHAR(50))";
        qrdb = openOrCreateDatabase(db_name, Context.MODE_WORLD_WRITEABLE,null);
        qrdb.execSQL(CREATE_TABLE);

        //-------------畫面預設 不要旋轉getwindow
        getSupportActionBar().hide();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //------------------------------------------------
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //這邊更新每次的前五個到reg_5裡面
        refresh();

        TextView show1 = (TextView)findViewById(R.id.qr_show1);
        TextView show2 = (TextView)findViewById(R.id.qr_show2);
        TextView show3 = (TextView)findViewById(R.id.qr_show3);
        TextView show4 = (TextView)findViewById(R.id.qr_show4);
        TextView show5 = (TextView)findViewById(R.id.qr_show5);
        show1.setText("1:"+reg_5[0]);
        show2.setText("2:"+reg_5[1]);
        show3.setText("3:"+reg_5[2]);
        show4.setText("4:"+reg_5[3]);
        show5.setText("5:"+reg_5[4]);

        ImageView qr = (ImageView)findViewById(R.id.qqq);
        qr.setImageResource(R.drawable.qr_picture);


    }
    public void QR_code_scanner(View V)
    {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setPrompt("掃描中.....");
        integrator.setCameraId(0);  // Use a specific camera of the device
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(false);
        integrator.initiateScan();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "QAQ並沒有掃到東西喔", Toast.LENGTH_LONG).show();
            } else {
                String log = result.getContents().toString();
                addData(log);
                //這邊確認是不是http 或 www開頭
                if (log.contains("http") || log.contains("www"))
                {
                    refresh();
                    Toast.makeText(this, "連結到以下網址: " + result.getContents(), Toast.LENGTH_LONG).show();
                    Uri uri = Uri.parse(log);
                    Intent i = new Intent(Intent.ACTION_VIEW,uri);
                    startActivity(i);
                }else{
                    Toast.makeText(this, "掃描到這端文字訊息 " + result.getContents(), Toast.LENGTH_LONG).show();
                    refresh();
                }

            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void addData(String url){
        ContentValues cv = new ContentValues(1);
        cv.put("url",url);
        qrdb.insert(col_name,null,cv);
    }

    private void refresh(){
        Cursor c = qrdb.rawQuery("SELECT * FROM "+col_name,null);

        if(c.moveToFirst())
        {
            int i = 0;
            do {
                String sho1 = c.getString(0).toString();
                reg_5[i] = sho1;
                //再變動前
                shift_index = i;
                i++;
                if(i>=5)
                {
                    i = 0;
                }
            }while(c.moveToNext());
        }
        TextView test = (TextView)findViewById(R.id.test_show);
        test.setText(""+shift_index);
        switch (shift_index){
            case 0:
                //F1
                //Do nothing
                //F2 1s4,2s3
                String reg0 = reg_5[1];
                reg_5[1] = reg_5[4];
                reg_5[4] = reg0;
                String reg1 = reg_5[2];
                reg_5[2] = reg_5[3];
                reg_5[3] = reg1;
                break;
            case 1:
                //F1 0s1
                String reg2 = reg_5[0];
                reg_5[0] = reg_5[1];
                reg_5[1] = reg2;
                //F2 2s4
                String reg3 = reg_5[2];
                reg_5[2] = reg_5[4];
                reg_5[4] = reg3;
                break;
            case 2:
                //F1 0s2
                String reg4 = reg_5[0];
                reg_5[0] = reg_5[2];
                reg_5[2] = reg4;
                //F2 3s4
                String reg5 = reg_5[3];
                reg_5[3] = reg_5[4];
                reg_5[4] = reg5;
                break;
            case 3:
                //F1 0s3,1s2
                String reg6 = reg_5[0];
                reg_5[0] = reg_5[3];
                reg_5[3] = reg6;
                String reg7 = reg_5[1];
                reg_5[1] = reg_5[2];
                reg_5[2] = reg7;
                //F2
                //Do nothing
                break;
            case 4:
                //F1 0s4 , 1s2
                String reg8 = reg_5[0];
                reg_5[0] = reg_5[4];
                reg_5[4] = reg8;
                String reg9 = reg_5[1];
                reg_5[1] = reg_5[2];
                reg_5[2] = reg9;
                //F2
                //Do nothing
                break;
        }


        TextView show1 = (TextView)findViewById(R.id.qr_show1);
        TextView show2 = (TextView)findViewById(R.id.qr_show2);
        TextView show3 = (TextView)findViewById(R.id.qr_show3);
        TextView show4 = (TextView)findViewById(R.id.qr_show4);
        TextView show5 = (TextView)findViewById(R.id.qr_show5);
        show1.setText("1:"+reg_5[0]);
        show2.setText("2:"+reg_5[1]);
        show3.setText("3:"+reg_5[2]);
        show4.setText("4:"+reg_5[3]);
        show5.setText("5:"+reg_5[4]);
    }
    private void swap(String a , String b)
    {
        String reg = a;
        a = b;
        b = reg;
    }

    public void href1(View v){
        if(reg_5[0] == null)
        {
            Toast.makeText(this, "裡面沒有存放url喔" + reg_5[0], Toast.LENGTH_LONG).show();
        }else{
            if (reg_5[0].contains("http") || reg_5[0].contains("www"))
            {
                Toast.makeText(this, "" + reg_5[0], Toast.LENGTH_LONG).show();
                Uri uri = Uri.parse(reg_5[0]);
                Intent i = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(i);
            }else{
                Toast.makeText(this, "無法傳送:" + reg_5[0] + " (應該只是一段文字)", Toast.LENGTH_LONG).show();
            }

        }


    }

    public void href2(View v){
        if(reg_5[1] == null)
        {
            Toast.makeText(this, "裡面沒有存放url喔" + reg_5[1], Toast.LENGTH_LONG).show();
        }else
        {
            if(reg_5[1].contains("http") || reg_5[1].contains("www"))
            {

                Uri uri = Uri.parse(reg_5[1]);
                Intent i = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(i);
            }else{
                Toast.makeText(this, "無法傳送:" + reg_5[1] + " (應該只是一段文字)", Toast.LENGTH_LONG).show();
            }

        }

    }

    public void href3(View v){
        if(reg_5[2] == null)
        {
            Toast.makeText(this, "裡面沒有存放url喔" + reg_5[2], Toast.LENGTH_LONG).show();
        }else{
            if(reg_5[2].contains("http") || reg_5[2].contains("www"))
            {
                Toast.makeText(this, "使用:" + reg_5[2], Toast.LENGTH_LONG).show();
                Uri uri = Uri.parse(reg_5[2]);
                Intent i = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(i);
            }else
            {
                Toast.makeText(this, "無法傳送:" + reg_5[2] + " (應該只是一段文字)", Toast.LENGTH_LONG).show();
            }

        }

    }
    public void href4(View v){
        if(reg_5[3] == null)
        {
            Toast.makeText(this, "裡面沒有存放url喔" + reg_5[3], Toast.LENGTH_LONG).show();
        }else{
            if(reg_5[3].contains("http") || reg_5[3].contains("www"))
            {
                Toast.makeText(this, "使用:" + reg_5[3], Toast.LENGTH_LONG).show();
                Uri uri = Uri.parse(reg_5[3]);
                Intent i = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(i);
            }else{
                Toast.makeText(this, "無法傳送:" + reg_5[3] + " (應該只是一段文字)", Toast.LENGTH_LONG).show();
            }

        }

    }
    public void href5(View v){
        if(reg_5[4] == null)
        {
            Toast.makeText(this, "裡面沒有存放url喔" + reg_5[4], Toast.LENGTH_LONG).show();
        }else{
            if (reg_5[4].contains("http"))
            {
                Toast.makeText(this, "使用:" + reg_5[4], Toast.LENGTH_LONG).show();
                Uri uri = Uri.parse(reg_5[4]);
                Intent i = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(i);
            }else{
                Toast.makeText(this, "無法傳送:" + reg_5[4] + " (應該只是一段文字)", Toast.LENGTH_LONG).show();
            }

        }

    }
}
