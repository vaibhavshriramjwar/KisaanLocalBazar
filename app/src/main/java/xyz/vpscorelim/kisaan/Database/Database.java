package xyz.vpscorelim.kisaan.Database;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

import xyz.vpscorelim.kisaan.customer.CustomerOrderModel;
import xyz.vpscorelim.kisaan.customer.ViewMyCart;

public class Database extends SQLiteAssetHelper {
    private static final String DB_NAME = "CustOrder.db";
    private static final int DB_VER = 1;
    Context context;


    public Database(Context context) {

        super(context, DB_NAME,null, DB_VER);
    }


    public String checkFoodExists(){
        SQLiteDatabase db = getReadableDatabase();
        String flag = "xyz";
        Cursor resultSet = db.rawQuery("SELECT FarmerID FROM CustomerOrder ORDER BY ROWID ASC LIMIT 1",null);
        resultSet.moveToNext();
        if(resultSet.getCount()==0){
            flag = "null";
        }
        else if (resultSet.getCount()>0){
            flag = resultSet.getString(resultSet.getColumnIndex("FarmerID"));
        }
        resultSet.close();
        return flag;
    }


    public List<CustomerOrderModel> getCarts(String UserPhone)
    {

        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        String[] sqlSelect = {"UserPhone","ProductID","ProductName","Quantity","Price","Unit","FarmerID","CustomerID","ProductImage"};
        String sqlTable ="CustomerOrder";
        qb.setTables(sqlTable);
        Cursor c = qb.query(db,sqlSelect,"UserPhone=?",new String[] {UserPhone},null,null,null);
        final List<CustomerOrderModel> result = new ArrayList<>();
        if(c.moveToFirst()){
            do{
                result.add(new CustomerOrderModel(
                        c.getString(c.getColumnIndex("UserPhone")),
                        c.getString(c.getColumnIndex("ProductID")),
                        c.getString(c.getColumnIndex("ProductName")),
                        c.getString(c.getColumnIndex("Quantity")),
                        c.getString(c.getColumnIndex("Price")),
                        c.getString(c.getColumnIndex("Unit")),
                        c.getString(c.getColumnIndex("FarmerID")),
                        c.getString(c.getColumnIndex("CustomerID")),
                        c.getString(c.getColumnIndex("ProductImage"))
                ));
            }while (c.moveToNext());
        }
        return  result;

    }


    public void check(CustomerOrderModel customerOrderModel){





    }


    public  void addToCart(CustomerOrderModel customerOrderModel)
    {
        SQLiteDatabase db = getReadableDatabase();


            //resultSet.moveToFirst();
            String query = String.format("INSERT OR REPLACE INTO CustomerOrder(UserPhone,ProductID,ProductName,Quantity,Price,Unit,FarmerID,CustomerID,ProductImage) VALUES('%s','%s','%s','%s','%s','%s','%s','%s','%s');",
                    customerOrderModel.getUserPhone(),
                    customerOrderModel.getProductID(),
                    customerOrderModel.getProductName(),
                    customerOrderModel.getQuantity(),
                    customerOrderModel.getPrice(),
                    customerOrderModel.getUnit(),
                    customerOrderModel.getFarmerID(),
                    customerOrderModel.getCustomerID(),
                    customerOrderModel.getProductImage());
            db.execSQL(query);





    }


    public  void cleanCart()
    {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM CustomerOrder");
        db.execSQL(query);
    }


    public  void deleteItem(CustomerOrderModel customerOrderModel)
    {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM CustomerOrder WHERE ProductID='%s'",customerOrderModel.getProductID());
        db.execSQL(query);
    }

    public void updateCartItem(CustomerOrderModel orderModel) {

        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("UPDATE CustomerOrder SET Quantity= '%s' WHERE UserPhone= '%s' AND ProductID='%s'",orderModel.getQuantity(),orderModel.getUserPhone(),orderModel.getProductID());
        db.execSQL(query);

    }
}
