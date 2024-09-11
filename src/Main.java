import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors
import java.math.BigInteger;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner; // Import the Scanner class to read text files
public class Main {
    public static void main(String[] args) {
        Database database = Database.GetDatabase();
        try {
            URL path = Main.class.getResource("supermarket_dataset_50K.csv");
            File myObj = new File(path.getFile());
            Scanner myReader = new Scanner(myObj);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            if(myReader.hasNext()) myReader.nextLine(); // skip first line
            while (myReader.hasNextLine()) {
                String row = myReader.nextLine();
                String[] col = row.split(",");
                DTO newDto = new DTO();
                newDto.uuid = col[0];
                newDto.CutomerName = col[1];
                newDto.date = formatter.parse(col[2]);
                newDto.ProductName = col[3];
                database.InsertDTO(newDto);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
            System.exit(0);
        }
        catch (ParseException e){
            System.out.println("An error occurred.");
            e.printStackTrace();
            System.exit(0);
        }
        catch (Exception e){
            System.out.println("An error occurred.");
            e.printStackTrace();
            System.exit(0);
        }
        ArrayList<DTO> allData = database.GetDataFromStorageObject();
        for(DTO dto : allData){
            TransactionModel model = new TransactionModel();
            // mapping
            model.CustomerName = dto.CutomerName;
            model.uuid = dto.uuid;
            model.ProductName = dto.ProductName;
            model.date = dto.date;
            database.InsertTransatcitonModel(model);
        }

        database.BenchMark(CollicionDetectionAlgorithm.LP , HashFunctionType.SSF , 0.5);
        PerformanceMonitor.Log();
        PerformanceMonitor.Clear();
        System.out.println("-".repeat(20));

        database.BenchMark(CollicionDetectionAlgorithm.LP , HashFunctionType.PAF , 0.5);
        PerformanceMonitor.Log();
        PerformanceMonitor.Clear();
        System.out.println("-".repeat(20));

        database.BenchMark(CollicionDetectionAlgorithm.LP , HashFunctionType.SSF , 0.8);
        PerformanceMonitor.Log();
        PerformanceMonitor.Clear();
        System.out.println("-".repeat(20));

        database.BenchMark(CollicionDetectionAlgorithm.LP , HashFunctionType.PAF , 0.8);
        PerformanceMonitor.Log();
        PerformanceMonitor.Clear();
        System.out.println("-".repeat(20));


        database.BenchMark(CollicionDetectionAlgorithm.DH , HashFunctionType.SSF , 0.5);
        PerformanceMonitor.Log();
        PerformanceMonitor.Clear();
        System.out.println("-".repeat(20));

        database.BenchMark(CollicionDetectionAlgorithm.DH , HashFunctionType.PAF , 0.5);
        PerformanceMonitor.Log();
        PerformanceMonitor.Clear();
        System.out.println("-".repeat(20));

        database.BenchMark(CollicionDetectionAlgorithm.DH , HashFunctionType.SSF , 0.8);
        PerformanceMonitor.Log();
        PerformanceMonitor.Clear();
        System.out.println("-".repeat(20));

        database.BenchMark(CollicionDetectionAlgorithm.DH , HashFunctionType.PAF , 0.8);
        PerformanceMonitor.Log();
        PerformanceMonitor.Clear();
        System.out.println("-".repeat(20));

    }

}