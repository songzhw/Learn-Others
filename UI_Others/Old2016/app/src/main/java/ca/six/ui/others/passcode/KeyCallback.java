package ca.six.ui.others.passcode;

/**
 * Created by Ashish on 25/12/15.
 */
public interface KeyCallback {

    public void onPrevious();
    public void onNext(char value);
    public void onDone();
    public void onDelete();

}
