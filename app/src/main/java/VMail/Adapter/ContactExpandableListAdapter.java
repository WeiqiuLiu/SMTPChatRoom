package VMail.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import VMail.R;


public class ContactExpandableListAdapter extends BaseExpandableListAdapter {
    Context context;
    String[] group;
    String[][] itemName;

    public ContactExpandableListAdapter(Context context, String[] group, String[][] itemName) {
        this.context = context;
        this.group = group;
        this.itemName = itemName;
    }

    @Override
    public int getGroupCount() {
        return group.length;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return itemName[groupPosition].length;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return group[groupPosition];
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return itemName[groupPosition][childPosition];
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
        ViewHolder vh;

        if (groupPosition == 0) {
            convertView = LayoutInflater.from(context).inflate(R.layout.contact_predefined, null);
        } else {
            convertView = LayoutInflater.from(context).inflate(R.layout.expandable_list_class, null);
            vh = new ViewHolder();
            vh.tv = (TextView) convertView.findViewById(R.id.class_name);
            convertView.setTag(vh);

            vh = (ViewHolder) convertView.getTag();

            vh.tv.setText(group[groupPosition]);
        }

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolder vh;

        if (groupPosition == 0) {
            convertView = LayoutInflater.from(context).inflate(R.layout.contact_predefined, null);
        } else {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.contact_item, null);
                vh = new ViewHolder();
                vh.tv = (TextView) convertView.findViewById(R.id.contact_item_tv);
                vh.iv = (ImageView) convertView.findViewById(R.id.contact_item_iv);
                convertView.setTag(vh);
            }
            vh = (ViewHolder) convertView.getTag();
            vh.tv.setText(itemName[groupPosition][childPosition]);
            vh.iv.setImageResource(R.mipmap.ic_launcher);
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    class ViewHolder {
        TextView tv;
        ImageView iv;
    }
}