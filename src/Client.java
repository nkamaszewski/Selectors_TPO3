import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Client extends Application {
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        launch(args);
    }

    private static String server = "localhost";
    private static int port = 49999;

    public void start(Stage primaryStage) throws Exception{

        Button subscribeButton = new Button("Subscribe");
        Button unsubscribeButton = new Button("Unsubscribe");
        final Label messageFromServer = new Label("");

        TextField subjectField = new TextField();

        subscribeButton.setOnAction(actionEvent -> {

        });

        unsubscribeButton.setOnAction(actionEvent -> {

        });


        GridPane gridPane = new GridPane();

        gridPane.setHgap(50);
        gridPane.setVgap(10);

        Scene scene = new Scene(gridPane, 800, 600);

        gridPane.add(new Label("Subject: "), 1, 1, 1, 1);
        gridPane.add(subjectField, 1, 2, 1, 1);

        gridPane.add(subscribeButton, 3, 2, 1, 1);
        gridPane.add(unsubscribeButton, 4, 2, 1, 1);

        gridPane.add(messageFromServer, 1, 3, 5, 3);

        primaryStage.setTitle("s16456 - Norbert Kamaszewski, subscribe & unsubscribe java program");
        primaryStage.setResizable(true);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}