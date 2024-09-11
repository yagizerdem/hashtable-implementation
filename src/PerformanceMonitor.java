public class PerformanceMonitor {
    static  int CollisionCount =0;
    static double IndexintTime = 0;
    static double avgSearchTime =0;
    static  double minSearchTime = 0;
    static double maxSearchTime = 0;

    static  void  Clear(){
        CollisionCount = 0;
        IndexintTime = 0;
        avgSearchTime =0;
        minSearchTime = 0;
        maxSearchTime =0;
    }

    static  void Log(){
        System.out.println("Collision Count : " + PerformanceMonitor.CollisionCount);
        System.out.println("Indexing time : " + PerformanceMonitor.IndexintTime);
        System.out.println("average serch time : " + PerformanceMonitor.avgSearchTime);
        System.out.println("min search time : " + PerformanceMonitor.minSearchTime);
        System.out.println("max search time : " + PerformanceMonitor.maxSearchTime);
    }
}
