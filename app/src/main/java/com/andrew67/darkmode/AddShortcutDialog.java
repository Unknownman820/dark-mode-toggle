package com.andrew67.darkmode;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.core.content.pm.ShortcutInfoCompat;
import androidx.core.content.pm.ShortcutManagerCompat;
import androidx.core.graphics.drawable.IconCompat;

public class AddShortcutDialog extends AppCompatDialogFragment implements Button.OnClickListener {
    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // From https://developer.android.com/guide/topics/ui/dialogs#CustomLayout
        // Get the layout inflater
        final LayoutInflater inflater = requireActivity().getLayoutInflater();
        // Pass null as the parent view because its going in the dialog layout
        final View root = inflater.inflate(R.layout.dialog_shorcuts, null);
        root.findViewById(R.id.btn_toggle).setOnClickListener(this);
        root.findViewById(R.id.btn_light).setOnClickListener(this);
        root.findViewById(R.id.btn_dark).setOnClickListener(this);

        // Inflate and set the layout for the dialog
        return new AlertDialog.Builder(requireActivity())
                .setTitle(R.string.shortcuts_button)
                .setView(root)
                .setNegativeButton(R.string.done, null)
                .create();
    }

    @Override
    public void onClick(View v) {
        ShortcutInfoCompat shortcutInfo;
        final Context context = requireActivity();
        switch (v.getId()) {
            case R.id.btn_toggle:
                shortcutInfo = new ShortcutInfoCompat.Builder(context, "toggle")
                        .setIcon(IconCompat.createWithResource(context, R.mipmap.ic_toggle))
                        .setShortLabel(context.getString(R.string.toggle_short))
                        .setLongLabel(context.getString(R.string.toggle_long))
                        .setIntent(new Intent()
                                .setAction(Intent.ACTION_VIEW)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK)
                                .setPackage("com.andrew67.darkmode")
                                .setClass(context, ShortcutEffect.class)
                                .setData(Uri.parse("content://com.andrew67.darkmode/toggle"))
                        )
                        .build();
                break;
            case R.id.btn_light:
                shortcutInfo = new ShortcutInfoCompat.Builder(context, "light")
                        .setIcon(IconCompat.createWithResource(context, R.mipmap.ic_light))
                        .setShortLabel(context.getString(R.string.light_short))
                        .setLongLabel(context.getString(R.string.light_long))
                        .setIntent(new Intent()
                                .setAction(Intent.ACTION_VIEW)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK)
                                .setPackage("com.andrew67.darkmode")
                                .setClass(context, ShortcutEffect.class)
                                .setData(Uri.parse("content://com.andrew67.darkmode/light"))
                        )
                        .build();
                break;
            case R.id.btn_dark:
                shortcutInfo = new ShortcutInfoCompat.Builder(context, "dark")
                        .setIcon(IconCompat.createWithResource(context, R.mipmap.ic_dark))
                        .setShortLabel(context.getString(R.string.dark_short))
                        .setLongLabel(context.getString(R.string.dark_long))
                        .setIntent(new Intent()
                                .setAction(Intent.ACTION_VIEW)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK)
                                .setPackage("com.andrew67.darkmode")
                                .setClass(context, ShortcutEffect.class)
                                .setData(Uri.parse("content://com.andrew67.darkmode/dark"))
                        )
                        .build();
                break;
            default:
                return;
        }
        if (ShortcutManagerCompat.requestPinShortcut(context, shortcutInfo, null)) {
            Toast.makeText(context, R.string.done, Toast.LENGTH_SHORT).show();
        }
    }
}
