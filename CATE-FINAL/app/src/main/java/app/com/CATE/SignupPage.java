package app.com.CATE;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import app.com.youtubeapiv3.R;

public class SignupPage extends Activity {
    private EditText editTextId;
    private EditText editTextPw1,editTextPw2,editTextName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        TrustManager[] dummyTrustManager = new TrustManager[] { new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        } };

        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, dummyTrustManager, new java.security.SecureRandom());

            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }


        editTextId = (EditText) findViewById(R.id.new_id);
        editTextPw1 = (EditText) findViewById(R.id.new_pw1);
        editTextPw2 = (EditText) findViewById(R.id.new_pw2);
        editTextName = (EditText) findViewById(R.id.new_Name);
    }
    public void insert(View view) {
        String Id = editTextId.getText().toString();
        String Pw1 = editTextPw1.getText().toString();
        String Pw2 = editTextPw2.getText().toString();
        String Name = editTextName.getText().toString();
        if(Pw1.equalsIgnoreCase(Pw2)) {
            insertoToDatabase(Id, Pw1,Name);
        }
        else{
            Toast.makeText(getApplicationContext(), "비번을 일치시켜라.", Toast.LENGTH_LONG).show();
        }
    }
    private void insertoToDatabase(String Id, String Pw,String Name) {
        class InsertData extends AsyncTask<String, Void, String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(SignupPage.this, "Please Wait", null, true, true);
            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
               // Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                if(s.equalsIgnoreCase("회원가입에 성공하셨습니다.")){
                    Intent intent = new Intent(SignupPage.this, LoginActivity.class);
                    startActivity(intent);
                    finish();

                }
            }
            @Override
            protected String doInBackground(String... params) {

                try {
                    String Id = (String) params[0];
                    String Pw = (String) params[1];
                    String Name = (String) params[2];

                    String link = "https://catapro.000webhostapp.com/fow_post.php";
                            String data = URLEncoder.encode("Id", "UTF-8") + "=" + URLEncoder.encode(Id, "UTF-8");
                    data += "&" + URLEncoder.encode("Pw", "UTF-8") + "=" + URLEncoder.encode(Pw, "UTF-8");
                    data += "&" + URLEncoder.encode("Name", "UTF-8") + "=" + URLEncoder.encode(Name, "UTF-8");

                    URL url = new URL(link);
                    URLConnection conn = url.openConnection();

                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                    wr.write(data);
                    wr.flush();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    // Read Server Response
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                        break;
                    }
                    return sb.toString();
                } catch (Exception e) {
                    return new String("Exception: " + e.getMessage());
                }
            }
        }
        InsertData task = new InsertData();
        task.execute(Id, Pw, Name);
    }
}
