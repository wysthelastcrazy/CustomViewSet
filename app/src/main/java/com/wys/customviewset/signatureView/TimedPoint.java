package com.wys.customviewset.signatureView;

/**
 * Created by yas on 2018/7/19.
 */

public class TimedPoint {
    public final float x;
    public final float y;
    public final long timestamp;

    public TimedPoint(float x,float y){
        this.x=x;
        this.y=y;
        this.timestamp=System.currentTimeMillis();
    }

    public float velocityFrom(TimedPoint start){
        float velocity=distanceTo(start)/(this.timestamp-start.timestamp);
        if (velocity!=velocity)
            return 0f;
        return velocity;
    }

    private float distanceTo(TimedPoint point) {
        return (float) Math.sqrt(Math.pow(point.x-this.x,2)+Math.pow(point.y-this.y,2));
    }
}
