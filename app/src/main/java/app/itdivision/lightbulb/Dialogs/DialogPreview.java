package app.itdivision.lightbulb.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import app.itdivision.lightbulb.Database.DatabaseAccess;
import app.itdivision.lightbulb.R;
import app.itdivision.lightbulb.VideoPlayerConfig;


public class DialogPreview extends AppCompatDialogFragment {

    String CourseName = " ";
    DatabaseAccess databaseAccess = DatabaseAccess.getInstance(getActivity());
    YouTubePlayerSupportFragment yt_player;
    YouTubePlayer youTubePlayer;

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog_preview, null);
        yt_player = (YouTubePlayerSupportFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.yt_player_preview);
        final YouTubePlayer.OnInitializedListener ytOnInitializedListener;
        Button btn_play_lesson = view.findViewById(R.id.buttonPreview);

        builder.setView(view);
        builder.setTitle("Preview");
        databaseAccess.open();
        final String URL = databaseAccess.getPreviewVideo(CourseName);
        databaseAccess.close();
        ytOnInitializedListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.loadVideo(URL);
                DialogPreview.this.youTubePlayer = youTubePlayer;
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        };
        btn_play_lesson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yt_player.initialize(VideoPlayerConfig.getApiKey(), ytOnInitializedListener);

            }
        });
        builder.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getActivity().getSupportFragmentManager().beginTransaction().remove(DialogPreview.this).commit();
                getActivity().getSupportFragmentManager().beginTransaction().remove(yt_player).commit();
            }
        });
        return  builder.create();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        youTubePlayer.release();
    }

    public void setParams(String CourseName){
        this.CourseName = CourseName;
    }

}
