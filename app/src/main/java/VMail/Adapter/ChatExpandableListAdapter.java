package VMail.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import VMail.Activity.ChatActivity;
import VMail.Entity.Friends;
import VMail.Entity.Groups;
import VMail.R;

import java.util.List;


public class ChatExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private String[] group;
    private List<Friends> friendsList;
    private List<Groups> gl;

    public ChatExpandableListAdapter(Context context, String[] group, List<Friends> fl, List<Groups> gl) {
        this.context = context;
        this.group = group;
        this.friendsList = fl;
        this.gl = gl;
    }

    @Override
    public int getGroupCount() {
        return group.length;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return friendsList.size() + gl.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return group[groupPosition];
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return friendsList.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        convertView = LayoutInflater.from(context).inflate(R.layout.nothing, null);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolder vh;
        convertView = LayoutInflater.from(context).inflate(R.layout.chat_item, null);
        vh = new ViewHolder();
        vh.tv = (TextView) convertView.findViewById(R.id.chat_item_tv);
        vh.iv = (ImageView) convertView.findViewById(R.id.chat_item_iv);
        vh.bt = (Button) convertView.findViewById(R.id.chat_button);
        int temp = friendsList.size();
        if (childPosition < temp) {
            vh.tv.setText(friendsList.get(childPosition).getName());
            vh.iv.setImageResource(R.mipmap.ic_launcher);
            vh.bt.setTag(friendsList.get(childPosition));
            vh.bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Friends f = (Friends) view.getTag();
                    start_chat(f);
                }
            });
        } else {
            vh.tv.setText(gl.get(childPosition - temp).getGroupname());
            vh.iv.setImageResource(R.mipmap.ic_launcher);
            vh.bt.setTag(gl.get(childPosition - temp));
            vh.bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Groups g = (Groups) view.getTag();
                    Log.d("11", g.getGroupname() + "groupname");
                    start_chat(g);
                }
            });
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private void start_chat(Groups g) {
        Intent intent = new Intent(context, ChatActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("name", g.getGroupname());
        Log.d("11", g.getGroupname());
        bundle.putString("group", "1");
        bundle.putString("id", g.getGroupsid());
        Log.d("11", g.getGroupsid());
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    private void start_chat(Friends f) {
        Intent intent = new Intent(context, ChatActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("name", f.getAddress());
        Log.d("5", f.getAddress());
        bundle.putString("group", "0");
        bundle.putString("id", f.getSpecialid());
        Log.d("5", f.getSpecialid());
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    class ViewHolder {
        TextView tv;
        ImageView iv;
        Button bt;
    }
}