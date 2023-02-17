package com.example.todolistprooject.dataModel;

import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class TodoData {
    public  static  TodoData instance  = new TodoData();
    private  String fileName = "file.txt";
    private DateTimeFormatter df;

    private ObservableList<TodoItem>todoItems;

    public TodoData() {

        this.df = DateTimeFormatter.ofPattern("MMMM d, yyyy");

        this.todoItems = FXCollections.observableArrayList();
        }
    public static TodoData getInstance() {
        return instance;
    }

    public String getFileName() {
        return fileName;
    }

    public ObservableList<TodoItem> getTodoItems() {
        return todoItems;
    }

    public void load() throws IOException {

        Path path = Paths.get(fileName);

        BufferedReader br = Files.newBufferedReader(path);

        String input = "";
        try{
            while((input=br.readLine())!=null){

                String[] ss = input.split("\t");

                String shortDescription = ss[0];

                String details = ss[1];

                String deadline = ss[2];

                LocalDate localDate = LocalDate.parse(deadline,this.df);

                todoItems.add(new TodoItem(shortDescription,details,localDate));

            }



        }finally {
            if(br!=null){

                br.close();
            }
        }


    }

    public void save() throws IOException {

        Path path = Path.of(this.fileName);

        BufferedWriter bw = Files.newBufferedWriter(path);

        Iterator<TodoItem>iterator = todoItems.iterator();

        try{
            while (iterator.hasNext()){
            TodoItem todoItem= iterator.next();
            bw.write(String.format("%s\t%s\t%s",

                    todoItem.getShortDescription(),
                    todoItem.getDetails(),
                    todoItem.getLocalDate().format(this.df)
                    ));

            bw.newLine();
            }

        }finally {
            if(bw!=null){
                bw.close();
            }

        }
    }

    public  void addTodoItem(TodoItem todoItem){


        this.todoItems.add(todoItem);

    }

    public void delete(TodoItem todoItem){

        this.todoItems.remove(todoItem);

    }




}
