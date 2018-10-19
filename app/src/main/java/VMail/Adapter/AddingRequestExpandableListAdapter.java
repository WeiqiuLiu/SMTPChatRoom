package VMail.Adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import VMail.Entity.Friends;
import VMail.Entity.settings;
import VMail.Mail.Transmitor;
import VMail.Management.Management;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;

import javax.mail.internet.InternetAddress;

import VMail.R;


public class AddingRequestExpandableListAdapter extends BaseExpandableListAdapter {
    Context context;
    String[] group;
    private ArrayList<String> addresslist = new ArrayList<String>();
    private ArrayList<String> idlist = new ArrayList<String>();
    private ArrayList<Boolean> confirm_list = new ArrayList<Boolean>();
    private ArrayList<Boolean> isFriend_list = new ArrayList<Boolean>();


    public AddingRequestExpandableListAdapter(Context context, String[] group, ArrayList<String> addresslist, ArrayList<String> idlist, ArrayList<Boolean> confirm_list, ArrayList<Boolean> isFriend_list) {
        this.context = context;
        this.group = group;
        this.addresslist = addresslist;
        this.idlist = idlist;
        this.isFriend_list = isFriend_list;
        this.confirm_list = confirm_list;
    }

    @Override
    public int getGroupCount() {
        return group.length;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return addresslist.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return group[groupPosition];
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return addresslist.get(childPosition);
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
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolder vh;


        convertView = LayoutInflater.from(context).inflate(R.layout.adding_request_item, null);
        vh = new ViewHolder();
        vh.tv1 = (TextView) convertView.findViewById(R.id.name);
        vh.tv2 = (TextView) convertView.findViewById(R.id.hello);
        vh.iv = (ImageView) convertView.findViewById(R.id.profile);
        vh.bt = (Button) convertView.findViewById(R.id.accept_button);

        vh.tv1.setText(addresslist.get(childPosition).split(";")[0]);
        if (isFriend_list.get(childPosition)) {
            vh.tv2.setText("request to add you as friend");
        } else {
            vh.tv2.setText("invite you to group");
        }

        vh.iv.setImageResource(R.mipmap.ic_launcher);

        if (!confirm_list.get(childPosition)) {
            vh.location = childPosition;
            vh.bt.setTag(vh);
            vh.bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ViewHolder vh = (ViewHolder) view.getTag();
                    String address = addresslist.get(childPosition);
                    confirm(address, idlist.get(childPosition), isFriend_list.get(childPosition));
                    Log.d("7", address);
                    vh.bt.setBackgroundColor(context.getResources().getColor(R.color.transparent));
                    vh.bt.setTextColor(context.getResources().getColor(R.color.grey));
                    vh.bt.setText(R.string.Added);
                    vh.bt.setClickable(false);
                    confirm_list.set(vh.location, true);
                }
            });
        } else {
            vh.bt.setBackgroundColor(context.getResources().getColor(R.color.transparent));
            vh.bt.setTextColor(context.getResources().getColor(R.color.grey));
            vh.bt.setText(R.string.Added);
            vh.bt.setClickable(false);
        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private void confirm(String address, String id, boolean isFriend) {
        Log.d("7", "" + address);
        String[] a = address.split(";");
        SendAckTask s = new SendAckTask(id, a, isFriend);
        s.execute((Void) null);
    }

    class ViewHolder {
        TextView tv1;
        TextView tv2;
        ImageView iv;
        Button bt;
        int location;
    }

    public class SendAckTask extends AsyncTask<Void, Void, Boolean> {

        private String id;
        private String[] address;
        private boolean isFriend;
        private String a = "";

        SendAckTask(String id, String[] address, boolean isFriend) {
            this.id = id;
            this.address = address;
            this.isFriend = isFriend;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            InternetAddress[] ia;

            ia = new InternetAddress[address.length];

            for (int i = 0; i < ia.length; i++) {
                try {
                    a += address[i] + ";";
                    ia[i] = new InternetAddress(address[i]);
                } catch (Exception e) {
                }
            }
            try {
                Log.d("9", isFriend + "");
                if (isFriend) {
                    return Transmitor.TxTransmitor(6, "hello (Ack)", DataSupport.findFirst(Friends.class).getSpecialid(), ia);
                } else {
                    Log.d("9", "" + address.length + "  length ");
                    return Transmitor.TxTransmitor(7, "hello (Ack) " + id + address[address.length - 1], id, ia);
                }
            } catch (Exception e) {
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            if (success) {
                if (isFriend) {
                    Management.addnewfriends(address[0], address[0], id);
                    Management.update_req(address[0], id);
                } else {
                    Management.addnewgroupmember(address[address.length - 1], address[0], address[0], id, true);
                    Log.d("20", "group id" + id);
                    Management.addnewgroupmember(address[address.length - 1], settings.username, settings.username, id, false);
                    Management.update_req(a, id);
                }
            }
        }

        @Override
        protected void onCancelled() {


        }
    }

}