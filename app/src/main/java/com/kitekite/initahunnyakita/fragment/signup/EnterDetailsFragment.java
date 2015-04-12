package com.kitekite.initahunnyakita.fragment.signup;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.grantlandchew.view.VerticalPager;
import com.kitekite.initahunnyakita.R;
import com.kitekite.initahunnyakita.activities.MainActivity;
import com.kitekite.initahunnyakita.util.Global;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

/**
 * Created by florianhidayat on 9/4/15.
 */
public class EnterDetailsFragment extends Fragment {
    private final static String SIGN_UP_API_ENDPOINT_URL = "http://molaja-backend.herokuapp.com/api/v1/registrations.json";
    View fragmentView;
    EditText usernameForm;
    EditText nameForm;
    EditText emailForm;
    EditText passwordForm;
    EditText passwordConfirmationForm;
    Button signUpBtn;

    //booleans for validations
    boolean isUsernameValid = false;
    boolean isNameValid = false;
    boolean isEmailValid = false;
    boolean isPasswordValid = false;
    boolean isPassConfirmValid = false;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_enter_details, null);
        bindViews();
        return fragmentView;
    }

    private void bindViews() {
        usernameForm =(EditText) fragmentView.findViewById(R.id.username_form);
        nameForm =(EditText) fragmentView.findViewById(R.id.name_form);
        emailForm =(EditText) fragmentView.findViewById(R.id.email_form);
        passwordForm =(EditText) fragmentView.findViewById(R.id.password_form);
        passwordConfirmationForm =(EditText) fragmentView.findViewById(R.id.password_confirmation_form);
        signUpBtn = (Button) fragmentView.findViewById(R.id.signup_btn);

        addTextWatchers();

        signUpBtn.setEnabled(false);

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doLogin();
            }
        });
    }

    private void addTextWatchers() {
        usernameForm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(usernameForm.getText().toString().length() > 3)
                    isUsernameValid = true;
                else
                    isUsernameValid = false;

                updateSignUpButton();
            }
        });

        nameForm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(nameForm.getText().toString().length() > 3)
                    isNameValid = true;
                else
                    isNameValid = false;

                updateSignUpButton();
            }
        });

        emailForm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(emailForm.getText().toString().matches("\\A[\\w+\\-.]+@[a-z\\d\\-.]+\\.[a-z]+\\z"))
                    isEmailValid = true;
                else
                    isEmailValid = false;
                updateSignUpButton();
            }
        });

        passwordForm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(passwordForm.getText().toString().length() > 5)
                    isPasswordValid = true;
                else
                    isPasswordValid = false;

                updateSignUpButton();
            }
        });

        passwordConfirmationForm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(passwordConfirmationForm.getText().toString().equals(passwordForm.getText().toString()))
                    isPassConfirmValid = true;
                else
                    isPassConfirmValid = false;

                updateSignUpButton();
            }
        });
    }

    private void updateSignUpButton() {
        boolean valid = isUsernameValid && isNameValid && isEmailValid && isPasswordValid && isPassConfirmValid;

        signUpBtn.setEnabled(valid);
    }

    private void doLogin() {
        JsonObject json = new JsonObject();
        JsonObject user = new JsonObject();
        user.addProperty("username", usernameForm.getText().toString());
        user.addProperty("name", nameForm.getText().toString());
        user.addProperty("email", emailForm.getText().toString());
        user.addProperty("password", passwordForm.getText().toString());
        user.addProperty("password_confirmation", passwordConfirmationForm.getText().toString());
        json.add("user",user);

        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(false);
        pDialog.setMessage(getString(R.string.logging_you_in));
        pDialog.show();

        Ion.with(getActivity())
                .load(SIGN_UP_API_ENDPOINT_URL)
                .noCache()
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        pDialog.dismiss();

                        if(e != null) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), getString(R.string.login_failed), Toast.LENGTH_SHORT).show();
                        } else {
                            boolean success = Boolean.parseBoolean(result.get("success").getAsString());
                            if (success) {
                                SharedPreferences.Editor editor = getActivity().getSharedPreferences(Global.login_cookies, 0).edit();
                                editor.putBoolean(Global.is_logged_in, true);
                                editor.putString(Global.username, result.getAsJsonObject("data").getAsJsonObject("user").get("username").getAsString());
                                editor.putString(Global.name, result.getAsJsonObject("data").getAsJsonObject("user").get("name").getAsString());
                                editor.putString(Global.token, result.getAsJsonObject("data").getAsJsonObject("user").get("authentication_token").getAsString());
                                editor.commit();
                                //getActivity().getIntent().
                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                getActivity().finish();
                            } else {
                                Toast.makeText(getActivity(), result.getAsJsonObject("info").toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    /*private boolean validate() {
        boolean usernameValid = usernameForm.getText().toString().length() > 3;
        if (!usernameValid) {
            Toast.makeText(getActivity(), "")
        }
        validation &= nameForm.getText().toString().length() > 3;
        validation &= emailForm.getText().toString().equals("/\\A[\\w+\\-.]+@[a-z\\d\\-]+(\\.[a-z\\d\\-]+)*\\.[a-z]+\\z/i");
        validation &= passwordForm.getText().toString().length() > 5;
        validation &= passwordConfirmationForm.getText().toString().equals(passwordForm.getText().toString());

        return true;
    }*/

}
