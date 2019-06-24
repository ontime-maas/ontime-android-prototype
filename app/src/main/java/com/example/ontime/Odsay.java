package com.example.ontime;

import android.content.Context;

import com.example.ontime.model.PubInfoModel;
import com.example.ontime.model.TimeModel;
import com.odsay.odsayandroidsdk.API;
import com.odsay.odsayandroidsdk.ODsayData;
import com.odsay.odsayandroidsdk.ODsayService;
import com.odsay.odsayandroidsdk.OnResultCallbackListener;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
public class Odsay extends Thread{
    Context context;
    ODsayService oDsayService;

    int firstPub;
    int firstPubCode;
    int busStation;
    String busIntervalTime;

    public Odsay(Context context) {
        this.context = context;
        this.oDsayService = ODsayService.init(context, String.valueOf("여기에 API 키를 넣어주세요"));
        oDsayService.setReadTimeout(5000);
        oDsayService.setReadTimeout(5000);
    }

    OnResultCallbackListener onResultCallbackListener = new OnResultCallbackListener() {

        @Override
        public void onSuccess(ODsayData oDsayData, API api) {
            try{
                System.out.println("API 번호 : "+api);
                if(api == API.SEARCH_PUB_TRANS_PATH){
                    int transTime = (int) oDsayData.getJson()
                            .getJSONObject("result").getJSONArray("path")
                            .getJSONObject(0).getJSONObject("info")
                            .get("totalTime");
                    JSONArray firstPubSubpath = oDsayData.getJson()
                            .getJSONObject("result").getJSONArray("path")
                            .getJSONObject(0).getJSONArray("subPath");
                    System.out.println("목적지까지의 subPath 길이 : "+firstPubSubpath.length());

                    // 첫번째 탈 대중교통 구하기
                    // 1-지하철, 2-버스, 3-도보
                    for(int i =0 ; i<firstPubSubpath.length(); i++){ // subPath 의 길이만큼 반복
                        if(firstPubSubpath.getJSONObject(i).getInt("trafficType") != 3){

                            firstPub = firstPubSubpath.getJSONObject(i).getInt("trafficType");
                            if(firstPub == 1){ // 첫번째 탈 대중교통이 지하철이면 그 노선번호를 구한다.
                                firstPubCode = firstPubSubpath.getJSONObject(i).getJSONArray("lane").getJSONObject(0).getInt("subwayCode");

                                ((MainActivity)context).toastMessage("현재 지하철을 타고가는 경로는 검색이 되지 않습니다.");
                            }
                            else if(firstPub == 2){ // 첫번째 탈 대중교통이 버스면 그 노선번호를 구한다.
                                // 첫번째로탈 버스정류장의 고유 ID
                                busStation = firstPubSubpath.getJSONObject(i).getJSONObject("passStopList").getJSONArray("stations").getJSONObject(0).getInt("stationID");
                                // 버스의 노선번호
                                firstPubCode = Integer.parseInt(firstPubSubpath.getJSONObject(i).getJSONArray("lane").getJSONObject(0).getString("busNo"));
                            }
                            break;
                        }
                    }

                    PubInfoModel pubInfoModel = PubInfoModel.getInstance();
                    pubInfoModel.setFirstPubType(firstPub);
                    pubInfoModel.setFirstPubNo(firstPubCode);

                    TimeModel timeModel = TimeModel.getInstance();
                    timeModel.setTransTime(transTime);

                    System.out.println("길찾기 성공!");
                    System.out.println("총 걸리는 시간 : "+transTime);
                    if(firstPub == 1){
                        System.out.println("첫번째 탈 대중교통은 지하철");
                    }
                    else if(firstPub == 2){
                        pubInfoModel.setBusStation(busStation);

                        System.out.println("첫번째 탈 대중교통은 버스입니다");
                    }
                    System.out.println("첫번째 탈 대중교통의 노선 : "+pubInfoModel.getFirstPubNo());
                    System.out.println("첫번째 탈 대중교통의 정류장 고유 ID : "+pubInfoModel.getBusStation());

                    if(firstPub == 1){ // 지하철

                    }
                    else if(firstPub == 2){ // 버스
                        // 배차시간 구하기
                        requestSearchBusStationInfo();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(int i, String s, API api) {
            System.out.println("에러!!");

            System.out.println("코드 " + i);
            System.out.println("메세지 " + s);
            System.out.println("API " + api);
        }
    };

    OnResultCallbackListener onSearchBusStationInfoCallback = new OnResultCallbackListener() {
        @Override
        public void onSuccess(ODsayData oDsayData, API api) {
            System.out.println("API 번호 : "+api);
            if(api == API.BUS_STATION_INFO){
                System.out.println("버스 배차 조회 성공!!");
                PubInfoModel pubInfoModel = PubInfoModel.getInstance();
                try {
                    JSONArray busLanes = oDsayData.getJson().
                            getJSONObject("result").getJSONArray("lane");
                    for(int i =0 ; i<busLanes.length(); i++){
                        if(busLanes.getJSONObject(i).getString("busNo").equals(Integer.toString(pubInfoModel.getFirstPubNo()))) {
                            busIntervalTime = busLanes.getJSONObject(i).getString("busInterval");
                            System.out.println("첫번째 탈 대중교통의 평균 배차시각 : "+busIntervalTime);
                            break;
                        }
                    }

                    TimeModel timeModel = TimeModel.getInstance();
                    timeModel.setBusIntervalTime(Integer.parseInt(busIntervalTime));

                    // UI 업데이트
                    ((MainActivity)context).setTextTotalTime();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onError(int i, String s, API api) {
            System.out.println("에러!!");

            System.out.println("코드 " + i);
            System.out.println("메세지 " + s);
            System.out.println("API " + api);
        }
    };

    public void requestBusStationInfo(){
        this.oDsayService.requestBusStationInfo("107475",onResultCallbackListener);
    }
    public void requestPubTransPath(String sx , String sy , String ex , String ey){
        this.oDsayService.requestSearchPubTransPath(sx,sy,ex,ey,"0","0","0",onResultCallbackListener);
        System.out.println("requestPubTransPath 끝!!!");
    }

    public void requestSearchBusStationInfo(){
        PubInfoModel pubInfoModel = PubInfoModel.getInstance();
        this.oDsayService.requestBusStationInfo(Integer.toString(pubInfoModel.getBusStation()),onSearchBusStationInfoCallback);
    }


}
