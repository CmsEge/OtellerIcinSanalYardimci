package com.example.melik.service;
import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import ai.api.AIDataService;
import ai.api.AIServiceException;
import ai.api.android.AIConfiguration;
import ai.api.model.AIRequest;
import ai.api.model.AIResponse;
import ai.api.model.Result;


public class Service {
    private  final AIConfiguration config = new AIConfiguration("ecd717ee86524b2e977ca6e4483c7346",
            AIConfiguration.SupportedLanguages.English,
            AIConfiguration.RecognitionEngine.System);
    private Boolean success;

    private Button listenButton;
    private TextView resultTextView ;
    private EditText queryText;

    public Service(Button listenButton,EditText queryText, TextView resultTextView){
        this.setListenButton(listenButton);
        this.setResultTextView(resultTextView);
        this.setQueryText(queryText);

    }
    public void setListenButton(Button listenButton) { this.listenButton = listenButton; }
    public void setQueryText(EditText queryText) { this.queryText = queryText; }
    public void setResultTextView(TextView resultTextView){ this.resultTextView=resultTextView; }


    @SuppressLint("StaticFieldLeak")
    public void StartChat(){
        String data;
        if (!queryText.getText().toString().isEmpty()) {

            final AIDataService aiDataService = new AIDataService(config);
            data = queryText.getText().toString();
            resultTextView.append("You: " + data + "\n");
            final AIRequest aiRequest = new AIRequest();
            aiRequest.setQuery(data);

            new AsyncTask<AIRequest, Void, AIResponse>() {

                @SuppressLint("WrongThread")
                @Override
                protected AIResponse doInBackground(AIRequest... requests) {
                    final AIRequest request = requests[0];
                    try {
                        final AIResponse response = aiDataService.request(aiRequest);
                        return response;
                    } catch (AIServiceException e) {
                    }
                    return null;
                }
                @Override
                protected void onPostExecute(AIResponse aiResponse) {

                    if (!aiResponse.toString().isEmpty()) {

                        if (aiResponse.getResult().getAction().equals("dinner-time")) {
                                String speech;
                                speech = aiResponse.getResult().getFulfillment().getSpeech();
                                speech = speech.replace("dinnerTimeStart","5");
                                speech = speech.replace("dinnerTimeFinish","10");
                                aiResponse.getResult().getFulfillment().setSpeech(speech);
                                Log.i("Bilgi",aiResponse.getResult().getFulfillment().getSpeech());
                        }

                        Result result = aiResponse.getResult();
                        String parameterString = result.getFulfillment().getSpeech();
                        resultTextView.append("Chatbot: " + parameterString + "\n");
                    }
                }
            }.execute(aiRequest);
        }
        queryText.setText("");
    }


    @SuppressLint("StaticFieldLeak")
    public void SyncData(){//veri tabanı bağlantısı

        new AsyncTask<String, String, ResultSet>() {
            @Override
            protected ResultSet doInBackground(String... strings) {
                try{
                    Class.forName("com.mysql.jdbc.Driver");
                    //burada sıkıntı var sorunu anlamıyorum
                    String url = "jdbc:mysql//localhost/CMS"; //192 li yere kendi ipv4 adresini yaz
                    //veri tabanında bir veri tabanı(schema) oluştur oluşturduğunun ismini ogrenci_schema yerine yaz, gerisine dokunma
                    Connection c = DriverManager.getConnection(url,"root","Saskin*9");
                    //kendi username ve şifren mysqldeki

                    //if(c == null){//JAVADA == KÖTÜ BİR ŞEYDİ DİYE HATIRLIYORUM NESNELERİ DENKLİĞE GÖRE KARŞILAŞTIRIYORDU SANKİ EŞİTLİĞE GÖRE DEĞİL.BURADA PROBLEM OLURSA AKLINIZDA BULUNSUN.
                    if(c.toString().isEmpty()){
                        success=false;
                    }else{
                        String query = "SELECT customerId,name,surName,idNo,roomNo,phoneNo FROM Customer";
                        Statement stmt = c.createStatement();
                        ResultSet rs = stmt.executeQuery(query);
                        rs.next();
                        Log.i("hoppili:",rs.getNString(1));//çalışmadı.

                        //


                        /*if(rs != null){//null direk olmuyor sanırım ya 107. satırda yazdığım gibi.Ben tostring yapıp sonra isempty yapıyorum genelde.
                            // Log.i(rs.getString("first_name"),rs.getString("last_name"));
                            success = true;
                            Log.i("success ","true rs null değil");
                            return rs;
                        }else{
                            success = false;
                            Log.i("success ","false rs null");
                            return null;
                        }*/
                    }

                } catch (ClassNotFoundException | SQLException e) {
                    e.printStackTrace();
                    Log.i("success ","false class forname de çaktı");
                    return null;
                }

                return null;
            }

            @Override
            protected void onPostExecute(ResultSet resultSet) {//doInBackgrounddan sonra onpost yapılıp sanki burada çektiğimiz sonuçla ilgili şeyler yapıcaz.
                super.onPostExecute(resultSet);
                Log.i("Sonuç:",resultSet.toString());
            }
        }.execute("");
    }
}
