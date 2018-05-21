package com.example.sindhu.alzheimerscaregiver;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;


@DynamoDBTable(tableName = "User_Location")
public class UserLocationDO {

    private String _id;
    private String _anomalyscore;
    private String _datetime;
    private String _latitude;
    private String _longitude;
    private String _patientusername;

    @DynamoDBAttribute(attributeName = "ID")
    public String getID(){
        return _id;
    }

    public void setID(String _id){
        this._id=_id;
    }

    @DynamoDBAttribute(attributeName = "AnomalyScore")
    public String getAnomalyScore(){
        return _anomalyscore;
    }

    public void setAnomalyScore(String _anomalyscore){
        this._anomalyscore=_anomalyscore;
    }

    @DynamoDBAttribute(attributeName = "DateTime")
    public String getDateTime(){
        return _datetime;
    }

    public void setDateTime(String _datetime){
        this._datetime=_datetime;
    }

    @DynamoDBAttribute(attributeName = "Latitude")
    public String getLatitude(){
        return _latitude;
    }

    public void setLatitude(String _latitude){
        this._latitude=_latitude;
    }

    @DynamoDBAttribute(attributeName = "Longitude")
    public String getLongitude(){
        return _longitude;
    }

    public void setLongitude(String _longitude){
        this._longitude=_longitude;
    }

    @DynamoDBAttribute(attributeName = "Patient_UserName")
    public String getUsername(){
        return _patientusername;
    }

    public void setUsername(String _patientusername){
        this._patientusername=_patientusername;
    }


}
