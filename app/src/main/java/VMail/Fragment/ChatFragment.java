package VMail.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import VMail.Adapter.ChatExpandableListAdapter;
import VMail.Entity.Friends;
import VMail.Entity.Groups;
import VMail.Management.Management;

import java.util.List;

import VMail.R;


public class ChatFragment extends Fragment {
    private ExpandableListView chat_list;

    private String[] group = {""};
    private List<Friends> fl;
    private ChatExpandableListAdapter chatExpandableListAdapter;
    private List<Groups> gl;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chats, container, false);
        chat_list = (ExpandableListView) view.findViewById(R.id.chat_list);

        init();

        chatExpandableListAdapter = new ChatExpandableListAdapter(getContext(), group, fl, gl);


        chat_list.setAdapter(chatExpandableListAdapter);

        chat_list.setGroupIndicator(null);

        for (int i = 0; i < group.length; i++) {
            chat_list.expandGroup(i);
        }

        chat_list.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });
        return view;
    }

    public void init() {
        fl = Management.getAllFriends();
        gl = Management.getGroupsList();

        Log.d("11", gl.size() + "");
        try {
            chatExpandableListAdapter.notifyDataSetChanged();
        } catch (Exception e) {

        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }
}