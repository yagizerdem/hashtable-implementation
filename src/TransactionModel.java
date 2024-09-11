import java.security.PublicKey;
import java.util.Date;

public class TransactionModel {
    String  ProductName;
    String CustomerName;
    Date date;
    TransactionModel next;
    String uuid;

    public TransactionModel(){
        this.next = null;
    }
    public int CompareDateTime(TransactionModel other){
         return  this.date.compareTo(other.date);
    }

    public  TransactionModel DeepCopy(){
        TransactionModel newModel = new TransactionModel();
        newModel.uuid = this.uuid;
        newModel.date = this.date;
        newModel.ProductName = this.ProductName;
        newModel.CustomerName = this.CustomerName;
        return  newModel;
    }
}

