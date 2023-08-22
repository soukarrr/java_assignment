package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Optional;
import java.util.ResourceBundle;

public class ItemController implements Initializable {
    private Database database;
    private Connection connect;
    private PreparedStatement prepare;
    private Statement statement;
    private ResultSet result;

    @FXML
    private Button addItems_addBtn;

    @FXML
    private TextField order_amount;
    @FXML
    private TableColumn<customerData, String> orders_col_itemBarcode;
    @FXML
    private AnchorPane itemForm;

    @FXML
    private Label order_change;
    @FXML
    private Button add_item_button;
    @FXML
    private Button invoices_button;
    @FXML
    private TextField addItems_itemID;
    @FXML
    private TextField addItems_itemName;
    @FXML
    private TextField addItems_itemBarcode;
    @FXML
    private TextField addItems_itemPrice;
    @FXML
    private TextField text;
    @FXML
    private Label orders_total;

    @FXML
    private TableColumn<customerData, Double> orders_col_price;
    @FXML
    private TableColumn<customerData, Integer> orders_col_id;
    @FXML
    private TableColumn<customerData, String> orders_col_itemName;
    @FXML
    private TableColumn<Item,String> addItems_col_itemId;
    @FXML
    private TableColumn<Item,String> addItems_col_itemBarcode;
    @FXML
    private TableColumn<Item, String> addItems_col_itemName;
    @FXML
    private TableColumn<Item, Double> addItems_col_itemPrice;
    @FXML
    private TableView<Item> addItems_tableView;

    @FXML
    private TableView<customerData> order_tableView;
    @FXML
    private AnchorPane tableForm;
    @FXML
    private AnchorPane invoiceForm;

    private ObservableList<Item> addItemList;

    private int customerid;

    public void switchForm(ActionEvent event)
    {
        if (event.getSource()==add_item_button) {
            itemForm.setVisible(true);
            tableForm.setVisible(true);

            invoiceForm.setVisible(false);
            buttonProperties("add_item_button");
        }
        if (event.getSource()==invoices_button)
        {
            itemForm.setVisible(false);
            tableForm.setVisible(false);
            invoiceForm.setVisible(true);
            buttonProperties("invoices_button");

        }
    }

    public ObservableList<Item> addItemsListData()
     {
        ObservableList<Item> items = FXCollections.observableArrayList();
        String sql="SELECT * FROM items";
        connect= database.ConnectDb();
        try
        {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            while (result.next())
            {
                Item item = new Item(result.getString("item_id"),
                        result.getString("item_name"),
                        result.getString("item_barcode"),
                        result.getDouble("item_price"));
                items.add(item);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
            try
            {
                prepare.close();
                result.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return items;
    }
    public void addItemsShowListData()
    {
        addItemList = addItemsListData();
        addItems_col_itemId.setCellValueFactory(new PropertyValueFactory<>("itemId"));
        addItems_col_itemName.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        addItems_col_itemBarcode.setCellValueFactory(new PropertyValueFactory<>("itemBarcode"));
        addItems_col_itemPrice.setCellValueFactory(new PropertyValueFactory<>("itemPrice"));
        addItems_tableView.setItems(addItemList);


    }
    public void checkData(String id,int value)
    {
        String variable = null;
        if (value==1)
            variable="item_id";
        else
            variable="item_barcode";
        String sql="SELECT * FROM items WHERE "+variable+"='"+id+"'";
        connect= database.ConnectDb();
        try
        {
        prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            Alert alert;
            if (result.next())
            {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please enter the "+variable+" again because it already exists");
                alert.showAndWait();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
            try
            {
                prepare.close();
                result.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public void addItemsAdd()
    {
        checkData(addItems_itemID.getText(),1);
        checkData(addItems_itemBarcode.getText(),2);

        String sql= "INSERT INTO items (item_id,item_name,item_barcode,item_price)"+"VALUES(?,?,?,?)";
        connect= database.ConnectDb();
        try {
            Alert alert;
            if (addItems_itemID.getText().isEmpty()
                    || addItems_itemName.getText().isEmpty()
                    || addItems_itemBarcode.getText().isEmpty()
                    || addItems_itemPrice.getText().isEmpty()) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            }
            else {
                    prepare = connect.prepareStatement(sql);

                    prepare.setString(1, addItems_itemID.getText());
                    prepare.setString(2, addItems_itemName.getText());
                    prepare.setString(3, addItems_itemBarcode.getText());
                    prepare.setString(4, addItems_itemPrice.getText());

                    prepare.executeUpdate();
                    addItemsShowListData();
                    addItemReset();

            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
            try
            {
                prepare.close();
                result.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public void addItemReset()
    {
        addItems_itemID.setText("");
        addItems_itemName.setText("");
        addItems_itemBarcode.setText("");
        addItems_itemPrice.setText("");
    }

    public ObservableList<customerData> orderListData()
    {
        customerId();
        ObservableList<customerData> listData = FXCollections.observableArrayList();
        String sql = "SELECT * FROM customer WHERE customer_id='"+customerid+"'";
        connect = database.ConnectDb();
        try
        {
            customerData customerD;
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            while (result.next())
            {
                customerD = new customerData(result.getInt("id"),result.getInt("customer_id")
                        ,result.getString("item_name")
                        ,result.getString("item_barcode")
                        ,result.getDouble("item_price")
                        ,result.getString("date"));
                listData.add(customerD);

            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
            try
            {
                prepare.close();
                result.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return listData;
    }
    private ObservableList<customerData> ordersList;
    public void ordersShowListData()
    {
        ordersList=orderListData();
        orders_col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        orders_col_itemName.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        orders_col_itemBarcode.setCellValueFactory(new PropertyValueFactory<>("itemBarcode"));
        orders_col_price.setCellValueFactory(new PropertyValueFactory<>("itemPrice"));
        order_tableView.setItems(ordersList);
        ordersDisplayTotal();

    }
    public void ordersAdd()
    {
        customerId();

        String sql="INSERT INTO customer(customer_id,item_name,item_barcode,item_price,date)"
                +"VALUES(?,?,?,?,?)";
        String s= text.getText();
        Item item = getItemByBarcode(s);

        connect= database.ConnectDb();
        try {
            prepare = connect.prepareStatement(sql);

            prepare.setString(1,String.valueOf(customerid));
            prepare.setString(2, item.getItemName());
            prepare.setString(3, item.getItemBarcode());
            prepare.setString(4, String.valueOf(item.getItemPrice()));
//            java.util.Date date = new java.util.Date();
//            java.sql.Date sqldate = new java.sql.Date(date.getTime());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            String ts = simpleDateFormat.format(timestamp);

            prepare.setString(5, ts);
            prepare.executeUpdate();
            ordersDisplayTotal();
            ordersShowListData();

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
            try
            {
                prepare.close();
                result.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    public Item getItemByBarcode(String barcode) {
        String sql = "SELECT * FROM items WHERE item_barcode='" + barcode+"'";
        connect = database.ConnectDb();
        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            Alert alert;
            if (!result.next()) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please enter the item_barcode again because it isn't exists");
                alert.showAndWait();
            }
            else
            {
                Item item= new Item(result.getString("item_id"),
                            result.getString("item_name")
                        ,result.getString("item_barcode")
                        ,result.getDouble("item_price")
                );
                return item;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try
            {
                prepare.close();
                result.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return null;
    }

    private double totalp;
    public void ordersDisplayTotal()
    {
        String sql = "SELECT SUM(item_price) FROM customer WHERE customer_id = '"+customerid+"'";
        connect= database.ConnectDb();
        try
        {
            prepare =connect.prepareStatement(sql);
            result = prepare.executeQuery();
            while (result.next())
            {
                totalp = result.getDouble("SUM(item_price)");
            }
            orders_total.setText("$"+String.valueOf(totalp));
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
            try
            {
                prepare.close();
                result.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    public void ordersPay()
    {
        customerId();
        String sql = "INSERT INTO customer_receipt (customer_id,total,amount,change,date)"
                + "VALUES(?,?,?,?,?)";
        connect = database.ConnectDb();
        try
        {
            double d = Double.parseDouble(order_amount.getText());
            Alert alert;
            if (totalp>0 && !order_amount.getText().isEmpty() && d>=totalp )
            {
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure?");
                Optional<ButtonType> option = alert.showAndWait();
                if(option.get().equals(ButtonType.OK))
                {
                    prepare = connect.prepareStatement(sql);
                    prepare.setString(1,String.valueOf(customerid));
                    prepare.setString(2, String.valueOf(totalp));
                    prepare.setString(3,String.valueOf(amountP));
                    prepare.setString(4,String.valueOf(changeP));

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                    String ts = simpleDateFormat.format(timestamp);
                    prepare.setString(5, ts);
                    prepare.executeUpdate();
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successful!");
                    alert.showAndWait();
                    ordersShowListData();
                    totalp =0 ;
                    changeP= 0;
                    amountP=0;
                    order_change.setText("$0.0");
                    order_amount.setText("");

                }
                else return;


            }
            else
            {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Invalid : 3");
                alert.showAndWait();
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
            try
            {
                prepare.close();
                result.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    private double amountP;
    private double changeP;
    public void ordersAmount() {
        Alert alert;
        if (!order_amount.getText().isEmpty()) {
            amountP = Double.parseDouble(order_amount.getText());
            if (totalp > 0) {
                if (amountP >= totalp) {
                    changeP = amountP - totalp;
                    order_change.setText("$" + String.valueOf(changeP));
                } else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(null);
                    alert.setContentText("Invalid :3");
                    alert.showAndWait();
                    order_amount.setText("");
                }
            } else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("Invalid :3");
                alert.showAndWait();
            }
        }else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("Invalid :3");
                alert.showAndWait();
            }


    }
    public void orderReset()
    {
        customerId();
        String sql ="DELETE FROM customer WHERE customer_id= '"+customerid+"'";
        connect = database.ConnectDb();
        Alert alert = null;
        try
        {
            if (!order_tableView.getItems().isEmpty())
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to reset?");
                Optional<ButtonType> option = alert.showAndWait();
                if (option.get().equals(ButtonType.OK))
                {
                    statement = connect.createStatement();
                    statement.executeUpdate(sql);
                    ordersShowListData();
                    totalp = 0;
                    changeP =0;
                    amountP =0;
                    order_change.setText("$0.0");
                    order_amount.setText("");
                    orders_total.setText("$0.0");
                }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
            try
            {
                prepare.close();
                result.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    public void customerId()
    {
        String customId = "SELECT * FROM customer";
        connect= database.ConnectDb();
        try {
            prepare = connect.prepareStatement(customId);
            result = prepare.executeQuery();
            int checkId = 0;
            while (result.next())
            {
                customerid = result.getInt("customer_id");
            }
            String checkData= "SELECT * FROM customer_receipt";
            statement = connect.createStatement();
            result = statement.executeQuery(checkData);
            while (result.next())
            {
                checkId = result.getInt("customer_id");
            }
            if (customerid==0)
            {
                customerid+=1;
            }
            else if(checkId == customerid)
            {
                customerid+=1;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
            try
            {
                prepare.close();
                result.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    public void buttonProperties(String s)
    {
        if(s=="add_item_button")
        {
            add_item_button.setStyle("-fx-background-color:linear-gradient(to bottom right, #25a473, #89892b);");
            invoices_button.setStyle("fx-background-color:transparent");
        }
        else
        {
            invoices_button.setStyle("-fx-background-color:linear-gradient(to bottom right, #25a473, #89892b);");
            add_item_button.setStyle("fx-background-color:transparent");
        }
    }
    public void orderReceipt()
    {

    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addItemsShowListData();
        buttonProperties("add_item_button");


    }



}
