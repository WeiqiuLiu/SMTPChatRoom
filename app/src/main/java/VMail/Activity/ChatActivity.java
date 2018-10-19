package VMail.Activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import VMail.Adapter.ChatMsgViewAdapter;
import VMail.Entity.ChatMsgEntity;
import VMail.Entity.Friends;
import VMail.Entity.settings;
import VMail.Mail.Transmitor;
import VMail.Management.Management;
import VMail.R;

import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.mail.internet.InternetAddress;


public class ChatActivity extends Activity implements OnClickListener {
    private static final int PHOTO_REQUEST_GALLERY = 1;
    public static Handler h;
    static private EditText mEditTextContent;

    private Button mBtnSend;
    private TextView mBtnBack;
    private TextView detail;
    private RelativeLayout mBottom;
    private ListView mListView;
    private ChatMsgViewAdapter mAdapter;
    private List<ChatMsgEntity> mDataArrays = new ArrayList<ChatMsgEntity>();
    private ImageView img1, sc_img1;
    private View rcChat_popup;
    private LinearLayout del_re;
    private ImageView chatting_mode_btn, volume;
    private Button image;
    private int flag = 1;
    private TextView actionbar_title;
    private String name;
    private String id;
    private boolean isgroup;
    private ArrayList<String> address = new ArrayList<String>();
    private Context ctx;

    public static void setComment(String et) {
        mEditTextContent.setText(et);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ctx = this;
        settings.context = this;

        Bundle bundle = this.getIntent().getExtras();
        name = bundle.getString("name");
        id = bundle.getString("id");
        isgroup = bundle.getString("group").equals("1");
        Log.d("11", isgroup + "");


        ActionBar actionBar = getActionBar();
        actionBar.setCustomView(R.layout.actionbar);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionbar_title = (TextView) findViewById(R.id.title);
        actionbar_title.setText(name);

        initView();

        initData();


        h = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0:
                        break;
                    case 111:
                        if (isgroup) {
                            ChatMsgEntity entity2 = new ChatMsgEntity();
                            entity2.setDate(settings.getDate());
                            entity2.setOutMsg(false);
                            String[] temp = ((String) msg.obj).split("-!!!-");
                            entity2.setName(temp[0]);
                            entity2.setFriend(false);
                            entity2.setPath(temp[1]);
                            entity2.setFrom_id(id);
                            mDataArrays.add(entity2);
                            mAdapter.notifyDataSetChanged();
                            mListView.setSelection(mListView.getCount() - 1);
                        }
                        break;
                    case 112:
                        if (isgroup) {
                            ChatMsgEntity entity2 = new ChatMsgEntity();
                            entity2.setDate(settings.getDate());
                            entity2.setOutMsg(false);
                            String[] temp = ((String) msg.obj).split("-!!!-");
                            entity2.setName(temp[0]);
                            entity2.setFriend(false);
                            entity2.setText(temp[1]);
                            entity2.setFrom_id(id);
                            mDataArrays.add(entity2);
                            mAdapter.notifyDataSetChanged();
                            mListView.setSelection(mListView.getCount() - 1);
                        }
                        break;
                    case 121:
                        if (!isgroup) {
                            ChatMsgEntity entity1 = new ChatMsgEntity();
                            entity1.setDate(settings.getDate());
                            entity1.setOutMsg(false);
                            entity1.setName(name);
                            entity1.setFriend(true);
                            entity1.setPath((String) msg.obj);
                            entity1.setFrom_id(id);
                            mDataArrays.add(entity1);
                            mAdapter.notifyDataSetChanged();
                            mListView.setSelection(mListView.getCount() - 1);
                        }
                        break;
                    case 122:
                        if (!isgroup) {
                            ChatMsgEntity entity1 = new ChatMsgEntity();
                            entity1.setDate(settings.getDate());
                            entity1.setOutMsg(false);
                            entity1.setName(name);
                            entity1.setFriend(true);
                            entity1.setText((String) msg.obj);
                            entity1.setFrom_id(id);
                            mDataArrays.add(entity1);
                            mAdapter.notifyDataSetChanged();
                            mListView.setSelection(mListView.getCount() - 1);
                        }
                        break;
                    case 5:
                        mDataArrays = Management.getChatMsg(id, !isgroup);
                        mAdapter = new ChatMsgViewAdapter(ctx, mDataArrays);
                        mListView.setAdapter(mAdapter);
                        break;
                    default:
                        break;
                }
            }
        };
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        h = null;
    }

    public void initView() {
        mListView = (ListView) findViewById(R.id.message_list);
        mBtnSend = (Button) findViewById(R.id.send_button);

        mBtnSend.setOnClickListener(this);
        detail = (TextView) findViewById(R.id.details);
        detail.setOnClickListener(this);

        image = (Button) findViewById(R.id.photo_button);
        image.setOnClickListener(this);

        mBtnBack = (TextView) findViewById(R.id.back_button);
        mBtnBack.setOnClickListener(this);


        mEditTextContent = (EditText) findViewById(R.id.edit_field);

    }

    public void initData() {

        mDataArrays = Management.getChatMsg(id, !isgroup);
        Log.d("5", mDataArrays.size() + "");

        mAdapter = new ChatMsgViewAdapter(this, mDataArrays);
        mListView.setAdapter(mAdapter);

    }

    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.send_button:
                send();
                break;
            case R.id.back_button:
                this.finish();
                break;
            case R.id.photo_button:
                Intent intent1 = new Intent(ChatActivity.this, SelectGalleryActivity.class);

                startActivityForResult(intent1, 1000);
                break;
            case R.id.details:
                if (isgroup) {
                    Intent intent = new Intent();
                    intent.setClass(ChatActivity.this, GroupManActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("sid", id);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000 && resultCode == 1001) {
            String path = data.getStringExtra("path");
            Log.d("25", path);
            sendFile(path);
        }
    }

    public void sendFile(String path) {
        if (isgroup) {
            address = Management.getAllUnblockAddressInGroup(id);
        } else {
            address.add(name);
        }
        String[] test_addr = address.toArray(new String[address.size()]);
        Log.d("11", test_addr.length + "");
        SendMsgTask task;
        if (!isgroup) {
            task = new SendMsgTask(DataSupport.findFirst(Friends.class).getSpecialid(), test_addr, path);
        } else {
            task = new SendMsgTask(id, test_addr, path);
        }
        task.execute((Void) null);
        ChatMsgEntity entity = new ChatMsgEntity(settings.username, settings.getDate(), !isgroup, path);
        entity.setFrom_id(id);
        entity.setOutMsg(true);
        Management.addChatMsg(settings.username, settings.getDate(), null, id, !isgroup, true, path);
        mDataArrays.add(entity);
        mAdapter.notifyDataSetChanged();
        mListView.setSelection(mListView.getCount() - 1);
    }

    private void send() {
        String contString = mEditTextContent.getText().toString();
        if (contString.length() > 0) {
            if (isgroup) {
                address = Management.getAllUnblockAddressInGroup(id);
            } else {
                address.add(name);
            }
            String[] test_addr = address.toArray(new String[address.size()]);
            Log.d("11", test_addr.length + "");
            SendMsgTask task;
            if (!isgroup) {
                task = new SendMsgTask(contString, DataSupport.findFirst(Friends.class).getSpecialid(), test_addr);
            } else {
                task = new SendMsgTask(contString, id, test_addr);
            }
            task.execute((Void) null);
            ChatMsgEntity entity = new ChatMsgEntity(settings.username, settings.getDate(), contString, !isgroup);
            entity.setFrom_id(id);
            entity.setOutMsg(true);
            Management.addChatMsg(settings.username, settings.getDate(), contString, id, !isgroup, true, null);
            mDataArrays.add(entity);
            mAdapter.notifyDataSetChanged();
            mEditTextContent.setText("");
            mListView.setSelection(mListView.getCount() - 1);
        }
    }

    public class SendMsgTask extends AsyncTask<Void, Void, Boolean> {

        private String id;
        private String msg;
        private String[] address;
        private boolean isFile = false;

        SendMsgTask(String msg, String id, String[] address) {
            this.msg = msg;
            this.id = id;
            this.address = address;
        }

        SendMsgTask(String id, String[] address, String path) {
            this.msg = path;
            this.id = id;
            this.address = address;
            this.isFile = true;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            if (!isFile) {
                try {
                    InternetAddress[] addresses = new InternetAddress[address.length];
                    for (int i = 0; i < address.length; i++) {
                        addresses[i] = new InternetAddress(address[i]);
                    }
                    Log.d("11", "" + isgroup + " " + msg + " " + id);
                    if (!isgroup) {
                        return Transmitor.TxTransmitor(122, msg, DataSupport.findFirst(Friends.class).getSpecialid(), addresses);
                    } else {
                        return Transmitor.TxTransmitor(112, msg, id, addresses);
                    }
                } catch (Exception e) {
                    Log.d("11", "" + e);
                    return false;
                }
            } else {
                try {
                    InternetAddress[] addresses = new InternetAddress[address.length];
                    for (int i = 0; i < address.length; i++) {
                        addresses[i] = new InternetAddress(address[i]);
                    }
                    Log.d("11", "" + isgroup + " " + msg + " " + id);
                    if (!isgroup) {
                        return Transmitor.TxTransmitor(121, msg, id, addresses);
                    } else {
                        return Transmitor.TxTransmitor(111, msg, id, addresses);
                    }
                } catch (Exception e) {
                    Log.d("11", "" + e);
                    return false;
                }
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