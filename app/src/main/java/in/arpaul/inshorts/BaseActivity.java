package in.arpaul.inshorts;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.internal.util.Predicate;
import com.arpaul.utilitieslib.LogUtils;

import java.util.ArrayList;
import java.util.Collection;

public abstract class BaseActivity extends AppCompatActivity {

    public LayoutInflater inflaterBase;
    public LinearLayout llBase;
    public Typeface tfRegular,tfBold;
    private Dialog dialogLoader;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        intialiseBaseUI();
    }

    /**
     * Shows Indefinite Progress Dialog.
     *
     * @param title
     * @param message
     * @param isCancelable
     */
    public void showLoader(final String title, final String message, boolean isCancelable) {
        runOnUiThread(new RunProgressDialog(title, message, isCancelable));
    }

    /**
     * Shows Indefinite Progress Dialog.
     *
     * @param title
     * @param message
     * @param progress
     * @param isCancelable
     */
    public void showLoader(final String title, final String message, final int progress, boolean isCancelable) {
        runOnUiThread(new RunProgressDialog(title, message, progress, isCancelable));
    }

    public void hideLoader() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (dialogLoader != null && dialogLoader.isShowing())
                        dialogLoader.dismiss();

                    dialogLoader = null;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    class RunProgressDialog implements Runnable {
        private String strTitle;// FarmName of the materialDialog
        private String strMessage;// Message to be shown in materialDialog
        private boolean isCancelable = false;
        private int progress;

        public RunProgressDialog(String strTitle, String strMessage, boolean isCancelable) {
            this.strTitle = strTitle;
            this.strMessage = strMessage;
            this.progress = 0;
            this.isCancelable = isCancelable;
        }

        public RunProgressDialog(String strTitle, String strMessage, int progress, boolean isCancelable) {
            this.strTitle = strTitle;
            this.strMessage = strMessage;
            this.progress = progress;
            this.isCancelable = isCancelable;
        }

        @Override
        public void run() {
            try {
                boolean show = false;
                if(dialogLoader == null) {
                    dialogLoader = new Dialog(BaseActivity.this);
                    dialogLoader.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialogLoader.setContentView(R.layout.custom_loader);
                    dialogLoader.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    show = true;
                } else if(dialogLoader != null && dialogLoader.isShowing()) {
                    dialogLoader.dismiss();
                    show = true;
                } else {
                    LogUtils.infoLog("showLoader", "none");
                }

                if(show) {
                    dialogLoader.setCancelable(isCancelable);

                    TextView tvLoading = (TextView) dialogLoader.findViewById(R.id.tvLoading);
                    tvLoading.setTypeface(tfRegular, Typeface.BOLD);
                    if (!TextUtils.isEmpty(strMessage))
                        tvLoading.setText(strMessage);
                    dialogLoader.show();
                }

            } catch (Exception e) {
                e.printStackTrace();
//                pbDialogLoader = null;
                dialogLoader = null;
                alertDialog = null;
            }
        }
    }

    /**
     * Shows Dialog with user defined buttons.
     *
     * @param title
     * @param message
     * @param okButton
     * @param noButton
     * @param from
     * @param isCancelable
     */
    public void showCustomDialog(final String title, final String message, final String okButton, final String noButton, final String from, boolean isCancelable) {
        runOnUiThread(new RunShowDialog(title, message, okButton, noButton, from, isCancelable));
    }

    public void hideCustomDialog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (alertDialog != null && alertDialog.isShowing())
                    alertDialog.dismiss();
            }
        });
    }

    class RunShowDialog implements Runnable {
        private String strTitle;// FarmName of the materialDialog
        private String strMessage;// Message to be shown in materialDialog
        private String firstBtnName;
        private String secondBtnName;
        private String from;
        private boolean isCancelable = false;

        public RunShowDialog(String strTitle, String strMessage, String firstBtnName, String secondBtnName, String from, boolean isCancelable) {
            this.strTitle = strTitle;
            this.strMessage = strMessage;
            this.firstBtnName = firstBtnName;
            this.secondBtnName = secondBtnName;
            this.isCancelable = isCancelable;
            if (from != null)
                this.from = from;
            else
                this.from = "";

        }

        @Override
        public void run() {
            showCustomDialog();
        }

        private void showCustomDialog() {
            try {
                boolean show = false;

                if(alertDialog == null) {
                    alertDialog = new AlertDialog.Builder(BaseActivity.this).create();
                    show = setDialog();
                } else if (alertDialog != null && alertDialog.isShowing()) {
                    alertDialog.dismiss();
                    show = setDialog();
                } else
                    show = setDialog();
                if(show)
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            alertDialog.show();
                        }
                    });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private boolean setDialog() {
            try {
                alertDialog.setTitle(strTitle);
                alertDialog.setMessage(strMessage);
                alertDialog.setCanceledOnTouchOutside(isCancelable);
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, firstBtnName,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialogYesClick(from);
                                dialog.dismiss();
                            }
                        });
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, secondBtnName,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialogNoClick(from);
                                dialog.dismiss();
                            }
                        });
            } catch (Exception ex) {
                ex.printStackTrace();
                return false;
            } finally {
                return true;
            }
        }
    }

    public void dialogYesClick(String from) {
        if(from.equalsIgnoreCase("")){

        }
    }

    public void dialogNoClick(String from) {
        if(from.equalsIgnoreCase("")){

        }
    }

    public void hideKeyBoard(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static ViewGroup getParentView(View v) {
        ViewGroup vg = null;

        if(v != null)
            vg = (ViewGroup) v.getRootView();

        return vg;
    }

    public static <T> Collection<T> filter(Collection<T> col, Predicate<T> predicate) {

        Collection<T> result = new ArrayList<T>();
        if(col!=null)
        {
            for (T element : col) {
                if (predicate.apply(element)) {
                    result.add(element);
                }
            }
        }
        return result;
    }

    public static class SingleClickDialog extends AppCompatDialogFragment {
        public interface OnSelectedListener {
            void onSelect(int position);
        }

        OnSelectedListener mListener;

        public void setOnSelectedListener(OnSelectedListener listener) {
            mListener = listener;
        }

        public static SingleClickDialog newInstance(String title, ArrayList<String> data) {
            Bundle args = new Bundle();
            args.putSerializable("title", title);
            args.putStringArrayList("data", data);
            SingleClickDialog fragment = new SingleClickDialog();
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            String title = getArguments().getString("title");
            ArrayList<String> data = getArguments().getStringArrayList("data");

            CharSequence[] charSequences = new CharSequence[data.size()];
            for (int i = 0; i < data.size(); i++) {
                charSequences[i] = data.get(i);//String.valueOf(i + 1);
            }

            return new AlertDialog.Builder(getActivity())
                    .setTitle(title)
                    .setItems(charSequences, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mListener.onSelect(which);
                        }
                    })
                    .create();
        }
    }

    public static void applyTypeface(ViewGroup v, Typeface f, int style) {
        if(v != null) {
            int vgCount = v.getChildCount();
            for(int i=0;i<vgCount;i++) {
                if(v.getChildAt(i) == null) continue;
                if(v.getChildAt(i) instanceof ViewGroup)
                    applyTypeface((ViewGroup)v.getChildAt(i), f, style);
                else {
                    View view = v.getChildAt(i);
                    if(view instanceof TextView)
                        ((TextView)(view)).setTypeface(f, style);
                    else if(view instanceof EditText)
                        ((EditText)(view)).setTypeface(f, style);
                    else if(view instanceof Button)
                        ((Button)(view)).setTypeface(f, style);
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_base, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void intialiseBaseUI() {
        inflaterBase            = this.getLayoutInflater();
        llBase                  = (LinearLayout) findViewById(R.id.llBase);

        createTypeFace();
    }

    private void createTypeFace(){
        tfRegular   = Typeface.createFromAsset(this.getAssets(),"fonts/Myriad Pro Regular.ttf");
        tfBold      = Typeface.createFromAsset(this.getAssets(),"fonts/Myriad Pro Regular.ttf");
    }
}
