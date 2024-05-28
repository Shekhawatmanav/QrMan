package com.example.qrman;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.zxing.WriterException;

import java.lang.reflect.GenericArrayType;
import java.util.regex.Pattern;



public class QrGenActivity extends AppCompatActivity {

    private TextView qrCodeTv;
    private ImageView qrCodeDisTv;
    private TextInputEditText linkEdt;
    private Button generateBtn;
    private QRGEncoder qrGEncoder;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_qr_gen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        qrCodeTv = findViewById(R.id.idTVGenerateQr);
        qrCodeDisTv = findViewById(R.id.idTVQrCode);
        linkEdt = findViewById(R.id.idEdtLink);
        generateBtn = findViewById(R.id.idGenQrBtn);

        generateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = linkEdt.getText().toString().trim();
                if (TextUtils.isEmpty(url)) {
                    Toast.makeText(QrGenActivity.this, "Please enter a URL", Toast.LENGTH_SHORT).show();
                } else {
                    url = formatUrl(url);
                    if (isValidUrl(url)) {
                        Toast.makeText(QrGenActivity.this, "URL is valid: " + url, Toast.LENGTH_SHORT).show();
                        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
                        Display display = manager.getDefaultDisplay();
                        Point point = new Point();
                        display.getSize(point);
                        int width = point.x;
                        int height = point.y;
                        int dimen = width<height ? width : height;
                        dimen = dimen * 3/4;

                        qrGEncoder = new QRGEncoder(linkEdt.getText().toString(),null, Contents.Type.TEXT,dimen);
                        try{
                            bitmap = qrGEncoder.encodeAsBitmap();
                            qrCodeTv.setVisibility(View.GONE);
                            qrCodeDisTv.setImageBitmap(bitmap);
                        }catch (WriterException e){
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(QrGenActivity.this, "Invalid URL", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private String formatUrl(String url) {
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            if (url.matches(".*\\.(com|in|org|net|edu|gov|mil|co|io|ai|biz|info|me|name|pro|us|tv|cc|asia|jobs|mobi|travel|int|tel|ac|ad|ae|af|ag|ai|al|am|ao|aq|ar|as|at|au|aw|ax|az|ba|bb|bd|be|bf|bg|bh|bi|bj|bm|bn|bo|br|bs|bt|bv|bw|by|bz|ca|cd|cf|cg|ch|ci|ck|cl|cm|cn|co|cr|cu|cv|cw|cx|cy|cz|de|dj|dk|dm|do|dz|ec|ee|eg|er|es|et|eu|fi|fj|fk|fm|fo|fr|ga|gb|gd|ge|gf|gg|gh|gi|gl|gm|gn|gp|gq|gr|gs|gt|gu|gw|gy|hk|hm|hn|hr|ht|hu|id|ie|il|im|in|io|iq|ir|is|it|je|jm|jo|jp|ke|kg|kh|ki|km|kn|kp|kr|kw|ky|kz|la|lb|lc|li|lk|lr|ls|lt|lu|lv|ly|ma|mc|md|me|mg|mh|mk|ml|mm|mn|mo|mp|mq|mr|ms|mt|mu|mv|mw|mx|my|mz|na|nc|ne|nf|ng|ni|nl|no|np|nr|nu|nz|om|pa|pe|pf|pg|ph|pk|pl|pm|pn|pr|ps|pt|pw|py|qa|re|ro|rs|ru|rw|sa|sb|sc|sd|se|sg|sh|si|sj|sk|sl|sm|sn|so|sr|ss|st|sv|sx|sy|sz|tc|td|tf|tg|th|tj|tk|tl|tm|tn|to|tr|tt|tv|tz|ua|ug|uk|um|us|uy|uz|va|vc|ve|vg|vi|vn|vu|wf|ws|ye|yt|za|zm|zw)\\b.*")) {
                url = "https://" + url;
            }
        }
        return url;
    }

    private boolean isValidUrl(String url) {
        Pattern urlPattern = Pattern.compile("^(https?)://.*$", Pattern.CASE_INSENSITIVE);
        return urlPattern.matcher(url).matches();
    }
}