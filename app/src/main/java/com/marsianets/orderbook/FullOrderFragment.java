package com.marsianets.orderbook;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.method.NumberKeyListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static android.text.InputType.TYPE_CLASS_NUMBER;
import static android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL;

/**
 * Created by Yuri Golub on 16.09.13
 */
public class FullOrderFragment extends Fragment {

    private View mainLayout;
    private HashMap<String, Object> valueList;
    private Order currentOrder;
    private ArrayList<EditText> editFields;

    public interface FullFragmentListener {
        void saveOrderButtonClicked();
    }

    FullFragmentListener fragmentListener;

    public static FullOrderFragment newInstance(int orderId) {
        FullOrderFragment fragment = new FullOrderFragment();
        Bundle args = new Bundle();
        args.putInt("orderId", orderId);
        fragment.setArguments(args);
        return fragment;
    }

    public static FullOrderFragment newInstance(Date date) {
        FullOrderFragment fragment = new FullOrderFragment();
        Bundle args = new Bundle();
        args.putSerializable("execDate", date);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            fragmentListener = (FullFragmentListener) activity;
        } catch (ClassCastException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (container == null) {
            return null;
        }

        mainLayout = inflater.inflate(R.layout.full_order_fragment, null);

        if (getArguments() != null) {
            Date date = (Date) getArguments().getSerializable("execDate");
            int orderId = getArguments().getInt("orderId", 0);
            if (date != null) {
                Order order = new Order();
                order.setExecDate(date);
                showOrder(order);
            } else if (orderId != 0) {
                showOrder(Order.getOrderFromBase(orderId));
            }
        } else {
            showOrder(new Order());
        }

        return mainLayout;
    }

    @Override
    public void onDetach()
    {
        saveOrder(currentOrder);
        super.onDetach();
    }


    public void showOrder(Order order) {
        currentOrder = order;
        valueList = currentOrder.getOrderValues();
        editFields = new ArrayList<EditText>();

        if (mainLayout != null) {

            EditText clientNameEdit = mainLayout.findViewById(R.id.clientNameEdit);
            clientNameEdit.setOnKeyListener(onTextChanged);
            clientNameEdit.setOnFocusChangeListener(onFocusLost);
            clientNameEdit.setText(currentOrder.getClientName());
            clientNameEdit.setTag("clientName");
            editFields.add(clientNameEdit);


            EditText clientPhoneEdit = mainLayout.findViewById(R.id.clientPhoneEdit);
            clientPhoneEdit.setOnKeyListener(onTextChanged);
            clientPhoneEdit.setOnFocusChangeListener(onFocusLost);
            clientPhoneEdit.setText(currentOrder.getClientPhone());
            clientPhoneEdit.setTag("clientPhone");
            editFields.add(clientPhoneEdit);

            EditText carMakerEdit = mainLayout.findViewById(R.id.carMakerEdit);
            carMakerEdit.setOnKeyListener(onTextChanged);
            carMakerEdit.setOnFocusChangeListener(onFocusLost);
            carMakerEdit.setText(currentOrder.getCarMaker());
            carMakerEdit.setTag("carMaker");
            editFields.add(carMakerEdit);

            EditText colorCodeEdit = mainLayout.findViewById(R.id.colorCodeEdit);
            colorCodeEdit.setOnKeyListener(onTextChanged);
            colorCodeEdit.setOnFocusChangeListener(onFocusLost);
            colorCodeEdit.setText(currentOrder.getColorCode());
            colorCodeEdit.setTag("colorCode");
            editFields.add(colorCodeEdit);


            EditText volumeBaseEdit = mainLayout.findViewById(R.id.volumeBaseEdit);
            volumeBaseEdit.setOnKeyListener(onTextChanged);
            volumeBaseEdit.setOnFocusChangeListener(onFocusLost);
            volumeBaseEdit.setFilters(new InputFilter[]{new RealNumberInputFilter()});
            volumeBaseEdit.setText(currentOrder.getVolumeBase().toString());
            volumeBaseEdit.setTag("volumeBase");
            editFields.add(volumeBaseEdit);

            EditText volumeAdditionEdit = mainLayout.findViewById(R.id.volumeAdditionEdit);
            volumeAdditionEdit.setOnKeyListener(onTextChanged);
            volumeAdditionEdit.setOnFocusChangeListener(onFocusLost);
            volumeAdditionEdit.setFilters(new InputFilter[]{new RealNumberInputFilter()});
            volumeAdditionEdit.setText(currentOrder.getVolumeAddition().toString());
            volumeAdditionEdit.setTag("volumeAddition");
            editFields.add(volumeAdditionEdit);

            EditText orderDateEdit = mainLayout.findViewById(R.id.orderDateEdit);
            orderDateEdit.setOnKeyListener(onTextChanged);
            orderDateEdit.setOnFocusChangeListener(onFocusLost);
            orderDateEdit.setFilters(new InputFilter[]{new DateInputFilter()});
            orderDateEdit.setText(currentOrder.getOrderDate());
            orderDateEdit.setTag("orderDate");
            editFields.add(orderDateEdit);

            EditText execDateEdit = mainLayout.findViewById(R.id.execDateEdit);
            execDateEdit.setOnKeyListener(onTextChanged);
            execDateEdit.setOnFocusChangeListener(onFocusLost);
            execDateEdit.setFilters(new InputFilter[]{new DateInputFilter()});
            execDateEdit.setText(currentOrder.getExecDate());
            execDateEdit.setTag("execDate");
            editFields.add(execDateEdit);

            NumberKeyListener realKeyListener = new NumberKeyListener() {
                @Override
                protected char[] getAcceptedChars() {
                    return new char[]{'.', ',', '0', '1', '2', '3', '4', '5',
                            '6', '7', '8', '9'};
                }

                @Override
                public int getInputType() {
                    return TYPE_CLASS_NUMBER | TYPE_NUMBER_FLAG_DECIMAL;
                }
            };
            volumeBaseEdit.setKeyListener(realKeyListener);
            volumeAdditionEdit.setKeyListener(realKeyListener);
        }
    }


    public View.OnKeyListener onTextChanged = new View.OnKeyListener() {
        @Override
        public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
            EditText currentField = (EditText) view;
            View nextField = mainLayout.findViewById(view.getNextFocusDownId());
            String currentFieldTag = (String) currentField.getTag();
            if (keyEvent.getAction() == KeyEvent.ACTION_DOWN &&
                    (keyCode == KeyEvent.KEYCODE_ENTER)) {
                if (currentField.getText() != null) {
                    valueList.put(currentFieldTag, currentField.getText().toString());
                }
                if (nextField != null) {
                    nextField.requestFocus();
                }
                return true;
            }

            return false;
        }
    };

    private void saveOrder(Order orderToSave) {
        valueList.put("_id", currentOrder.getOrderId());
        for (EditText fieldToSave : editFields) {
            if (fieldToSave.getText() != null) {
                valueList.put(fieldToSave.getTag().toString(), fieldToSave.getText().toString());
            }
        }
        orderToSave.setOrderValues(valueList);
        if (orderToSave.save()) {
            Toast toast = Toast.makeText(MainActivity.appContext, "Order " + orderToSave.getOrderId() +
                     orderToSave.getExecDate() + " saved", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    View.OnFocusChangeListener onFocusLost = new View.OnFocusChangeListener() {

        public void onFocusChange(View view, boolean hasFocus) {
            EditText currentEditField = (EditText) view;
            if (!hasFocus) {
                if (currentEditField.getText() != null) {
                    valueList.put(currentEditField.getTag().toString(), currentEditField.getText().toString());
                }
            }
        }
    };

}
