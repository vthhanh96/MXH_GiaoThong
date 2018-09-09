package com.khoaluan.mxhgiaothong.activities.login;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

import com.khoaluan.mxhgiaothong.R;
import com.khoaluan.mxhgiaothong.customView.dialog.ErrorMessageDialogFragment;
import com.khoaluan.mxhgiaothong.restful.ApiManager;
import com.khoaluan.mxhgiaothong.restful.RestCallback;
import com.khoaluan.mxhgiaothong.restful.RestError;
import com.khoaluan.mxhgiaothong.restful.request.LoginUseRequest;
import com.khoaluan.mxhgiaothong.restful.response.BaseResponse;
import com.khoaluan.mxhgiaothong.utils.AppUtils;


public class RegisterStep7Fragment extends Fragment {
    private View view;
    private ViewPager _mViewPager;
    private MultiAutoCompleteTextView mactvCharacter;
    private MultiAutoCompleteTextView mactvStyle;
    private MultiAutoCompleteTextView mactvFood;
    private Button btnNextStep;
    private LinearLayout llRoot;

    public static Fragment newInstance() {
        return new RegisterStep7Fragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_register_step7, container, false);
        init();
        llRootTouch();
        setActvHobby();
        clickButtonNextStep();
        return view;
    }

    private void init() {
        _mViewPager = (ViewPager) getActivity().findViewById(R.id.activity_register_viewPager);
        mactvCharacter = (MultiAutoCompleteTextView) view.findViewById(R.id.fragment_register_step7_actv_character);
        mactvCharacter.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        mactvStyle = (MultiAutoCompleteTextView) view.findViewById(R.id.fragment_register_step7_actv_style);
        mactvStyle.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        mactvFood = (MultiAutoCompleteTextView) view.findViewById(R.id.fragment_register_step7_actv_food);
        mactvFood.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        btnNextStep = (Button) view.findViewById(R.id.fragment_register_step7_btn_register);
        llRoot = (LinearLayout) view.findViewById(R.id.fragment_register_step7_ll_root);
    }

    private void setActvHobby() {
        ArrayAdapter hobbyAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.character));
        mactvCharacter.setAdapter(hobbyAdapter);
        mactvCharacter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mactvCharacter.getText().toString().endsWith(",")) {
                    mactvCharacter.setText(mactvCharacter.getText().toString().trim() + ",");
                }
                mactvCharacter.setSelection(mactvCharacter.getText().toString().length());
            }
        });
        hobbyAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.style));
        mactvStyle.setAdapter(hobbyAdapter);
        mactvStyle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mactvStyle.getText().toString().endsWith(",")) {
                    mactvStyle.setText(mactvStyle.getText().toString().trim() + ",");
                }
                mactvStyle.setSelection(mactvStyle.getText().toString().length());
            }
        });
        hobbyAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.food));
        mactvFood.setAdapter(hobbyAdapter);
        mactvFood.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mactvFood.getText().toString().endsWith(",")) {
                    mactvFood.setText(mactvFood.getText().toString().trim() + ",");
                }
                mactvFood.setSelection(mactvFood.getText().toString().length());
            }
        });
    }

    /*Hide softkeyboard when touch outsite edittext*/
    private void llRootTouch() {
        llRoot.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                AppUtils.hideSoftKeyboard(getActivity());
                return true;
            }
        });
    }

    private void clickButtonNextStep() {
        btnNextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stringCharacter = AppUtils.notRepeatElementInString(mactvCharacter.getText().toString().trim());
                String stringStyle = AppUtils.notRepeatElementInString(mactvStyle.getText().toString().trim());
                String stringFood = AppUtils.notRepeatElementInString(mactvFood.getText().toString().trim());
                if (stringCharacter.trim().endsWith(",")) {
                    mactvCharacter.setText(stringCharacter.trim().substring(0, stringCharacter.lastIndexOf(",")));
                }

                if (stringStyle.trim().endsWith(",")) {
                    mactvStyle.setText(stringStyle.substring(0, stringStyle.trim().lastIndexOf(",")));
                }

                if (stringFood.trim().endsWith(",")) {
                    mactvFood.setText(stringFood.substring(0, stringFood.trim().lastIndexOf(",")));
                }
                RegisterStep1Fragment.user.setTargetCharacter(mactvCharacter.getText().toString());
                RegisterStep1Fragment.user.setTargetStyle(mactvStyle.getText().toString());
                RegisterStep1Fragment.user.setTargetFood(mactvFood.getText().toString());
                InsertUserIntoDB();
            }
        });
    }

    private void InsertUserIntoDB() {
        if (!AppUtils.isNetworkAvailable(getContext())) {
            Toast.makeText(getContext(), getResources().getString(R.string.invalid_network), Toast.LENGTH_SHORT).show();
        } else {
            ApiManager.getInstance().getUserService().register(new LoginUseRequest(
                    RegisterStep1Fragment.user.getEmail(),
                    RegisterStep1Fragment.user.getPassword(),
                    RegisterStep1Fragment.user.getFullName(),
                    RegisterStep1Fragment.user.getAvatar(),
                    RegisterStep1Fragment.user.getAddress(),
                    RegisterStep1Fragment.user.getGender(),
                    RegisterStep1Fragment.user.getBirthday(),
                    RegisterStep1Fragment.user.getPhone(),
                    RegisterStep1Fragment.user.getLatlngAdress(),
                    RegisterStep1Fragment.user.getMyCharacter(),
                    RegisterStep1Fragment.user.getMyStyle(),
                    RegisterStep1Fragment.user.getTargetCharacter(),
                    RegisterStep1Fragment.user.getTargetStyle(),
                    RegisterStep1Fragment.user.getTargetFood()
            )).enqueue(new RestCallback<BaseResponse>() {
                @Override
                public void success(BaseResponse res) {
                    Toast.makeText(getActivity(), "Đăng kí thành công", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void failure(RestError error) {
                    ErrorMessageDialogFragment errorDialog = new ErrorMessageDialogFragment();

                    errorDialog.setError(error.message);
                    errorDialog.show(getFragmentManager(), RegisterActivity.class.getName());
                }
            });

        }
    }
}
