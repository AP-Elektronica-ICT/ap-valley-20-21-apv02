package com.example.loginregister.ui.home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class Growbox {

    private String growing;
    private String name;
    private String url;
   // private double[] waterusage ;
    private List<Double> waterusage = new List<Double>() {
        @Override
        public int size() {
            return 0;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public boolean contains(@Nullable Object o) {
            return false;
        }

        @NonNull
        @Override
        public Iterator<Double> iterator() {
            return null;
        }

        @NonNull
        @Override
        public Object[] toArray() {
            return new Object[0];
        }

        @NonNull
        @Override
        public <T> T[] toArray(@NonNull T[] a) {
            return null;
        }

        @Override
        public boolean add(Double aDouble) {
            return false;
        }

        @Override
        public boolean remove(@Nullable Object o) {
            return false;
        }

        @Override
        public boolean containsAll(@NonNull Collection<?> c) {
            return false;
        }

        @Override
        public boolean addAll(@NonNull Collection<? extends Double> c) {
            return false;
        }

        @Override
        public boolean addAll(int index, @NonNull Collection<? extends Double> c) {
            return false;
        }

        @Override
        public boolean removeAll(@NonNull Collection<?> c) {
            return false;
        }

        @Override
        public boolean retainAll(@NonNull Collection<?> c) {
            return false;
        }

        @Override
        public void clear() {

        }

        @Override
        public Double get(int index) {
            return null;
        }

        @Override
        public Double set(int index, Double element) {
            return null;
        }

        @Override
        public void add(int index, Double element) {

        }

        @Override
        public Double remove(int index) {
            return null;
        }

        @Override
        public int indexOf(@Nullable Object o) {
            return 0;
        }

        @Override
        public int lastIndexOf(@Nullable Object o) {
            return 0;
        }

        @NonNull
        @Override
        public ListIterator<Double> listIterator() {
            return null;
        }

        @NonNull
        @Override
        public ListIterator<Double> listIterator(int index) {
            return null;
        }

        @NonNull
        @Override
        public List<Double> subList(int fromIndex, int toIndex) {
            return null;
        }
    };





    public Growbox(String _name, String _url, String _growing, List<Double> _waterusage) {
       this.name = _name;
       this.url = _url;
       this.growing = _growing;
       this.waterusage = _waterusage;
    }


    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getGrowing() {
        return growing;
    }


    public List<Double> getWaterusage() {
        return waterusage;
    }



}
