package com.marsianets.orderbook;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.SimpleExpandableListAdapter;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.TreeMap;

public class SummaryOrderFragment extends Fragment {

    private final Context context = MainActivity.appContext;
    private View mainLayout;
    private final int IDM_DELETE = 101;
    private final int IDM_ADD_ORDER = 102;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("EEEEEEEEEEE, dd.MM.yy");

    public interface SummaryFragmentListener {
        void newOrderButtonClicked(Date date);

        void currentOrderClicked(int orderId);

        void deleteCurrentOrder(int orderId);
    }

    SummaryFragmentListener fragmentListener;

    @Override
    public void onActivityCreated(Bundle savedInstance) {
        super.onActivityCreated(savedInstance);
        ExpandableListView summaryOrderListView =
                getActivity().findViewById(R.id.summaryOrdersListView);
        for (int groupPos = 0; groupPos < summaryOrderListView.getExpandableListAdapter().getGroupCount(); groupPos++) {
            summaryOrderListView.expandGroup(groupPos);
        }
        registerForContextMenu(summaryOrderListView);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainLayout = inflater.inflate(R.layout.summary_order_fragment, container, false);
        showOrders();

        return mainLayout;
    }

    public void showOrders() {
        ArrayList<Order> allOrders = Order.getAllOrdersFromBase();
        TreeMap<Date, ArrayList<Order>> ordersSortedByDate = Order.sortByDate(allOrders);

        ArrayList<HashMap<String, String>> groupData = new ArrayList<HashMap<String, String>>();
        ArrayList<ArrayList<HashMap<String, String>>> childData =
                new ArrayList<ArrayList<HashMap<String, String>>>();
        for (Date date : ordersSortedByDate.keySet()) {
            HashMap<String, String> groupItem = new HashMap<String, String>();
            groupItem.put("execDate", dateFormat.format(date));
            groupData.add(groupItem);

            ArrayList<HashMap<String, String>> childDataItem = new ArrayList<HashMap<String, String>>();
            for (Order currentOrder : ordersSortedByDate.get(date)) {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("_id", String.valueOf(currentOrder.getOrderId()));
                map.put("clientName", currentOrder.getClientName());
                map.put("carMaker", currentOrder.getCarMaker());
                map.put("colorCode", currentOrder.getColorCode());
                childDataItem.add(map);
            }
            childData.add(childDataItem);
        }

        String[] groupFrom = new String[]{"execDate"};
        int[] groupTo = new int[]{R.id.execDateView};
        String[] childFrom = new String[]{"clientName", "carMaker", "colorCode"};
        int[] childTo = new int[]{R.id.clientNameView, R.id.carMakerView, R.id.colorCodeView};

        SimpleExpandableListAdapter sExpListAdapter = new SimpleExpandableListAdapter(
                getActivity(),
                groupData,
                R.layout.expandable_group_item,
                groupFrom,
                groupTo,
                childData,
                R.layout.expandable_list_item,
                childFrom,
                childTo);

        ExpandableListView summaryOrdersListView =
                mainLayout.findViewById(R.id.summaryOrdersListView);
        summaryOrdersListView.setAdapter(sExpListAdapter);
        summaryOrdersListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View child, int groupPosition, int childPosition, long id) {
                try {
                    HashMap<String, String> selectedItemMap =
                            (HashMap<String, String>) parent.getExpandableListAdapter().getChild(groupPosition, childPosition);
                    fragmentListener.currentOrderClicked(Integer.parseInt(selectedItemMap.get("_id")));
                } catch (Exception ex) {
                    Toast.makeText(context, ex.toString(), Toast.LENGTH_LONG).show();
                }
                return true;
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            fragmentListener = (SummaryFragmentListener) activity;
        } catch (ClassCastException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        ExpandableListView.ExpandableListContextMenuInfo listMenuInfo =
                (ExpandableListView.ExpandableListContextMenuInfo) menuInfo;
        ExpandableListView summaryOrderListView =
                getActivity().findViewById(R.id.summaryOrdersListView);
        long selectedItemPos = listMenuInfo.packedPosition;
        for (int groupPos = 0; groupPos < summaryOrderListView.getExpandableListAdapter().getGroupCount(); groupPos++) {
            long groupPackedPosition = ExpandableListView.getPackedPositionForGroup(groupPos);
            if (groupPackedPosition == selectedItemPos) {
                HashMap<String, String> currentGroupMap = (HashMap<String, String>) summaryOrderListView.getExpandableListAdapter().getGroup(groupPos);
                String currentGroupDate = currentGroupMap.get("execDate");
                menu.add(Menu.NONE, IDM_ADD_ORDER, Menu.NONE, "Новый заказ на " + currentGroupDate);
                return;
            }
        }
        menu.add(Menu.NONE, IDM_DELETE, Menu.NONE, "Удалить");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case IDM_DELETE:
                try {
                    ExpandableListView.ExpandableListContextMenuInfo info =
                            (ExpandableListView.ExpandableListContextMenuInfo) item.getMenuInfo();
                    ExpandableListView parent = getActivity().findViewById(R.id.summaryOrdersListView);
                    HashMap<String, String> selectedOrderMap =
                            (HashMap<String, String>) parent.getAdapter().getItem(parent.getFlatListPosition(info.packedPosition));
                    fragmentListener.deleteCurrentOrder(Integer.parseInt(selectedOrderMap.get("_id")));
                } catch (Exception ex) {
                    Toast.makeText(context, ex.toString(), Toast.LENGTH_LONG).show();
                }
                return true;
            case IDM_ADD_ORDER:
                try {
                    ExpandableListView.ExpandableListContextMenuInfo info =
                            (ExpandableListView.ExpandableListContextMenuInfo) item.getMenuInfo();
                    ExpandableListView parent = getActivity().findViewById(R.id.summaryOrdersListView);
                    HashMap<String, String> selectedOrderMap =
                            (HashMap<String, String>) parent.getAdapter().getItem(parent.getFlatListPosition(info.packedPosition));
                    fragmentListener.newOrderButtonClicked(dateFormat.parse(selectedOrderMap.get("execDate")));
                } catch (Exception ex) {
                    Toast.makeText(context, ex.toString(), Toast.LENGTH_LONG).show();
                }
                return true;
            default:
                return super.onContextItemSelected(item);

        }
    }

}
