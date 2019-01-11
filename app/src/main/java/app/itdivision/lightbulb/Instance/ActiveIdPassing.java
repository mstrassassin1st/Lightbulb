package app.itdivision.lightbulb.Instance;

import android.provider.ContactsContract;

import app.itdivision.lightbulb.Database.DatabaseAccess;

public class ActiveIdPassing {

        private static ActiveIdPassing instance;
        private int ActiveId;
        private String reward;

        public static ActiveIdPassing getInstance(){
            if(instance == null){
                instance = new ActiveIdPassing();
            }
            return instance;
        }

        public int getActiveId(){
            return ActiveId;
        }

        public void setActiveId(int activeId){
            ActiveId = activeId;
        }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }
}
