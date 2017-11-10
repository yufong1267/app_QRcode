package tw.com.flag.qrcodetest;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity {
    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final Activity activity = this;
        button  = (Button)findViewById(R.id.scan_btn);
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
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }
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
