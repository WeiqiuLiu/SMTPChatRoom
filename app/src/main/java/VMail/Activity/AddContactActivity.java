package VMail.Activity;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import VMail.R;

import VMail.Adapter.AddingRequestExpandableListAdapter;
import VMail.Entity.Requests;
import VMail.Management.Management;

import java.util.ArrayList;
import java.util.List;


public class AddContactActivity extends Activity implements View.OnClickListener {
    public static Handler h;
    private ExpandableListView request_list;
    private String[] group = {""};
    private ArrayList<String> addresslist = new ArrayList<String>();
    private ArrayList<String> idlist = new ArrayList<String>();
    private ArrayList<Boolean> confirm_list = new ArrayList<Boolean>();
    private ArrayList<Boolean> isFriend_list = new ArrayList<Boolean>();
    private EditText et;
    View.OnKeyListener onKey = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                String address = et.getText().toString();
                if (isValid(address)) {
                    Intent intent = new Intent(AddContactActivity.this, AddContactActivity2.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("address", address);
                    intent.putExtras(bundle);
                    AddContactActivity.this.startActivity(intent);
                } else {
                    et.setText("");
                    Toast.makeText(AddContactActivity.this, "wrong mail address", Toast.LENGTH_LONG).show();
                }
            }
            return false;
        }
    };
    private TextView actionbar_title;
    private AddingRequestExpandableListAdapter addingRequestExpandableListAdapter;

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button:
                this.finish();
                break;
        }
    }

    private boolean isValid(String address) {

        return address.contains("@");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        ActionBar actionBar = getActionBar();
        actionBar.setCustomView(R.layout.actionbar);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionbar_title = (TextView) findViewById(R.id.title);
        actionbar_title.setText(R.string.new_friend);

        TextView mBtnBack = (TextView) findViewById(R.id.back_button);
        mBtnBack.setOnClickListener(this);

        TextView mBtnAdd = (TextView) findViewById(R.id.details);
        mBtnAdd.setVisibility(View.GONE);

        request_list = (ExpandableListView) findViewById(R.id.adding_request_list);
        addingRequestExpandableListAdapter = new AddingRequestExpandableListAdapter(this, group, addresslist, idlist, confirm_list, isFriend_list);
        request_list.setAdapter(addingRequestExpandableListAdapter);
        init();
        request_list.setGroupIndicator(null);
        for (int i = 0; i < group.length; i++) {
            request_list.expandGroup(i);
        }
        request_list.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });

        et = (EditText) findViewById(R.id.edit_field);
        et.setOnKeyListener(onKey);
    }

    private void init() {
        List<Requests> l = Management.allrequest();
        Log.d("5", l.size() + "");
        for (Requests item : l) {
            Log.d("5", item.getAddress() + "");
            addresslist.add(item.getAddress());
            idlist.add(item.getRSid());
            confirm_list.add(item.isIs_added());
            isFriend_list.add(item.isIs_friend());
        }
        addingRequestExpandableListAdapter.notifyDataSetChanged();

        try {
            h = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    switch (msg.what) {
                        case 0:
                            break;
                        case 2:
                            String temp1 = (String) msg.obj;
                            String[] l1 = temp1.split(" ");
                            String address1 = l1[3] + ";";
                            String id1 = l1[4];
                            addresslist.add(address1);
                            idlist.add(id1);
                            confirm_list.add(false);
                            isFriend_list.add(true);
                            addingRequestExpandableListAdapter.notifyDataSetChanged();
                            break;
                        case 31:
                            String temp2 = (String) msg.obj;
                            String[] l2 = temp2.split("--");
                            String address2 = l2[0];
                            Log.d("7", address2 + "");
                            String id2 = l2[1];
                            addresslist.add(address2);
                            idlist.add(id2);
                            confirm_list.add(false);
                            isFriend_list.add(false);
                            addingRequestExpandableListAdapter.notifyDataSetChanged();
                            break;
                        default:
                            break;
                    }
                }
            };
        } catch (Exception e) {
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        h = null;
    }
}