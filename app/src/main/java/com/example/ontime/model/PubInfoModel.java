package com.example.ontime.model;

public class PubInfoModel {
    private static PubInfoModel pubInfoModel;

    int firstPubType;

    int firstPubNo;

    int busStation;

    int subWayCode;






    public synchronized static PubInfoModel getInstance(){
        if(pubInfoModel == null){
            pubInfoModel = new PubInfoModel();
        }
        return pubInfoModel;
    }


    public int getFirstPubType() {
        return firstPubType;
    }

    public synchronized void setFirstPubType(int firstPubType) {
        this.firstPubType = firstPubType;
    }

    public int getFirstPubNo() {
        return firstPubNo;
    }

    public synchronized void setFirstPubNo(int firstPubNo) {
        this.firstPubNo = firstPubNo;
    }

    public int getBusStation() {
        return busStation;
    }

    public synchronized void setBusStation(int busStation) {
        this.busStation = busStation;
    }

    public int getSubWayCode() {
        return subWayCode;
    }

    public synchronized void setSubWayCode(int subWayCode) {
        this.subWayCode = subWayCode;
    }
}
