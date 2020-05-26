import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Admin extends Application {
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        launch(args);
    }

    private static String server = "localhost";
    private static int port = 48888;

    public void start(Stage primaryStage) throws Exception{

        Button addSubjectButton = new Button("Add Subject");
        Button deleteSubjectButton = new Button("Delete Subject");
        Button sendNewsButton = new Button("Send News to subscribers");

        TextField subjectField = new TextField();
        TextField newsSubjectField = new TextField();
        TextField newsField = new TextField();

        addSubjectButton.setOnAction(actionEvent -> {

        });

        deleteSubjectButton.setOnAction(actionEvent -> {

        });


        GridPane gridPane = new GridPane();

        gridPane.setHgap(50);
        gridPane.setVgap(10);

        Scene scene = new Scene(gridPane, 800, 600);

        gridPane.add(new Label("Subject: "), 1, 1, 1, 1);
        gridPane.add(subjectField, 1, 2, 1, 1);

        gridPane.add(addSubjectButton, 3, 2, 1, 1);
        gridPane.add(deleteSubjectButton, 4, 2, 1, 1);

        gridPane.add(new Label("News (paste subject and message):  "), 1, 5, 1, 1);
        gridPane.add(newsSubjectField, 1, 6, 1, 1);

        gridPane.add(newsField, 1, 7, 5, 1);
        gridPane.add(sendNewsButton, 1, 8, 1, 1);

        primaryStage.setTitle("s16456 - Norbert Kamaszewski, admin java program");
        primaryStage.setResizable(true);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
