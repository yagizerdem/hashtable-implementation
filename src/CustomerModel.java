public class CustomerModel {
    public String uuid;
    public String CustomerName;
    public  SLL CustomerTransactions;
    public  CustomerModel(){
        this.CustomerTransactions= new SLL();
    }

}
