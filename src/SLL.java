public class SLL {
    TransactionModel head;
    public SLL(){
        this.head = null;
    }

    public void  InsertHead(TransactionModel model){
        if(this.head == null) {
            this.head = model;
        }
        else{
            model.next = this.head;
            this.head =model;
        }
    }
    // sorted Insert
    public  void SortedInsert(TransactionModel model){
        TransactionModel current = this.head;
        TransactionModel prev = null;
        if(current == null){
            InsertHead(model);
            return;
        }
        while (current != null){
            int flag = model.CompareDateTime(current);
            if(flag >= 0){
                if(current == head){
                    InsertHead(model);
                    return;
                }
                else{
                    model.next = current;
                    prev.next = model;
                    return;
                }
            }
            prev = current;
            current= current.next;
        }
        prev.next = model;
    }
}
