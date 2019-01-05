package pl.agh.edu.stockinger.calculation;

public class RatioCalculator {
    private RatioCalculator ratioCalculator;

    public RatioCalculator getInstance(){
        if(ratioCalculator == null){
            ratioCalculator = new RatioCalculator();
        }
        return ratioCalculator;
    }

    public double calculateCurrentRatio(int currentAssets, int currentLiabilities){
            return currentAssets/currentAssets;
    }

    public double calculateQuickRatio(int currentAssets, int currentLiabilities, int stocks, int reckoning){
        return (currentAssets - stocks - reckoning)/currentLiabilities;
    }



    private RatioCalculator(){}


}
