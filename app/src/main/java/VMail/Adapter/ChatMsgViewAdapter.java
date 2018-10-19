package VMail.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.mail.internet.InternetAddress;

import VMail.Activity.ChatActivity;
import VMail.Activity.DisplayActivity;
import VMail.Entity.ChatMsgEntity;
import VMail.Entity.Friends;
import VMail.Mail.Transmitor;
import VMail.Management.Management;
import VMail.Management.MediaFile;
import VMail.R;


public class ChatMsgViewAdapter extends BaseAdapter {

    private static final String TAG = ChatMsgViewAdapter.class.getSimpleName();
    private List<ChatMsgEntity> coll;
    private Context ctx;
    private LayoutInflater mInflater;
    private MediaPlayer mMediaPlayer = new MediaPlayer();

    public ChatMsgViewAdapter(Context context, List<ChatMsgEntity> coll) {
        ctx = context;
        this.coll = coll;
        mInflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return coll.size();
    }

    public Object getItem(int position) {
        return coll.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public int getItemViewType(int position) {

        ChatMsgEntity entity = coll.get(position);

        if (entity.isFriend()) {
            return IMsgViewType.IMVT_COM_MSG;
        } else {
            return IMsgViewType.IMVT_TO_MSG;
        }

    }

    public int getViewTypeCount() {

        return 2;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final ChatMsgEntity entity = coll.get(position);
        boolean isOutMsg = entity.isOutMsg();

        final ViewHolder viewHolder;
        if (isOutMsg) {
            convertView = mInflater.inflate(R.layout.message_item_right, null);
        } else {
            convertView = mInflater.inflate(R.layout.message_item_left, null);
        }
        viewHolder = new ViewHolder();
        viewHolder.tvSendTime = (TextView) convertView.findViewById(R.id.tv_sendtime);
        viewHolder.tvUserName = (TextView) convertView.findViewById(R.id.tv_username);
        viewHolder.tvContent = (TextView) convertView.findViewById(R.id.tv_chatcontent);

        viewHolder.isComMsg = isOutMsg;

        final LinearLayout hidden = (LinearLayout) convertView.findViewById(R.id.hidden);

        viewHolder.tvContent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (hidden.getVisibility() != View.GONE) {
                    hidden.setVisibility(View.GONE);
                }
            }
        });
        viewHolder.tvContent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                hidden.setVisibility(View.VISIBLE);
                Button comment = (Button) hidden.findViewById(R.id.comment);
                final Button like = (Button) hidden.findViewById(R.id.like);
                comment.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        hidden.setVisibility(View.GONE);
                        ChatActivity.setComment("Reply " + (String) viewHolder.tvUserName.getText() + " \"" + (String) viewHolder.tvContent.getText() + "\": ");
                    }
                });
                if (coll.get(position).getNum_like() > 0) {
                    like.setText("" + coll.get(position).getNum_like());
                }
                if (!coll.get(position).isLikedByMe()) {
                    like.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            like(coll.get(position));
                            like.setClickable(false);
                            coll.get(position).setLikedByMe(true);
                            coll.get(position).setNum_like(coll.get(position).getNum_like() + 1);
                            Drawable d = ctx.getResources().getDrawable(R.drawable.ic_favorite_red_24dp);
                            like.setBackground(d);
                            like.setText("" + coll.get(position).getNum_like());
                        }
                    });
                } else {
                    Drawable d = ctx.getResources().getDrawable(R.drawable.ic_favorite_red_24dp);
                    like.setBackground(d);
                }
                return true;
            }
        });

        viewHolder.tvSendTime.setText(entity.getDate());

        if (entity.getText() != null) {
            viewHolder.tvContent.setText(entity.getText());
            viewHolder.tvContent.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

        } else {
            Drawable d;
            viewHolder.tvContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (hidden.getVisibility() != View.GONE) {
                        hidden.setVisibility(View.GONE);
                    } else {
                        if (MediaFile.isVideoFileType(entity.getPath())) {
                            Intent intent = new Intent(ctx, DisplayActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("type", "video");
                            bundle.putString("path", entity.getPath());
                            intent.putExtras(bundle);
                            ctx.startActivity(intent);
                        } else if (MediaFile.isImageFileType(entity.getPath())) {
                            Intent intent = new Intent(ctx, DisplayActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("type", "image");
                            bundle.putString("path", entity.getPath());
                            intent.putExtras(bundle);
                            ctx.startActivity(intent);
                        }
                    }
                }
            });
            Log.d("26", entity.getPath());
            try {
                int x = 0;
                int y = 0;
                if (MediaFile.isImageFileType(entity.getPath())) {
                    Bitmap b = BitmapFactory.decodeFile(entity.getPath());
                    if (b.getHeight() > b.getWidth()) {
                        b = ThumbnailUtils.extractThumbnail(b, b.getWidth() * 800 / b.getHeight(), 800);
                    } else {
                        b = ThumbnailUtils.extractThumbnail(b, 500, b.getHeight() * 500 / b.getWidth());
                    }
                    d = new BitmapDrawable(b);
                    viewHolder.tvContent.setBackground(d);
                } else {
                    d = ctx.getResources().getDrawable(R.drawable.ic_play_circle_outline_black_24dp);
                    Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(entity.getPath(), MediaStore.Video.Thumbnails.MICRO_KIND);
                    Drawable drawable = new BitmapDrawable(bitmap);
                    viewHolder.tvContent.setBackground(drawable);
                    x = bitmap.getWidth() / 2 - d.getMinimumWidth() / 2;
                    y = bitmap.getHeight() / 2 - d.getMinimumHeight() / 2;
                    if (x < 0) x = 0;
                    if (y < 0) y = 0;
                    //drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                    d.setBounds(x, y, d.getMinimumWidth(), d.getMinimumHeight());
                    viewHolder.tvContent.setCompoundDrawables(d, null, null, null);
                }

            } catch (Exception e) {
                Log.d("26", e + "");
            }
        }
        viewHolder.tvUserName.setText(entity.getName());
        System.gc();
        return convertView;
    }

    public void like(final ChatMsgEntity e) {
        if (Management.likedByMe(e)) {
            ArrayList<String> address = new ArrayList<String>();
            final InternetAddress[] addresses;
            if (e.isFriend()) {
                addresses = new InternetAddress[1];
                try {
                    addresses[0] = new InternetAddress(Management.getAddressById(e.getFrom_id()));
                } catch (Exception exc) {
                    Log.d("40", "1");
                }
            } else {
                address = Management.getAllUnblockAddressInGroup(e.getFrom_id());
                addresses = new InternetAddress[address.size()];
                try {
                    for (int i = 0; i < address.size(); i++) {
                        addresses[i] = new InternetAddress(address.get(i));
                    }
                } catch (Exception exc) {
                    Log.d("40", "2");
                }
            }
            if (e.getText() != null) {
                try {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if(e.isFriend()){
                                    Transmitor.TxTransmitor(5, "true" + "---!!!!---" + e.getText() + "---!!!!---" + e.getName()+"---!!!!---"+ DataSupport.findFirst(Friends.class).getSpecialid(), e.getFrom_id(), addresses);
                                }else{
                                    Transmitor.TxTransmitor(5, "true" + "---!!!!---" + e.getText() + "---!!!!---" + e.getName()+"---!!!!---"+e.getFrom_id(), e.getFrom_id(), addresses);
                                }
                                } catch (Exception exc) {
                                Log.d("40", "" + exc);
                            }
                        }
                    }).start();
                } catch (Exception exc) {
                    Log.d("40", "" + exc);
                }
            } else {
                try {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String path[] = e.getPath().split(File.separator);
                            Log.d("40", "true" + "---!!!!---" + path[path.length - 1] + "---!!!!---" + e.getName());
                            try {
                                if(e.isFriend()) {
                                    Transmitor.TxTransmitor(5, "false" + "---!!!!---" + path[path.length - 1] + "---!!!!---" + e.getName()+"---!!!!---"+ DataSupport.findFirst(Friends.class).getSpecialid(), e.getFrom_id(), addresses);
                                } else{
                                    Transmitor.TxTransmitor(5, "false" + "---!!!!---" + path[path.length - 1] + "---!!!!---" + e.getName()+"---!!!!---"+e.getFrom_id(), e.getFrom_id(), addresses);
                                }
                            } catch (Exception exc) {
                            }
                        }
                    }).start();
                } catch (Exception exc) {
                    Log.d("40", "4");
                }
            }
        }
    }

    public static interface IMsgViewType {
        int IMVT_COM_MSG = 0;
        int IMVT_TO_MSG = 1;
    }

    static class ViewHolder {
        public TextView tvSendTime;
        public TextView tvUserName;
        public TextView tvContent;

        public boolean isComMsg = true;
    }
}