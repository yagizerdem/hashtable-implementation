import java.awt.*;
import java.util.ArrayList;

public class HashTable {
    CollicionDetectionAlgorithm collicionDetectionAlgorithm;
    HashFunctionType hashFunctionType;
    CustomerModel[] store;
    int UsedIndexCounter;
    double loadFactor;
    public  HashTable(int size , CollicionDetectionAlgorithm collicionDetectionAlgorithm , HashFunctionType hashFunctionType , double laodFactor){
        this.store = new CustomerModel[size];
        this.loadFactor = laodFactor;
        this.collicionDetectionAlgorithm =collicionDetectionAlgorithm;
        this.hashFunctionType = hashFunctionType;
        this.UsedIndexCounter = 0;
    }
    public  void Insert(TransactionModel transactionModel){
        if(collicionDetectionAlgorithm == CollicionDetectionAlgorithm.LP){
            InsertLinearProbing(transactionModel);
        }
        if(collicionDetectionAlgorithm == CollicionDetectionAlgorithm.DH){
            InsertDoubleHash(transactionModel);
        }
    }
    private void Reshape(){
        this.UsedIndexCounter = 0;
        ArrayList<CustomerModel> keys = new ArrayList<>();
        for(int i = 0 ; i < this.store.length; i++){
            CustomerModel model = this.store[i];
            if(model != null){
                keys.add(model);
            }
        }
        this.store = new CustomerModel[this.store.length *2];
        for(int i = 0 ; i < keys.size(); i++){
            CustomerModel customerModel = keys.get(i);
            TransactionModel transactionModel = customerModel.CustomerTransactions.head;
            while (transactionModel != null){
                Insert(transactionModel.DeepCopy());
                transactionModel = transactionModel.next;
            }
        }
    }
    private  double getCurrentLoadFactor(){
        double curretnLoadFactor = this.UsedIndexCounter * 1.0 /  this.store.length;
        return  curretnLoadFactor;
    }
    private void InsertLinearProbing(TransactionModel transactionModel){
        double currentLoadFactor = getCurrentLoadFactor();
        if(currentLoadFactor >= this.loadFactor){
            Reshape();
            return;
        }
        int hash = this.hashFunctionType == HashFunctionType.SSF ? SSF(transactionModel.uuid , this.store.length) : PAF(transactionModel.uuid , this.store.length);
        if(this.store[hash] != null) PerformanceMonitor.CollisionCount++;
        for(int i = hash ; i < hash + this.store.length;i++){
            int index = i % this.store.length;
            if(this.store[index] == null){
                CustomerModel newCustomerModel = new CustomerModel();
                newCustomerModel.uuid = transactionModel.uuid;
                newCustomerModel.CustomerName = transactionModel.CustomerName;
                newCustomerModel.CustomerTransactions = new SLL();
                this.store[index] = newCustomerModel;
                this.store[index].CustomerTransactions.SortedInsert(transactionModel);
                this.UsedIndexCounter++;
                break;
            }
            else{
                if(this.store[index].uuid.equals(transactionModel.uuid)){
                    this.store[index].CustomerTransactions.SortedInsert(transactionModel);
                    break;
                }
            }

        }
    }

    private  void InsertDoubleHash(TransactionModel transactionModel){
        double currentLoadFactor = getCurrentLoadFactor();
        if(currentLoadFactor >= this.loadFactor){
            Reshape();
            return;
        }
        int PrimaryHash = this.hashFunctionType == HashFunctionType.SSF ? SSF(transactionModel.uuid , this.store.length) : PAF(transactionModel.uuid , this.store.length);
        int SecondaryHash = this.hashFunctionType == HashFunctionType.PAF ? SSF(transactionModel.uuid , this.store.length) : PAF(transactionModel.uuid , this.store.length);
        int hash = (PrimaryHash + 0 * SecondaryHash) % this.store.length;
        if(this.store[hash] != null) PerformanceMonitor.CollisionCount++;
        for(int i = 0 ; i < this.store.length ; i++){
            hash = (PrimaryHash + i * SecondaryHash) % this.store.length;
            if(this.store[hash] == null){
                CustomerModel newModel = new CustomerModel();
                newModel.uuid = transactionModel.uuid;
                newModel.CustomerName = transactionModel.CustomerName;
                newModel.CustomerTransactions = new SLL();
                this.store[hash] = newModel;
                this.store[hash].CustomerTransactions.SortedInsert(transactionModel);
                this.UsedIndexCounter++;
                break;
            }else{
                if(this.store[hash].uuid.equals(transactionModel.uuid)){
                    this.store[hash].CustomerTransactions.SortedInsert(transactionModel);
                    break;
                }
            }

        }
    }

    // hash functions
    private  int SSF(String uuid , int M){
        int sum= 0;
        char[] arr = uuid.toCharArray();
        for(char c : arr){
            sum += c;
        }
        return  sum % M;
    }
    private    int PAF(String uuid , int M){
        int sum = 0;
        for(int i =1  ; i<=uuid.length();i++){
            char c = uuid.charAt(i-1);
            int total = c + (int)Math.pow(3 , uuid.length() -1) % M;
            sum += total;
        }
        return sum % M;
    }


    public  CustomerModel GetTransactions(String uuid){
        if(this.collicionDetectionAlgorithm == CollicionDetectionAlgorithm.LP){
            return  GetTransactionsLinearProbing(uuid);
        }
        if(this.collicionDetectionAlgorithm == CollicionDetectionAlgorithm.DH){
            return  GetTransactionDoubleHashing(uuid);
        }
        return  null;
    }
    private CustomerModel GetTransactionsLinearProbing(String uuid){
        int hash = this.hashFunctionType == HashFunctionType.SSF ? SSF(uuid , this.store.length) : PAF(uuid , this.store.length);
        for(int i = hash ; i < hash + this.store.length ; i++){
            int index = i % this.store.length;
            if(this.store[index] != null && this.store[index].uuid.equals(uuid)){
                return  this.store[index];
            }
        }
        return  null;
    }

    private  CustomerModel GetTransactionDoubleHashing(String uuid){
        int PrimaryHash = this.hashFunctionType == HashFunctionType.SSF ? SSF(uuid , this.store.length) : PAF(uuid , this.store.length);
        int SecondaryHash = this.hashFunctionType == HashFunctionType.PAF ? SSF(uuid , this.store.length) : PAF(uuid , this.store.length);
        for(int i = 0; i < this.store.length ; i++){
            int hash = (PrimaryHash + i * SecondaryHash) % this.store.length;
            if(this.store[hash] != null && this.store[hash].uuid.equals(uuid)){
                return this.store[hash];
            }
        }
        return  null;
    }

    public void  DeleteCustomer(String uuid){
        if(this.collicionDetectionAlgorithm == CollicionDetectionAlgorithm.LP){
            DeleteCustomerLinerProbing(uuid);
        }
        if(this.collicionDetectionAlgorithm == CollicionDetectionAlgorithm.DH){
            DeleteCustomerDoubleHasing(uuid);
        }
    }


    private void DeleteCustomerLinerProbing(String  uuid){
        int hash = this.hashFunctionType == HashFunctionType.SSF ? SSF(uuid , this.store.length) : PAF(uuid , this.store.length);
        for(int i = hash ; i < hash + store.length ; i++){
            int index = i % this.store.length;
            if(this.store[index] != null && this.store[index].uuid.equals(uuid)){
                this.store[index] = null;
                this.UsedIndexCounter--;
                break;
            }
        }
    }

    private  void DeleteCustomerDoubleHasing(String uuid){
        int PrimaryHash = this.hashFunctionType == HashFunctionType.SSF ? SSF(uuid , this.store.length) : PAF(uuid , this.store.length);
        int SecondaryHash = this.hashFunctionType == HashFunctionType.PAF ? SSF(uuid , this.store.length) : PAF(uuid , this.store.length);
        for(int i = 0 ; i < this.store.length; i++){
            int hash = (PrimaryHash + i * SecondaryHash) % this.store.length;
            if(this.store[hash] != null && this.store[hash].uuid.equals(uuid)){
                this.store[hash] = null;
                this.UsedIndexCounter--;
            }
        }
    }
}
