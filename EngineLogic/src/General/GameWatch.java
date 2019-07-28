package General;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

public class GameWatch implements Serializable {
    private long m_StartTime = System.currentTimeMillis();
    private long m_Milliseconds;
    private long m_CurrentTime;
    private long m_SavedTime = 0;

    public String getTimeElapsed(){
        m_CurrentTime = System.currentTimeMillis();
        m_Milliseconds = m_CurrentTime - m_StartTime;
        return toString();
    }

    public String toString(){
        return String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(m_Milliseconds + m_SavedTime),
                TimeUnit.MILLISECONDS.toSeconds(m_Milliseconds + m_SavedTime) % 60 );
    }

    public void stop(){
        getTimeElapsed();
        m_SavedTime += m_Milliseconds;
        reset();
    }

    public void reset(){
        m_StartTime = System.currentTimeMillis();
    }
}
