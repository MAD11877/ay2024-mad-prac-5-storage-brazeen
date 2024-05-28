package sg.edu.np.mad.madpractical5;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbHandler dbHandler = new dbHandler(this, null, null, 1);
        TextView nameview = findViewById(R.id.TextView2);
        TextView descview = findViewById(R.id.TextView3);
        Button followbtn = findViewById(R.id.Button1);

        Intent receivingEnd = getIntent();
        String username = receivingEnd.getStringExtra("name");
        String userdescription = receivingEnd.getStringExtra("description");
        nameview.setText(username);
        descview.setText(userdescription);


        user = dbHandler.getUser(username);
        Log.i("pe", user.name);

        if (user.getFollowed()){
            followbtn.setText("UNFOLLOW");
        }
        else {
            followbtn.setText("FOLLOW");
        }


        followbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.getFollowed()){
                    followbtn.setText("FOLLOW");
                    user.setFollowed(false);
                    dbHandler.updateUser(user);
                    Toast.makeText(MainActivity.this,"Unfollowed", Toast.LENGTH_SHORT).show();
                }
                else {
                    followbtn.setText("UNFOLLOW");
                    user.setFollowed(true);
                    dbHandler.updateUser(user);
                    Toast.makeText(MainActivity.this, "Followed", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }




}