package com.example.todolistprooject;

import com.example.todolistprooject.dataModel.TodoData;
import com.example.todolistprooject.dataModel.TodoItem;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.util.Callback;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class HelloController {
    @FXML
    private  ContextMenu contextMenu;
    @FXML
    private BorderPane main;
    @FXML
    private TextArea textArea;
    @FXML
    private  Label deadlineLabel;
    @FXML
    private ListView<TodoItem>todoItemListView;

    private List<TodoItem> todoItemlist = new ArrayList<>();
    @FXML
    private ToggleButton toggleButton;

    FilteredList<TodoItem>filteredList;
    @FXML
   public  void initialize(){

    /*    TodoItem item1 = new TodoItem("Mail birthday card", "Buy a 30th birthday card for John",
                LocalDate.of(2016, Month.APRIL, 25));
        TodoItem item2 = new TodoItem("Doctor's Appointment", "See Dr. Smith at 123 Main Street.  Bring paperwork",
                LocalDate.of(2016, Month.MAY, 23));
        TodoItem item3 = new TodoItem("Finish design proposal for client", "I promised Mike I'd email website mockups by Friday 22nd April",
                LocalDate.of(2016, Month.APRIL, 22));
        TodoItem item4 = new TodoItem("Pickup Doug at the train station", "Doug's arriving on March 23 on the 5:00 train",
                LocalDate.of(2016, Month.MARCH, 23));
        TodoItem item5 = new TodoItem("Pick up dry cleaning", "The clothes should be ready by Wednesday",
                LocalDate.of(2016, Month.APRIL,20));

        todoItemlist = new ArrayList<TodoItem>();
        todoItemlist.add(item1);
        todoItemlist.add(item2);
        todoItemlist.add(item3);
        todoItemlist.add(item4);
        todoItemlist.add(item5);

        TodoData.getInstance().setTodoItems(todoItemlist);

     */
        contextMenu = new ContextMenu();
        MenuItem item = new MenuItem("Delete");

        item.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                TodoItem item = todoItemListView.getSelectionModel().getSelectedItem();
                deleteItem(item);
            }
        });

        contextMenu.getItems().addAll(item);

        filteredList = new FilteredList<>(TodoData.getInstance().getTodoItems(), new Predicate<TodoItem>() {
            @Override
            public boolean test(TodoItem todoItem) {
                return true;
            }
        });

        SortedList<TodoItem>sortedList = new SortedList<>(filteredList, new Comparator<TodoItem>() {
            @Override
            public int compare(TodoItem o1, TodoItem o2) {
                return  o1.getLocalDate().compareTo(o2.getLocalDate());
            }
        }) ;
        todoItemListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        todoItemListView.setItems(sortedList);
        todoItemListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TodoItem>() {
            @Override
            public void changed(ObservableValue<? extends TodoItem> observableValue, TodoItem todoItem, TodoItem t1) {
                if(t1!=null){
                    TodoItem todoItem1 = todoItemListView.getSelectionModel().getSelectedItem();
                    textArea.setText(todoItem1.getDetails());
                    DateTimeFormatter df = DateTimeFormatter.ofPattern("MMMM d yyyy");
                    deadlineLabel.setText( df.format(todoItem1.getLocalDate()));
                }
            }
        });

        todoItemListView.setCellFactory(new Callback<ListView<TodoItem>, ListCell<TodoItem>>() {
            @Override
            public ListCell<TodoItem> call(ListView<TodoItem> todoItemListView) {

                ListCell<TodoItem>listCell = new ListCell<>(){
                    @Override
                    protected void updateItem(TodoItem todoItem, boolean b) {
                        super.updateItem(todoItem, b);

                        if(b){
                            setText(null);
                        }

                        else{
                            setText(todoItem.getShortDescription());
                            if(todoItem.getLocalDate().equals(LocalDate.now())){
                               setTextFill(Color.BROWN);
                            }

                            else if(todoItem.getLocalDate().equals(LocalDate.now().plusDays(1))){

                                setTextFill(Color.RED);
                            };

                    }
                }


            };

                listCell.emptyProperty().addListener((obs,wasEmpty,isEmpty)->{

                    if(isEmpty){

                        listCell.setContextMenu(null);
                    }

                    else{

                        listCell.setContextMenu(contextMenu);

                    }
                });

                return listCell;
        }

    });
    }

    public void showNewDialogItem(){

        Dialog<ButtonType>dialog = new Dialog<>();
        dialog.initOwner(main.getScene().getWindow());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("todoItemDialog.fxml"));

        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());

        }catch (IOException e){

            e.getStackTrace();

        }
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType>res = dialog.showAndWait();
        if(res.isPresent()&&res.get()==ButtonType.OK){

            todoItemDialogController controller = fxmlLoader.getController();

          TodoItem todoItem =   controller.processResults();

          todoItemListView.getSelectionModel().select(todoItem);

        }



    }

    @FXML
    public void setToggleButton(){

        if(toggleButton.isSelected()){

            filteredList.setPredicate(new Predicate<TodoItem>() {
                @Override
                public boolean test(TodoItem todoItem) {

                    return todoItem.getLocalDate().equals(LocalDate.now());
                }
            });


        } else{

            filteredList.setPredicate(new Predicate<TodoItem>() {
                @Override
                public boolean test(TodoItem todoItem) {
                    return true;
                }
            });


        }




    }

    public void deleteItem(TodoItem todoItem){

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        alert.setHeaderText("Delete Item");

        alert.setContentText("Are you sure about deleting this item ? ");

        Optional<ButtonType>res  = alert.showAndWait();

        if(res.isPresent()&&res.get()==ButtonType.OK){

            TodoData.getInstance().delete(todoItem);
        }

    }



}