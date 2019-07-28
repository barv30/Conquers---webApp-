package Users;

public class User
{
    private String m_Name;
    private String color= "None";

    public void setColor(String color) {
        this.color = color;
    }

    public User(String i_Name){

        m_Name=i_Name;
    }

    public String getName() {
        return m_Name;
    }

/*
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof String) {
            return m_Name.equals(obj);
        }
        return false;
    }
*/
    @Override
    public int hashCode() {
        return super.hashCode() + m_Name.hashCode() ;
    }
}
