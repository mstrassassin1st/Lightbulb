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

public class DialogUsername extends AppCompatDialogFragment {
    EditText username;
    DialogUsernameListener listener;

    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog_username, null);

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
                String newUsername = username.getText().toString();
                DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getActivity());
                ActiveIdPassing activeIdPassing = ActiveIdPassing.getInstance();
                databaseAccess.open();
                databaseAccess.changeUsername(newUsername, activeIdPassing.getActiveId());
                Toast.makeText(getActivity(), "Username Changed!", Toast.LENGTH_SHORT).show();
                databaseAccess.close();
                listener.applyTextsUsername(newUsername);
            }
        });
        username = view.findViewById(R.id.editName);
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (DialogUsernameListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString());
        }
    }

    public interface DialogUsernameListener{
        void applyTextsUsername(String username);
    }
}
