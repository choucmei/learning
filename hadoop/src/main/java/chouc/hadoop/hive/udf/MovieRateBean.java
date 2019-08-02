package chouc.hadoop.hive.udf;


// {"movie":"1193","rate":"5","timeStamp":"978300760","uid":"1"}
public class MovieRateBean {
    private String movie;
    private String rate;
    private String timeStamp;
    private String uid;


    public void set(String movie, String rate, String timeStamp, String uid) {
        this.movie = movie;
        this.rate = rate;
        this.timeStamp = timeStamp;
        this.uid = uid;
    }

    public String getMovie() {
        return movie;
    }

    public void setMovie(String movie) {
        this.movie = movie;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }


    @Override
    public String toString() {
        return this.movie+","+this.rate+","+this.timeStamp+","+this.uid;
    }

}
