package notification.avishkar.com.pushnotification;

/**
 * Created by Prateeksha Singh on 11/4/2017.
 */

public class AdvisorEvent {

    String name, location, date,  time;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void settitle(String name) {
        this.name = name;
    }

    public void setsubtitle(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    @Override
    public String toString() {
        return "AdvisorEvent{" +
                "name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", date='" + date + '\'' +
                ", time=" + time +
                '}';
    }
}
