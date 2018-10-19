package VMail.Fragment;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import VMail.Activity.MainActivity;
import VMail.R;


public class ChatPopup extends PopupWindow {
    private View mainView;
    private LinearLayout add_contact, new_group;

    public ChatPopup(Activity paramActivity, MainActivity.OnClickListener paramOnClickListener) {
        super(paramActivity);
        mainView = LayoutInflater.from(paramActivity).inflate(R.layout.popup_chat, null);
        add_contact = ((LinearLayout) mainView.findViewById(R.id.add_contact));
        new_group = (LinearLayout) mainView.findViewById(R.id.new_group);
        if (paramOnClickListener != null) {
            add_contact.setOnClickListener(paramOnClickListener);
            new_group.setOnClickListener(paramOnClickListener);
        }
        setContentView(mainView);
        setBackgroundDrawable(new ColorDrawable(0));
    }
}