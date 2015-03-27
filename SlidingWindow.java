package dk.aau.cs.psylog.module_lib;

import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class SlidingWindow<T extends Number> {

    private ArrayList<Pair<T,Long>> arrayList = new ArrayList<>();
    private final int MAXSIZE = 1000;
    private double errorThreshold;
    public SlidingWindow(double errorThreshold){
        this.errorThreshold = errorThreshold;
    }

    public boolean addPointAndCheck(Pair<T,Long> point){
        arrayList.add(point);
        return checkErrorThreshold();
    }

    private boolean checkErrorThreshold(){
        if(arrayList.size() > 1000 || calculateHighestDistanceForList() > errorThreshold)
            return true;
        return false;
    }

    public Pair<T,Long> resetSlidingWindow(){
        Pair<T, Long> beforeCurrent = arrayList.get(arrayList.size()-2);
        Pair<T, Long> current = arrayList.get(arrayList.size()-1);
        arrayList.clear();
        arrayList.add(beforeCurrent);
        arrayList.add(current);
        return beforeCurrent;
    }

    private double calculateHighestDistanceForList(){
        return calculateDistanceFromPointsToLine(arrayList.get(0), arrayList.get(arrayList.size() - 1), arrayList);
    }

    private double calculateDistanceFromPointsToLine(Pair<T,Long> pointOfLine1, Pair<T,Long> pointOfLine2, List<Pair<T,Long>> points){
        double denominator = (float)Math.sqrt(Math.pow((pointOfLine2.first.doubleValue() - pointOfLine1.first.doubleValue()),2)+Math.pow((pointOfLine2.second - pointOfLine1.second),2));
        double nominatorPart1 = pointOfLine2.first.doubleValue() - pointOfLine1.first.doubleValue();
        double nominatorPart2 = pointOfLine2.second - pointOfLine1.second;

        double highestDistance = 0;
        for(Pair<T,Long> point : points){
            double tempDistance = calculateDistanceFromPointToLine(point, pointOfLine1, denominator, nominatorPart1, nominatorPart2);
            if(tempDistance > highestDistance)
                highestDistance = tempDistance;
        }
        return highestDistance;
    }

    private double calculateDistanceFromPointToLine(Pair<T,Long> point, Pair<T,Long> pointOnLine, double denominator, double nominatorPart1, double nominatorPart2){
        return Math.abs(nominatorPart1 * (pointOnLine.second - point.second) - (pointOnLine.first.doubleValue() - point.first.doubleValue()) * nominatorPart2) / denominator;
    }

}
