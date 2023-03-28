package com.rnar.umlgraph.example;

import com.rnar.umlgraph.labels.LabelSource;

/**
 *
 * @author r.naryshkin99
 */
public class Distance {
    private int distance;

    public Distance(int distance) {
        this.distance = distance;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }
    
    @LabelSource
    public String getDisplayDistance() {
        /* If the above annotation is not present, the toString()
        will be used as the edge label. */
        
        return distance + " km";
    }

    @Override
    public String toString() {
        return "Distance{" + "distance=" + distance + '}';
    }

    

    
    
}
