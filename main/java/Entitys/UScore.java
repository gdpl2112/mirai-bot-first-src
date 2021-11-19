package Entitys;

public class UScore {
    private Number score=1000;
    private Number times=0;
    private Number fz=0;
    private Number times_=0;
    private Number days=0;
    private Number score_=200;
    private Number k=-1;
    private Number day=-1;
    private Number timesDay=-1;
    private Number who;

    public Long getScore() {
        return this.score.longValue();
    }

    public UScore setScore(Number score) {
        this.score = score;
        return this;
    }

    public Number getTimes() {
        return this.times.intValue();
    }

    public UScore setTimes(Number times) {
        this.times = times;
        return this;
    }

    public int getFz() {
        return this.fz.intValue();
    }

    public UScore setFz(Number fz) {
        this.fz = fz;
        return this;
    }

    public long getTimes_() {
        return this.times_.longValue();
    }

    public UScore setTimes_(Number times_) {
        this.times_ = times_.intValue();
        return this;
    }

    public Number getDays() {
        return this.days.intValue();
    }

    public UScore setDays(Number days) {
        this.days = days;
        return this;
    }

    public Long getScore_() {
        return this.score_.longValue();
    }

    public UScore setScore_(Number score_) {
        this.score_ = score_;
        return this;
    }

    public long getK() {
        return this.k.longValue();
    }

    public UScore setK(Number k) {
        this.k = k;
        return this;
    }

    public int getDay() {
        return this.day.intValue();
    }

    public UScore setDay(Number day) {
        this.day = day;
        return this;
    }

    public Number getTimesDay() {
        return this.timesDay.intValue();
    }

    public UScore setTimesDay(Number timesDay) {
        this.timesDay = timesDay;
        return this;
    }

    public Number getWho() {
        return this.who.longValue();
    }

    public UScore setWho(Number who) {
        this.who = who;
        return this;
    }
}