package com.example.ontime;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.ontime.model.AddressModel;
import com.example.ontime.model.TimeModel;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*
* TODO
* 알람 구현
* */
public class MainActivity extends AppCompatActivity  {
    TextView txt_totalTime , txt_alramTime , txt_promiseTime ,txt_startTime;
    EditText edit_start , edit_end , edit_ready;
    Button btn_start_apply , btn_end_apply , btn_ready_apply,
            btn_arriveTime , btn_setAlram, btn_cancleAlram , btn_promiseTime;
    Context context;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    int ready_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.context = this;

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        final Intent my_intent = new Intent(this.context, Alarm_Receiver.class);

        txt_totalTime = (TextView)findViewById(R.id.txt_totalTime);
        txt_alramTime = (TextView)findViewById(R.id.txt_alramTime);
        txt_promiseTime = (TextView)findViewById(R.id.txt_promiseTime);
        txt_startTime =(TextView)findViewById(R.id.txt_startTime);

        edit_start = (EditText)findViewById(R.id.edit_start);
        edit_end = (EditText)findViewById(R.id.edit_end);
        edit_ready = (EditText)findViewById(R.id.edit_ready);


        btn_start_apply = (Button)findViewById(R.id.btn_start_apply);
        btn_end_apply = (Button)findViewById(R.id.btn_end_apply);
        btn_ready_apply = (Button)findViewById(R.id.btn_ready_apply);
        btn_arriveTime = (Button)findViewById(R.id.btn_arriveTime);
        btn_setAlram = (Button)findViewById(R.id.btn_setAlram);
        btn_cancleAlram = (Button)findViewById(R.id.btn_cancleAlram);
        btn_promiseTime =  (Button)findViewById(R.id.btn_promiseTime);

        final Odsay odsay = new Odsay(this);

        // 조회한 주소데이터를 담을 싱글턴
        final AddressModel addressModel = AddressModel.getInstance();

        // 이동시간을 담을 싱글턴
        final TimeModel timeModel = TimeModel.getInstance();

        // 약속시간 정하기
        btn_promiseTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TimePickerFragment mTimePickerFragment = new TimePickerFragment();
                mTimePickerFragment.show(getSupportFragmentManager() , "test");
            }
        });

        // 출발지 검색
        btn_start_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String start_address = null;
                // 주소형태로 입력받는다
                start_address = edit_start.getText().toString();
                if(start_address.isEmpty()){
                    Toast.makeText(getApplicationContext(), "주소를 입력하세요.", Toast.LENGTH_SHORT).show();
                }else {
                    //odsay.requestBusStationInfo();

                    // 주소 잘못입력했을시 앱 튕김 예외처리 필요
                    final Call<AddressRepo> res = NetRetrofit.getInstance().getDaumKakaoService().getLocalAddress(start_address);
                    res.enqueue(new Callback<AddressRepo>() {
                        @Override
                        public void onResponse(Call<AddressRepo> call, Response<AddressRepo> response) {
                            AddressRepo addressRepo = response.body();
                            System.out.println("검색한 주소 : " + addressRepo.getDocuments().get(0).getAddress_name());
                            System.out.println("검색한 x 좌표 : " + addressRepo.getDocuments().get(0).getX());
                            System.out.println("검색한 y 좌표 : " + addressRepo.getDocuments().get(0).getY());

                            addressModel.setSaddress(addressRepo.getDocuments().get(0).getAddress_name());
                            addressModel.setSx(addressRepo.getDocuments().get(0).getX());
                            addressModel.setSy(addressRepo.getDocuments().get(0).getY());

                            Toast.makeText(getApplicationContext(), "출발지 등록 완료!", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<AddressRepo> call, Throwable t) {
                            System.out.println("실패 : " + t.getMessage());
                        }
                    });
                }
            }
        });

        // 도착지 검색
        btn_end_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String end_address = null;
                end_address = edit_end.getText().toString();
                if(end_address.isEmpty()){
                    Toast.makeText(getApplicationContext(), "주소를 입력하세요.", Toast.LENGTH_SHORT).show();
                }else {
                    // 주소 잘못입력했을시 앱 튕김 예외처리 필요
                    final Call<AddressRepo> res = NetRetrofit.getInstance().getDaumKakaoService().getLocalAddress(end_address); // 위도 경도 찾아줌
                    res.enqueue(new Callback<AddressRepo>() {
                        @Override
                        public void onResponse(Call<AddressRepo> call, Response<AddressRepo> response) {
                            AddressRepo addressRepo = response.body();
                            System.out.println("검색한 주소 : " + addressRepo.getDocuments().get(0).getAddress_name());
                            System.out.println("검색한 x 좌표 : " + addressRepo.getDocuments().get(0).getX());
                            System.out.println("검색한 y 좌표 : " + addressRepo.getDocuments().get(0).getY());

                            addressModel.setEaddress(addressRepo.getDocuments().get(0).getAddress_name());
                            addressModel.setEx(addressRepo.getDocuments().get(0).getX());
                            addressModel.setEy(addressRepo.getDocuments().get(0).getY());

                            Toast.makeText(getApplicationContext(), "도착지 등록 완료!", Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onFailure(Call<AddressRepo> call, Throwable t) {
                            System.out.println("실패 : " + t.getMessage());
                        }
                    });
                }
            }
        });

        // 준비시간 등록
        btn_ready_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(edit_ready.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "준비시간을 입력하세요.", Toast.LENGTH_SHORT).show();
                }else {
                    ready_time = Integer.parseInt(edit_ready.getText().toString());
                    TimeModel timeModel = TimeModel.getInstance();
                    timeModel.setReadyTime(ready_time);

                    Toast.makeText(getApplicationContext(), "준비시간 등록 완료!", Toast.LENGTH_SHORT).show();

                }
            }
        });

        // 예상 도착시간 계산하기 버튼
        btn_arriveTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "온타임이 예상 도착시간을 계산중입니다...!", Toast.LENGTH_SHORT).show();

                System.out.println("길찾기 버튼 호출!");

                System.out.println(addressModel.getSx());
                System.out.println(addressModel.getSy());

                System.out.println(addressModel.getEx());
                System.out.println(addressModel.getEy());

                if(addressModel.getSx() == null || addressModel.getSy() == null || addressModel.getEx() == null || addressModel.getEy() == null){
                    Toast.makeText(getApplicationContext(), "먼저 출발지와 도착지를 등록해주세요!", Toast.LENGTH_SHORT).show();
                    return ;
                }

                TimeModel timeModel = TimeModel.getInstance();

                int promiseTime_hour = timeModel.getPromiseTime_hour();
                int promiseTime_min = timeModel.getPromiseTime_min();

                System.out.println("길찾기 시 약속시간 시 : "+promiseTime_hour);
                System.out.println("길찾기 시 약속시간 분: "+promiseTime_min);

                if(promiseTime_hour == 0  && promiseTime_min == 0 ){
                    Toast.makeText(getApplicationContext(), "약속 시간을 등록해주세요!", Toast.LENGTH_SHORT).show();
                    return ;
                }

                // 길찾기
                odsay.requestPubTransPath(addressModel.getSx(),addressModel.getSy(),addressModel.getEx(),addressModel.getEy());
            }
        });

        btn_setAlram.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                TimeModel timeModel = TimeModel.getInstance();

                int transTime = timeModel.getTransTime();
                int readyTime = timeModel.getReadyTime();
                int intervalTime = timeModel.getBusIntervalTime();

                int totalTime = transTime+readyTime+intervalTime;

                int sec = totalTime / 10;
                Toast.makeText(MainActivity.this,"Alarm 예정 " + sec+"초" ,Toast.LENGTH_SHORT).show();

                my_intent.putExtra("state","alarm on");

                pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, my_intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

                alarmManager.set(AlarmManager.RTC_WAKEUP, sec,
                        pendingIntent);
            }
        });

        btn_cancleAlram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this,"알람 종료",Toast.LENGTH_SHORT).show();
                // 알람매니저 취소
                alarmManager.cancel(pendingIntent);

                my_intent.putExtra("state","alarm off");

                // 알람취소
                sendBroadcast(my_intent);
            }
        });
    }

    public void setTextTotalTime() throws ParseException {
        TimeModel timeModel = TimeModel.getInstance();

        int transTime = timeModel.getTransTime();
        int readyTime = timeModel.getReadyTime();
        int intervalTime = timeModel.getBusIntervalTime();

        int promiseTime_hour = timeModel.getPromiseTime_hour();
        int promiseTime_min = timeModel.getPromiseTime_min();

        int totalTime = transTime+readyTime+intervalTime;

        txt_promiseTime.setText("당신의 약속시간은 내일 " + promiseTime_hour + " 시" + promiseTime_min +" 분 까지네요!");
        txt_totalTime.setText("출발지부터 목적지까지의 \n총 예상 소요시간은 " + (transTime+intervalTime) +  " 분 입니다");

        System.out.println("총 이동시간 : "+totalTime);

        System.out.println("버스의 배차시간 : "+timeModel.getBusIntervalTime()); // 길찾기에 이용되는 첫번째 대중교통의 배차시간
        System.out.println("가는 길 시간 : "+transTime);
        //System.out.println("여유시간 : " + Constants.FREE_TIME);
        System.out.println("준비시간 : " + ready_time);

        // promiseTime = 10 시 는 36000 초 . (10 * 3600)
        //

        SimpleDateFormat hhmmf = new SimpleDateFormat("HH:mm" , Locale.KOREA);

        Date d1 = hhmmf.parse(promiseTime_hour+":"+promiseTime_min);

        Date d2 = hhmmf.parse("00"+":"+(transTime+intervalTime));

        System.out.println("d1 : "+d1);
        System.out.println("d2 : "+d2);

        long diff = d1.getTime() - d2.getTime();

        System.out.println("diff : "+diff);

        System.out.println("시 : "+diff/3600000);
        System.out.println("분 : "+diff/60000);
        System.out.println("초 : "+diff/1000);

        long needMinutes = diff/60000;
        long hour = TimeUnit.MINUTES.toHours(needMinutes); //
        long minutes = TimeUnit.MINUTES.toMinutes(needMinutes) - TimeUnit.HOURS.toMinutes(hour);


        System.out.println("시 : "+hour);
        System.out.println("분 : "+minutes);


        if(promiseTime_min >= (transTime+intervalTime)){
            txt_startTime.setText("당신이 출발해야 하는 시간은 약 " + promiseTime_hour + "시" + (promiseTime_min -(transTime+intervalTime))+"분 입니다." );
        }
        else{
            txt_startTime.setText("당신이 출발해야 하는 시간은 약 " + hour + "시" +minutes+"분 입니다.");
        }

        System.out.println("계산된 시간 : " + totalTime / 60);
        System.out.println("계산된 분 : "+ totalTime  % 60);

//        txt_alramTime.setText("약속시간으로 부터 " + totalTime +"분 전에 깨워드릴게요!");
        txt_alramTime.setText("\n약속시간으로 부터 " + totalTime / 60 +"시간 "+totalTime % 60+"분 전에 깨워드릴게요!");

        txt_promiseTime = (TextView)findViewById(R.id.txt_promiseTime);
        txt_startTime =(TextView)findViewById(R.id.txt_startTime);
    }

    TimePickerDialog.OnTimeSetListener onStartTimeListener = new TimePickerDialog.OnTimeSetListener() {


        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            System.out.println("리스너 작동 !!!");
            String AM_PM ;
            if(hourOfDay < 12) {
                AM_PM = "AM";
            } else {
                AM_PM = "PM";
            }

            //mStartTime.setText(hourOfDay + " : " + minute + " " + AM_PM );
        }
    };

    public void toastMessage(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
