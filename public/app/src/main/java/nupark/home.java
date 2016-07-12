package nupark;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import nupark.R;

public class home extends AppCompatActivity {
    TextView nome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        nome = (TextView)findViewById(R.id.nome);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String value = extras.getString("nome");
            nome.setText(value);

        }
    }
}
