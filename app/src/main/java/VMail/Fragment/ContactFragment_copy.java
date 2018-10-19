package VMail.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import VMail.Adapter.ContactExpandableListAdapter;
import VMail.Entity.Friends;
import VMail.Entity.Groups;
import VMail.Management.Management;
import VMail.R;

import java.util.ArrayList;
import java.util.List;


public class ContactFragment_copy extends Fragment {
    String[] group = {"", "Friends", "Groups"};
    String[][] itemName = {{}, {}, {}};
    private ExpandableListView contact_list;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);
        contact_list = (ExpandableListView) view.findViewById(R.id.contact_list);
        init();

        ContactExpandableListAdapter contactExpandableListAdapter = new ContactExpandableListAdapter(getContext(), group, itemName);

        contact_list.setAdapter(contactExpandableListAdapter);

        contact_list.setGroupIndicator(null);

        for (int i = 0; i < group.length; i++) {
            contact_list.expandGroup(i);
        }

        contact_list.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });
        return view;
    }

    public void init() {
        List<Friends> list = Management.getAllFriends();
        ArrayList<String> name_list = new ArrayList<String>();
        for (Friends item : list) {
            name_list.add(item.getName());
        }
        itemName[1] = name_list.toArray(new String[name_list.size()]);

        List<Groups> list_group = Management.getGroupsList();
        ArrayList<String> group_list = new ArrayList<String>();
        for (Groups item : list_group) {
            group_list.add(item.getGroupname());
        }
        itemName[2] = group_list.toArray(new String[group_list.size()]);
    }
}