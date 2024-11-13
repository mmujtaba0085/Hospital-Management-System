package packages.Others;

public class Bill {
    int amount;
    boolean paid;
    Bill(){
        amount=0;
        paid=false;
    }
    Bill(int amount){
        this.amount=amount;
    }
    public void payBill(int amt){
        if(amt>amount){
            amount-=amt;
        }
        else if(amt<amount){
            amount-=amt;
        }
        else{
            amount-=amt;
        }
    }
    void generateBill(){

    }
    public void displayBill(){
        System.out.println("Amount to be paid: " + amount + "\n");
    }
    void billPaid(boolean paid){
        if(amount==0){
            this.paid=paid;
        }
    }
}
