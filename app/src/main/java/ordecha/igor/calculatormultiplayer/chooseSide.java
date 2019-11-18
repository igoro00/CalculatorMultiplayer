package ordecha.igor.calculatormultiplayer;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

import android.view.MenuItem;
import android.view.View;

public class chooseSide extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_side);

    }

    public void toSP(View v){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("isMP", false);
        startActivity(intent);
    }

    public void toMP(View v){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("isMP", true);
        startActivity(intent);
    }

    public void showPopup(View v){
        PopupMenu popup = new PopupMenu(this, v);
        // This activity implements OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.setting:
                        Intent intent = new Intent(chooseSide.this, SettingsActivity.class);
                        startActivity(intent);
                        return true;

                    default:
                        return false;
                }
            }
        });
        popup.inflate(R.menu.mymenu);
        popup.show();
    }
}
