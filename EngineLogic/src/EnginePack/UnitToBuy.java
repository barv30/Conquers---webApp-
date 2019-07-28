package EnginePack;

public class UnitToBuy
{
    String type;
    int amount;

    public UnitToBuy(String _type , int _amount)
    {
        this.type = _type;
        this.amount=_amount;
    }

    public int getAmount() {
        return amount;
    }

    public String getType() {
        return type;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
