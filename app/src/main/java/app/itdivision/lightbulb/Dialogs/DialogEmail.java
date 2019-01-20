package app.itdivision.lightbulb.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import app.itdivision.lightbulb.Database.DatabaseAccess;
import app.itdivision.lightbulb.Instance.ActiveIdPassing;
import app.itdivision.lightbulb.R;

public class DialogEmail extends AppCompatDialogFragment {
    EditText email;
    DialogEmailListener listener;

    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog_email, null);

        builder.setView(view);
        builder.setTitle("Change Email");
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newEmail = email.getText().toString();
                DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getActivity());
                ActiveIdPassing activeIdPassing = ActiveIdPassing.getInstance();
                databaseAccess.open();
                databaseAccess.changeEmail(newEmail, activeIdPassing.getActiveId());
                databaseAccess.close();
                Toast.makeText(getActivity(), "Email Changed!", Toast.LENGTH_SHORT).show();
                listener.applyTextsEmail(newEmail);
            }
        });
        email = view.findViewById(R.id.editEmail);
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (DialogEmailListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString());
        }
    }

    public interface DialogEmailListener{
        void applyTextsEmail(String email);
    }
}
