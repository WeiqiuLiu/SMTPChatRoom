package VMail.Activity;

import android.app.ActionBar;
import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import VMail.R;

import java.io.File;

public class DisplayActivity extends Activity implements View.OnClickListener {

    private VideoView video;
    private ImageView iv;
    private TextView actionbar_title;
    private String path;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        Bundle bundle = this.getIntent().getExtras();
        path = bundle.getString("path");
        type = bundle.getString("type");


        ActionBar actionBar = getActionBar();
        actionBar.setCustomView(R.layout.actionbar);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionbar_title = (TextView) findViewById(R.id.title);
        actionbar_title.setText("display");

        TextView mBtnBack = (TextView) findViewById(R.id.back_button);
        mBtnBack.setOnClickListener(this);

        TextView mBtnAdd = (TextView) findViewById(R.id.details);
        mBtnAdd.setVisibility(View.GONE);

        initView();
    }

    private void initView() {
        if (type.equals("video")) {
            video = (VideoView) findViewById(R.id.video);
            video.setVisibility(View.VISIBLE);
            Uri uri = Uri.parse(path);
            video.setVideoURI(uri);
            video.setMediaController(new MediaController(DisplayActivity.this));
            video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    video.start();
                }
            });
        } else if (type.equals("image")) {
            iv = (ImageView) findViewById(R.id.photo);
            iv.setVisibility(View.VISIBLE);
            Uri uri = Uri.fromFile(new File(path));
            iv.setImageURI(uri);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button:
                finish();
                break;
        }
    }
}