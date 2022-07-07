package au.edu.unsw.infs2605.donorlist;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.paint.Color;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Stream;

public class AppController implements Initializable {
    @FXML
    private TextField textFirstName; // firstname input
    @FXML
    private TextField textLastName; // lastname input
    @FXML
    private TextField textEmailAddress; // email input
    @FXML
    private TextField textMobile; // mobile input
    @FXML
    private TextField textAddress; // address input
    @FXML
    private TextArea textareaNotes; // noyes input
    @FXML
    private DatePicker datePickBirth; // birth input
    @FXML
    private ComboBox cboxGender; // gender input
    @FXML
    private ComboBox cboxBloodType; // bloodtype input

    @FXML
    private Label labelBlooddate; // Blooddate display

    @FXML
    private Label labelBloodcount; // Bloodcount display

    @FXML
    private Label labelPlasmadate; // Plasmadate display

    @FXML
    private Label labelPlasmacount; // Plasmacount display


    @FXML
    private ListView<String> listDonors; //left list


    @FXML
    private Button buttonBloodPlus;//button for plus blood donation
    @FXML
    private Button buttonBloodMinus;//button for minus blood donation
    @FXML
    private Button buttonPlasmaPlus;//button for plus plasma donation
    @FXML
    private Button buttonPlasmaMinus;//button for minus plasma donation

    @FXML
    private Button buttonSave;//button for save
    @FXML
    private Button buttonNew;//button for new

    private List<String> donorDataList;//donor data in memory

    private String currentEditData=""; //specification for current editing data

    //initialize donor data for test
    private void initialTestData() {

        donorDataList = new ArrayList();

        donorDataList.add("JOHN|WILLIAMS|1997-10-04|MALE|O+|JW@A.COM|88888888|10 A STREET|EXTRA NOTES|6|2022-06-29|12|2022-6-23");
        donorDataList.add("ALICE|KYRIE|1995-10-04|FEMALE|AB-|AK@A.COM|78888888|5 A STREET|NO EXTRA NOTES|5|2022-05-19|3|2022-3-17");
    }

    //extends for display the donor's name in list from the whole data
    class NameCell extends ListCell<String> {
        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);

            if (item != null) {
                String[] infos = item.split("\\|");
                setText(infos[0]+" "+infos[1]);

            } else {
                setItem(null);
                setText("");
            }
        }

    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle)  {




        ObservableList<String> bloodtypeOptions =
                FXCollections.observableArrayList(
                        "O+",
                        "O-",
                        "A+","A-","B+","B-","AB+","AB-");//initialize the blood type

        ObservableList<String> genderOtions =
                FXCollections.observableArrayList(
                        "FEMALE",
                        "MALE");//initialize the gender

        cboxBloodType.setItems(bloodtypeOptions);
        cboxGender.setItems(genderOtions);

        initialTestData();


        listDonors.setCellFactory((ListView<String> l) -> new NameCell());

        ObservableList<String> items = FXCollections.observableArrayList (donorDataList);//bind the donor data



        listDonors.setItems(items);

        listDonors.getSelectionModel().selectedItemProperty().addListener(

                (ObservableValue<? extends String> observable, String oldValue, String newValue) -> {

                    if(newValue==null)
                    {
                        return;
                    }
                    currentEditData=newValue; //set current edit data

                    String[] infos = newValue.split("\\|");

                    //display the donor data
                    textFirstName.setText(infos[0]);
                    textLastName.setText(infos[1]);
                    datePickBirth.setValue(LocalDate.parse(infos[2]));
                    cboxGender.setValue(infos[3]);
                    cboxBloodType.setValue(infos[4]);
                    textEmailAddress.setText(infos[5]);
                    textMobile.setText(infos[6]);
                    textAddress.setText(infos[7]);
                    textareaNotes.setText(infos[8]);

                    labelBloodcount.setText(infos[9]);
                    labelBlooddate.setText(infos[10]);
                    labelPlasmacount.setText(infos[11]);
                    labelPlasmadate.setText(infos[12]);



                });

        buttonBloodPlus.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String bloodcount = labelBloodcount.getText();
                if(bloodcount=="-")
                {
                    labelBloodcount.setText("1");
                }
                else
                {
                    System.out.println("aaaa"+bloodcount);
                    int count = Integer.parseInt(bloodcount);
                    count++;
                    labelBloodcount.setText(Integer.toString(count));//add blood count
                }
                SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
                Date date = new Date(System.currentTimeMillis());
                labelBlooddate.setText(formatter.format(date)); //refresh last blood date
            }
        });

        buttonBloodMinus.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String bloodcount = labelBloodcount.getText();
                if(bloodcount=="-")
                {

                }
                else
                {
                    int count = Integer.parseInt(bloodcount);
                    if(count>0) {
                        count--;
                        labelBloodcount.setText(Integer.toString(count)); //minus the blood data, no need to refresh the date
                    }
                }
            }
        });

        buttonPlasmaPlus.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String bloodcount = labelPlasmacount.getText();
                if(bloodcount=="-")
                {
                    labelPlasmacount.setText("1");
                }
                else
                {
                    int count = Integer.parseInt(bloodcount);
                    count++;
                    labelPlasmacount.setText(Integer.toString(count));//add plasma count
                }
                SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
                Date date = new Date(System.currentTimeMillis());
                labelPlasmadate.setText(formatter.format(date));//refresh last plasma date
            }
        });

        buttonPlasmaMinus.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String bloodcount = labelPlasmacount.getText();
                if(bloodcount=="-")
                {

                }
                else
                {
                    int count = Integer.parseInt(bloodcount);
                    if(count>0) {
                        count--;
                        labelPlasmacount.setText(Integer.toString(count));//minus the plasma data, no need to refresh the date
                    }
                }
            }
        });

        buttonSave.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String newData=textFirstName.getText()+"|"
                        +textLastName.getText()+"|"
                        +datePickBirth.getValue().format(formatter)+"|"
                        +cboxGender.getValue()+"|"
                        +cboxBloodType.getValue()+"|"
                        +textEmailAddress.getText()+"|"
                        +textMobile.getText()+"|"
                        +textAddress.getText()+"|"
                        +textareaNotes.getText()+"|"
                        +labelBloodcount.getText()+"|"
                        +labelBlooddate.getText()+"|"
                        +labelPlasmacount.getText()+"|"
                        +labelPlasmadate.getText(); //combine the data
                if(currentEditData=="") //if no current data, create a new donor
                {
                    donorDataList.add(newData);
                    currentEditData=newData;
                    listDonors.getItems().clear();
                    ObservableList<String> items = FXCollections.observableArrayList (donorDataList);
                    listDonors.setItems(items);
                }
                else {

                    for (int i = 0; i < donorDataList.stream().count(); i++) {
                        if (donorDataList.get(i) == currentEditData) //find the current data and update it
                        {
                            System.out.println(i);
                            donorDataList.set(i, newData);
                            currentEditData = newData;
                            listDonors.getItems().clear();
                            ObservableList<String> items = FXCollections.observableArrayList(donorDataList);
                            listDonors.setItems(items);
                            return;
                        }
                    }
                }

            }
        });

        buttonNew.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //clear the form and clear the current data
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                textFirstName.setText("");
                textLastName.setText("");
                datePickBirth.setValue(LocalDate.now());
                cboxGender.setValue("");
                cboxBloodType.setValue("");
                textEmailAddress.setText("");
                textMobile.setText("");
                textAddress.setText("");
                textareaNotes.setText("");
                labelBloodcount.setText("-");
                labelBlooddate.setText("-");
                labelPlasmacount.setText("-");
                labelPlasmadate.setText("-");
                currentEditData="";

            }
        });




    }
}
