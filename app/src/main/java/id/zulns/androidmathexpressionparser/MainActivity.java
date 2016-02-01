package id.zulns.androidmathexpressionparser;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;


public class MainActivity extends Activity implements AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener, DialogInterface.OnClickListener {

    private static final int RESULT_FOREGROUND_COLOR = Color.WHITE;
    private static final int RESULT_BACKGROUND_COLOR = Color.BLUE;
    //private static final int ERROR_FOREGROUND_COLOR = Color.WHITE;
    private static final int ERROR_BACKGROUND_COLOR = Color.RED;
    private static final int LIST_VIEW_HISTORIES = 1;
    private static final int LIST_VIEW_FUNCTIONS = 2;
    private static final int LIST_VIEW_VARIABLES = 3;
    private static final int ACTION_DELETE_ALL_HISTORY = 1;
    private static final int ACTION_DELETE_ALL_VARIABLE = 2;
    private static final int ACTION_DELETE_ITEM_HISTORY = 3;
    private static final int ACTION_DELETE_ITEM_VARIABLE = 4;
    private static final int RESULT_VIEW_NORMAL = 1;
    private static final int RESULT_VIEW_HEX = 2;
    private static final int RESULT_VIEW_REAL_HEX = 3;
    private static final int RESULT_VIEW_DMS = 4;
    private static final int RESULT_VIEW_ERROR = 5;
    private ArrayAdapter<String> mArrayAdapterHistories;
    private ArrayAdapter<String> mArrayAdapterFunctions;
    private ArrayAdapter<String> mArrayAdapterVariables;
    private ListView mListView;
    private TextView mTextViewResult;
    private EditText mEditTextExpression;
    private Button mButtonEvaluation;
    private MenuItem mMenuItemEnableEvaluation;
    private MenuItem mMenuItemViewHistories;
    private MenuItem mMenuItemViewFunctions;
    private MenuItem mMenuItemViewVariables;
    private MenuItem mMenuItemDeleteAll;
    private MenuItem mMenuItemResultNormal;
    private MenuItem mMenuItemResultHex;
    private MenuItem mMenuItemResultRealHex;
    private MenuItem mMenuItemResultDMS;
    private Builder mBuilder;
    private int mListViewMode;
    private int mDialogAction;
    private String mListItemString;
    private int mListItemPos;
    private Parser mParser;
    private InputMethodManager mInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set application subtitle where placed on action bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ActionBar ab = getActionBar();
            if (ab != null) {
                ab.setSubtitle(R.string.app_about);
            }
        }

        mListView = (ListView) findViewById(R.id.listView);
        mTextViewResult = (TextView) findViewById(R.id.textViewResult);
        mEditTextExpression = (EditText) findViewById(R.id.editTextExpr);
        mButtonEvaluation = (Button) findViewById(R.id.buttonEval);

        mParser = new Parser();
        mBuilder = new Builder(this);

        mArrayAdapterHistories = new ArrayAdapter<>(this, R.layout.list_view_expr);
        mArrayAdapterFunctions = new ArrayAdapter<>(this, R.layout.list_view_expr);
        mArrayAdapterVariables = new ArrayAdapter<>(this, R.layout.list_view_expr);

        for (String item : Parser.getFunctionsList()) {
            mArrayAdapterFunctions.add(item);
        }

        mListView.setAdapter(mArrayAdapterHistories);
        mListViewMode = LIST_VIEW_HISTORIES;

        mInput = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);

        mBuilder.setPositiveButton(R.string.dialog_button_ok_label, this);
        mBuilder.setNegativeButton(R.string.dialog_button_cancel_label, this);

        mTextViewResult.setTextColor(RESULT_FOREGROUND_COLOR);
        mTextViewResult.setBackgroundColor(RESULT_BACKGROUND_COLOR);
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        
        if (mMenuItemViewHistories == null) {
            mButtonEvaluation.setEnabled(false);
            mArrayAdapterHistories.add("Dear user,");
            mArrayAdapterHistories.add("Simply tap to copy from this item list to expression entry.");
            mArrayAdapterHistories.add("Long tap to delete this item list.");
            mArrayAdapterHistories.add("To begin please enable Eval button via options menu.");
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                mArrayAdapterHistories.add(" ");
                mArrayAdapterHistories.add(getString(R.string.app_about));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        mMenuItemEnableEvaluation = menu.findItem(R.id.item_enable_evaluation);
        mMenuItemViewHistories = menu.findItem(R.id.item_view_histories);
        mMenuItemViewFunctions = menu.findItem(R.id.item_view_functions);
        mMenuItemViewVariables = menu.findItem(R.id.item_view_variables);
        mMenuItemDeleteAll = menu.findItem(R.id.item_delete_all);
        mMenuItemResultNormal = menu.findItem(R.id.item_result_normal);
        mMenuItemResultHex = menu.findItem(R.id.item_result_hex);
        mMenuItemResultRealHex = menu.findItem(R.id.item_result_real_hex);
        mMenuItemResultDMS = menu.findItem(R.id.item_result_dms);

        if (mButtonEvaluation.isEnabled()) {
            mMenuItemEnableEvaluation.setVisible(false);
        } else {
            mMenuItemViewFunctions.setVisible(false);
        }
        mMenuItemViewHistories.setVisible(false);
        mMenuItemViewVariables.setVisible(false);
        mMenuItemDeleteAll.setVisible(false);
        mMenuItemResultNormal.setVisible(false);
        mMenuItemResultHex.setVisible(false);
        mMenuItemResultRealHex.setVisible(false);
        mMenuItemResultDMS.setVisible(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.item_enable_evaluation:
                mMenuItemEnableEvaluation.setVisible(false);
                mMenuItemViewFunctions.setVisible(true);
                mButtonEvaluation.setEnabled(true);
                mArrayAdapterHistories.clear();
                mListView.setOnItemClickListener(this);
                mListView.setOnItemLongClickListener(this);
                break;

            case R.id.item_view_histories:
                setListView(LIST_VIEW_HISTORIES);
                break;

            case R.id.item_view_functions:
                setListView(LIST_VIEW_FUNCTIONS);
                break;

            case R.id.item_view_variables:
                setListView(LIST_VIEW_VARIABLES);
                break;

            case R.id.item_delete_all:
                int action = (mListViewMode == LIST_VIEW_HISTORIES)?
                        ACTION_DELETE_ALL_HISTORY : ACTION_DELETE_ALL_VARIABLE;
                showDialogBox(action);
                break;

            case R.id.item_result_normal:
                setResultView(RESULT_VIEW_NORMAL);
                break;

            case R.id.item_result_hex:
                setResultView(RESULT_VIEW_HEX);
                break;

            case R.id.item_result_real_hex:
                setResultView(RESULT_VIEW_REAL_HEX);
                break;

            case R.id.item_result_dms:
                setResultView(RESULT_VIEW_DMS);
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String ins = ((TextView) view).getText().toString();
        switch (mListViewMode) {
            case LIST_VIEW_FUNCTIONS:
                if (ins.contains("()"))
                    ins = ins.substring(0, ins.indexOf("()") + 2);
                else
                    ins = ins.substring(0, ins.indexOf("(") + 1);
                break;

            case LIST_VIEW_VARIABLES:
                ins = ins.substring(0, ins.indexOf(" ="));
        }
        int start = mEditTextExpression.getSelectionStart();
        int end = mEditTextExpression.getSelectionEnd();
        if (start < end) {
            String src = mEditTextExpression.getText().toString();
            String rpl = src.substring(start, end);
            mEditTextExpression.setText(src.replace(rpl, ""));
            mEditTextExpression.setSelection(start);
        }
        mEditTextExpression.getText().insert(start, ins);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        mListItemString = ((TextView) view).getText().toString();
        switch (mListViewMode) {
            case LIST_VIEW_HISTORIES:
                showDialogBox(ACTION_DELETE_ITEM_HISTORY);
                break;

            case LIST_VIEW_VARIABLES:
                mListItemPos = position;
                showDialogBox(ACTION_DELETE_ITEM_VARIABLE);
        }
        return true;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        dialog.dismiss();

        if (which == DialogInterface.BUTTON_NEGATIVE) {
            return;
        }

        switch (mDialogAction) {
            case ACTION_DELETE_ALL_HISTORY:
                mArrayAdapterHistories.clear();
                mMenuItemDeleteAll.setVisible(false);
                break;

            case ACTION_DELETE_ALL_VARIABLE:
                mParser.clearVariables();
                setListView(LIST_VIEW_HISTORIES);
                break;

            case ACTION_DELETE_ITEM_HISTORY:
                mArrayAdapterHistories.remove(mListItemString);
                if (mArrayAdapterHistories.getCount() == 0)
                    mMenuItemDeleteAll.setVisible(false);
                break;

            case ACTION_DELETE_ITEM_VARIABLE:
                mArrayAdapterVariables.remove(mListItemString);
                mParser.removeVariableAt(mListItemPos);
                if (mArrayAdapterVariables.getCount() == 0)
                    setListView(LIST_VIEW_HISTORIES);
        }
    }

    public void onClickEval(View v) {
        String expr = mEditTextExpression.getText().toString();
        if (expr.length() > 0) {
            mParser.parse(expr);
            if (mParser.isError()){
                if (mParser.getErrorSelectionStart() != -1) {
                    mEditTextExpression.setText(mParser.getExpression());
                    mEditTextExpression.setSelection(mParser.getErrorSelectionStart(),
                            mParser.getErrorSelectionEnd());
                }
                //mTextViewResult.setTextColor(ERROR_FOREGROUND_COLOR);
                mTextViewResult.setBackgroundColor(ERROR_BACKGROUND_COLOR);
                mTextViewResult.setText(mParser.getErrorMessage());
                setResultView(RESULT_VIEW_ERROR);
            } else {
                //mTextViewResult.setTextColor(RESULT_FOREGROUND_COLOR);
                mTextViewResult.setBackgroundColor(RESULT_BACKGROUND_COLOR);
                setResultView(RESULT_VIEW_NORMAL);
                //mTextViewResult.setText("= " + mParser.getResultString());
                mArrayAdapterHistories.remove(mParser.getExpression());
                mArrayAdapterHistories.add(mParser.getExpression());
                if (mListViewMode != LIST_VIEW_HISTORIES) {
                    setListView(LIST_VIEW_HISTORIES);
                } else {
                    if (! mMenuItemViewVariables.isVisible() && mParser.getVariablesSize() > 0) {
                        mMenuItemViewVariables.setVisible(true);
                    }
                    if (! mMenuItemDeleteAll.isVisible()) {
                        mMenuItemDeleteAll.setVisible(true);
                    }
                }
                mEditTextExpression.setText("");
                View view = getCurrentFocus();
                if (view != null) {
                    mInput.hideSoftInputFromWindow(view.getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        }
    }

    private void setListView(int mode) {
        switch (mode) {
            case LIST_VIEW_HISTORIES:
                mListView.setAdapter(mArrayAdapterHistories);
                mMenuItemViewHistories.setVisible(false);
                mMenuItemViewFunctions.setVisible(true);

                if (mParser.getVariablesSize() > 0)
                    mMenuItemViewVariables.setVisible(true);
                else
                    mMenuItemViewVariables.setVisible(false);

                if (mArrayAdapterHistories.getCount() > 0)
                    mMenuItemDeleteAll.setVisible(true);
                else
                    mMenuItemDeleteAll.setVisible(false);

                break;

            case LIST_VIEW_FUNCTIONS:
                mListView.setAdapter(mArrayAdapterFunctions);
                mMenuItemViewHistories.setVisible(true);
                mMenuItemViewFunctions.setVisible(false);

                if (mParser.getVariablesSize() > 0)
                    mMenuItemViewVariables.setVisible(true);
                else
                    mMenuItemViewVariables.setVisible(false);

                mMenuItemDeleteAll.setVisible(false);
                break;

            case LIST_VIEW_VARIABLES:
                String[] list = mParser.getVariablesList();
                mArrayAdapterVariables.clear();

                for (String s: list)
                    mArrayAdapterVariables.add(s);

                mListView.setAdapter(mArrayAdapterVariables);
                mMenuItemViewHistories.setVisible(true);
                mMenuItemViewFunctions.setVisible(true);
                mMenuItemViewVariables.setVisible(false);
                mMenuItemDeleteAll.setVisible(true);
        }

        mListViewMode = mode;
    }

    private void setResultView(int mode) {
        switch (mode) {
            case RESULT_VIEW_NORMAL:
                mTextViewResult.setText("= ".concat(mParser.getResultString()));
                mMenuItemResultNormal.setVisible(false);
                mMenuItemResultHex.setVisible(true);
                mMenuItemResultRealHex.setVisible(true);
                mMenuItemResultDMS.setVisible(true);
                break;

            case RESULT_VIEW_HEX:
                mTextViewResult.setText("= ".concat(mParser.toHexString()));
                mMenuItemResultNormal.setVisible(true);
                mMenuItemResultHex.setVisible(false);
                mMenuItemResultRealHex.setVisible(true);
                mMenuItemResultDMS.setVisible(true);
                break;

            case RESULT_VIEW_REAL_HEX:
                mTextViewResult.setText("= ".concat(mParser.toOriginalHexString()));
                mMenuItemResultNormal.setVisible(true);
                mMenuItemResultHex.setVisible(true);
                mMenuItemResultRealHex.setVisible(false);
                mMenuItemResultDMS.setVisible(true);
                break;

            case RESULT_VIEW_DMS:
                mTextViewResult.setText("= ".concat(mParser.toDMSString()));
                mMenuItemResultNormal.setVisible(true);
                mMenuItemResultHex.setVisible(true);
                mMenuItemResultRealHex.setVisible(true);
                mMenuItemResultDMS.setVisible(false);
                break;

            case RESULT_VIEW_ERROR:
                mMenuItemResultNormal.setVisible(false);
                mMenuItemResultHex.setVisible(false);
                mMenuItemResultRealHex.setVisible(false);
                mMenuItemResultDMS.setVisible(false);
        }
    }

    private void showDialogBox(int action) {
        mDialogAction = action;
        String msg = "";
        switch (action) {
            case ACTION_DELETE_ALL_HISTORY:
                mBuilder.setTitle(R.string.msg_delete_all_history);
                break;

            case ACTION_DELETE_ALL_VARIABLE:
                mBuilder.setTitle(R.string.msg_delete_all_variable);
                break;

            case ACTION_DELETE_ITEM_HISTORY:
                mBuilder.setTitle(R.string.msg_delete_item_history);
                msg = mListItemString;
                break;

            case ACTION_DELETE_ITEM_VARIABLE:
                mBuilder.setTitle(R.string.msg_delete_item_variable);
                msg = mListItemString;
        }
        mBuilder.setMessage(msg);
        AlertDialog dialog = mBuilder.create();
        dialog.setCancelable(false);
        dialog.show();
    }
}
