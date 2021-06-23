package za.co.appwitch.avoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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

import java.util.HashMap;

import io.paperdb.Paper;
import za.co.appwitch.avoapp.models.OrdersModel;

public class OrderAvocadosActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference orderRef;
    FirebaseAuth mAuth;

    LinearLayout codLL, eftLL, inAppLL;
    TextView codTotalAmount, eftTotalAmount, inAppTotalAmount;
    CheckBox codCB, eftCB, inAppCB;

    AppCompatButton finaliseOrderForDeliveryButton, checkOrdersButton;

    String userID, orderID;
    String orderValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_avocados);
        Paper.init(this);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        orderRef = database.getReference();

        userID = mAuth.getUid();

        codLL = findViewById(R.id.cod_linear_layout);
        eftLL = findViewById(R.id.eft_linear_layout);
        inAppLL = findViewById(R.id.in_app_linear_layout);
        codTotalAmount = findViewById(R.id.cod_total_price_text_view);
        eftTotalAmount = findViewById(R.id.eft_total_price_text_view);
        inAppTotalAmount = findViewById(R.id.in_app_total_price_text_view);
        codCB = findViewById(R.id.cod_check_box);
        codCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (codCB.isChecked())
                {
                    codLL.setVisibility(View.VISIBLE);
                    codTotalAmount = findViewById(R.id.cod_total_price_text_view);
                    GetTotals();
                }
                else
                {
                    codLL.setVisibility(View.GONE);
                }
            }
        });

        eftCB = findViewById(R.id.eft_check_box);
        eftCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (eftCB.isChecked())
                {
                    eftLL.setVisibility(View.VISIBLE);
                    eftTotalAmount = findViewById(R.id.eft_total_price_text_view);
                    GetTotals();
                }
                else
                {
                    eftLL.setVisibility(View.GONE);
                }
            }
        });

        inAppCB = findViewById(R.id.in_app_check_box);
        inAppCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (inAppCB.isChecked())
                {
                    inAppLL.setVisibility(View.VISIBLE);
                    inAppTotalAmount = findViewById(R.id.in_app_total_price_text_view);
                    GetTotals();
                }
                else
                {
                    inAppLL.setVisibility(View.GONE);
                }
            }
        });

        finaliseOrderForDeliveryButton = findViewById(R.id.finalise_order_for_delivery);
        finaliseOrderForDeliveryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (codCB.isChecked())
                {
                    HashMap<String, Object> codMap = new HashMap<>();
                    codMap.put("userID", userID);
                    codMap.put("orderID", orderID);
                    codMap.put("cod", "true");
                    codMap.put("eft", "false");
                    codMap.put("orderStatus", "onOrder");
                    codMap.put("paymentStatus", "COD:paymentOnDelivery");
                    codMap.put("deliveryDate", "toBeConfirmed");
                    codMap.put("deliveryConfirmed", "false");
                    orderRef.child("preOrders").child(userID).updateChildren(codMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                            {
                                Toast.makeText(OrderAvocadosActivity.this, "Order Placed, You will be notified of the delivery date shortly", Toast.LENGTH_SHORT).show();

                                Intent codIntent = new Intent(OrderAvocadosActivity.this, HomeActivity.class);
                                startActivity(codIntent);
                                finish();
                            }
                        }
                    });
                }
                if (eftCB.isChecked())
                {
                    HashMap<String, Object> eftMap = new HashMap<>();
                    eftMap.put("userID", userID);
                    eftMap.put("orderID", orderID);
                    eftMap.put("cod", "false");
                    eftMap.put("eft", "true");
                    eftMap.put("orderValue", orderValue);
                    eftMap.put("orderStatus", "onOrder");
                    eftMap.put("paymentStatus", "EFT:waitingPayment");
                    eftMap.put("deliveryDate", "toBeConfirmed");
                    eftMap.put("deliveryConfirmed", "false");
                    orderRef.child("preOrders").child(userID).updateChildren(eftMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                            {
                                Toast.makeText(OrderAvocadosActivity.this, "Order Placed, You will be notified of the delivery date after payment reflects", Toast.LENGTH_SHORT).show();

                                Intent eftIntent = new Intent(OrderAvocadosActivity.this, HomeActivity.class);
                                startActivity(eftIntent);
                                finish();
                            }
                        }
                    });
                }
            }
        });

        GetOrderID();
    }

    private void GetOrderID() {
        orderRef.child("preOrders").child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot s : snapshot.getChildren())
                {
                    OrdersModel orderModel = snapshot.getValue(OrdersModel.class);
                    if (orderModel != null)
                    {
                        int intOrderID = Integer.parseInt(orderModel.getOrderID()) + 1;
                        orderID = String.valueOf(intOrderID);
                    }
                    else
                    {
                        Toast.makeText(OrderAvocadosActivity.this, "No order ID has been Set", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void GetTotals() {
        orderRef.child("preOrders").child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot s : snapshot.getChildren())
                {
                    OrdersModel orderModel = snapshot.getValue(OrdersModel.class);
                    if (orderModel != null) {
                        if (codLL.getVisibility() == View.VISIBLE) {
                            codTotalAmount = findViewById(R.id.cod_total_price_text_view);
                            eftCB.setChecked(false);
                            inAppCB.setChecked(false);
                            if (orderModel.getPaidDelivery().equals("false")) {
                                orderValue = (orderModel.getOrder());
                                codTotalAmount.setText(String.valueOf(orderValue));
                            }
                            if (orderModel.getPaidDelivery().equals("true"))
                            {
                                int orderAmount = Integer.parseInt(orderModel.getOrder());
                                int deliveryCost = Integer.parseInt(orderModel.getDeliveryFee());
                                int totalCost = orderAmount + deliveryCost;

                                orderValue =  String.valueOf(totalCost);
                                codTotalAmount.setText(orderValue);
                            }
                        }
                        if (eftLL.getVisibility() == View.VISIBLE) {
                            eftTotalAmount = findViewById(R.id.eft_total_price_text_view);
                            codCB.setChecked(false);
                            inAppCB.setChecked(false);
                            if (orderModel.getPaidDelivery().equals("true")) {
                                orderValue = (orderModel.getOrder());
                                eftTotalAmount.setText(String.valueOf(orderValue));
                            }
                            else
                            {
                                int orderAmount = Integer.parseInt(orderModel.getOrder());
                                int deliveryCost = Integer.parseInt(orderModel.getDeliveryFee());
                                int totalCost = orderAmount + deliveryCost;

                                orderValue =  String.valueOf(totalCost);
                                eftTotalAmount.setText(orderValue);
                            }
                        }
                        if (inAppLL.getVisibility() == View.VISIBLE) {
                            inAppTotalAmount = findViewById(R.id.in_app_total_price_text_view);
                            codCB.setChecked(false);
                            eftCB.setChecked(false);
                            if (orderModel.getPaidDelivery().equals("true")) {
                                orderValue = orderModel.getOrder();
                                inAppTotalAmount.setText(String.valueOf(orderValue));
                            }
                            else
                            {
                                int orderAmount = Integer.parseInt(orderModel.getOrder());
                                int deliveryCost = Integer.parseInt(orderModel.getDeliveryFee());
                                int totalCost = orderAmount + deliveryCost;

                                orderValue =  String.valueOf(totalCost);
                                inAppTotalAmount.setText(String.valueOf(orderValue));
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}