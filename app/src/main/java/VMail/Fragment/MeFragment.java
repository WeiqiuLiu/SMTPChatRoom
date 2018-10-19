package VMail.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import VMail.Entity.settings;
import VMail.Management.Management;
import VMail.R;


public class MeFragment extends Fragment implements View.OnClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        Button cleallall = view.findViewById(R.id.clearall);
        cleallall.setOnClickListener(this);
        Button backup = view.findViewById(R.id.backup);
        backup.setVisibility(View.GONE);
        backup.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.clearall:
                Management.clearall();
                System.exit(0);
                break;
            case R.id.back_button:
                break;
            default:
                break;
        }
    }
}
