package xyz.vpscorelim.kisaan.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

import xyz.vpscorelim.kisaan.customer.CustomerOrderModel;
import xyz.vpscorelim.kisaan.dealer.DealerModel.DealerOrderListModel;

public class DealerDatabase extends SQLiteAssetHelper {
    private static final String DB_NAME = "DealerOrderDB.db";
    private static final int DB_VER = 1;
    Context context;


    public DealerDatabase(Context context) {

        super(context, DB_NAME, null, DB_VER);
    }


    public String checkFoodExists() {
        SQLiteDatabase db = getReadableDatabase();
        String flag = "xyz";
        Cursor resultSet = db.rawQuery("SELECT FarmerID FROM DealerOrder ORDER BY ROWID ASC LIMIT 1", null);
        resultSet.moveToNext();
        if (resultSet.getCount() == 0) {
            flag = "null";
        } else if (resultSet.getCount() > 0) {
            flag = resultSet.getString(resultSet.getColumnIndex("FarmerID"));
        }
        resultSet.close();
        return flag;
    }


    public List<DealerOrderListModel> getCarts(String UserPhone) {

        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        String[] sqlSelect = {"UserPhone", "ProductID", "ProductName", "Quantity", "Price", "Unit", "FarmerID", "DealerID", "ProductImage"};
        String sqlTable = "DealerOrder";
        qb.setTables(sqlTable);
        Cursor c = qb.query(db, sqlSelect, "UserPhone=?", new String[]{UserPhone}, null, null, null);
        final List<DealerOrderListModel> result = new ArrayList<>();
        if (c.moveToFirst()) {
            do {
                result.add(new DealerOrderListModel(
                        c.getString(c.getColumnIndex("UserPhone")),
                        c.getString(c.getColumnIndex("ProductID")),
                        c.getString(c.getColumnIndex("ProductName")),
                        c.getString(c.getColumnIndex("Quantity")),
                        c.getString(c.getColumnIndex("Price")),
                        c.getString(c.getColumnIndex("Unit")),
                        c.getString(c.getColumnIndex("FarmerID")),
                        c.getString(c.getColumnIndex("DealerID")),
                        c.getString(c.getColumnIndex("ProductImage"))
                ));
            } while (c.moveToNext());
        }
        return result;

    }


    public void check(DealerOrderListModel customerOrderModel) {


    }


    public void addToCart(DealerOrderListModel customerOrderModel) {
        SQLiteDatabase db = getReadableDatabase();


        //resultSet.moveToFirst();
        String query = String.format("INSERT OR REPLACE INTO DealerOrder(UserPhone,ProductID,ProductName,Quantity,Price,Unit,FarmerID,DealerID,ProductImage) VALUES('%s','%s','%s','%s','%s','%s','%s','%s','%s');",
                customerOrderModel.getUserPhone(),
                customerOrderModel.getProductID(),
                customerOrderModel.getProductName(),
                customerOrderModel.getQuantity(),
                customerOrderModel.getPrice(),
                customerOrderModel.getUnit(),
                customerOrderModel.getFarmerID(),
                customerOrderModel.getDealerID(),
                customerOrderModel.getProductImage());
        db.execSQL(query);


    }


    public void cleanCart() {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM DealerOrder");
        db.execSQL(query);
    }


    public void deleteItem(DealerOrderListModel customerOrderModel) {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM DealerOrder WHERE ProductID='%s'", customerOrderModel.getProductID());
        db.execSQL(query);
    }

    public void updateCartItem(DealerOrderListModel orderModel) {

        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("UPDATE DealerOrder SET Quantity= '%s' WHERE UserPhone= '%s' AND ProductID='%s'", orderModel.getQuantity(), orderModel.getUserPhone(), orderModel.getProductID());
        db.execSQL(query);

    }

}