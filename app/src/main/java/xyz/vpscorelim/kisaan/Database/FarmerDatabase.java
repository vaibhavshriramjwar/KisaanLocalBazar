package xyz.vpscorelim.kisaan.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

import xyz.vpscorelim.kisaan.customer.CustomerOrderModel;
import xyz.vpscorelim.kisaan.farmer.FarmerOrderModel;

public class FarmerDatabase extends SQLiteAssetHelper {

    private static final String DB_NAME = "new.db";
    private static final int DB_VER = 1;
    Context context;

    public FarmerDatabase(Context context) {

        super(context, DB_NAME,null, DB_VER);
    }

    public String checkFoodExists(){
        SQLiteDatabase db = getReadableDatabase();
        String flag = "xyz";
        Cursor resultSet = db.rawQuery("SELECT DealerID FROM FarmerOrder ORDER BY ROWID ASC LIMIT 1",null);
        resultSet.moveToNext();
        if(resultSet.getCount()==0){
            flag = "null";
        }
        else if (resultSet.getCount()>0){
            flag = resultSet.getString(resultSet.getColumnIndex("DealerID"));
        }
        resultSet.close();
        return flag;
    }


    public List<FarmerOrderModel> getCarts(String UserPhone)
    {

        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        String[] sqlSelect = {"FarmerPhone","ProductID","ProductName","Quantity","Price","Unit","DealerID","FarmerID","ProductImage","ProductBrand"};
        String sqlTable ="FarmerOrder";
        qb.setTables(sqlTable);
        Cursor c = qb.query(db,sqlSelect,"FarmerPhone=?",new String[] {UserPhone},null,null,null);
        final List<FarmerOrderModel> result = new ArrayList<>();
        if(c.moveToFirst()){
            do{
                result.add(new FarmerOrderModel(
                        c.getString(c.getColumnIndex("FarmerPhone")),
                        c.getString(c.getColumnIndex("ProductID")),
                        c.getString(c.getColumnIndex("ProductName")),
                        c.getString(c.getColumnIndex("Quantity")),
                        c.getString(c.getColumnIndex("Price")),
                        c.getString(c.getColumnIndex("Unit")),
                        c.getString(c.getColumnIndex("DealerID")),
                        c.getString(c.getColumnIndex("FarmerID")),
                        c.getString(c.getColumnIndex("ProductImage")),
                        c.getString(c.getColumnIndex("ProductBrand"))

                ));
            }while (c.moveToNext());
        }
        return  result;

    }


    public void check(FarmerOrderModel customerOrderModel){





    }



    public  void addToCart(FarmerOrderModel customerOrderModel)
    {
        SQLiteDatabase db = getReadableDatabase();


        //resultSet.moveToFirst();
        String query = String.format("INSERT OR REPLACE INTO FarmerOrder(FarmerPhone,ProductID,ProductName,Quantity,Price,Unit,DealerID,FarmerID,ProductImage,ProductBrand) VALUES('%s','%s','%s','%s','%s','%s','%s','%s','%s','%s');",
                customerOrderModel.getFarmerPhone(),
                customerOrderModel.getProductID(),
                customerOrderModel.getProductName(),
                customerOrderModel.getQuantity(),
                customerOrderModel.getPrice(),
                customerOrderModel.getUnit(),
                customerOrderModel.getDealerID(),
                customerOrderModel.getFarmerID(),
                customerOrderModel.getProductImage(),
                customerOrderModel.getProductBrand());

        db.execSQL(query);





    }


    public  void cleanCart()
    {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM FarmerOrder");
        db.execSQL(query);
    }



    public  void deleteItem(FarmerOrderModel customerOrderModel)
    {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM FarmerOrder WHERE ProductID='%s'",customerOrderModel.getProductID());
        db.execSQL(query);
    }


    public void updateCartItem(FarmerOrderModel orderModel) {

        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("UPDATE FarmerOrder SET Quantity= '%s' WHERE FarmerPhone= '%s' AND ProductID='%s'",orderModel.getQuantity(),orderModel.getFarmerPhone(),orderModel.getProductID());
        db.execSQL(query);

    }



}
