package com.example.loginregister.ui.home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.security.PublicKey;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class Growschedule {

    public int [] Time;
    public int [] Interval;

    public int [] LighTime;
    public int [] LightInterval;
    private String name;
    private int numbWeeks;

    public Growschedule(String _name, int[] _time, int[] _interval, int _numbWeeks, int[] _LightTime, int[] _LightInterval){
        //aantal weken dat de plant moet groeien
        this.numbWeeks = _numbWeeks;
        //times array aanmaken en verolgens instellen
        Time = new int[_numbWeeks];
        this.Time = _time;
        //interval array aanmaken en verolgens instellen
        Interval = new int[_numbWeeks];
        this.Interval = _interval;

        //set licht
        LighTime = new int[_numbWeeks];
        this.LighTime = _LightTime;
        LightInterval = new int[numbWeeks];
        this.LightInterval = _LightInterval;
        //naam van de plant die we gaan groeien
        this.name = _name;

    }

    public String GetName(){
        return name;
    }
    public String SetName(){
       return this.name = name;
    }


}
