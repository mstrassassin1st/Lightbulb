package app.itdivision.lightbulb.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import app.itdivision.lightbulb.Database.DatabaseAccess;
import app.itdivision.lightbulb.Instance.ActiveIdPassing;
import app.itdivision.lightbulb.R;

public class DialogPassword extends AppCompatDialogFragment {
    EditText password;
    EditText confpass;
    EditText oldPass;

    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog_password, null);

        builder.setView(view);
        builder.setTitle("Change Username");
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String oldp = oldPass.getText().toString();
                ActiveIdPassing activeIdPassing = ActiveIdPassing.getInstance();
                DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getActivity());
                databaseAccess.open();
                if(oldp.equals(databaseAccess.getOldPassword(activeIdPassing.getActiveId()))){
                    String newPass = password.getText().toString();
                    String conf = confpass.getText().toString();
                    if(newPass.equals(conf)){
                        databaseAccess.changePassword(newPass, activeIdPassing.getActiveId());
                        Toast.makeText(getActivity(), "Password Changed!", Toast.LENGTH_SHORT).show();
                    }
                }
                databaseAccess.close();
            }
        });

        oldPass = view.findViewById(R.id.oldPassword);
        password = view.findViewById(R.id.editPassword);
        confpass = view.findViewById(R.id.editConfirmPassword);
        return builder.create();
    }
}
