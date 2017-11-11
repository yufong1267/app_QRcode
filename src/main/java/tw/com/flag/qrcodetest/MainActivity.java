package tw.com.flag.qrcodetest;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity {
    private Button button;
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //not change display method
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        //for always strangt display
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //always screen on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //close title bar
        getSupportActionBar().hide();
        //close system bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //create database block

        db = openOrCreateDatabase("testdb", Context.MODE_PRIVATE,null);
        //create table the name of test
        String createTable = "CREATE TABLE IF NOT EXISTS " + "test" + "(item VARCHAR(32))";
        db.execSQL(createTable);

        final Activity activity = this;
//        button  = (Button)findViewById(R.id.scan_btn);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                IntentIntegrator integrator = new IntentIntegrator(activity);
//                integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
//                integrator.setPrompt("Scan a barcode");
//                integrator.setCameraId(0);  // Use a specific camera of the device
//                integrator.setBeepEnabled(false);
//                integrator.setBarcodeImageEnabled(true);
//                integrator.initiateScan();
////                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
////                integrator.setPrompt("Scan");
////                integrator.setCameraId(0);
////                integrator.setBeepEnabled(false);
////                integrator.setBarcodeImageEnabled(true);
////                integrator.initiateScan();
//            }
//        });
    }
    //QRcode method
    public void onClick(View v) {


        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setPrompt("Scanning...");
        integrator.setCameraId(0);  // Use a specific camera of the device
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(false);
        integrator.initiateScan();
//                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
//                integrator.setPrompt("Scan");
//                integrator.setCameraId(0);
//                integrator.setBeepEnabled(false);
//                integrator.setBarcodeImageEnabled(true);
//                integrator.initiateScan();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "cancelled", Toast.LENGTH_LONG).show();
            } else {
                String log = result.getContents().toString();
                if(log.equals("陳昕"))
                {
                    //嗆報小志志
                    final TextView fuck = (TextView)findViewById(R.id.fuxk);

                    fuck.setText("志剛小雞雞 吃宵夜搂~");
                }
//                adddata in table
               addData(log);
               Cursor c = db.rawQuery("SELECT * FROM test" , null);
//               String output = c.getString(0);
               final TextView print = (TextView)findViewById(R.id.out);
                c.moveToFirst();
                boolean check = false;


                do {
                    if(c.getString(0).equals("item333"))
                    {
                        check = true;
                        break;
                    }
                }while(c.moveToNext());
                if(check)
                {
                    print.setVisibility(View.VISIBLE);
                }

                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

//    build function of adding data
    public void addData(String item)
    {
        ContentValues cv = new ContentValues(1);
        cv.put("item" , item);

        db.insert("test" , null , cv);
    }

//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        IntentResult result = IntentIntegrator.parseActivityResult(requestCode , resultCode , data);
//        if(result != null){
//            if (result.getContents() == null)
//            {
//                Toast.makeText(this,"You cancelled the scanning " , Toast.LENGTH_LONG).show();
//            }else{
//                Toast.makeText(this,result.getContents() , Toast.LENGTH_LONG).show();
//            }
//        }
//
//            else{
//            super.onActivityResult(requestCode, resultCode, data);
//        }
//    }
}
