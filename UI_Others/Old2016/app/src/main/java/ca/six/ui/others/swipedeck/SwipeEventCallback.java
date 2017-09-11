package ca.six.ui.others.swipedeck;

public interface SwipeEventCallback {
        //returning the object position in the adapter
        void cardSwipedLeft(int position);
        void cardSwipedRight(int position);
        void cardsDepleted();
    }