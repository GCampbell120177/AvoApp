package za.co.appwitch.avoapp;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.HashMap;

import io.paperdb.Paper;
import za.co.appwitch.avoapp.models.OrdersModel;
import za.co.appwitch.avoapp.models.StockModel;
import za.co.appwitch.avoapp.models.UserModel;

public class HomeActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference stockRef, userRef, orderRef;

    Date date;

    TextView currentBagAmount, minimumOrderTotal;
    EditText numberOfBagsOrderd;
    AppCompatButton orderAvo, processOrder, checkOrdersButton;
    LinearLayout processOrderLL;
    AppCompatImageButton notifications, deliveryDateNotifications;

    int totalStock, amendStock, orderID;
    String userID, userFullName, userMobile, userEmail, userStreet, userSuburb, userCity, currentOrder;
    String deliveryCost;
    int finalOrderID = 0;
    String sFinalOrderID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Paper.init(this);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        stockRef = database.getReference("stock");
        userRef = database.getReference("users");
        orderRef = database.getReference();

        notifications = findViewById(R.id.notifications);
        notifications.setVisibility(View.INVISIBLE);
        deliveryDateNotifications = findViewById(R.id.delivery_date_notification);
        deliveryDateNotifications.setVisibility(View.INVISIBLE);
        numberOfBagsOrderd = findViewById(R.id.number_of_bags_to_order_edit_text);
        currentBagAmount = findViewById(R.id.current_bag_amount_text_view);
        minimumOrderTotal = findViewById(R.id.minimum_order_total_text_view);
        processOrderLL = findViewById(R.id.order_some_avocados_linear_layout);
        orderAvo = findViewById(R.id.order_some_avocados_button);
        orderAvo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processOrderLL.setVisibility(View.VISIBLE);
                GetMinimumDeliveryAmount();
            }
        });

        processOrder = findViewById(R.id.process_order_button);
        processOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Paper.book().write(Constants.userOrderStatus, "orderNotPlaced");
                ProcessOrder();
            }
        });

        checkOrdersButton = findViewById(R.id.check_orders_button);
        checkOrdersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckOrders();
            }
        });

        GetStockTotals();
        GetUserDetails();
    }

    private void CheckOrders() {
        Intent checkOrders = new Intent(HomeActivity.this, CheckOrdersHistoryActivity.class);
        startActivity(checkOrders);
    }


    private void GetMinimumDeliveryAmount() {
        userRef.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot s : snapshot.getChildren()) {
                    OrdersModel orderModel = snapshot.getValue(OrdersModel.class);
                    if (orderModel != null) {
                        if (orderModel.getUserCity().equals("Centurion")) {
                            int minimumDelivery = 2;
                            minimumOrderTotal.setText(String.format("%s", String.valueOf(minimumDelivery)));
                        }
                        if (orderModel.getUserCity().equals("KrugersDorp")) {
                            int minimumDelivery = 4;
                            minimumOrderTotal.setText(String.format("%s", String.valueOf(minimumDelivery)));
                        }
                        if (orderModel.getUserCity().equals("Roodepoort")) {
                            int minimumDelivery = 3;
                            minimumOrderTotal.setText(String.format("%s", String.valueOf(minimumDelivery)));
                        }
                        if (orderModel.getUserCity().equals("Pretoria")) {
                            int minimumDelivery = 3;
                            minimumOrderTotal.setText(String.format("%s", String.valueOf(minimumDelivery)));
                        }
                        if (orderModel.getUserCity().equals("BedfordView")) {
                            int minimumDelivery = 3;
                            minimumOrderTotal.setText(String.format("%s", String.valueOf(minimumDelivery)));
                        }
                        if (orderModel.getUserCity().equals("Fourways")) {
                            int minimumDelivery = 3;
                            minimumOrderTotal.setText(String.format("%s", String.valueOf(minimumDelivery)));
                        }
                        if (orderModel.getUserCity().equals("Sandton")) {
                            int minimumDelivery = 3;
                            minimumOrderTotal.setText(String.format("%s", String.valueOf(minimumDelivery)));
                        }
                        if (orderModel.getUserCity().equals("Irene")) {
                            int minimumDelivery = 3;
                            minimumOrderTotal.setText(String.format("%s", String.valueOf(minimumDelivery)));
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void GetUserDetails() {
        userID = mAuth.getUid();
        if (userID != null) {
            userRef.child(userID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot s : snapshot.getChildren()) {
                        UserModel userModel = snapshot.getValue(UserModel.class);
                        if (userModel != null) {
                            userFullName = userModel.getUserFullName();
                            userMobile = userModel.getUserMobile();
                            userEmail = userModel.getUserEmail();
                            userStreet = userModel.getUserStreet();
                            userSuburb = userModel.getUserSuburb();
                            userCity = userModel.getUserCity();
                            currentOrder = userModel.getCurrentOrder();
                            if (currentOrder == null)
                            {
                                currentOrder = "false";
                            }
                            if (currentOrder.equals("true"))
                            {
                                ShowNotification(userID);
                            }
                        } else {
                            Toast.makeText(HomeActivity.this, "user data is NULL", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    private void ShowNotification(String userID) {
        orderRef.child("preOrders").child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot s : snapshot.getChildren())
                {
                    OrdersModel ordersModel = snapshot.getValue(OrdersModel.class);
                    if (!ordersModel.getDeliveryConfirmed().equals("false"))
                    {
                        deliveryDateNotifications.setVisibility(View.VISIBLE);
                        deliveryDateNotifications.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent acceptDelivery = new Intent(getApplicationContext(), AcceptDeliveryActivity.class);
                                startActivity(acceptDelivery);
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void GetStockTotals() {
        stockRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot s : snapshot.getChildren()) {
                    StockModel stockModel = snapshot.getValue(StockModel.class);
                    if (stockModel != null) {
                        String stockQuantity = stockModel.getStockQuantity();
                        totalStock = Integer.parseInt(stockQuantity);
                        String currentBags;
                        int finalStockAmount;
                        int finalBagAmount;
                        finalStockAmount = Integer.parseInt(stockQuantity);
                        finalBagAmount = finalStockAmount;
                        currentBags = String.valueOf(finalBagAmount);
                        currentBagAmount.setText(String.format("Bags: %s", currentBags));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void ProcessOrder() {

        userID = mAuth.getUid();
        String orderStatus = Paper.book().read(Constants.userOrderStatus);
        if (!(orderStatus.equals("orderPlaced"))) {
            int bagsOrdered = Integer.parseInt(numberOfBagsOrderd.getText().toString());
            int minimumOrder = Integer.parseInt(minimumOrderTotal.getText().toString());
            String orderAmount = String.valueOf(numberOfBagsOrderd.getText());
            deliveryCost = String.valueOf(minimumOrder * 45);
            if (((bagsOrdered) + 0.1) <= minimumOrder) {
                String finalOrderAmount = orderAmount;
                new AlertDialog.Builder(this)
                        .setTitle(" Accept Delivery Cost" +
                                "\n Fee: R" + deliveryCost)
                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which)
                            {
                                Paper.book().write(Constants.userDeliveryCostAcceptKey, "true");
                                if (userID != null) {
                                    String delivery = finalOrderAmount + deliveryCost;
                                    HashMap<String, Object> orderMap = new HashMap<>();
                                    orderMap.put("order", delivery);
                                    orderMap.put("orderID", "0");
                                    orderMap.put("userID", userID);
                                    orderMap.put("userFullName", userFullName);
                                    orderMap.put("userMobile", userMobile);
                                    orderMap.put("userEmail", userEmail);
                                    orderMap.put("userStreet", userStreet);
                                    orderMap.put("userSuburb", userSuburb);
                                    orderMap.put("userCity", userCity);
                                    orderMap.put("paidDelivery", "true");
                                    orderMap.put("deliveryFee", deliveryCost);
                                    orderMap.put("cod", "false");
                                    orderMap.put("eft", "false");
                                    orderMap.put("orderStatus", "preOrder");

                                    orderRef.child("preOrders").child(userID).updateChildren(orderMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(HomeActivity.this, "Your Order Has Been Processed", Toast.LENGTH_SHORT).show();
                                            amendStock = Integer.parseInt(finalOrderAmount);
                                            int finalStockAmount = totalStock - amendStock;
                                            String _finalStockAmount = String.valueOf(finalStockAmount);
                                            HashMap<String, Object> stockMap = new HashMap<>();
                                            stockMap.put("stockQuantity", _finalStockAmount);
                                            stockRef.updateChildren(stockMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    Paper.book().write(Constants.userOrderStatus, "orderPlaced");
                                                    Intent declarePayment = new Intent(HomeActivity.this, OrderAvocadosActivity.class);
                                                    startActivity(declarePayment);
                                                }
                                            });

                                        }
                                    });
                                }
                            }
                        })
                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Paper.book().write(Constants.userDeliveryCostAcceptKey, "false");
                                Intent freeDelivery = new Intent(HomeActivity.this, HomeActivity.class);
                                startActivity(freeDelivery);
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            } else {
                if (userID != null) {
                    String sOrderID = String.valueOf(orderID);
                    int orderRandValue = Integer.parseInt(orderAmount) * 100;
                    orderAmount = String.valueOf(orderRandValue);
                    HashMap<String, Object> orderMap = new HashMap<>();
                    orderMap.put("order", orderAmount);
                    orderMap.put("orderID", "0");
                    orderMap.put("userID", userID);
                    orderMap.put("userFullName", userFullName);
                    orderMap.put("userMobile", userMobile);
                    orderMap.put("userEmail", userEmail);
                    orderMap.put("userStreet", userStreet);
                    orderMap.put("userSuburb", userSuburb);
                    orderMap.put("userCity", userCity);
                    orderMap.put("paidDelivery", "false");
                    orderMap.put("deliveryFee", "included");
                    orderMap.put("cod", "false");
                    orderMap.put("eft", "false");
                    orderMap.put("orderStatus", "preOrder");

                    String finalOrderAmount1 = orderAmount;
                    orderRef.child("preOrders").child(userID).updateChildren(orderMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(HomeActivity.this, "Your Order Has Been Processed", Toast.LENGTH_SHORT).show();
                            amendStock = Integer.parseInt(finalOrderAmount1);
                            int finalStockAmount = totalStock - amendStock;
                            String _finalStockAmount = String.valueOf(finalStockAmount);
                            HashMap<String, Object> stockMap = new HashMap<>();
                            stockMap.put("stockQuantity", _finalStockAmount);
                            stockRef.updateChildren(stockMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Paper.book().write(Constants.userOrderStatus, "orderPlaced");

                                    Intent declarePayment = new Intent(HomeActivity.this, OrderAvocadosActivity.class);
                                    startActivity(declarePayment);
                                }
                            });
                        }
                    });
                }
            }
        } else {
            processOrder.setVisibility(View.GONE);
        }
    }
}