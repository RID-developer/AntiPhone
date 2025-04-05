package com.RIDdev.antiphone;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.RIDdev.antiphone.background.Constant;

public class AppDetails extends AppCompatActivity {
    public static PackSet pack = new PackSet();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_app_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        pack.Reset(
        );

        pack.PackName = getIntent().getStringExtra("packageName");
        Log.d("AppDetails", "PackName: " + pack.PackName);
        Constant.Opr.Retrieve(pack);
        Log.d("AppDetails", "PackName: " + pack.PackName);

         TextView packageName = findViewById(R.id.packageName);
        TextView timeDisplay = findViewById(R.id.timeDisplay);
        CheckBox blacklist = findViewById(R.id.blacklist);
        EditText limit = findViewById(R.id.limit);
        EditText message = findViewById(R.id.Message);
        Button backButton = findViewById(R.id.backButton);

        packageName.setText("Package Name: " + pack.PackName);
        timeDisplay.setText("Total Time: " + pack.time+"s");


        blacklist.setChecked(pack.blacklist);

        backButton.setOnClickListener(v -> {
            pack.blacklist = blacklist.isChecked();

            String text = limit.getText().toString();
            if (!text.isEmpty()) {
                pack.lim = Integer.parseInt(text);
            }
            text = message.getText().toString();
            if(!text.isEmpty())
            {
                pack.Message = text;
            }


            Constant.Opr.Insert(pack);

            Intent intent = new Intent(AppDetails.this, AppBlock.class);
            startActivity(intent);
        });
    }
}