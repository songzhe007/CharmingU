package zhe.charmu.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import zhe.charmu.R;
import zhe.charmu.authentication.LoginActivity;
import zhe.charmu.authentication.RegisterActivity;

/**
 * Fragment Class for Authentication Buttons: Register and Log In.
 */

public class AuthenticationFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_authenticate, container, false);

        // handle buttons
        View register = rootView.findViewById(R.id.button_register);
        View logIn = rootView.findViewById(R.id.button_logIn);

        // set on click event
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        return rootView;
    }
}
