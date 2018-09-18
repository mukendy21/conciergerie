package ci.deminacconciergerie.com.conciergerie;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class passwords extends AppCompatActivity {

    private ImageView close_activity_passwords;
    private TextView validate_btn_passwords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passwords);


        close_activity_passwords = (ImageView)findViewById(R.id.close_activity_passwords);
        validate_btn_passwords = (TextView)findViewById(R.id.validate_btn_passwords);


        close_activity_passwords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        validate_btn_passwords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(passwords.this , Login.class));
            }
        });
    }
}
