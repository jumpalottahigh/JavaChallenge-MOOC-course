
import nhlstats.NHLStatistics;

public class Main {

    public static void main(String[] args) {
        NHLStatistics.sortByGoals();
        NHLStatistics.top(10);

        NHLStatistics.sortByPenalties();
        NHLStatistics.top(25);

        NHLStatistics.searchByPlayer("Sidney Crosby");

        NHLStatistics.teamStatistics("PHI");

        NHLStatistics.sortByPoints();
        NHLStatistics.teamStatistics("ANA");

    }
}
