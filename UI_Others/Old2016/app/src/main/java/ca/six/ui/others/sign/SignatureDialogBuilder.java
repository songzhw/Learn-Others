package ca.six.ui.others.sign;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;

import java.io.File;

import ca.six.ui.others.R;

public class SignatureDialogBuilder {

    public interface SignatureEventListener {
        public void onSignatureEntered(File savedFile);
        public void onSignatureInputCanceled();
        public void onSignatureInputError(Throwable e);
    }

    public void show(Activity activity, final SignatureEventListener eventListener){
        final View view = buildView(activity);
        final SignatureInputView inputView = (SignatureInputView) view.findViewById(R.id.sig__input_view);

        new AlertDialog.Builder(activity)
                .setTitle("Please sign")
                .setView(view)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        try {
                            if (!inputView.isSignatureInputAvailable())
                                throw new NoSignatureException("No signature found");

                            File saved = inputView.saveSignature();

                            eventListener.onSignatureEntered(saved);
                        } catch (Exception e) {
                            e.printStackTrace();

                            eventListener.onSignatureInputError(e);
                        }
                    }
                })
                .setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        eventListener.onSignatureInputCanceled();
                    }
                })
                .show();

        view.findViewById(R.id.sig__action_undo)
                .setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        inputView.undoLastSignaturePath();
                    }
                });
    }

    protected View buildView(Activity activity){
        return activity.getLayoutInflater()
                .inflate(R.layout.sig__default_dialog, null, false);
    }


}
