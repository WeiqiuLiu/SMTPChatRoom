package VMail.Activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import VMail.R;


public class GroupManActivity extends Activity implements View.OnClickListener {
    private String groupid;
    private TextView actionbar_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_man);
        Bundle bundle = this.getIntent().getExtras();
        groupid = bundle.getString("sid");
        Button block = (Button) findViewById(R.id.block_button);
        block.setOnClickListener(this);

        ActionBar actionBar = getActionBar();
        actionBar.setCustomView(R.layout.actionbar);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionbar_title = (TextView) findViewById(R.id.title);
        actionbar_title.setText("Select File");
        TextView t = (TextView) findViewById(R.id.details);
        t.setVisibility(View.GONE);

        t = (TextView) findViewById(R.id.back_button);
        t.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.block_button:
                block();
                break;
            case R.id.back_button:
                this.finish();
                break;
            default:
                break;
        }
    }

    public void block() {
        Intent intent = new Intent();
        intent.setClass(GroupManActivity.this, BlockMembersActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("sid", groupid);
        Log.d("22", groupid);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
