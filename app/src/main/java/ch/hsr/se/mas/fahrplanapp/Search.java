package ch.hsr.se.mas.fahrplanapp;

/**
 * Created by Alexandra on 31.01.2017.
 */

public class Search {

    private String fromStation;
    private String toStation;
    private String date;
    private String time;
    private boolean isArrivalTime;

    public String getFromStation() {
        return fromStation;
    }

    public void setFromStation(String fromStation) {
        this.fromStation = fromStation;
    }

    public String getToStation() {
        return toStation;
    }

    public void setToStation(String toStation) {
        this.toStation = toStation;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isArrivalTime() {
        return isArrivalTime;
    }

    public void setArrivalTime(boolean arrivalTime) {
        isArrivalTime = arrivalTime;
    }
}
