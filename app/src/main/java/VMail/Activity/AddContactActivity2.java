package VMail.Activity;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import VMail.Entity.Friends;
import VMail.Mail.Transmitor;
import VMail.R;

import org.litepal.crud.DataSupport;

import javax.mail.internet.InternetAddress;

public class AddContactActivity2 extends Activity implements View.OnClickListener {

    private String address;
    private TextView actionbar_title;
    private EditText et;

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button:
                this.finish();
                break;
            case R.id.details:
                send();
                finish();
                break;
        }
    }

    private void send() {
        String hello = et.getText().toString();
        SendAddTask s = new SendAddTask(hello, address);
        s.execute((Void) null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact2);
        Bundle bundle = this.getIntent().getExtras();
        address = bundle.getString("address");

        ActionBar actionBar = getActionBar();
        actionBar.setCustomView(R.layout.actionbar);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionbar_title = (TextView) findViewById(R.id.title);
        actionbar_title.setText(address);

        TextView mBtnBack = (TextView) findViewById(R.id.back_button);
        mBtnBack.setOnClickListener(this);

        TextView mBtnAdd = (TextView) findViewById(R.id.details);
        Drawable drawable = getResources().getDrawable(R.drawable.ic_send_white_24dp);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        mBtnAdd.setCompoundDrawables(drawable, null, null, null);

        mBtnAdd.setOnClickListener(this);

        et = (EditText) findViewById(R.id.edit_field);

    }

    public class SendAddTask extends AsyncTask<Void, Void, Boolean> {

        private String hello;
        private String address;

        SendAddTask(String hello, String address) {
            this.hello = hello;
            this.address = address;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            InternetAddress[] i = new InternetAddress[1];
            try {
                i[0] = new InternetAddress(address);
                Transmitor.TxTransmitor(2, hello, DataSupport.findFirst(Friends.class).getSpecialid(), i);
                return true;
            } catch (Exception e) {
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {

        }

        @Override
        protected void onCancelled() {

        }
    }
}
