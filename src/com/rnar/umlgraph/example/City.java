package com.rnar.umlgraph.example;

import com.rnar.umlgraph.labels.LabelSource;

/**
 *
 * @author r.naryshkin99
 */
public class City {
    private String name;
    private int population;

    public City(String name, int age) {
        this.name = name;
        this.population = age;
    }

    @LabelSource
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    
    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    @Override
    public String toString() {
        return "City{" + "name=" + name + ", population=" + population + '}';
    }
   
    
}
