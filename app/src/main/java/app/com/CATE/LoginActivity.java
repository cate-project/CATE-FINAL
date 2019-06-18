package app.com.CATE;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import app.com.youtubeapiv3.R;

public class LoginActivity extends AppCompatActivity {

    Button BtnSignUp;
    String finalresult;
    public static Context mContext;
    public static EditText idText;
    public static EditText passwordText;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext = this;


        //강의에서 final을 추가시켜줌
        idText = (EditText) findViewById(R.id.idText);
        passwordText = (EditText) findViewById(R.id.passwordText);
        final Button loginbtn = (Button) findViewById(R.id.loginbtn);
        BtnSignUp = (Button) findViewById(R.id.btn_signup);

        BtnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(LoginActivity.this, SignupPage.class);
                LoginActivity.this.startActivity(registerIntent);
                finish();
            }
        });

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new UserofPcroom().execute();
            }
        });

    }

    public class UserofPcroom extends AsyncTask<Void, Void, String> {
        String target;

        @Override
        protected void onPreExecute() {
            //List.php은 파싱으로 가져올 웹페이지
            target = "https://catapro.000webhostapp.com/fow_tag.php";
        }

        @Override
        protected String doInBackground(Void... params) {

            try {

                URL url = new URL(target);//URL 객체 생성

                //URL을 이용해서 웹페이지에 연결하는 부분
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);

                OutputStreamWriter wr = new OutputStreamWriter(httpURLConnection.getOutputStream());


                //바이트단위 입력스트림 생성 소스는 httpURLConnection
                InputStream inputStream = httpURLConnection.getInputStream();

                //웹페이지 출력물을 버퍼로 받음 버퍼로 하면 속도가 더 빨라짐
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String temp;

                //문자열 처리를 더 빠르게 하기 위해 StringBuilder클래스를 사용함
                StringBuilder stringBuilder = new StringBuilder();


                //한줄씩 읽어서 stringBuilder에 저장함
                while ((temp = bufferedReader.readLine()) != null) {
                    stringBuilder.append(temp + "\n");//stringBuilder에 넣어줌
                }

                //사용했던 것도 다 닫아줌
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();//trim은 앞뒤의 공백을 제거함

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(final String result) {

            Response.Listener<String> responseListener = new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    try {

                        JSONObject jsonResponse = new JSONObject(response);

                        boolean success = jsonResponse.getBoolean("success");


                        //서버에서 보내준 값이 true이면?
                        if (success) {

                            Toast.makeText(getApplicationContext(), "로그인에 성공하셨습니다.", Toast.LENGTH_SHORT).show();

                            //Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();

                            String userID = jsonResponse.getString("userID");
                            String userName = jsonResponse.getString("userName");


                            //로그인에 성공했으므로 MenuPage로 넘어감
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("userID", userID);
                            intent.putExtra("userName", userName);
                            intent.putExtra("Category", result);

                            LoginActivity.this.startActivity(intent);
                            finish();

                        } else {//로그인 실패시

                            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                            builder.setMessage("로그인에 실패하셨습니다.")
                                    .setNegativeButton("retry", null)
                                    .create()
                                    .show();


                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };

            LoginRequest loginRequest = new LoginRequest(idText.getText().toString(), passwordText.getText().toString(), responseListener);
            RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
            queue.add(loginRequest);
        }


    }
}