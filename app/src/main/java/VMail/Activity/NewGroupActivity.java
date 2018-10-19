package VMail.Activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;

import VMail.R;

import java.util.ArrayList;
import java.util.List;

import javax.mail.internet.InternetAddress;

import VMail.Adapter.NewGroupExpandableListAdapter;
import VMail.Entity.Friends;
import VMail.Mail.Transmitor;
import VMail.Management.Management;

public class NewGroupActivity extends Activity implements View.OnClickListener {
    private ExpandableListView member_list;

    private String[] group = {""};
    private String[][] itemName1 = {{}};
    private boolean[] isSelected = null;
    private TextView actionbar_title;
    private NewGroupExpandableListAdapter newGroupExpandableListAdapter;
    private String groupname = "";


    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.back_button:
                this.finish();
                break;
            case R.id.details:
                save(newGroupExpandableListAdapter.getList());
                break;
        }
    }

    private void save(final ArrayList<String> group_list) {
        final EditText edt = new EditText(this);
        if (group_list.size() > 0) {
            new AlertDialog.Builder(this)
                    .setTitle("Group Name")
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setView(edt)
                    .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            groupname = edt.getText().toString();
                            if (!groupname.equals("")) {
                                String sid = Management.addnewcluster(groupname);
                                SendGrpTask t = new SendGrpTask(group_list, groupname, sid);
                                t.execute((Void) null);
                                finish();
                            }
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        } else {
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group);

        ActionBar actionBar = getActionBar();
        actionBar.setCustomView(R.layout.actionbar);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionbar_title = (TextView) findViewById(R.id.title);
        actionbar_title.setText(R.string.new_group);

        TextView mBtnBack = (TextView) findViewById(R.id.back_button);
        mBtnBack.setOnClickListener(this);
        mBtnBack.setText("Cancel");

        TextView save = (TextView) findViewById(R.id.details);
        save.setCompoundDrawables(null, null, null, null);
        save.setOnClickListener(this);
        save.setText("Done   ");

        member_list = (ExpandableListView) findViewById(R.id.expandable_list);
        setItems();


        newGroupExpandableListAdapter = new NewGroupExpandableListAdapter(this, group, itemName1, isSelected);

        member_list.setAdapter(newGroupExpandableListAdapter);

        member_list.setGroupIndicator(null);

        for (int i = 0; i < group.length; i++) {
            member_list.expandGroup(i);
        }

        member_list.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });

    }

    private void setItems() {
        List<Friends> list = Management.getAllFriends();
        list.remove(0);
        ArrayList<String> name_list = new ArrayList<String>();
        boolean[] temp4 = new boolean[list.size()];
        int i = 0;
        Log.d("7", "" + list.size());
        for (Friends item : list) {
            name_list.add(item.getAddress());
            temp4[i] = false;
            i++;
        }
        itemName1[0] = name_list.toArray(new String[name_list.size()]);

        isSelected = temp4;

    }

    public class SendGrpTask extends AsyncTask<Void, Void, Boolean> {

        private String groupid;
        private String groupname;
        private ArrayList<String> fl;

        SendGrpTask(ArrayList<String> fl, String groupname, String groupid) {
            this.fl = fl;
            this.groupname = groupname;
            this.groupid = groupid;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                InternetAddress[] addresses = new InternetAddress[fl.size()];
                for (int i = 0; i < addresses.length; i++) {
                    addresses[i] = new InternetAddress(fl.get(i));
                }
                return Transmitor.TxTransmitor(31, groupname, groupid, addresses);
            } catch (Exception e) {
                Log.d("1", "" + e);
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