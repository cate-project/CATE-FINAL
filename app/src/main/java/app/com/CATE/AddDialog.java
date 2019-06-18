package app.com.CATE;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import app.com.youtubeapiv3.R;

public class AddDialog extends Dialog implements View.OnClickListener{
    private static final int LAYOUT = R.layout.dialog_add;

    private Context context;

    private TextInputEditText titleEt;
    private TextInputEditText urlEt;
    private TextInputEditText tagEt;
    private String kind_video;
    private Button twitchRb;
    private Button youtubeRb;
    private Button confirmBt;

    private String name;

    public AddDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    public AddDialog(Context context,String name){
        super(context);
        this.context = context;
        this.name = name;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);

        titleEt = (TextInputEditText) findViewById(R.id.title_input);
        urlEt = (TextInputEditText) findViewById(R.id.url_input);
        tagEt = (TextInputEditText) findViewById(R.id.category_input);

        youtubeRb = (Button) findViewById(R.id.radioButton_youtube);
        twitchRb = (Button) findViewById(R.id.radioButton_twitch);
        confirmBt = (Button) findViewById(R.id.button_confirm);

        youtubeRb.setOnClickListener(this);
        twitchRb.setOnClickListener(this);
        confirmBt.setOnClickListener(this);

        if(name != null){
            titleEt.setText(name);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_confirm:
              //  Toast.makeText(context,kind_video,Toast.LENGTH_SHORT).show();
//                Response.Listener<String> responseListener = new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            JSONObject jsonResponse = new JSONObject(response);
//                            boolean success = jsonResponse.getBoolean("success");
//
//
//                            //서버에서 보내준 값이 true이면?
//                            if (success) {
//
//                                Toast.makeText(context, "비디오 추가하셨습니다.", Toast.LENGTH_SHORT).show();
//
//                                //Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();
//
//                                String userMoney = jsonResponse.getString("userMoney");
//
//
//
//
//                            } else {//충전 실패시
//                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                                builder.setMessage("추가에 실패하셨습니다.")
//                                        .setNegativeButton("retry", null)
//                                        .create()
//                                        .show();
//
//
//                            }
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                };
//                ChargeRequest ChargeRequest = new ChargeRequest(strId, chargeMoney, responseListener);
//                RequestQueue queue = Volley.newRequestQueue(ChargeActivity.this);
//                queue.add(ChargeRequest);

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");


                            //서버에서 보내준 값이 true이면?
                            if (success) {

                                Toast.makeText(context, "비디오 추가하셨습니다.", Toast.LENGTH_SHORT).show();

                                //Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();

                                String userMoney = jsonResponse.getString("userMoney");




                            } else {//충전 실패시
                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                builder.setMessage("추가에 실패하셨습니다.")
                                        .setNegativeButton("retry", null)
                                        .create()
                                        .show();


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                VideoChugaRequest VideoChugaRequest = new VideoChugaRequest(titleEt.getText().toString(), urlEt.getText().toString(),
                        tagEt.getText().toString(),kind_video,MainActivity.AAA, responseListener);
                RequestQueue queue = Volley.newRequestQueue(context);
                queue.add(VideoChugaRequest);
                cancel();
                break;
            case R.id.radioButton_youtube :
                kind_video = "YOUTUBE";
              //  Toast.makeText(context,kind_video,Toast.LENGTH_SHORT).show();
                break;
            case R.id.radioButton_twitch :
                kind_video = "TWITCH";
               // Toast.makeText(context,kind_video,Toast.LENGTH_SHORT).show();
                break;
        }
    }
}