package VMail.Activity;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import VMail.Entity.settings;
import VMail.Fragment.ChatFragment;
import VMail.Fragment.ChatFragment_copy;
import VMail.Fragment.ChatPopup;
import VMail.Fragment.ContactFragment;
import VMail.Fragment.ContactFragment_copy;
import VMail.Fragment.MeFragment;
import VMail.R;


public class MainActivity extends FragmentActivity {

    public static Handler h;
    public int location;
    private TextView actionbar_title;
    private FragmentManager fm = null;
    private FragmentTransaction ft = null;
    private ChatFragment cf = null;
    private ChatFragment_copy cfc = null;
    private ContactFragment conf = null;
    private ContactFragment_copy confc = null;
    private MeFragment mef = null;
    private ChatPopup cp = null;
    private OnClickListener paramOnClickListener = null;
    private TextView mBtnAdd = null;
    private BottomNavigationView navigation;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            ft = fm.beginTransaction();
            location = item.getItemId();
            switch (item.getItemId()) {
                case R.id.navigation_chats:
                    if (cf == null) {
                        cf = new ChatFragment();
                    }
                    ft.replace(R.id.fragment_content, cf);
                    actionbar_title.setText(R.string.title_chats);
                    break;
                case R.id.navigation_contacts:
                    if (conf == null) {
                        conf = new ContactFragment();
                    }
                    ft.replace(R.id.fragment_content, conf);
                    actionbar_title.setText(R.string.title_contacts);
                    break;
                case R.id.navigation_me:
                    if (mef == null) {
                        mef = new MeFragment();
                    }
                    ft.replace(R.id.fragment_content, mef);
                    actionbar_title.setText(R.string.title_me);
                    break;
                default:
                    return false;
            }

            ft.commit();
            return true;
        }
    };

    public void setPopupWindow() {
        if (cp == null) {

            cp = new ChatPopup(MainActivity.this, paramOnClickListener);

            cp.getContentView().setOnFocusChangeListener(new View.OnFocusChangeListener() {

                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        cp.dismiss();
                    }
                }
            });
        }
        cp.setFocusable(true);
        cp.showAsDropDown(mBtnAdd, 0, 0);
        cp.update();
    }

    public void init() {

        ActionBar actionBar = getActionBar();
        actionBar.setCustomView(R.layout.actionbar);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        paramOnClickListener = new OnClickListener();

        TextView mBtnBack = (TextView) findViewById(R.id.back_button);
        mBtnBack.setVisibility(View.GONE);

        mBtnAdd = (TextView) findViewById(R.id.details);
        mBtnAdd.setOnClickListener(paramOnClickListener);

        Drawable drawable = getResources().getDrawable(R.drawable.ic_add_white_24dp);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        mBtnAdd.setCompoundDrawables(drawable, null, null, null);

        actionbar_title = (TextView) findViewById(R.id.title);
        actionbar_title.setText(R.string.title_chats);

        location = R.id.navigation_chats;

        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        if (cf == null) {
            cf = new ChatFragment();
        }
        ft.replace(R.id.fragment_content, cf);
        ft.commit();
        actionbar_title.setText(R.string.title_chats);

        navigation = (BottomNavigationView) findViewById(R.id.navigation);

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }

    public void init_handler() {
        h = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0:
                        break;
                    case 6:
                        Log.d("6", "receive");
                        refresh();
                        break;
                    default:
                        break;
                }
            }
        };
    }

    @Override
    public void onPause() {
        super.onPause();
        h = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        h = null;
        settings.stop = true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        settings.context = this;
        init();
        init_handler();
    }

    public void refresh() {
        ft = fm.beginTransaction();
        switch (location) {
            case R.id.navigation_chats:
                if (cfc == null) {
                    cfc = new ChatFragment_copy();
                    ft.replace(R.id.fragment_content, cfc);
                    cf = null;
                } else {
                    cf = new ChatFragment();
                    ft.replace(R.id.fragment_content, cf);
                    cfc = null;
                }
                break;
            case R.id.navigation_contacts:
                if (confc == null) {
                    confc = new ContactFragment_copy();
                    ft.replace(R.id.fragment_content, confc);
                    conf = null;
                } else {
                    conf = new ContactFragment();
                    ft.replace(R.id.fragment_content, conf);
                    confc = null;
                }
                break;
            case R.id.navigation_me:
                actionbar_title.setText(R.string.title_me);
                break;
            default:
        }
        ft.commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        settings.context = this;
        init_handler();
        refresh();
    }


    public class OnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.details:
                    setPopupWindow();
                    break;
                case R.id.add_contact:
                    Intent intent1 = new Intent();
                    intent1.setClass(MainActivity.this, AddContactActivity.class);
                    startActivity(intent1);
                    break;
                case R.id.new_group:
                    Intent intent2 = new Intent();
                    intent2.setClass(MainActivity.this, NewGroupActivity.class);
                    startActivity(intent2);
                    break;
                default:
                    break;
            }

        }

    }
}
