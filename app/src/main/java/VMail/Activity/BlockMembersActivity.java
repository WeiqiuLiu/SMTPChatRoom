package VMail.Activity;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import VMail.Adapter.NewGroupExpandableListAdapter;
import VMail.Entity.Groups;
import VMail.Management.Management;
import VMail.R;

import java.util.ArrayList;
import java.util.List;

public class BlockMembersActivity extends Activity implements View.OnClickListener {
    private String sid;
    private ExpandableListView member_list;
    private NewGroupExpandableListAdapter newGroupExpandableListAdapter;

    private String[] group = {""};
    private String[][] itemName1 = {{}};
    private boolean[] isSelected = null;
    private TextView actionbar_title;
    private ArrayList<String> address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block_members);


        ActionBar actionBar = getActionBar();
        actionBar.setCustomView(R.layout.actionbar);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionbar_title = (TextView) findViewById(R.id.title);
        actionbar_title.setText("Blocking list");

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
        Bundle bundle = this.getIntent().getExtras();
        sid = bundle.getString("sid");
        address = Management.getAllAddressInGroup(sid);
        itemName1[0] = address.toArray(new String[address.size()]);
        List<Groups> gl = Management.findMembersInGroupBySid(sid);
        boolean[] temp4 = new boolean[gl.size()];
        for (int i = 0; i < gl.size(); i++) {
            temp4[i] = gl.get(i).isIs_blocking();
        }
        isSelected = temp4;
    }

    private void save(ArrayList<String> al) {
        for (String a : address) {
            Management.unblockmember(a, sid);
        }
        for (String a : al) {
            Management.blockmember(a, sid);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_button:
                this.finish();
                break;
            case R.id.details:
                save(newGroupExpandableListAdapter.getList());
                this.finish();
                break;
        }
    }
}
