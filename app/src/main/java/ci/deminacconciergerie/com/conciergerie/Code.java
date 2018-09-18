package ci.deminacconciergerie.com.conciergerie;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class Code extends AppCompatActivity {

    private TextView backToEmail , validate_btn;

    private ImageView close_activity_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code);


        validate_btn = (TextView) findViewById(R.id.validate_button);
        backToEmail = (TextView)findViewById(R.id.backToEmail);
        close_activity_code = (ImageView)findViewById(R.id.close_activity_code);



        validate_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Code.this , passwords.class));
            }
        });

        backToEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Code.this , password.class));
            }
        });


        close_activity_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
