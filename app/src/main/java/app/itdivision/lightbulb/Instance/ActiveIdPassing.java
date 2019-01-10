package app.itdivision.lightbulb.Instance;

public class ActiveIdPassing {

        private static ActiveIdPassing instance;
        private int ActiveId;

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

}
