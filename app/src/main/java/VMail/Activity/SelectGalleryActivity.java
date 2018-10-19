package VMail.Activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import VMail.R;

public class SelectGalleryActivity extends Activity implements View.OnClickListener {

    private static final int PHOTO_REQUEST_GALLERY = 1;
    private TextView actionbar_title;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_gallery);

        ActionBar actionBar = getActionBar();
        actionBar.setCustomView(R.layout.actionbar);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionbar_title = (TextView) findViewById(R.id.title);
        actionbar_title.setText("Select resource");
        TextView t = (TextView) findViewById(R.id.details);
        t.setVisibility(View.GONE);

        t = (TextView) findViewById(R.id.back_button);
        t.setOnClickListener(this);

    }


    public void gallery(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
    }

    public void choiceVideo(View view) {
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, 66);
    }

    private String getPath(Uri uri) {
        String[] projection = {MediaStore.Video.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PHOTO_REQUEST_GALLERY) {
            if (data != null) {
                Uri uri = data.getData();
                String path = getPath(uri);

                Log.d("25", path);

                Intent intent = new Intent();
                intent.putExtra("path", path);
                setResult(1001, intent);
                finish();


            }
        } else if (requestCode == 66) {
            if (data != null) {
                Uri uri2 = data.getData();
                String path = getPath(uri2);

                Log.d("25", path);

                Intent intent = new Intent();
                intent.putExtra("path", path);
                setResult(1001, intent);
                finish();

                /*String[] filePathColumn = {MediaStore.Video.Media.DATA};

                Cursor cursor = getContentResolver().query(uri2,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String videopath = cursor.getString(columnIndex);
                cursor.close();
                text.setText(videopath);*/
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button:
                this.finish();
                break;
            default:
                break;
        }
    }
}
