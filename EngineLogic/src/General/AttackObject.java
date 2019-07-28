package General;

import EnginePack.UnitToBuy;

import java.util.ArrayList;

public class AttackObject {
   private boolean isAttackSuccess;
   private ArrayList<UnitToBuy> unitsInTerritory;
   private String unitsString;

   public AttackObject(boolean isAttackSuccess, ArrayList<UnitToBuy> list)
   {
       this.isAttackSuccess = isAttackSuccess;
       this.unitsInTerritory = list;
       makeStringOfUnitsInTerritory();
   }
    public void setAttackSuccess(boolean attackSuccess) {
        isAttackSuccess = attackSuccess;
    }

    public void setUnitsInTerritory(ArrayList<UnitToBuy> unitsInTerritory) {
        this.unitsInTerritory = unitsInTerritory;
    }

    public void makeStringOfUnitsInTerritory()
    {
        String res = "";
        for(int i=0;i<unitsInTerritory.size();i++)
        {
            res+= unitsInTerritory.get(i).getType()+":"+unitsInTerritory.get(i).getAmount()+"\n";
        }
        unitsString = res;
    }
}




