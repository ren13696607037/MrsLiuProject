package com.techfly.liutaitai.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class CustormDialogFragment extends DialogFragment {

    public static CustormDialogFragment newInstance(Bundle arg) {
        CustormDialogFragment fragment = new CustormDialogFragment();
        fragment.setArguments(arg);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle arg = getArguments();
        EDialog type = EDialog.values()[arg.getInt("dialog_type")];
        /*switch (type) {
        case LIST_DIALOG:
            ListAdapter adapter = (ListAdapter) arg
                    .getSerializable("dialog_adapter");
            return new ListDialog.Builder(getActivity().getParent())
                    .setAdapter(adapter).create();
        }*/
        return super.onCreateDialog(savedInstanceState);
    }
}
