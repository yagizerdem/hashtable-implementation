import java.time.Instant;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Random;
import java.util.function.Predicate;

public class Database {
    static  Database instance;
    private ArrayList<DTO> DataFromStorage;
    private  ArrayList<TransactionModel> TransactionModels;
    private Random random = null;
    private   Database(){
        if(Database.instance != null) return;
        this.DataFromStorage = new ArrayList<DTO>();
        this.TransactionModels = new ArrayList<>();
        this.random =new Random() ;
    }
    // enforce singleton
    public static Database GetDatabase(){
        if(Database.instance != null)  return  Database.instance;
        Database db = new Database();
        Database.instance = db;
        return  Database.instance;
    }

    public ArrayList<DTO> GetDataFromStorageObject(){
        return  this.DataFromStorage;
    }
    public  void InsertDTO(DTO dto){
        this.DataFromStorage.add(dto);
    }

    public  ArrayList<TransactionModel> GetTransactionModels(){
        return  this.TransactionModels;
    }
    public  void InsertTransatcitonModel(TransactionModel model){
        this.TransactionModels.add(model);
    }

    public  void BenchMark(CollicionDetectionAlgorithm collicionDetectionAlgorithm , HashFunctionType hashFunctionType , double loadFactor){
        try{
            HashTable ht = new HashTable(100000 , collicionDetectionAlgorithm , hashFunctionType , loadFactor);
            PerformanceMonitor.IndexintTime =  Instant.now().toEpochMilli();
            for(int i = 0 ; i < this.TransactionModels.size() / 100; i++){
                ht.Insert(this.TransactionModels.get(i));
            }
            PerformanceMonitor.IndexintTime =  Instant.now().toEpochMilli() - PerformanceMonitor.IndexintTime;
            // avg time
            double sumOfAllSerchTimes = 0;
            for(int i = 0 ; i < 1000 ; i++){
                int random = this.random.nextInt(10000);
                String uuid = this.TransactionModels.get(random).uuid;
                double start = Instant.now().toEpochMilli();
                ht.GetTransactions(uuid);
                double end = Instant.now().toEpochMilli();
                double result = end - start;
                if(PerformanceMonitor.maxSearchTime < result){
                    PerformanceMonitor.maxSearchTime = result;
                }
                if(PerformanceMonitor.minSearchTime > result){
                    PerformanceMonitor.minSearchTime = result;
                }
                sumOfAllSerchTimes+=result;
            }
            PerformanceMonitor.avgSearchTime = sumOfAllSerchTimes / 1000;
        }catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
            System.exit(0);
        }


    }
}
