package Server.Utils;

public class ResultOfRequest{
    private String m_RedirectURL;
    private boolean m_HasRequestSucceeded = false;
    private String m_Message;
    public ResultOfRequest(String i_RedirectURL, boolean i_HasRequestSucceeded)
    {
        m_RedirectURL=i_RedirectURL;
        m_HasRequestSucceeded=i_HasRequestSucceeded;
    }
    public void setMessage(String i_Message)
    {
        m_Message=i_Message;
    }
}
