package VMail.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import VMail.R;

import java.util.ArrayList;


public class NewGroupExpandableListAdapter extends BaseExpandableListAdapter {
    Context context;
    String[] group;
    String[][] itemName;
    boolean[] isSelected;
    private ArrayList<String> group_memebr = new ArrayList<String>();

    public NewGroupExpandableListAdapter(Context context, String[] group, String[][] itemName, boolean[] isSelected) {
        this.context = context;
        this.group = group;
        this.itemName = itemName;
        this.isSelected = isSelected;
        for (int i = 0; i < isSelected.length; i++) {
            if (isSelected[i]) {
                group_memebr.add(itemName[0][i]);
            }
        }
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
        convertView = LayoutInflater.from(context).inflate(R.layout.nothing, null);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolder vh;


        convertView = LayoutInflater.from(context).inflate(R.layout.group_member_item, null);
        vh = new ViewHolder();
        vh.tv1 = (TextView) convertView.findViewById(R.id.name);
        vh.iv = (ImageView) convertView.findViewById(R.id.profile);
        vh.bt = (Button) convertView.findViewById(R.id.background_button);
        vh.cb = (CheckBox) convertView.findViewById(R.id.checkBox);

        vh.tv1.setText(itemName[groupPosition][childPosition]);
        vh.iv.setImageResource(R.mipmap.ic_launcher);
        vh.location = childPosition;

        vh.bt.setTag(vh);
        vh.bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewHolder vh = (ViewHolder) view.getTag();
                vh.cb.setChecked(!vh.cb.isChecked());
            }
        });
        vh.cb.setChecked(isSelected[childPosition]);

        vh.cb.setTag(vh);

        vh.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                ViewHolder vh = (ViewHolder) buttonView.getTag();
                group_memebr.clear();
                for (int i = 0; i < isSelected.length; i++) {
                    if (isSelected[i]) {
                        group_memebr.add(itemName[0][i]);
                    }
                }
                if (isChecked) {
                    group_memebr.add(itemName[0][vh.location]);
                    isSelected[vh.location] = true;
                } else {
                    group_memebr.remove(itemName[0][vh.location]);
                    isSelected[vh.location] = false;
                }
            }
        });

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public ArrayList<String> getList() {
        return group_memebr;
    }


    class ViewHolder {
        TextView tv1;
        CheckBox cb;
        ImageView iv;
        Button bt;
        int location;
    }

}